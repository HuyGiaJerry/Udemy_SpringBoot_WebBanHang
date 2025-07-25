package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @JsonProperty("fullname")
    private String fullName;
    @NotBlank(message = "PhoneNumber is required")
    @JsonProperty("phone_number")
    private String phoneNumber;
    private String address;
    @NotBlank(message = "Passsword cannot be blank")
    private String password;
    @JsonProperty("retype_password")
    private String retypePassword;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;
    @JsonProperty("facebook_id")
    private Long facebookId;
    @JsonProperty("google_id")
    private Long googleId;
    @NotNull(message = "RoleId is required")
    @JsonProperty("role_id")
    private Long roleId;

}
