package com.project.shopapp.services.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.shopapp.responses.ProductListResponse;
import com.project.shopapp.responses.ProductResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Set;


@AllArgsConstructor
@Service
public class ProductRedisService implements IProductRedisService{

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper redisObjectMapper;

    @Override
    public void clear() { // đang là xóa hết key trong redis (hơi nguy hiểm )
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    public void clearProductsCache(){
        Set<String> keys = redisTemplate.keys("all_products::*");
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    private String generateKey(String keyword,
                               Long categoryId,
                               PageRequest pageRequest) {
        return String.format("all_products::keyword=%s::categoryId=%s::page=%d::limit=%d::sort=%s",
                keyword != null ? keyword : "",
                categoryId != null ? categoryId : 0, // 0 mean all category => repository handle
                pageRequest.getPageNumber(),
                pageRequest.getPageSize(),
                pageRequest.getSort().toString().replace(" ", ""));
        // "all_products::keyword=phone::categoryId=2::page=0::limit=10::sort=asc" // sort =>  product id
    }

    @Override
    public ProductListResponse getProducts(String keyword, Long categoryId, PageRequest pageRequest) throws JsonProcessingException {
        String key = this.generateKey(keyword, categoryId, pageRequest);

        String json = (String) redisTemplate.opsForValue().get(key);
        return json != null ? redisObjectMapper.readValue(json, ProductListResponse.class) : null;

    }
    @Override
    public void saveAllProducts(ProductListResponse products, String keyword, Long categoryId, PageRequest pageRequest) throws JsonProcessingException {
        String key = this.generateKey(keyword, categoryId, pageRequest);
        String json = redisObjectMapper.writeValueAsString(products);
        redisTemplate.opsForValue().set(key, json, Duration.ofMinutes(5));
    }
}
