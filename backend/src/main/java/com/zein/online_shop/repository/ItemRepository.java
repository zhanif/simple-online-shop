package com.zein.online_shop.repository;

import com.zein.online_shop.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    Page<Item> findAllBy(PageRequest pageRequest);
    boolean existsByCode(String code);
}
