package com.project.shopapp.services.user;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.PermissionDenyException;
import com.project.shopapp.models.Role;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.RoleRepository;
import com.project.shopapp.repositories.UserRepository;
import com.project.shopapp.utils.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtToken jwtToken;
    private final AuthenticationManager authenticationManager;


    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        String phoneNumber = userDTO.getPhoneNumber();
        // Kiểm tra xem sdt đã tồn tại trong hệ thống chưa ?
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Số điện thoại đã tồn tại trong hệ thống");
        }
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role không tồn tại trong hệ thống"));

        if(role.getName().toUpperCase().equals(Role.ADMIN)){
            throw new PermissionDenyException("Không thể tạo account với vai trò ADMIN");
        }
        // Convert UserDTO sang User
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(phoneNumber)
                .address(userDTO.getAddress())
                .password(userDTO.getPassword())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookId(userDTO.getFacebookId())
                .googleId(userDTO.getGoogleId())
                .build();
            newUser.setRole(role);

        // Kiểm tra nếu có accountId , không yêu cầu nhập mật khẩu
        if (userDTO.getFacebookId() == 0 && userDTO.getGoogleId() == 0) {
            String password = userDTO.getPassword();
            // Trong phần spring security
              String encodedPassword = passwordEncoder.encode(password);
              newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) throws Exception {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if(optionalUser.isEmpty()) {
            throw new DataNotFoundException("Invalid phone number or password");
        }
        User existingUser = optionalUser.get();
        // Kiểm tra nếu có accountId , không yêu cầu nhập mật khẩu
        if (existingUser.getFacebookId() == 0 && existingUser.getGoogleId() == 0) {
            if(!passwordEncoder.matches(password, existingUser.getPassword())) {
                throw new BadCredentialsException("Wrong phone number or password");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password,existingUser.getAuthorities());
        // Xác thực người dùng (Kiểm tra có ng dùng trong db ko ...)
        authenticationManager.authenticate(authenticationToken);

        return jwtToken.generateToken(existingUser);
    }
}
