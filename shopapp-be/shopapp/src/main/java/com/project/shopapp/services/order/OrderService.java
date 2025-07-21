package com.project.shopapp.services.order;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.helpers.services.BaseServiceImpl;
import com.project.shopapp.mappers.OrderMapper;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderStatus;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class OrderService extends BaseServiceImpl<Order, OrderDTO, Long> {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, OrderMapper orderMapper) {
        super(orderRepository);
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;

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
            orderRepository.save(order);
            return order;


    }

    @Override
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

    public List<Order> getByUserId(Long userId){
        return orderRepository.findByUserId(userId);
    }

    @Override
    public void delete(Long id) throws DataNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order not found with id: " + id));
        order.setActive(false); // Thay vì xóa cứng, đánh dấu là không hoạt động
        orderRepository.save(order);
    }

}
