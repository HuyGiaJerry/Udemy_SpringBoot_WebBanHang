package com.project.shopapp.controllers;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.dtos.UserLoginDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errMessages = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errMessages);
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword()))
            {
                return ResponseEntity.badRequest().body("Passwords do not match");
            }
            return ResponseEntity.ok().body("Register successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO
    )
    {
        // Kiểm tra thông tin đăng nhập
        //  Trả về token trong response
        return ResponseEntity.ok().body("Some token");
    }

}
