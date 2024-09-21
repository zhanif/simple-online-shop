package com.zein.online_shop.repository;

import com.zein.online_shop.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    Page<Item> findAllBy(PageRequest pageRequest);
    Page<Item> findAllByNameContainingIgnoreCase(String name, PageRequest pageRequest);
    boolean existsByCode(String code);
    Optional<Item> findByIdAndIsAvailable(Integer itemId, Boolean isAvailable);

    List<Item> findByNameContainingIgnoreCaseAndIsAvailable(String search, boolean b);
}
