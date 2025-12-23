package com.project.shopapp.controllers;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.dtos.UserLoginDTO;
import com.project.shopapp.models.User;
import com.project.shopapp.responses.LoginResponse;
import com.project.shopapp.responses.RegisterResponse;
import com.project.shopapp.responses.UserResponse;
import com.project.shopapp.services.user.UserService;
import com.project.shopapp.utils.LocalizationUtil;
import com.project.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

    private final UserService userService;
    // Inject Component for Response and Language
    private final LocalizationUtil localizationUtil;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errMessages = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(RegisterResponse
                        .builder()
                        .message(localizationUtil.getLocalizedMessage(MessageKeys.REGISTER_FAILED, errMessages.toString()))
                        .build()
                );
            }
            if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                return ResponseEntity.badRequest().body(RegisterResponse
                        .builder()
                        .message(localizationUtil.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH))
                        .build());
            }
            User user = userService.createUser(userDTO);
            return ResponseEntity.ok().body(RegisterResponse
                    .builder()
                    .message(localizationUtil.getLocalizedMessage(MessageKeys.REGISTER_SUCCESS))
                    .user(user)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(RegisterResponse
                    .builder()
                    .message(localizationUtil.getLocalizedMessage(MessageKeys.REGISTER_FAILED, e.getMessage()))
                    .build()

            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO
    ) {
        try {
            // Kiểm tra thông tin đăng nhập
            String token = userService.login(
                    userLoginDTO.getPhoneNumber(),
                    userLoginDTO.getPassword(),
                    userLoginDTO.getRoleId() == null ? 1 : userLoginDTO.getRoleId()
            );

            //  Trả về dạng json : token , message
            return ResponseEntity.ok().body(LoginResponse
                    .builder()
                    .message(localizationUtil.getLocalizedMessage(MessageKeys.LOGIN_SUCCESS))
                    .token(token)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(LoginResponse
                    .builder()
                    .message(localizationUtil.getLocalizedMessage(MessageKeys.LOGIN_FAILED, e.getMessage()))
                    .build());
        }
    }@PostMapping("/details")
    public ResponseEntity<UserResponse> getUserDetails(@RequestHeader("Authorization") String token){
        try{
            String extractedToken = token.substring(7); // Loại bỏ "Bearer " khỏi chuỗi token
            User user = userService.getUserDetailFromToken(extractedToken);
            return ResponseEntity.ok(UserResponse.fromUser(user));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }




}
