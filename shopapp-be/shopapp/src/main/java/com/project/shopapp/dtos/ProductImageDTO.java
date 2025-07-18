package com.project.shopapp.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageDTO {
    @JsonProperty("product_id")
    @Min(value = 1,message = "Product ID must be greater than 0")
    private Long productId;

    @JsonProperty("image_url")
    @Size(min = 5,max = 200, message = "Image URL must be between 5 and 200 characters")
    private String imageUrl;
}
