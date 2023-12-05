package com.organicshop.util;

import com.organicshop.domain.dto.view.OrderViewDto;
import com.organicshop.domain.entities.*;
import com.organicshop.domain.enums.GenderEnum;
import com.organicshop.domain.enums.OrderStatusEnum;
import com.organicshop.domain.enums.ProductCategoryEnum;
import com.organicshop.domain.enums.UserRoleEnum;
import com.organicshop.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
public class TestDataUtils {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final ShoppingCartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TestDataUtils(UserRoleRepository userRoleRepository,
                         UserRepository userRepository,
                         ShoppingCartRepository cartRepository,
                         ProductRepository productRepository,
                         OrderRepository orderRepository,
                         ModelMapper modelMapper) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    private void initRoles() {
        if (userRoleRepository.count() == 0) {
            UserRoleEntity adminRole = new UserRoleEntity().setRole(UserRoleEnum.ADMIN);
            UserRoleEntity userRole = new UserRoleEntity().setRole(UserRoleEnum.USER);
            UserRoleEntity employeeRole = new UserRoleEntity().setRole(UserRoleEnum.EMPLOYEE);

            userRoleRepository.save(adminRole);
            userRoleRepository.save(userRole);
            userRoleRepository.save(employeeRole);
        }
    }

    public UserEntity createTestAdmin(String email,
                                      String username) {

        initRoles();

        UserEntity admin = new UserEntity()
                .setEmail(email)
                .setUsername(username)
                .setFirstName("Admin")
                .setLastName("Adminov")
                .setPassword("password")
                .setGender(GenderEnum.MALE)
                .setAge(42)
                .setCart(createCart())
                .setOrders(new ArrayList<>())
                .setPhoneNumber("0888123456")
                .setRoles(userRoleRepository.findAll());

        return userRepository.save(admin);
    }

    public void addProduct(UserEntity user,
                           ProductEntity product) {
        user.getCart().addProduct(product);
    }


    public UserEntity createTestUser(String email,
                                     String username) {

        initRoles();

        UserEntity user = new UserEntity()
                .setEmail(email)
                .setUsername(username)
                .setFirstName("UserFirstName1")
                .setLastName("UserLastName1")
                .setPassword("password")
                .setGender(GenderEnum.MALE)
                .setAge(28)
                .setCart(createCart())
                .setOrders(new ArrayList<>())
                .setPhoneNumber("0888112233")
                .setRoles(userRoleRepository.
                        findAll().stream().
                        filter(r -> r.getRole() == UserRoleEnum.USER).
                        toList());

        return userRepository.save(user);
    }

    public UserEntity createTestEmployee(String email,
                                         String username) {

        initRoles();

        UserEntity user = new UserEntity()
                .setEmail(email)
                .setUsername(username)
                .setFirstName("UserFirstName2")
                .setLastName("UserLastName")
                .setPassword("password")
                .setGender(GenderEnum.FEMALE)
                .setAge(28)
                .setCart(createCart())
                .setOrders(new ArrayList<>())
                .setPhoneNumber("0888987654")
                .setRoles(userRoleRepository.
                        findAll().stream().
                        filter(r -> r.getRole() != UserRoleEnum.ADMIN).
                        toList());

        return userRepository.save(user);
    }

    public OrderViewDto createOrderDetailViewDto(String ownerEmail,
                                                 String ownerName) {

        OrderEntity entity = new OrderEntity()
                .setOwner(createTestUser(ownerEmail, ownerName))
                .setPrice(BigDecimal.TEN)
                .setAddress("orderAddress")
                .setCreatedOn(LocalDateTime.now())
                .setContactNumber("orderContactNumber")
                .setStatus(OrderStatusEnum.IN_PROGRESS);

        return this.modelMapper.map(entity, OrderViewDto.class);
    }


    public CartEntity createCart() {

        CartEntity cart = new CartEntity();

        return cartRepository.save(cart);

    }

    public ProductEntity createProductBread(String name) {

        ProductEntity product = new ProductEntity()
                .setPrice(BigDecimal.TEN)
                .setName(name)
                .setCategory(ProductCategoryEnum.bread)
                .setDescription("description product");

        return productRepository.saveAndFlush(product);
    }

    public ProductEntity createProductFruit(String name) {

        ProductEntity product = new ProductEntity()
                .setPrice(BigDecimal.TEN)
                .setName(name)
                .setCategory(ProductCategoryEnum.fruits)
                .setDescription("description product");

        return productRepository.saveAndFlush(product);
    }

    public OrderEntity createOrder(UserEntity owner) {

        OrderEntity order = new OrderEntity()
                .setPrice(BigDecimal.TEN)
                .setCreatedOn(LocalDateTime.now())
                .setAddress("orderAddress")
                .setContactNumber("0888456789")
                .setComment("orderComment")
                .setOwner(owner)
                .setStatus(OrderStatusEnum.IN_PROGRESS);

        return orderRepository.save(order);
    }


    public void cleanUpDatabase() {
        orderRepository.deleteAll();
        userRepository.deleteAll();
        cartRepository.deleteAll();
        productRepository.deleteAll();
        userRoleRepository.deleteAll();
    }

}
