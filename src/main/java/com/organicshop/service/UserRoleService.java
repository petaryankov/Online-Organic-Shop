package com.organicshop.service;

import com.organicshop.domain.entities.UserRoleEntity;
import com.organicshop.domain.enums.UserRoleEnum;
import com.organicshop.repositories.UserRoleRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public UserRoleEntity getRole(UserRoleEnum role){
        return this.userRoleRepository.findByRole(role);
    }

}
