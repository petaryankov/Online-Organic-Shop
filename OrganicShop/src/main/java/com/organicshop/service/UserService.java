package com.organicshop.service;

import com.organicshop.domain.dto.binding.EditUserBindingDto;
import com.organicshop.domain.dto.binding.UserRegistrationBindingDto;
import com.organicshop.domain.dto.view.UserViewDto;
import com.organicshop.domain.entities.CartEntity;
import com.organicshop.domain.entities.UserEntity;
import com.organicshop.domain.entities.UserRoleEntity;
import com.organicshop.domain.enums.UserRoleEnum;
import com.organicshop.exception.NotFoundObjectException;
import com.organicshop.repositories.UserRepository;
import com.organicshop.repositories.UserRoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.organicshop.constants.Messages.USER;
import static com.organicshop.constants.Messages.EMPLOYEE;

@Service
public class UserService {
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final UserRoleRepository userRoleRepository;
    private final CartService cartService;


    public UserService(ModelMapper modelMapper,
                       PasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       UserRoleService userRoleService,
                       UserRoleRepository userRoleRepository,
                       CartService cartService) {
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.userRoleRepository = userRoleRepository;
        this.cartService = cartService;
    }

    public void registerUser(UserRegistrationBindingDto userToRegister) {

        UserEntity userToSave = this.mapToUserEntity(userToRegister);

        final UserRoleEntity userRole = this.userRoleService.getRole(UserRoleEnum.USER);
        final CartEntity shoppingCart = this.cartService.getNewCart();

        userToSave
                .setPassword(passwordEncoder.encode(userToSave.getPassword()))
                .setRoles(new ArrayList<>(Collections.singletonList(userRole)))
                .setCart(shoppingCart);

        this.userRepository.saveAndFlush(userToSave);
    }

    public UserEntity getUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    public UserViewDto getUserViewByUsername(String username) {
        return mapToUserView(this.userRepository.findByUsername(username));
    }

    public UserViewDto getUserById(Long id) {
        final UserEntity userById = this.userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundObjectException(id, USER));

        return this.mapToUserView(userById);
    }

    public UserEntity mapToUserEntity(UserRegistrationBindingDto modelDto) {
        return this.modelMapper.map(modelDto, UserEntity.class);
    }

    public UserViewDto mapToUserView(UserEntity userEntity) {
        return this.modelMapper.map(userEntity, UserViewDto.class);
    }

    public List<UserViewDto> getAllUsers() {
        return this.userRepository.findAll().stream().map(this::mapToUserView).toList();
    }

    public void removeRole(Long userId) {

        UserEntity userById = this.userRepository.findUserEntityById(userId);

        userById.getRoles().removeIf(userRoleEntity -> userRoleEntity.getRole().name().equals(EMPLOYEE));

        this.userRepository.saveAndFlush(userById);

    }

    public void addRole(Long userId) {

        UserEntity userById = this.userRepository.findUserEntityById(userId);

        userById.getRoles().add(userRoleRepository.findByRole(UserRoleEnum.EMPLOYEE));

        this.userRepository.saveAndFlush(userById);

    }

    public void editUser(Long id,
                            EditUserBindingDto editedUser) {

        UserEntity user = this.userRepository.findUserEntityById(id);

        user.setFirstName(editedUser.getFirstName()).setLastName(editedUser.getLastName());
        this.userRepository.saveAndFlush(user);
    }

}
