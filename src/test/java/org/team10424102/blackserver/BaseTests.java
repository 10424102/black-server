package org.team10424102.blackserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.team10424102.blackserver.config.security.TokenAuthenticationFilter;

import javax.servlet.Filter;
import javax.transaction.Transactional;
import java.util.TimeZone;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.team10424102.blackserver.App.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebAppConfiguration
@IntegrationTest("server.port:0") // random port
@Transactional
public class BaseTests {
    protected MockMvc mockMvc;
    @Autowired WebApplicationContext context;
    @Autowired Filter springSecurityFilterChain;
    private String token;
    public final String UUID_PATTERN = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
    public final String AUTH_HEADER = TokenAuthenticationFilter.HTTP_HEADER;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).addFilter(springSecurityFilterChain).build();
    }

    @BeforeClass
    public static void beforeClass() {
        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
    }

    protected void printFormatedJsonString(MvcResult result) throws Exception {
        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Object obj = mapper.readValue(json, Object.class);
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
    }

    protected String getToken() throws Exception {
        if (token != null) return token;
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(API_TOKEN)
                .param("phone", "123456789")
                .param("vcode", "1234"))
                .andExpect(status().isOk()).andReturn();
        String tokenJson = result.getResponse().getContentAsString();
        JSONObject obj = new JSONObject(tokenJson);
        token = obj.getString("token");
        return token;
    }
}
