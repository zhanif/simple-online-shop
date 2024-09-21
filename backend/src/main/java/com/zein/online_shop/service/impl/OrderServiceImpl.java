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
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
        if (item.getStock() == 0) item.setIsAvailable(false);

        order = orderRepository.save(order);
        item = itemRepository.save(item);

        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public void delete(Integer id) {
        orderRepository.deleteById(id);
    }

    @Override
    public byte[] export(int page, int size, List<String> sort) {
        List<Order> orders = orderRepository.findAllBy(PageRequest.of(page, size, Sort.by(getSortOrder(sort)))).toList();
        byte[] data;
        try {
            InputStream template = getClass().getResourceAsStream("/reports/simple-online-shop.jrxml");
            if (template == null) throw new FileNotFoundException("Template not found");

            JasperReport jasperReport = JasperCompileManager.compileReport(template);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            currencyFormat.setMaximumFractionDigits(0);

            var collectionDataSource = orders.stream()
                .map(order -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("date", dateFormat.format(order.getDate()));
                    map.put("code", order.getCode());
                    map.put("quantity", order.getQuantity());
                    map.put("total_price", currencyFormat.format(order.getTotalPrice()));
                    map.put("customer_name", order.getCustomer().getName());
                    map.put("item_name", order.getItem().getName());

                    return map;
                }).collect(Collectors.toList());
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(collectionDataSource);

            HashMap<String, Object> params = new HashMap<>();
            params.put("image", ClassLoader.getSystemResource("reports/leaf_banner_gray.png").getPath());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            data = JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (FileNotFoundException | JRException e) {
            throw new RuntimeException(e);
        }

        return data;
    }
}
