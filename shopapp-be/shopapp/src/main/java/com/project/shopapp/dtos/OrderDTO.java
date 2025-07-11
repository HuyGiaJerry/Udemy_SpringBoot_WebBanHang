package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OrderDTO {
    @JsonProperty("user_id")
    @Min(value = 1,message = "UserId must > 0")
    private long userId;
    @JsonProperty("fullname")
    private String fullName;

    private String email;

    @JsonProperty("phone_number")
    @NotBlank(message = "PhoneNumber cannot be blank")
    @Size(min=5, message ="PhoneNumber > 5 characters")
    private String phoneNumber;

    private String address;

    private String note;
    @Min(value = 1,message = "Total Money must >= 0")
    private Float total;
    @JsonProperty("shipping_method")
    private String shippingMethod;
    @JsonProperty("shipping_address")
    private String shippingAddress;
    @JsonProperty("payment_method")
    private String paymentMethod;


}
