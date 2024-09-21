package com.zein.online_shop.repository;

import com.zein.online_shop.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Page<Customer> findAllBy(PageRequest pageRequest);
    Page<Customer> findAllByNameContainingIgnoreCase(String name, PageRequest pageRequest);
    boolean existsByPhone(String phone);
    boolean existsByCode(String code);
    boolean existsByPhoneAndIdNot(String phone, Integer id);
    Optional<Customer> findByIdAndIsActive(Integer customerId, Boolean isActive);

    List<Customer> findByNameContainingIgnoreCaseAndIsActive(String search, boolean b);
}
