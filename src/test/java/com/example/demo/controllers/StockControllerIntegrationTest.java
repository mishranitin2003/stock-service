package com.example.demo.controllers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StockControllerIntegrationTest {

    @Autowired
    private StockController stockController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenUserControllerInjected_thenNotNull() throws Exception {
        Assert.assertNotNull(stockController);
    }

    @Test
    public void whenGetRequestToUsersEndPointWithIdPathVariable_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/recommendation/{product}", "a").param("retailer", "One-Retailer"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$['product']").value("a"));
    }

}
