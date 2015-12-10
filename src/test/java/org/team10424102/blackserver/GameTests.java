package org.team10424102.blackserver;

import org.junit.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Locale;

import static com.jcabi.matchers.RegexMatchers.matchesPattern;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class GameTests extends BaseTests {

    @Test
    public void getGame_zh_CN() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(App.API_GAME).header(AUTH_HEADER, getToken())
                .param("identifier", "MINECRAFT").locale(Locale.SIMPLIFIED_CHINESE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identifier", is("MINECRAFT")))
                .andExpect(jsonPath("$.logo", matchesPattern(UUID_PATTERN)))
                .andExpect(jsonPath("$.localizedName", is("我的世界")));
    }

    @Test
    public void getGame_en_US() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(App.API_GAME).header(AUTH_HEADER, getToken())
                .param("identifier", "MINECRAFT").locale(Locale.ENGLISH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identifier", is("MINECRAFT")))
                .andExpect(jsonPath("$.logo", matchesPattern(UUID_PATTERN)))
                .andExpect(jsonPath("$.localizedName", is("Minecraft")));
    }

}
