package com.zein.online_shop.controller;

import com.zein.online_shop.dto.request.CustomerRequest;
import com.zein.online_shop.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<?> getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "#{'createdTime,desc'.split('_')}") List<String> sort
    ) {
        return ResponseEntity.ok(customerService.getAll(page, size, sort));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> get(@Valid @PathVariable Integer id) {
        return ResponseEntity.ok(customerService.get(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CustomerRequest request) {
        var customer = customerService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.ok(customerService.update(id, request));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        customerService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
