package com.zein.online_shop.service.impl;

import com.zein.online_shop.dto.request.CreateOrderRequest;
import com.zein.online_shop.dto.request.UpdateOrderRequest;
import com.zein.online_shop.dto.response.ListResponse;
import com.zein.online_shop.dto.response.OrderResponse;
import com.zein.online_shop.exception.InsufficientCapacityException;
import com.zein.online_shop.model.Customer;
import com.zein.online_shop.model.Item;
import com.zein.online_shop.model.Order;
import com.zein.online_shop.repository.CustomerRepository;
import com.zein.online_shop.repository.ItemRepository;
import com.zein.online_shop.repository.OrderRepository;
import com.zein.online_shop.service.OrderService;
import com.zein.online_shop.utility.UniqueCodeGenerator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderServiceImpl extends BaseServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    @Override
    public ListResponse getAll(int page, int size, List<String> sort) {
        Page<Order> orders = orderRepository.findAllBy(PageRequest.of(page, size, Sort.by(getSortOrder(sort))));
        List<OrderResponse> responses = Arrays.asList(modelMapper.map(orders.toList(), OrderResponse[].class));

        return new ListResponse(responses, getPageMetadata(orders));
    }

    @Override
    public OrderResponse get(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order (ID: " + id + ") not found"));
        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        Item item = itemRepository.findByIdAndIsAvailable(request.getItemId(), true)
                .orElseThrow(() -> new EntityNotFoundException("Item (ID: " + request.getItemId() + ") not found"));

        Customer customer = customerRepository.findByIdAndIsActive(request.getCustomerId(), true)
                .orElseThrow(() -> new EntityNotFoundException("Customer (ID: " + request.getCustomerId() + ") not found"));

        Order order = modelMapper.map(request, Order.class);
        order.setItem(item);
        order.setCustomer(customer);
        order.setTotalPrice(item.getPrice() * request.getQuantity());
        item.setStock(item.getStock() - request.getQuantity());
        customer.setLastOrderDate(request.getDate());

        int maxLoop = 5;
        while(maxLoop > 0) {
            // generating custom 6-digit code
            String code = UniqueCodeGenerator.generate(6);

            if (!orderRepository.existsByCode(code)) {
                order.setCode(code);
                break;
            }

            maxLoop--;
        }

        order = orderRepository.save(order);
        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    @Transactional
    public OrderResponse update(Integer id, UpdateOrderRequest request) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Order (ID: " + id + ") not found"));

        Item item = order.getItem();
        int stock = item.getStock() + order.getQuantity() - request.getQuantity();

        if (stock < 0) throw new InsufficientCapacityException("Insufficient amount of stock");

        item.setStock(stock);
        order.setDate(request.getDate());
        order.setQuantity(request.getQuantity());
        order.setTotalPrice(order.getQuantity() * item.getPrice());

        order = orderRepository.save(order);
        item = itemRepository.save(item);

        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public void delete(Integer id) {
        orderRepository.deleteById(id);
    }
}
