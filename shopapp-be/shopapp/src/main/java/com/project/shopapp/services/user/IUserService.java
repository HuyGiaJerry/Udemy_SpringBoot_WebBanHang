package com.project.shopapp.services.user;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.dtos.UserUpdateDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;

    String login (String phoneNumber, String password,Long roleId) throws Exception;

    User getUserDetailFromToken(String token) throws Exception ;

    User updateUser(Long userId, UserUpdateDTO updateUserDTO) throws  Exception;
}
