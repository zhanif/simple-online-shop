package com.zein.online_shop.service.impl;

import com.zein.online_shop.dto.request.CustomerRequest;
import com.zein.online_shop.dto.response.ListResponse;
import com.zein.online_shop.dto.response.CustomerResponse;
import com.zein.online_shop.model.Customer;
import com.zein.online_shop.repository.CustomerRepository;
import com.zein.online_shop.service.CustomerService;
import com.zein.online_shop.utility.UniqueCodeGenerator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
        var customers = customerRepository.findAllBy(PageRequest.of(page, size, Sort.by(getSortOrder(sort))));
        List<CustomerResponse> responses = Arrays.asList(modelMapper.map(customers.toList(), CustomerResponse[].class));

        return new ListResponse(responses, getPageMetadata(customers));
    }

    @Override
    public CustomerResponse get(Integer id) {
        var customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer (ID: " + id + ") not found"));

        return modelMapper.map(customer, CustomerResponse.class);
    }

    @Override
    public CustomerResponse create(CustomerRequest request) {
        Customer customer = modelMapper.map(request, Customer.class);
        Customer result = null;

        int maxLoop = 5;
        while(maxLoop > 0) {
            try {
                customer.setCode(UniqueCodeGenerator.generate(6));
                result = customerRepository.save(customer);
                break;
            }
            catch(Exception e) {
                maxLoop--;
            }
        }

        return modelMapper.map(result, CustomerResponse.class);
    }

    @Override
    public CustomerResponse update(Integer id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Customer (ID: " + id + ") not found"));

        customer.setName(request.getName());
        customer.setAddress(request.getAddress());
        customer.setPhone(request.getPhone());
        customer.setIsActive(request.getIsActive());
        customer.setPic(request.getPic());

        Customer result = customerRepository.save(customer);
        return modelMapper.map(result, CustomerResponse.class);
    }

    @Override
    public void delete(Integer id) {
        customerRepository.deleteById(id);
    }
}
