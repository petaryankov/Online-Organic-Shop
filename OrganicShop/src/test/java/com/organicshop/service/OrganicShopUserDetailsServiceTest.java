package com.organicshop.service;

import com.organicshop.domain.entities.UserEntity;
import com.organicshop.domain.entities.UserRoleEntity;
import com.organicshop.domain.enums.GenderEnum;
import com.organicshop.domain.enums.UserRoleEnum;
import com.organicshop.repositories.UserRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opentest4j.AssertionFailedError;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrganicShopUserDetailsServiceTest {

    @Mock
    private UserRepository mockUserRepository;

    private OrganicShopUserDetailsService serviceToTest;

    @BeforeEach
    void setUp() {
        serviceToTest = new OrganicShopUserDetailsService(mockUserRepository);
    }

    @Test
    void testLoadUserByUsername_UserExists() {

        UserEntity testUser = new UserEntity()
                .setUsername("user")
                .setPassword("password")
                .setEmail("email@email.com")
                .setAge(42)
                .setPhoneNumber("0888444444")
                .setFirstName("FirstName")
                .setLastName("LastName")
                .setGender(GenderEnum.MALE)
                .setRoles(List.of(
                        new UserRoleEntity().setRole(UserRoleEnum.ADMIN),
                        new UserRoleEntity().setRole(UserRoleEnum.EMPLOYEE),
                        new UserRoleEntity().setRole(UserRoleEnum.USER)
                ));

        when(mockUserRepository.findUserEntityByUsername(testUser.getUsername()))
                .thenReturn(Optional.of(testUser));

        UserDetails userDetails = serviceToTest.loadUserByUsername(testUser.getUsername());

        Assertions.assertNotNull(userDetails);

        Assertions.assertEquals(testUser.getUsername(), userDetails.getUsername());
        Assertions.assertEquals(testUser.getPassword(), userDetails.getPassword());

        Assertions.assertEquals(3, userDetails.getAuthorities().size());
        assertRole(userDetails.getAuthorities(), "ROLE_ADMIN");
        assertRole(userDetails.getAuthorities(), "ROLE_EMPLOYEE");
        assertRole(userDetails.getAuthorities(), "ROLE_USER");
    }

    private void assertRole(Collection<? extends GrantedAuthority> authorities,
                            String role) {
        authorities.
                stream().
                filter(a -> role.equals(a.getAuthority())).
                findAny().
                orElseThrow(() -> new AssertionFailedError("Role " + role + " not found!"));
    }

    @Test
    void testLoadUserByUsername_UserDoesNotExist() {
        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> serviceToTest.loadUserByUsername("unregistered")
        );
    }

}
