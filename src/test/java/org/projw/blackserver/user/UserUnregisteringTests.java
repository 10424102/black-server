package org.projw.blackserver.user;

import com.fasterxml.jackson.databind.deser.Deserializers;
import org.junit.Test;
import org.projw.blackserver.BaseTests;

import static org.projw.blackserver.App.API_USER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserUnregisteringTests extends BaseTests {

    @Test
    public void user_unregisterAccount() throws Exception {
        mockMvc.perform(delete(API_USER)
                .header(AUTH_HEADER, getToken()))
                .andExpect(status().isOk());
    }
}
