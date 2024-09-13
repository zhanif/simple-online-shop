package com.zein.online_shop.service.impl;

import com.zein.online_shop.dto.request.CustomerRequest;
import com.zein.online_shop.dto.response.ListResponse;
import com.zein.online_shop.dto.response.CustomerResponse;
import com.zein.online_shop.model.Customer;
import com.zein.online_shop.repository.CustomerRepository;
import com.zein.online_shop.service.CustomerService;
import com.zein.online_shop.utility.UniqueCodeGenerator;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerServiceImpl extends BaseServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    @Override
    public ListResponse getAll(int page, int size, List<String> sort) {
        Page<Customer> customers = customerRepository.findAllBy(PageRequest.of(page, size, Sort.by(getSortOrder(sort))));
        List<CustomerResponse> responses = Arrays.asList(modelMapper.map(customers.toList(), CustomerResponse[].class));

        return new ListResponse(responses, getPageMetadata(customers));
    }

    @Override
    public CustomerResponse get(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer (ID: " + id + ") not found"));

        return modelMapper.map(customer, CustomerResponse.class);
    }

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

        customer = customerRepository.save(customer);
        return modelMapper.map(customer, CustomerResponse.class);
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
        customer.setPic(request.getPic());

        customer = customerRepository.save(customer);
        return modelMapper.map(customer, CustomerResponse.class);
    }

    @Override
    public void delete(Integer id) {
        customerRepository.deleteById(id);
    }
}
