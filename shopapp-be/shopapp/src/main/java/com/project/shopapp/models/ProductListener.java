package com.project.shopapp.models;


import com.project.shopapp.services.product.IProductRedisService;
import com.project.shopapp.services.product.ProductRedisService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@AllArgsConstructor
public class ProductListener {
    private final ProductRedisService productRedisService;

    private static final Logger logger = LoggerFactory.getLogger(ProductListener.class);


    @PrePersist
    public void prePersist(Product product) {
        logger.info("Product PrePersist");
    }

    @PostPersist
    public void postPersist(Product product) {
        // Update Redis Cache
        logger.info("Product PostPersist");
        productRedisService.clearProductsCache();
    }


    @PreUpdate
    public void preUpdate(Product product) {
        logger.info("Product PreUpdate");

    }

    @PostUpdate
    public void postUpdate(Product product) {
        // Update Redis Cache
        logger.info("Product PostUpdate");
        productRedisService.clearProductsCache();
    }

    @PreRemove
    public void preRemove(Product product) {
        logger.info("Product PreRemove");
    }


    @PostRemove
    public void postRemove(Product product) {
        // Update Redis Cache
        logger.info("Product PostRemove");
        productRedisService.clearProductsCache();
    }


}
