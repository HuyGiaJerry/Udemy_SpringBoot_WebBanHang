package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
    @PostMapping("")
    public ResponseEntity<?> insertOrderDetail(
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
            return ResponseEntity.ok().body("Insert order detail successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetailById(
        @Valid @PathVariable("id") Long id
    ){
        return ResponseEntity.ok().body("Get Order Detail "+id+" successfully");
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getAllOrderDetailByOrderId(
            @Valid @PathVariable("orderId") Long orderId
    ){
        return ResponseEntity.ok().body("Get All Order Details "+orderId+" successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
        @Valid @PathVariable("id") Long id,
        @Valid @RequestBody OrderDetailDTO newOrderDetailDTO
    ){
        return ResponseEntity.ok().body("Update Order Detail "+id+" successfully"+"-- new data "+newOrderDetailDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(
            @Valid @PathVariable("id") Long id
    ){
        return ResponseEntity.ok().body("Delete Order Detail "+id+" successfully");
    }
}
