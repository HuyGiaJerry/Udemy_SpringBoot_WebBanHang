package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.mappers.OrderDetailMapper;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.responses.OrderDetailResponse;
import com.project.shopapp.services.orderDetail.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {

    private final OrderDetailService orderDetailService;
    private final OrderDetailMapper orderDetailMapper;


    // Create new Order Detail
    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO,
            BindingResult bindingResult
            ){
        try {
            if (bindingResult.hasErrors()) {
                List<String> errMessages = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errMessages);
            }
            OrderDetail orderDetail =  orderDetailService.create(orderDetailDTO);
            return ResponseEntity.ok().body(orderDetailMapper.toResponse(orderDetail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get OrderDetail by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetailById(
        @Valid @PathVariable("id") Long id
    ){
        try {
            OrderDetail orderDetail = orderDetailService.getById(id);
            return ResponseEntity.ok().body(orderDetailMapper.toResponse(orderDetail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // Get All OrderDetails by Order id
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getAllOrderDetailByOrderId(
            @Valid @PathVariable("orderId") Long orderId
    ){
        try {
            List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);
            List<OrderDetailResponse> orderDetailResponses = orderDetails.stream()
                    .map(orderDetailMapper::toResponse)
                    .toList();

            return ResponseEntity.ok().body(orderDetailResponses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // Update OrderDetail by id
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
        @Valid @PathVariable("id") Long id,
        @Valid @RequestBody OrderDetailDTO newOrderDetailDTO,
        BindingResult bindingResult
    ){
        try {
            if (bindingResult.hasErrors()) {
                List<String> errMessages = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errMessages);
            }
            orderDetailService.update(id, newOrderDetailDTO);
            return ResponseEntity.ok().body(newOrderDetailDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // Delete Order Detail by id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(
            @Valid @PathVariable("id") Long id
    ){
        try {
            orderDetailService.delete(id);
            return ResponseEntity.ok().body("Delete Order Detail "+id+" successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
