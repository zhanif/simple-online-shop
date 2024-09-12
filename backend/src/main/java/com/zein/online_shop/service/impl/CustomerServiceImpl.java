package com.zein.online_shop.service.impl;

import com.zein.online_shop.dto.request.CreateCustomerRequest;
import com.zein.online_shop.dto.response.CustomerResponse;
import com.zein.online_shop.model.Customer;
import com.zein.online_shop.repository.CustomerRepository;
import com.zein.online_shop.service.CustomerService;
import com.zein.online_shop.utility.UniqueCodeGenerator;
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
    public List<CustomerResponse> getAll(int page, int size, List<String> sort) {
        List<Customer> customers = customerRepository.findAllBy(PageRequest.of(page, size, Sort.by(getSortOrder(sort)))).toList();
        return Arrays.asList(modelMapper.map(customers, CustomerResponse[].class));
    }

    @Override
    public CustomerResponse create(CreateCustomerRequest request) {
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
}
