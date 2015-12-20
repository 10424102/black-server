package org.team10424102.blackserver;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.team10424102.blackserver.models.Notification;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.team10424102.blackserver.App.*;

public class ApiTests extends BaseTests {

    //@Test
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

    @Test
    public void unregisterAccount() throws Exception {
        // TODO test: unregister account
    }

    @Test
    public void getMyProfile() throws Exception {
        MvcResult result = mockMvc.perform(get(API_USER).header(AUTH_HEADER, getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(2)))
                .andExpect(jsonPath("$.username", equalTo("TEST")))
                .andExpect(jsonPath("$.nickname", equalTo("测试专用用户")))
                .andExpect(jsonPath("$.email", equalTo("test@example.com")))
                .andExpect(jsonPath("$.gender", equalTo("OTHER")))
                .andExpect(jsonPath("$.birthday", equalTo("2015-12-12")))
                .andExpect(jsonPath("$.phone", equalTo("18921273088")))
                .andExpect(jsonPath("$.signature", anything()))
                .andExpect(jsonPath("$.hometown", anything()))
                .andExpect(jsonPath("$.highschool", anything()))
                .andExpect(jsonPath("$.grade", anything()))
                .andExpect(jsonPath("$.hometown", anything()))
                .andExpect(jsonPath("$.avatar", anything()))
                .andExpect(jsonPath("$.background", anything()))
                .andExpect(jsonPath("$.college", anything()))
                .andExpect(jsonPath("$.academy", anything()))
                .andExpect(jsonPath("$.friends", isA(Integer.class)))
                .andExpect(jsonPath("$.fans", isA(Integer.class)))
                .andExpect(jsonPath("$.focuses", isA(Integer.class)))
                .andReturn();
        printFormatedJsonString(result);
    }

    //@Test
    public void updateUsername() throws Exception {
        // FIXME 500 without any error hints
        mockMvc.perform(patch(API_USER + "/username")
                .header(AUTH_HEADER, getToken()).param("val", "testa"))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get(API_USER).header(AUTH_HEADER, getToken()))
                .andExpect(status().isOk())
                .andReturn();

        printFormatedJsonString(result);

//        result = mockMvc.perform(get(API_USER).header(AUTH_HEADER, getTokenA()))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        printFormatedJsonString(result);
    }

    @Test
    public void updateNickname() throws Exception {
        // TODO test: update nickname
    }

    @Test
    public void updateEmail() throws Exception {
        // TODO test: update email
    }

    @Test
    public void updateSignature() throws Exception {
        // TODO test: update signature
    }

    @Test
    public void updateGender() throws Exception {
        // TODO test: update gender
    }

    @Test
    public void updateHighschool() throws Exception {
        // TODO test: udpate highschool
    }

    @Test
    public void updateHometown() throws Exception {
        // TODO test: update hometown
    }

    @Test
    public void updateBirthday() throws Exception {
        // TODO test: udpate birthday
    }

    @Test
    public void updateCollege() throws Exception {
        // TODO test: update college (with academy and grade)
    }

    @Test
    public void updateAvatar() throws Exception {
        // TODO test: udpate avatar
    }

    @Test
    public void updateBackground() throws Exception {
        // TODO test: update background
    }

    @Test
    public void getAllFriends() throws Exception {
        // TODO test: get all friends
    }

    @Test
    public void getAllFans() throws Exception {
        // TODO test: get all friends
    }

    @Test
    public void getAllFocuses() throws Exception {
        // TODO test: get all focuses
    }

    @Test
    public void getAllLikes() throws Exception {
        // TODO test: get all likes (post)
    }

    @Test
    public void getAllJoinedGroups() throws Exception {
        // TODO test: get all joined group
    }

    @Test
    public void addFriend_replyYes() throws Exception {
        mockMvc.perform(post(API_USER + "/friends/" + USER_B_ID)
                .param("attachment", "我是隔壁老王啊")
                .header(AUTH_HEADER, getTokenA()))
                .andExpect(status().isOk());


        MvcResult result = mockMvc.perform(get(API_NOTIFICATION).header(AUTH_HEADER, getTokenA()))
                .andExpect(status().isOk())
                .andReturn();

        printFormatedJsonString(result);

        MvcResult result2 = mockMvc.perform(get(API_NOTIFICATION).header(AUTH_HEADER, getTokenB()))
                .andExpect(status().isOk())
                .andReturn();

        printFormatedJsonString(result2);

        System.out.println("----------------");

        JsonNode notificationArray = mapper.readTree(result2.getResponse().getContentAsString());
        JsonNode notification = notificationArray.get(0);
        long id = notification.get("id").asLong();

        mockMvc.perform(put(API_NOTIFICATION + "/" + id)
                .param("reply", Notification.REPLY_YES + "")
                .header(AUTH_HEADER, getTokenA()))
                .andExpect(status().isOk())
                .andReturn();


        MvcResult result3 = mockMvc.perform(get(API_NOTIFICATION).header(AUTH_HEADER, getTokenA()))
                .andExpect(status().isOk())
                .andReturn();

        printFormatedJsonString(result3);

        MvcResult result4 = mockMvc.perform(get(API_NOTIFICATION).header(AUTH_HEADER, getTokenB()))
                .andExpect(status().isOk())
                .andReturn();

        printFormatedJsonString(result4);

    }

    @Test
    public void addFriend_replyNo() throws Exception {
        mockMvc.perform(post(API_USER + "/friends/" + USER_B_ID)
                .param("attachment", "我是隔壁老王啊")
                .header(AUTH_HEADER, getTokenA()))
                .andExpect(status().isOk());


        MvcResult result = mockMvc.perform(get(API_NOTIFICATION).header(AUTH_HEADER, getTokenA()))
                .andExpect(status().isOk())
                .andReturn();

        printFormatedJsonString(result);

        MvcResult result2 = mockMvc.perform(get(API_NOTIFICATION).header(AUTH_HEADER, getTokenB()))
                .andExpect(status().isOk())
                .andReturn();

        printFormatedJsonString(result2);

        System.out.println("----------------");

        JsonNode notificationArray = mapper.readTree(result2.getResponse().getContentAsString());
        JsonNode notification = notificationArray.get(0);
        long id = notification.get("id").asLong();

        mockMvc.perform(put(API_NOTIFICATION + "/" + id)
                .param("reply", Notification.REPLY_NO + "")
                .header(AUTH_HEADER, getTokenA()))
                .andExpect(status().isOk())
                .andReturn();


        MvcResult result3 = mockMvc.perform(get(API_NOTIFICATION).header(AUTH_HEADER, getTokenA()))
                .andExpect(status().isOk())
                .andReturn();

        printFormatedJsonString(result3);

        MvcResult result4 = mockMvc.perform(get(API_NOTIFICATION).header(AUTH_HEADER, getTokenB()))
                .andExpect(status().isOk())
                .andReturn();

        printFormatedJsonString(result4);
    }

    @Test
    public void addFriend_replyDelete() throws Exception {
        mockMvc.perform(post(API_USER + "/friends/" + USER_B_ID)
                .param("attachment", "我是隔壁老王啊")
                .header(AUTH_HEADER, getTokenA()))
                .andExpect(status().isOk());


        MvcResult result = mockMvc.perform(get(API_NOTIFICATION).header(AUTH_HEADER, getTokenA()))
                .andExpect(status().isOk())
                .andReturn();

        printFormatedJsonString(result);

        MvcResult result2 = mockMvc.perform(get(API_NOTIFICATION).header(AUTH_HEADER, getTokenB()))
                .andExpect(status().isOk())
                .andReturn();

        printFormatedJsonString(result2);

        System.out.println("----------------");

        JsonNode notificationArray = mapper.readTree(result2.getResponse().getContentAsString());
        JsonNode notification = notificationArray.get(0);
        long id = notification.get("id").asLong();

        mockMvc.perform(put(API_NOTIFICATION + "/" + id)
                .param("reply", Notification.REPLY_DELETE + "")
                .header(AUTH_HEADER, getTokenA()))
                .andExpect(status().isOk())
                .andReturn();


        MvcResult result3 = mockMvc.perform(get(API_NOTIFICATION).header(AUTH_HEADER, getTokenA()))
                .andExpect(status().isOk())
                .andReturn();

        printFormatedJsonString(result3);

        MvcResult result4 = mockMvc.perform(get(API_NOTIFICATION).header(AUTH_HEADER, getTokenB()))
                .andExpect(status().isOk())
                .andReturn();

        printFormatedJsonString(result4);
    }

    @Test
    public void removeFriend() throws Exception {
        // TODO test: remove a friend
    }

    @Test
    public void focusSomeone() throws Exception {
        // TODO test: focus someone
    }

    @Test
    public void unfocusSomeone() throws Exception {
        // TODO test: unfocus someone
    }

    @Test
    public void joinActivity_replyYes() throws Exception {
        // TODO test: join a activity (reply yes)
    }

    @Test
    public void joinActivity_replyNO() throws Exception {
        // TODO test: join a activity (reply no)
    }

    @Test
    public void joinActivity_replyDelete() throws Exception {
        // TODO test: join a activity (reply delete)
    }

    @Test
    public void leaveActivity() throws Exception {
        // TODO test: leave a activity
    }

    @Test
    public void getRecommendedActivities() throws Exception {
        String token = getToken();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(App.API_ACTIVITY).header("X-Token", token)
                .param("category", "recommendations")
                .param("page", "0")
                .param("size", "5")
        )
                .andExpect(status().isOk())
                .andReturn();
        printFormatedJsonString(result);
    }

    @Test
    public void getSameSchoolActivities() throws Exception {
        String token = getToken();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(App.API_ACTIVITY).header("X-Token", token)
                .param("category", "school")
                .param("page", "0")
                .param("size", "5")
        )
                .andExpect(status().isOk())
                .andReturn();
        printFormatedJsonString(result);
    }

    @Test
    public void getFriendsActivities() throws Exception {
        // TODO test: get friends' activities
    }

    @Test
    public void getFocusesActivities() throws Exception {
        // TODO test: get focuses' activities
    }

    @Test
    public void getActivityDetails() throws Exception {
        String token = getToken();
        MvcResult result = mockMvc.perform(get(App.API_ACTIVITY + "/1").header("X-Token", token))
                .andExpect(status().isOk())
                .andReturn();
        printFormatedJsonString(result);
    }

    @Test
    public void getActivitiesComments() throws Exception {
        MvcResult result = mockMvc.perform(get(API_ACTIVITY + "/2/comments")
                .header(AUTH_HEADER, getToken())
                .header("Accept-Language", "zh_CN"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        printFormatedJsonString(result);
    }

    @Test
    public void commentActivity() throws Exception {
        mockMvc.perform(post(API_ACTIVITY + "/2/comments")
                .header(AUTH_HEADER, getToken())
                .header("Accept-Language", "zh_CN")
                .param("content", "梅杰菜狗"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteActivityComment() throws Exception {
//        String token = getToken();
//        mockMvc.perform(delete(App.API_ACTIVITY + "/2/comment/delete/29")
//            .header("X-Token", token)
//            .header("Accept-Language", "zh_CN"))
//            .andExpect(status().isOk());
    }

    @Test
    public void getGame_zh_CN() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(App.API_GAME).header(AUTH_HEADER, getToken())
                .param("key", "MINECRAFT").locale(Locale.SIMPLIFIED_CHINESE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameKey", is("MINECRAFT")))
                .andExpect(jsonPath("$.logo", anything()))
                .andExpect(jsonPath("$.localizedName", is("我的世界")));
    }

    @Test
    public void getGame_en_US() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(App.API_GAME).header(AUTH_HEADER, getToken())
                .param("key", "MINECRAFT").locale(Locale.ENGLISH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameKey", is("MINECRAFT")))
                .andExpect(jsonPath("$.logo", anything()))
                .andExpect(jsonPath("$.localizedName", is("Minecraft")));
    }

    @Test
    public void getSameSchoolPosts() throws Exception {
        String token = getToken();

        MvcResult result = mockMvc.perform(get(App.API_POST)
                .param("category", "school")
                .param("page", "0")
                .param("size", "5")
                .header("X-Token", token))
                .andExpect(status().isOk())
                .andReturn();

        printFormatedJsonString(result);

        // TODO i18n test here
    }

    @Test
    public void getFriendsPosts() throws Exception {
        // TODO test: get friends' posts
    }

    @Test
    public void getFocusesPosts() throws Exception {
        // TODO test: get focuses's posts
    }

    @Test
    public void deletePost() {
        // TODO test: delete post
    }

    /**
     * 创建推文
     */
    @Test
    public void postCrud() throws Exception {
        MvcResult result;

        result = mockMvc.perform(post(API_POST)
                .param("content", "测试推文")
                .header(AUTH_HEADER, getToken()))
                .andExpect(status().isOk())
                .andReturn();

        long id = getId(result);

        // 回复
        result = mockMvc.perform(post(API_POST + "/" + id + "/comments")
                .param("content", "测试回复")
                .header(AUTH_HEADER, getToken()))
                .andExpect(status().isOk())
                .andReturn();

        //点赞
        result = mockMvc.perform(post(API_POST + "/" + id + "/likes")
                .header(AUTH_HEADER, getToken()))
                .andExpect(status().isOk())
                .andReturn();

        // TODO 由于 Hibernate 的 Session 缓存机制导致缓存的 post 无法察觉 like 的更新

        //获取我自己发的推文
        result = mockMvc.perform(get(API_POST)
                .param("category", "myself")
                .header(AUTH_HEADER, getToken()))
                .andExpect(status().isOk())
                .andReturn();

        printFormatedJsonString(result);
    }

    private long getId(MvcResult result) throws Exception {
        return mapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }


    @Test
    public void getPostComments() throws Exception {
        String token = getToken();

        MvcResult result = mockMvc.perform(get(App.API_POST + "/6/comments")
                .header("X-Token", token)
                .header("Accept-Language", "zh_CN"))
                .andExpect(status().isOk())
                .andReturn();
        printFormatedJsonString(result);
    }

    @Test
    public void commentPost() throws Exception {
        String token = getToken();
        mockMvc.perform(post(App.API_POST + "/6/comments")
                .header("X-Token", token)
                .header("Accept-Language", "zh_CN")
                .param("content", "梅杰菜狗"))
                .andExpect(status().isOk());
    }

    @Test
    public void deletePostComment() throws Exception {
//        String token = getToken();
//        mockMvc.perform(delete(App.API_POST + "/6/comment/delete/28")
//            .header("X-Token", token)
//            .header("Accept-Language", "zh_CN"))
//            .andExpect(status().isOk());
    }

    @Test
    public void likePost() throws Exception {
        String token = getToken();
        mockMvc.perform(post(App.API_POST + "/28/likes")
                .header("X-Token", token)
                .header("Accept-Language", "zh_CN"))
                .andExpect(status().isOk());
    }

    @Test
    public void unlikePost() throws Exception {
        String token = getToken();
        mockMvc.perform(delete(App.API_POST + "/6/likes")
                .header("X-Token", token)
                .header("Accept-Language", "zh_CN"))
                .andExpect(status().isOk());
    }

    @Test
    public void searchUsers() throws Exception {
        // TODO test: search for users
    }

    @Test
    public void searchActivities() throws Exception {
        // TODO test: search for activities
    }

    @Test
    public void searchPosts() throws Exception {
        // TODO test: search for posts
    }

}
