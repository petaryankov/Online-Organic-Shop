package com.organicshop.web;

import com.organicshop.domain.entities.ProductEntity;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private ProductEntity fruit1, fruit2, fruit3;

    @BeforeEach
    void setUp() {
        fruit1 = testDataUtils.createProductFruit("fruit1");
        fruit2 = testDataUtils.createProductFruit("fruit2");
        fruit3 = testDataUtils.createProductFruit("fruit3");
    }

    @AfterEach
    void tearDown(){
        testDataUtils.cleanUpDatabase();
    }


    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testGetAddProductWorksCorrectly() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-product"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testAddProductCorrectly() throws Exception {

        mockMvc.perform(post("/products/add")
                        .param("name", "Test fruit")
                        .param("description", "Test fruit description")
                        .param("category", "fruit")
                        .param("price", "10.00")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/add"));

    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testAddProductWithInvalidData() throws Exception {

        mockMvc.perform(post("/products/add")
                        .param("name", "")
                        .param("description", "Test fruit description")
                        .param("category", "fruit")
                        .param("price", "10.00")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/add"));

    }

    //TODO: TEST WITH OTHER FIELD INVALID DATA

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testGetEditProductShowsUp() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/products/edit/{id}", fruit1.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-product"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testGetEditProductThrowsException() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/products/edit/28"))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("object-not-found"))
                .andExpect(model().attributeExists("objectId", "objectType"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testDeleteProductWorksCorrectly() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/products/delete/{id}", fruit2.getId())
                        .param("id", fruit2.getId().toString())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories/fruits"));

    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testUpdateProductWorksCorrectly() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.patch("/products/edited/{id}", fruit3.getId())
                        .with(csrf())
                        .param("description", "new description")
                        .param("price", "600"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories/fruits"));

    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testUpdateProductWithInvalidData() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.patch("/products/edited/{id}", fruit3.getId())
                        .with(csrf())
                        .param("description", "new description")
                        .param("price", "-600"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/edit/" + fruit3.getId().toString()));

    }



}