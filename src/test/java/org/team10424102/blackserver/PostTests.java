package org.team10424102.blackserver;

import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Locale;
import java.util.Random;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostTests extends BaseTests {

    @Test
    public void createPost() throws Exception {
        String token = getToken();

        mockMvc.perform(MockMvcRequestBuilders.post(App.API_POST).param("content", "不要给我生猴子").header("X-Token", token))
                .andExpect(status().isOk());
    }

    @Test
    public void deletePost() {

    }

    @Test
    public void getSchoolPosts() throws Exception {
        String token = getToken();

        MvcResult result = mockMvc.perform(get(App.API_POST + "/SCHOOL?page=0&size=5")
                .header("X-Token", token))
                .andExpect(status().isOk())
                .andReturn();

        printFormatedJsonString(result);
    }

    @Test
    public void getSchoolPosts_randomLanguage() throws Exception {
        String token = getToken();

        Random rand = new Random();

        if (rand.nextBoolean()) {
            mockMvc.perform(get(App.API_POST + "/SCHOOL?page=0&size=5")
                    .header("X-Token", token)
                    .locale(Locale.SIMPLIFIED_CHINESE))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[4].extension.data.hero", is("敌法师")));
            mockMvc.perform(get(App.API_POST + "/SCHOOL?page=0&size=5")
                    .header("X-Token", token)
                    .header("Accept-Language", "zh_CN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[4].extension.data.hero", is("敌法师")));
        } else {
            mockMvc.perform(get(App.API_POST + "/SCHOOL?page=0&size=5")
                    .header("X-Token", token)
                    .locale(Locale.ENGLISH))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[4].extension.data.hero", is("Anti-Mage")));
            mockMvc.perform(get(App.API_POST + "/SCHOOL?page=0&size=5")
                    .header("X-Token", token)
                    .header("Accept-Language", "en_US"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[4].extension.data.hero", is("Anti-Mage")));
        }

    }

}
