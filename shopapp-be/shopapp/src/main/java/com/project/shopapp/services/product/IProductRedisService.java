package com.project.shopapp.services.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.shopapp.responses.ProductListResponse;
import com.project.shopapp.responses.ProductResponse;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductRedisService {
    void clear();


    ProductListResponse getProducts(String keyword,
                                    Long categoryId,
                                    PageRequest pageRequest) throws JsonProcessingException;

    void saveAllProducts(ProductListResponse products,
                         String keyword,
                         Long categoryId,
                         PageRequest pageRequest
                         ) throws JsonProcessingException;
}
