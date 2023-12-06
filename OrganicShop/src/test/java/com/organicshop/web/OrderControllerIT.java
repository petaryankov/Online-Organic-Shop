package com.organicshop.web;

import com.organicshop.domain.entities.OrderEntity;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private UserEntity testUser, testAdmin;

    private OrderEntity testOrder;

    @BeforeEach
    void setUp() {
        testUser = testDataUtils.createTestUser("user@user.com", "userTest");
        testAdmin = testDataUtils.createTestAdmin("admin@admin.com", "adminTest");
        testOrder = testDataUtils.createOrder(testUser);
    }

    @AfterEach
    void tearDown() {
        testDataUtils.cleanUpDatabase();
    }

    @Test
    @WithMockUser(username = "userTest", roles = "USER")
    void testGetFinalize_ShowsUp() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/finalize"))
                .andExpect(status().isOk())
                .andExpect(view().name("finalize-order"))
                .andExpect(model().attributeExists("totalPrice", "countProducts"));

    }

    @Test
    @WithMockUser(username = "userTest", roles = "USER")
    void testFinalizeOrder_WorksCorrectly() throws Exception {
        mockMvc.perform(post("/orders/finalize")
                        .param("comment", "Test comment")
                        .param("address", "Test address")
                        .param("contactNumber", "088876543")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/finalize"));
    }

    @Test
    @WithMockUser(username = "userTest", roles = "USER")
    void testFinalizeOrder_WithInvalidData() throws Exception {
        mockMvc.perform(post("/orders/finalize")
                        .param("comment", "Test comment")
                        .param("address", "Test address")
                        .param("contactNumber", "")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/finalize"));
    }

    @Test
    @WithMockUser(username = "userTest", roles = "USER")
    void testGetOrdersHistory_ShowsUp() throws Exception {
        mockMvc.perform(get("/orders/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders-history-user"))
                .andExpect(model().attributeExists("orders"));
    }

    @Test
    @WithMockUser(username = "userTest", roles = "USER")
    void testGetOrderDetails_ShowsUp() throws Exception {
        mockMvc.perform(get("/orders/details/{id}", testOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("order-details-api"))
                .andExpect(model().attributeExists("order", "idAtr"));
    }

    @Test
    @WithMockUser(username = "userTest", roles = "USER")
    void testGetOrderDetails_ThrowsException() throws Exception {
        mockMvc.perform(get("/orders/details/789"))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("object-not-found"))
                .andExpect(model().attributeExists("objectId", "objectType"));
    }

    @Test
    @WithMockUser(username = "adminTest", roles = {"USER", "ADMIN", "EMPLOYEE"})
    void testGetAllOrdersHistory_ShowsUp() throws Exception {
        mockMvc.perform(get("/orders/all/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders-history"))
                .andExpect(model().attributeExists("allOrders"));
    }

}
