package com.organicshop.web;

import com.organicshop.domain.entities.ProductEntity;
import com.organicshop.domain.entities.UserEntity;
import com.organicshop.util.TestDataUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private UserEntity testUser;

    private ProductEntity fruit, bread;

    @BeforeEach
    void setUp() {
        testUser = testDataUtils.createTestUser("user1@user.com", "userTest");

        bread = testDataUtils.createProductBread("white bread");
        fruit = testDataUtils.createProductFruit("orange");

        testDataUtils.addProduct(testUser, bread);
    }

    @AfterEach
    void tearDown() {
        testDataUtils.cleanUpDatabase();
    }


    @Test
    @WithMockUser(username = "userTest", roles = "USER")
    void testGetCart_ShowsUp() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/cart"))
                .andExpect(view().name("order-cart"))
                .andExpect(model().attributeExists("cartProducts", "productsPrice", "countProducts"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "userTest", roles = "USER")
    void testAddingProductToCart_WorksCorrectly() throws Exception {

        mockMvc.perform(patch("/cart/add/{id}", bread.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories/bread"));

    }

    @Test
    @WithMockUser(username = "userTest", roles = "USER")
    void testRemoveFromCart_WorksCorrectly() throws Exception {

        mockMvc.perform(patch("/cart/remove/{id}", bread.getId())
                        .param("id", bread.getId().toString()).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

    }

}