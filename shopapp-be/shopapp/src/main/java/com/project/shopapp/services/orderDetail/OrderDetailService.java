package com.project.shopapp.services.orderDetail;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.helpers.services.BaseServiceImpl;
import com.project.shopapp.mappers.OrderDetailMapper;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.models.Product;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService extends BaseServiceImpl<OrderDetail, OrderDetailDTO,Long>  {


    private final OrderDetailMapper orderDetailMapper;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;


    public OrderDetailService(OrderDetailMapper orderDetailMapper, OrderDetailRepository orderDetailRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        super(orderDetailRepository);
        this.orderDetailMapper = orderDetailMapper;
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public OrderDetail create(OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        // Kiểm tra có order theo orderId request gửi xuống ko ?
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Order not found with id: " + orderDetailDTO.getOrderId()));

        // Kiểm tra có product theo productId request gửi xuống ko ?
        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + orderDetailDTO.getProductId()));

        OrderDetail orderDetail = orderDetailMapper.toEntity(orderDetailDTO);

        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail update(Long id, OrderDetailDTO dto) throws DataNotFoundException {
        // Tìm xem orderDetail có tồn tại ko
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("OrderDetail not found with id: " + id));
        // Kiểm tra có order theo orderId request gửi xuống ko ?
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Order not found with id: " + dto.getOrderId()));
        // Kiểm tra có product theo productId request gửi xuống ko ?
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + dto.getProductId()));

        // Cập nhật thông tin orderDetail
        orderDetailMapper.updateOrderDetailFromDTO(dto, existingOrderDetail);
        existingOrderDetail.setOrder(order);
        existingOrderDetail.setProduct(product);
        // Lưu lại orderDetail đã cập nhật
        return orderDetailRepository.save(existingOrderDetail);
    }

    public List<OrderDetail> getOrderDetailsByOrderId(Long orderId) throws DataNotFoundException {
        // Kiểm tra có order theo orderId request gửi xuống ko ?
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order not found with id: " + orderId));

        return orderDetailRepository.findByOrderId(orderId);
    }

}
