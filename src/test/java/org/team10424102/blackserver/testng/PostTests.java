package org.team10424102.blackserver.testng;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.team10424102.blackserver.App;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.Filter;
import java.util.Locale;
import java.util.Random;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class PostTests extends AbstractTestNGSpringContextTests {
    @Value("${local.server.port}")
    private int port;

    private ObjectMapper mapper = new ObjectMapper();


    protected MockMvc mockMvc;
    @Autowired WebApplicationContext context;
    @Autowired Filter springSecurityFilterChain;


    @BeforeMethod
    public void setUp() {
        //TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
        mockMvc = MockMvcBuilders.webAppContextSetup(context).addFilter(springSecurityFilterChain).build();
    }

    protected String getToken() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(App.API_USER + "token").param("phone", "123456789").param("vcode", "1234"))
                .andExpect(status().isOk()).andReturn();
        String tokenJson = result.getResponse().getContentAsString();
        return mapper.readTree(tokenJson).get("token").asText();
    }

    //@Test(invocationCount = 500, threadPoolSize = 1000)
    public void getSchoolPosts_zh() throws Exception {
        String token = getToken();

        Random rand = new Random();

        if (rand.nextBoolean()) {
            mockMvc.perform(get(App.API_POST + "/SCHOOL?page=0&size=5")
                    .header("X-Token", token)
                    .locale(Locale.SIMPLIFIED_CHINESE))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[4].extension.data.heroName", is("敌法师")));
        } else {
            mockMvc.perform(get(App.API_POST + "/SCHOOL?page=0&size=5")
                    .header("X-Token", token)
                    .locale(Locale.ENGLISH))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[4].extension.data.heroName", is("Anti-Mage")));
        }

    }


}
