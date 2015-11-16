package com.example;

import com.example.daos.UserRepo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebAppConfiguration
public class AccountControllerTest {

    @Autowired UserRepo repository;

    @Autowired WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @After
    public void cleanUp() {
    }


    @Test
    public void registration() throws Exception {
        mockMvc.perform(post("/api/register")
                .param("phone", "987654321")
                .param("vcode","1234")
                .param("zone","86"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }
    /*
    @Test
    public void registerAndGetToken() throws Exception {
        mockMvc.perform(post("/register")
                .param("username", "张全蛋")
                .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(content().string("注册成功"));

        MvcResult result = mockMvc.perform(get("/api/token")
                .param("username", "张全蛋")
                .param("password", "password000"))
                .andExpect(status().isOk())
                .andReturn();

        String token = result.getResponse().getContentAsString();

        System.out.println(">>> Token String: " + token);
    }

    @Test
    public void getToken() throws Exception {
        mockMvc.perform(get("/api/token")
                .param("username", "test1")
                .param("password", "123"))
                .andExpect(status().isOk())
                .andExpect(content().string("这就是一个加密了的 Token"));
    }
    */
}
