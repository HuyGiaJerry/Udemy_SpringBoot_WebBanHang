package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value=1,message = "OrderId > 1")
    private Long orderId;
    @JsonProperty("product_id")
    @Min(value=1,message = "ProductId > 1")
    private Long productId;
    @Min(value=0,message = "Price >= 0")
    private Long price;
    @Min(value=1,message = "Quantity >= 1")
    private Long quantity;
    @Min(value=0,message = "TotalMoney >= 0")
    private Long total;

    private String color;
}
