package com.organicshop.service;

import com.organicshop.domain.entities.UserEntity;
import com.organicshop.domain.entities.UserRoleEntity;
import com.organicshop.domain.enums.GenderEnum;
import com.organicshop.domain.enums.UserRoleEnum;
import com.organicshop.repositories.UserRepository;
import com.organicshop.repositories.UserRoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatabaseInitializationService {

    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;
    private final UserRepository userRepository;

    public DatabaseInitializationService(UserRoleRepository userRoleRepository,
                                         PasswordEncoder passwordEncoder,
                                         CartService cartService,
                                         UserRepository userRepository) {
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        initRoles();
        initAdmin();
        initEmployees();
        initUsers();
    }

    public void initRoles() {
        if (this.userRoleRepository.count() == 0) {
            this.userRoleRepository.saveAllAndFlush(getUserRoles());
        }
    }

    private static List<UserRoleEntity> getUserRoles() {
        List<UserRoleEntity> roles = new ArrayList<>();

        roles.add(new UserRoleEntity().setRole(UserRoleEnum.ADMIN));
        roles.add(new UserRoleEntity().setRole(UserRoleEnum.USER));
        roles.add(new UserRoleEntity().setRole(UserRoleEnum.EMPLOYEE));

        return roles;
    }

    public void initAdmin() {
        if (this.userRepository.count() == 0) {
            UserEntity user = new UserEntity()
                    .setFirstName("Admin")
                    .setLastName("Adminov")
                    .setAge(40)
                    .setEmail("admin@organic.com")
                    .setPassword(passwordEncoder.encode("password1!"))
                    .setGender(GenderEnum.MALE)
                    .setUsername("admin")
                    .setPhoneNumber("0888444444")
                    .setRoles(userRoleRepository.findAll())
                    .setCart(this.cartService.getNewCart());

            this.userRepository.saveAndFlush(user);
        }
    }

    public void initEmployees() {
        if (this.userRepository.count() == 1) {

            UserEntity employeeOne = new UserEntity()
                    .setFirstName("Employee")
                    .setLastName("One")
                    .setAge(22)
                    .setEmail("employee1@organic.com")
                    .setPassword(passwordEncoder.encode("password1!"))
                    .setGender(GenderEnum.MALE)
                    .setUsername("employee1")
                    .setPhoneNumber("0888000001")
                    .setRoles(userRoleRepository.findAll()
                            .stream()
                            .filter(r -> r.getRole() != UserRoleEnum.ADMIN)
                            .collect(Collectors.toList()))
                    .setCart(this.cartService.getNewCart());

            UserEntity employeeTwo = new UserEntity()
                    .setFirstName("Employee")
                    .setLastName("Two")
                    .setAge(25)
                    .setEmail("employee2@organic.com")
                    .setPassword(passwordEncoder.encode("password1!"))
                    .setGender(GenderEnum.FEMALE)
                    .setUsername("employee2")
                    .setPhoneNumber("0888000002")
                    .setRoles(userRoleRepository.findAll()
                            .stream()
                            .filter(r -> r.getRole() != UserRoleEnum.ADMIN)
                            .collect(Collectors.toList()))
                    .setCart(this.cartService.getNewCart());

            this.userRepository.saveAndFlush(employeeOne);
            this.userRepository.saveAndFlush(employeeTwo);
        }

    }

    private void initUsers() {
        if (this.userRepository.count() == 3) {

            UserEntity userOne = new UserEntity()
                    .setFirstName("User")
                    .setLastName("One")
                    .setAge(42)
                    .setEmail("user1@organic.com")
                    .setPassword(passwordEncoder.encode("password1!"))
                    .setGender(GenderEnum.MALE)
                    .setUsername("user1")
                    .setPhoneNumber("0888111111")
                    .setRoles(userRoleRepository.findAll()
                            .stream()
                            .filter(r -> r.getRole() == UserRoleEnum.USER)
                            .collect(Collectors.toList()))
                    .setCart(this.cartService.getNewCart());

            UserEntity userTwo = new UserEntity()
                    .setFirstName("User")
                    .setLastName("Two")
                    .setAge(38)
                    .setEmail("user2@organic.com")
                    .setPassword(passwordEncoder.encode("password1!"))
                    .setGender(GenderEnum.FEMALE)
                    .setUsername("user2")
                    .setPhoneNumber("888222222")
                    .setRoles(userRoleRepository.findAll()
                            .stream()
                            .filter(r -> r.getRole() == UserRoleEnum.USER)
                            .collect(Collectors.toList()))
                    .setCart(this.cartService.getNewCart());

            this.userRepository.saveAndFlush(userOne);
            this.userRepository.saveAndFlush(userTwo);

        }
    }

}
