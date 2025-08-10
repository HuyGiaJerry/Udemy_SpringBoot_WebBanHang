package com.project.shopapp.dtos;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    @NotBlank(message = "PhoneNumber is required")
    @JsonProperty("phone_number")
    private String phoneNumber;
    @NotBlank(message = "Passsword cannot be blank")
    private String password;
    @JsonProperty("role_id")
    private Long roleId;
}
