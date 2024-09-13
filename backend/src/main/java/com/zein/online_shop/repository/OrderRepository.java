package com.zein.online_shop.repository;

import com.zein.online_shop.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    boolean existsByCode(String code);
    Page<Order> findAllBy(PageRequest pageRequest);
}
