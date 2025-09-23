package com.project.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.helpers.responses.BaseResponse;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse {
    private String name;
    private float price;
    private String description;
    private String thumpnail;
    private Long categoryId;
//    @JsonProperty("product_images")
//    private List<ProductImage> productImages = new ArrayList<>();

    public static ProductResponse convertProductToResponse(Product product) {
        ProductResponse productResponse = ProductResponse
                .builder()
                .name(product.getName())
                .price(product.getPrice())
                .thumpnail(product.getThumpnail())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())
//                .productImages(product.getProductImages())
                .build();
        productResponse.setCreateAt(product.getCreateAt());
        productResponse.setUpdateAt(product.getUpdateAt());
        return productResponse;
    }
}
