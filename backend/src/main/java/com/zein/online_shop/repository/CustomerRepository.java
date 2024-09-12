package com.zein.online_shop.repository;

import com.zein.online_shop.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Page<Customer> findAllBy(PageRequest pageRequest);
}
