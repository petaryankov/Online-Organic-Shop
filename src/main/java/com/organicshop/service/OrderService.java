package com.organicshop.service;

import com.organicshop.domain.dto.binding.OrderBindingDto;
import com.organicshop.domain.dto.view.OrderViewDto;
import com.organicshop.domain.dto.view.ProductViewDto;
import com.organicshop.domain.entities.OrderEntity;
import com.organicshop.domain.entities.ProductEntity;
import com.organicshop.domain.entities.UserEntity;
import com.organicshop.domain.enums.OrderStatusEnum;
import com.organicshop.exception.NotFoundObjectException;
import com.organicshop.repositories.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.organicshop.constants.Messages.*;

@Service
public class OrderService {

    private final UserService userService;

    private final OrderRepository orderRepository;

    private final ModelMapper modelMapper;

    public OrderService(UserService userService,
                        OrderRepository orderRepository,
                        ModelMapper modelMapper) {
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public List<ProductViewDto> getProductsInTheCart(String username) {

        final UserEntity user = this.userService.getUserByUsername(username);

        return user.getCart()
                .getProducts()
                .stream()
                .map(this::mapToProductViewDto)
                .collect(Collectors.toList());
    }

    private ProductViewDto mapToProductViewDto(ProductEntity productEntity) {
        return this.modelMapper.map(productEntity, ProductViewDto.class);
    }

    public BigDecimal getProductsPrice(String username) {

        final UserEntity user = this.userService.getUserByUsername(username);

        return user.getCart().getProductsSum();

    }

    public BigDecimal getTotalPrice(String username) {
        OrderBindingDto orderBindingDto = new OrderBindingDto();
        final UserEntity user = this.userService.getUserByUsername(username);

        return user.getCart().getProductsSum()
                .add(BigDecimal.valueOf(user.getCart().getCountProducts() * orderBindingDto.getBagFee()))
                .add(BigDecimal.valueOf(orderBindingDto.getDeliveryFee()));
    }

    @Transactional
    public void makeOrder(OrderBindingDto orderDto,
                          String username) {

        OrderEntity order = new OrderEntity();

        final UserEntity user = this.userService.getUserByUsername(username);

        createOrder(orderDto, order, user);

        this.orderRepository.saveAndFlush(order);

        user.getCart().setProducts(new ArrayList<>()).setProductsSum(BigDecimal.ZERO);
        user.getCart().setCountProducts(0L);
    }

    private static void createOrder(OrderBindingDto orderDto,
                                    OrderEntity order,
                                    UserEntity user) {

        BigDecimal price = user.getCart().getProductsSum()
                .add(BigDecimal.valueOf(user.getCart().getCountProducts() * orderDto.getBagFee()))
                .add(BigDecimal.valueOf(orderDto.getDeliveryFee()));

        order
                .setOwner(user)
                .setPrice(price)
                .setCreatedOn(LocalDateTime.now())
                .setComment(orderDto.getComment() != null ? orderDto.getComment() : NO_COMMENT)
                .setAddress(orderDto.getAddress())
                .setContactNumber(orderDto.getContactNumber())
                .setStatus(OrderStatusEnum.IN_PROGRESS);
    }

    @Transactional
    public List<OrderViewDto> getOrdersByUser(String username) {

        final UserEntity user = this.userService.getUserByUsername(username);

        return this.orderRepository
                .findAllByOwner_Id(user.getId())
                .stream()
                .map(this::mapToOrderViewDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<OrderViewDto> getInProgressOrdersByUser(UserEntity userEntity) {

        return this.orderRepository
                .findAllByStatusAndOwner_Id(OrderStatusEnum.IN_PROGRESS, userEntity.getId())
                .stream()
                .map(this::mapToOrderViewDto)
                .collect(Collectors.toList());
    }

    private OrderViewDto mapToOrderViewDto(OrderEntity orderEntity) {

        OrderViewDto orderDetail = this.modelMapper.map(orderEntity, OrderViewDto.class);

        orderDetail.setClient(orderEntity.getOwner().getUsername());

        return orderDetail;
    }

    public OrderViewDto getOrderById(Long id) {

        OrderEntity order = this.orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundObjectException(id, ORDER));

        if (order.getComment().isEmpty()) {
            order.setComment(NO_COMMENT_MESSAGE);
        }

        return mapToOrderViewDto(order);
    }

    public List<OrderViewDto> getAllOrders() {
        return this.orderRepository
                .findAll()
                .stream()
                .map(this::mapToOrderViewDto)
                .collect(Collectors.toList());
    }

    public void finishOrder(Long orderId) {

        OrderEntity orderEntity = this.orderRepository
                .findById(orderId)
                .orElseThrow(() -> new NotFoundObjectException(orderId, ORDER));

        orderEntity.setStatus(OrderStatusEnum.DELIVERED);
        orderEntity.setDeliveredOn(LocalDateTime.now());

        this.orderRepository.saveAndFlush(orderEntity);
    }

    public void cancelOrder(Long orderId) {

        OrderEntity orderEntity = this.orderRepository
                .findById(orderId)
                .orElseThrow(() -> new NotFoundObjectException(orderId, ORDER));

        orderEntity.setStatus(OrderStatusEnum.CANCELLED);

        this.orderRepository.saveAndFlush(orderEntity);
    }

}
