package com.project.shopapp.services.order;

import com.project.shopapp.dtos.CartItemDTO;
import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.helpers.services.BaseServiceImpl;
import com.project.shopapp.mappers.OrderMapper;
import com.project.shopapp.models.*;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService extends BaseServiceImpl<Order, OrderDTO, Long> {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, OrderMapper orderMapper, ProductRepository productRepository, OrderDetailRepository orderDetailRepository) {
        super(orderRepository);
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;
        this.productRepository = productRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public Order create(OrderDTO orderDTO) throws DataNotFoundException {

            User user = userRepository.
                    findById(orderDTO.getUserId())
                    .orElseThrow(() -> new DataNotFoundException("User not found with id: " + orderDTO.getUserId()));
            // Convert OrderDTO to Order entity
            // Dùng thư viện Map struct
            Order order = orderMapper.toEntity(orderDTO);
            order.setUser(user);
            order.setOrderDate(new Date());
            order.setStatus(OrderStatus.PENDING);
            order.setActive(true);
            LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
            if(shippingDate.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Shipping date must be in the future");
            }

            order.setShippingDate(shippingDate);
            order.setTotal(orderDTO.getTotal());
            orderRepository.save(order);
            // Tạo danh sách các đối tượng OrderDetail từ cart items
            List<OrderDetail> orderDetails = new ArrayList<>();
            for(CartItemDTO cartItemDTO : orderDTO.getCartItems())
            {
                // Tạo ra đối tượng OrderDetail từ CartItemDTO
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);

                // Lấy thông tin sản phẩm từ cartItemDTO
                Long productId = cartItemDTO.getProductId();
                int quantity = cartItemDTO.getQuantity();


                // Tìm thông tin sản phẩm từ csdl (có thể cache nếu cần)
                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + productId));

                // Đặt thông tin cho orderDetail
                orderDetail.setProduct(product);
                orderDetail.setQuantity(quantity);
                orderDetail.setPrice(product.getPrice());
                orderDetail.setTotal(product.getPrice() * quantity);
                orderDetails.add(orderDetail);
            }
            orderDetailRepository.saveAll(orderDetails);
            return order;


    }

    @Override
    @Transactional
    public Order update(Long id, OrderDTO dto) throws DataNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order not found with id: " + id));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User not found with id: " + dto.getUserId()));
        orderMapper.updateOrderFromDTO(dto, order);
        order.setUser(user);
        order.setShippingDate(dto.getShippingDate());
        orderRepository.save(order);
        return order;
    }

    @Override
    @Transactional
    public void delete(Long id){
        Order order = orderRepository.findById(id).orElse(null);
        if(order!= null) {
            order.setActive(false); // Đánh dấu là không hoạt động thay vì xóa cứng
            orderRepository.save(order);
        }
    }



    public List<Order> getByUserId(Long userId){
        return orderRepository.findByUserId(userId);
    }



}
