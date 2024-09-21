package com.zein.online_shop.service.impl;

import com.zein.online_shop.dto.request.CustomerRequest;
import com.zein.online_shop.dto.response.ListResponse;
import com.zein.online_shop.dto.response.CustomerResponse;
import com.zein.online_shop.dto.response.SearchOptionResponse;
import com.zein.online_shop.model.Customer;
import com.zein.online_shop.repository.CustomerRepository;
import com.zein.online_shop.service.CustomerService;
import com.zein.online_shop.utility.UniqueCodeGenerator;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lib.minio.MinioService;
import lib.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerServiceImpl extends BaseServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final MinioService minioService;
    private final RedisService redisService;
    private static String REDIS_CACHE_PREFIX = "customer:pic:";
    private static Duration REDIS_DURATION = Duration.ofMinutes(10);
    private static String MINIO_BUCKET = "simple-online-shop";

    @Override
    public ListResponse getAll(int page, int size, List<String> sort, String search) {
        Page<Customer> customers = null;
        var pageRequest = PageRequest.of(page, size, Sort.by(getSortOrder(sort)));

        if (search != null) customers = customerRepository.findAllByNameContainingIgnoreCase(search, pageRequest);
        else customers = customerRepository.findAllBy(pageRequest);

        List<CustomerResponse> responses = customers.stream().map(this::mapCustomerToResponse).toList();
        return new ListResponse(responses, getPageMetadata(customers));
    }

    @Override
    public CustomerResponse get(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer (ID: " + id + ") not found"));

        return mapCustomerToResponse(customer);
    }

    @Transactional
    @Override
    public CustomerResponse create(CustomerRequest request) {
        if (customerRepository.existsByPhone(request.getPhone())) {
            throw new EntityExistsException("Phone number is already exist");
        }

        Customer customer = modelMapper.map(request, Customer.class);
        int maxLoop = 5;

        while(maxLoop > 0) {
            // generating custom 6-digit code
            String code = UniqueCodeGenerator.generate(6);

            if (!customerRepository.existsByCode(code)) {
                customer.setCode(code);
                break;
            }

            maxLoop--;
        }

        if (request.getPic() != null && !request.getPic().isEmpty())  {
            var uploaded = minioService.upload(request.getPic(), MINIO_BUCKET);
            customer.setPic(uploaded.getFileName());
        }

        customer = customerRepository.save(customer);

        return mapCustomerToResponse(customer);
    }

    @Override
    public CustomerResponse update(Integer id, CustomerRequest request) {
        if (customerRepository.existsByPhoneAndIdNot(request.getPhone(), id)) {
            throw new EntityExistsException("Phone number is already exist");
        }

        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Customer (ID: " + id + ") not found"));

        customer.setName(request.getName());
        customer.setAddress(request.getAddress());
        customer.setPhone(request.getPhone());
        customer.setIsActive(request.getIsActive());

        if (request.getPic() != null && !request.getPic().isEmpty()) {
            try {
                if (customer.getPic() != null && !customer.getPic().isEmpty()) {
                    minioService.remove(MINIO_BUCKET, customer.getPic());
                    redisService.delete(REDIS_CACHE_PREFIX + customer.getId());
                }
            }
            catch (Exception e) {
                var msg = String.format("Ignored Exception: %s", e.getMessage());
                System.out.println(msg);
            }
            var uploaded = minioService.upload(request.getPic(), MINIO_BUCKET);
            customer.setPic(uploaded.getFileName());
        }

        customer = customerRepository.save(customer);
        return mapCustomerToResponse(customer);
    }

    @Override
    public void delete(Integer id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Customer (ID: " + id + ") not found"));

        try {
            minioService.remove(MINIO_BUCKET, customer.getPic());
            redisService.delete(REDIS_CACHE_PREFIX + customer.getId());
        }
        catch (Exception e) {
            var msg = String.format("Ignored Exception: %s", e.getMessage());
            System.out.println(msg);
        }

        customerRepository.deleteById(id);
    }

    private CustomerResponse mapCustomerToResponse(Customer customer) {
        CustomerResponse response = modelMapper.map(customer, CustomerResponse.class);

        if (customer.getPic() != null && !customer.getPic().isEmpty()) {
            String cacheKey = REDIS_CACHE_PREFIX + customer.getId();
            Object cachedUrl = redisService.getData(cacheKey);

            if (cachedUrl == null) {
                String imageUrl = minioService.getImageUrl(MINIO_BUCKET, customer.getPic(), REDIS_DURATION);
                redisService.save(cacheKey, imageUrl, REDIS_DURATION);
                response.setPic(imageUrl);
            }
            else {
                response.setPic((String) cachedUrl);
            }
        }

        return response;
    }

    @Override
    public List<SearchOptionResponse> search(String search) {
        List<Customer> customers;

        if (search != null) {
            customers = customerRepository.findByNameContainingIgnoreCaseAndIsActive(search, true);
        }
        else {
            List<String> sorts = List.of("name,asc");
            customers = customerRepository.findAllBy(PageRequest.of(0, 10, Sort.by(getSortOrder(sorts)))).toList();
        }

        return Arrays.asList(modelMapper.map(customers, SearchOptionResponse[].class));
    }
}
