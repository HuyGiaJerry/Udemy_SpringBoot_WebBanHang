package com.project.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Role;
import com.project.shopapp.models.User;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse   {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("fullname")
    private String fullname;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private String address;
    @JsonProperty("date_of_birth")
    private Date dateOfBirth;
    private boolean active;
    @JsonProperty("facebook_id")
    private Long facebookId;
    @JsonProperty("google_id")
    private Long googleId;
    @JsonProperty("role")
    private Role role;

    public static UserResponse fromUser(User user){
        if (user == null) return null;
        return UserResponse.builder()
                .id(user.getId())
                .fullname(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .dateOfBirth(user.getDateOfBirth())
                .active(user.getActive() != null ? user.getActive() : false)
                .facebookId(user.getFacebookId())
                .googleId(user.getGoogleId())
                .role(user.getRole())
                .build();
    }
}
