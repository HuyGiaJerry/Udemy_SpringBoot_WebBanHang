package com.project.shopapp.mappers;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.responses.OrderDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {

    @Mapping(target = "order.id", source = "orderId")
    @Mapping(target = "product.id", source = "productId")
    OrderDetail toEntity(OrderDetailDTO orderDetailDTO);

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "productId", source = "product.id")
    OrderDetailResponse toResponse(OrderDetail orderDetail);


    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    void updateOrderDetailFromDTO(OrderDetailDTO orderDetailDTO, @MappingTarget OrderDetail orderDetail);



}
