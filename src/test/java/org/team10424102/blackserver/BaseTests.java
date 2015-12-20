package org.team10424102.blackserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.team10424102.blackserver.config.TokenAuthenticationFilter;

import javax.servlet.Filter;
import java.util.TimeZone;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.team10424102.blackserver.App.API_USER;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
public class BaseTests {
    private static final Logger logger = LoggerFactory.getLogger(BaseTests.class);

    protected MockMvc mockMvc;
    @Autowired WebApplicationContext context;
    @Autowired Filter springSecurityFilterChain;
    private String token;
    private String tokenA;
    private String tokenB;
    public final String UUID_PATTERN = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
    public final String AUTH_HEADER = TokenAuthenticationFilter.HTTP_HEADER;
    public static final int USER_ID = 2;
    public static final int USER_A_ID = 3;
    public static final int USER_B_ID = 4;
    public static final String USER_PHONE = "18921273088";
    public static final String USER_A_PHONE = "18921273089";
    public static final String USER_B_PHONE = "18921273090";
    protected final ObjectMapper mapper = new ObjectMapper();

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
        Object obj = mapper.readValue(json, Object.class);
        System.out.println("\n=========================================================================");
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
        System.out.println("=========================================================================\n");
    }

    protected String getToken() throws Exception {
        if (token != null) return token;
        return token = getTokenInternal(USER_PHONE);
    }

    private String getTokenInternal(String phone) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(API_USER + "/token")
                .param("phone", phone)
                .param("vcode", "1234"))
                .andExpect(status().isOk()).andReturn();
        String tokenJson = result.getResponse().getContentAsString();
        return mapper.readTree(tokenJson).get("token").asText();
    }

    protected String getTokenA() throws Exception {
        if (tokenA != null) return tokenA;
        return tokenA = getTokenInternal(USER_A_PHONE);
    }

    protected String getTokenB() throws Exception {
        if (tokenB != null) return tokenB;
        return tokenB = getTokenInternal(USER_B_PHONE);
    }
}
