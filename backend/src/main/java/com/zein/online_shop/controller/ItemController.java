package com.zein.online_shop.controller;

import com.zein.online_shop.dto.request.CustomerRequest;
import com.zein.online_shop.dto.request.ItemRequest;
import com.zein.online_shop.service.CustomerService;
import com.zein.online_shop.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "#{'createdTime,desc'.split('_')}") List<String> sort,
            @RequestParam(required = false) String search
    ) {
        return ResponseEntity.ok(search == null ? itemService.getAll(page, size, sort) : itemService.search(search));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> get(@Valid @PathVariable Integer id) {
        return ResponseEntity.ok(itemService.get(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ItemRequest request) {
        var item = itemService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody ItemRequest request) {
        return ResponseEntity.ok(itemService.update(id, request));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        itemService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
