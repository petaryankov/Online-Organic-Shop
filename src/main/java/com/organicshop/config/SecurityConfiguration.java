package com.organicshop.config;

import com.organicshop.domain.enums.UserRoleEnum;
import com.organicshop.repositories.UserRepository;
import com.organicshop.service.OrganicShopUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeHttpRequests()
                //static resources
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                //for everyone
                .requestMatchers("/",
                        "/categories",
                        "/categories/**",
                        "/contact",
                        "/users/profile").permitAll()
                //for none users
                .requestMatchers("/users/login"
                        , "/users/register",
                        "/users/login-error",
                        "/",
                        "/contact").anonymous()
                //for users
                .requestMatchers("/categories/**",
                        "/categories",
                        "/closed",
                        "/contact",
                        "/users/edit/**",
                        "/orders/finalize",
                        "/",
                        "/cart",
                        "/orders/details/**",
                        "/orders/history",
                        "/users/profile").hasRole(UserRoleEnum.USER.name())
                //for employees
                .requestMatchers("/",
                        "/categories",
                        "/categories/**",
                        "/users/edit/**",
                        "/orders/details/**",
                        "/orders/all/history",
                        "/messages",
                        "/users/profile").hasRole(UserRoleEnum.EMPLOYEE.name())
                //for admins
                .requestMatchers(
                        "/",
                        "/products/add",
                        "/users/all",
                        "/categories",
                        "/categories/**",
                        "/products/edit/**",
                        "/orders/all/history",
                        "/users/change/**",
                        "/users/profile",
                        "/messages",
                        "/users/profile/**")
                .hasRole(UserRoleEnum.ADMIN.name())

                .anyRequest()
                .authenticated()

                .and()
                .formLogin()
                .loginPage("/users/login")
                .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
                .defaultSuccessUrl("/")
                .failureForwardUrl("/users/login-error")

                .and()
                .logout()
                .logoutUrl("/users/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)

                .and()
                .rememberMe()
                .key("placeForUniqueKeyOrganicShop")
                .tokenValiditySeconds(7 * 24 * 60 * 60);

        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new OrganicShopUserDetailsService(userRepository);
    }

}