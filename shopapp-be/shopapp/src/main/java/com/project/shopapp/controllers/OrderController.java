package com.project.shopapp.controllers;
import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.mappers.OrderMapper;
import com.project.shopapp.models.Order;
import com.project.shopapp.responses.OrderResponse;
import com.project.shopapp.services.order.OrderService;
import com.project.shopapp.utils.LocalizationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final LocalizationUtil localizationUtil;


    // Insert new Order
    @PostMapping("")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult bindingResult
    ) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errMessages = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errMessages);
            }

            Order order = orderService.create(orderDTO);
            OrderResponse orderResponse = orderMapper.toResponse(order);

            return ResponseEntity.ok().body(orderResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get all orders by user_id
    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOrdersByUserId(
            @Valid @PathVariable("user_id") Long userId) {
        try {
            List<Order> orders = orderService.getByUserId(userId);

            return ResponseEntity.ok().body(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get order by order_id
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(
            @Valid @PathVariable("id") Long id) {
        try {
            Order existingOrder =  orderService.getById(id);
            return ResponseEntity.ok().body(existingOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable Long id,
            @Valid @RequestBody OrderDTO orderDTO
            ){
        try {
            Order updatedOrder = orderService.update(id, orderDTO);
            return ResponseEntity.ok().body(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(
            @Valid @PathVariable Long id
    ){
        // Xóa mềm qua trường active
            orderService.delete(id);
            return ResponseEntity.ok().body("Delete Order "+id+" successful");
    }

}
