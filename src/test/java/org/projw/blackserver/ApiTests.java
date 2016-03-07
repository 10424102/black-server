package org.projw.blackserver;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.restassured.RestAssured;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.projw.blackserver.models.Notification;
import org.projw.blackserver.utils.Cryptor;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.projw.blackserver.App.*;

public class ApiTests extends BaseTests {

    @Test
    public void user_getMyProfile() throws Exception {
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
    public void user_updateUsername() throws Exception {
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
    public void user_updateNickname() throws Exception {
        mockMvc.perform(patch(API_USER + "/nickname")
                .header(AUTH_HEADER, getToken())
                .param("val", "nick"))
                .andExpect(status().isOk());
    }

    @Test
    public void user_updateEmail() throws Exception {
        mockMvc.perform(patch(API_USER + "/email")
                .header(AUTH_HEADER, getToken())
                .param("val", "john@exmaple.com"))
                .andExpect(status().isOk());
    }

    @Test
    public void user_updateSignature() throws Exception {
        mockMvc.perform(patch(API_USER + "/signature")
                .header(AUTH_HEADER, getToken())
                .param("val", "今天好开心"))
                .andExpect(status().isOk());
    }

    @Test
    public void user_updateGender() throws Exception {
        mockMvc.perform(patch(API_USER + "/gender")
                .header(AUTH_HEADER, getToken())
                .param("val", "male"))
                .andExpect(status().isOk());
    }

    @Test
    public void user_updateHighschool() throws Exception {
        mockMvc.perform(patch(API_USER + "/highschool")
                .header(AUTH_HEADER, getToken())
                .param("val", "某某高中"))
                .andExpect(status().isOk());
    }

    @Test
    public void user_updateHometown() throws Exception {
        mockMvc.perform(patch(API_USER + "/hometown")
                .header(AUTH_HEADER, getToken())
                .param("val", "黑龙江"))
                .andExpect(status().isOk());
    }

    @Test
    public void user_updateBirthday() throws Exception {
        mockMvc.perform(patch(API_USER + "/birthday")
                .header(AUTH_HEADER, getToken())
                .param("val", "1995-4-1"))
                .andExpect(status().isOk());
    }

    @Test
    public void user_updateCollege() throws Exception {
        mockMvc.perform(patch(API_USER + "/college")
                .header(AUTH_HEADER, getToken())
                .param("val", "某某大学"))
                .andExpect(status().isOk());
    }

    @Test
    public void user_updateAcademy() throws Exception {
        mockMvc.perform(patch(API_USER + "/academy")
                .header(AUTH_HEADER, getToken())
                .param("val", "某某学院"))
                .andExpect(status().isOk());
    }

    @Test
    public void user_updateGrade() throws Exception {
        mockMvc.perform(patch(API_USER + "/grade")
                .header(AUTH_HEADER, getToken())
                .param("val", "大三"))
                .andExpect(status().isOk());
    }

    @Test
    public void user_updateAvatar() throws Exception {
        MockMultipartFile avatarFile = new MockMultipartFile("avatar.png", getClass().getResourceAsStream("/test_avatar.png"));
        mockMvc.perform(fileUpload(API_USER + "/avatar")
                .file(avatarFile)
                .header(AUTH_HEADER, getToken()))
                .andExpect(status().isOk());
    }

    @Test
    public void user_updateBackground() throws Exception {
        // TODO test: update background
    }

    @Test
    public void user_getAllFriends() throws Exception {
        // TODO test: get all friends
    }

    @Test
    public void user_getAllFans() throws Exception {
        // TODO test: get all friends
    }

    @Test
    public void user_getAllFocuses() throws Exception {
        // TODO test: get all focuses
    }

    @Test
    public void user_getAllLikes() throws Exception {
        // TODO test: get all likes (post)
    }

    @Test
    public void user_getAllJoinedGroups() throws Exception {
        // TODO test: get all joined group
    }

    @Test
    public void user_addFriend_replyYes() throws Exception {
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
    public void user_addFriend_replyNo() throws Exception {
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
    public void user_addFriend_replyDelete() throws Exception {
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
    public void user_removeFriend() throws Exception {
        // TODO test: remove a friend
    }

    @Test
    public void user_focusSomeone() throws Exception {
        // TODO test: focus someone
    }

    @Test
    public void user_unfocusSomeone() throws Exception {
        // TODO test: unfocus someone
    }

    @Test
    public void user_joinActivity_replyYes() throws Exception {
        // TODO test: join a activity (reply yes)
    }

    @Test
    public void user_joinActivity_replyNO() throws Exception {
        // TODO test: join a activity (reply no)
    }

    @Test
    public void user_joinActivity_replyDelete() throws Exception {
        // TODO test: join a activity (reply delete)
    }

    @Test
    public void user_leaveActivity() throws Exception {
        // TODO test: leave a activity
    }

    @Test
    public void activity_getRecommendedActivities() throws Exception {
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
    public void activity_getSameSchoolActivities() throws Exception {
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
    public void activity_getFriendsActivities() throws Exception {
        // TODO test: get friends' activities
    }

    @Test
    public void activity_getFocusesActivities() throws Exception {
        // TODO test: get focuses' activities
    }

    @Test
    public void activity_getActivityDetails() throws Exception {
        String token = getToken();
        MvcResult result = mockMvc.perform(get(App.API_ACTIVITY + "/1").header("X-Token", token))
                .andExpect(status().isOk())
                .andReturn();
        printFormatedJsonString(result);
    }

    @Test
    public void activity_getActivitiesComments() throws Exception {
        MvcResult result = mockMvc.perform(get(API_ACTIVITY + "/2/comments")
                .header(AUTH_HEADER, getToken())
                .header("Accept-Language", "zh_CN"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        printFormatedJsonString(result);
    }

    @Test
    public void activity_commentActivity() throws Exception {
        mockMvc.perform(post(API_ACTIVITY + "/2/comments")
                .header(AUTH_HEADER, getToken())
                .header("Accept-Language", "zh_CN")
                .param("content", "梅杰菜狗"))
                .andExpect(status().isOk());
    }

    @Test
    public void activity_deleteActivityComment() throws Exception {
//        String token = getToken();
//        mockMvc.perform(delete(App.API_ACTIVITY + "/2/comment/delete/29")
//            .header("X-Token", token)
//            .header("Accept-Language", "zh_CN"))
//            .andExpect(status().isOk());
    }

    @Test
    public void game_getGame_zh_CN() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(App.API_GAME).header(AUTH_HEADER, getToken())
                .param("key", "MINECRAFT").locale(Locale.SIMPLIFIED_CHINESE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameKey", is("MINECRAFT")))
                .andExpect(jsonPath("$.logo", anything()))
                .andExpect(jsonPath("$.localizedName", is("我的世界")));
    }

    @Test
    public void game_getGame_en_US() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(App.API_GAME).header(AUTH_HEADER, getToken())
                .param("key", "MINECRAFT").locale(Locale.ENGLISH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameKey", is("MINECRAFT")))
                .andExpect(jsonPath("$.logo", anything()))
                .andExpect(jsonPath("$.localizedName", is("Minecraft")));
    }

    @Test
    public void post_getSameSchoolPosts() throws Exception {
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
    public void post_getFriendsPosts() throws Exception {
        // TODO test: get friends' posts
    }

    @Test
    public void post_getFocusesPosts() throws Exception {
        // TODO test: get focuses's posts
    }

    @Test
    public void post_deletePost() {
        // TODO test: delete post
    }

    /**
     * 创建推文
     */
    @Test
    public void post_postCrud() throws Exception {
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
    public void post_getPostComments() throws Exception {
        String token = getToken();

        MvcResult result = mockMvc.perform(get(App.API_POST + "/6/comments")
                .header("X-Token", token)
                .header("Accept-Language", "zh_CN"))
                .andExpect(status().isOk())
                .andReturn();
        printFormatedJsonString(result);
    }

    @Test
    public void post_commentPost() throws Exception {
        String token = getToken();
        mockMvc.perform(post(App.API_POST + "/6/comments")
                .header("X-Token", token)
                .header("Accept-Language", "zh_CN")
                .param("content", "梅杰菜狗"))
                .andExpect(status().isOk());
    }

    @Test
    public void post_deletePostComment() throws Exception {
//        String token = getToken();
//        mockMvc.perform(delete(App.API_POST + "/6/comment/delete/28")
//            .header("X-Token", token)
//            .header("Accept-Language", "zh_CN"))
//            .andExpect(status().isOk());
    }

    @Test
    public void post_likePost() throws Exception {
        String token = getToken();
        mockMvc.perform(post(App.API_POST + "/28/likes")
                .header("X-Token", token)
                .header("Accept-Language", "zh_CN"))
                .andExpect(status().isOk());
    }

    @Test
    public void post_unlikePost() throws Exception {
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

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @Rollback(false)
    public void image_getImage() throws Exception {
        MvcResult result = mockMvc.perform(get(API_ACTIVITY)
                .header(AUTH_HEADER, getToken())
                .param("category", "recommendations"))
                .andExpect(status().isOk())
                .andReturn();
        printFormatedJsonString(result);
        String s = mapper.readTree(result.getResponse().getContentAsString()).get(0).get("cover").asText();
        String[] parts = s.split("~");
        String token = parts[0];
        String hash = parts[1];
        MvcResult imageData = mockMvc.perform(get(API_IMAGE)
                .header(AUTH_HEADER, getToken())
                .param("q", token))
                .andExpect(status().isOk())
                .andReturn();
        Assert.assertEquals("image/png", imageData.getResponse().getContentType());
        byte[] data = imageData.getResponse().getContentAsByteArray();
        Assert.assertEquals(hash, Cryptor.md5_hex(data));
    }

    @Test
    public void image_getImage_restAssured() throws Exception {
        String result = RestAssured.given()
                .header(AUTH_HEADER, getToken())
                .param("category","recommendations")
                .when().get(API_ACTIVITY)
                .then().statusCode(200)
                .extract().asString();
        String s = mapper.readTree(result).get(0).get("cover").asText();
        String[] parts = s.split("~");
        String token = parts[0];
        String hash = parts[1];
        byte[] data = RestAssured.given()
                .header(AUTH_HEADER, getToken())
                .param("q", token)
                .when().get(API_IMAGE)
                .then().statusCode(200)
                .extract().asByteArray();
        Assert.assertEquals(hash, Cryptor.md5_hex(data));
    }



    @Test
    public void public_getServerStatus() throws Exception {
        mockMvc.perform(get("/status")).andExpect(jsonPath("$.status", equalTo("OK")));
    }

}
