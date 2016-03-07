package org.projw.blackserver.user;

import org.junit.Test;
import org.projw.blackserver.BaseTests;

import static org.hamcrest.CoreMatchers.anything;
import static org.projw.blackserver.App.API_USER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserTokenTests extends BaseTests {

    @Test
    public void getToken_withInvalidPhone() throws Exception {
        //TODO 使用真正的手机短信验证框架后, 修改该测试
        mockMvc.perform(get(API_USER + "/token")
                .param("phone", "11111111111")
                .param("vcode", "1234"))
                .andExpect(status().isBadRequest());
    }

    // TODO expected = VcodeVerificationException.class
    @Test(expected = Exception.class)
    public void getToken_withInvalidVcode() throws Exception {
        mockMvc.perform(get(API_USER + "/token")
                .param("phone", "15610589653")
                .param("vcode", "xxxx"));
    }

    @Test
    public void getToken_withValidVcode() throws Exception {
        //TODO 使用真正的手机短信验证框架后, 修改该测试
        mockMvc.perform(get(API_USER + "/token")
                .param("phone", "15610589653")
                .param("vcode", "1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", anything()));
    }

    @Test
    public void getToken_missingParameter() throws Exception {
        mockMvc.perform(get(API_USER + "/token")
                .param("phone", "15610589653"))
                .andExpect(status().isBadRequest());
    }

}
