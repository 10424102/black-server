package org.team10424102.blackserver;

import org.junit.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserRelationTests extends BaseTests {

    @Test
    public void focusSomeone() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(App.API_USER).param("phone", "1234").param("vcode", "1234"))
                .andExpect(status().isOk());
    }

    @Test
    public void friendSomeone() {

    }

    @Test
    public void getFocuses() {

    }

    @Test
    public void getFans() {

    }

    @Test
    public void unfocusSomone() {

    }

    @Test
    public void getFriends() {

    }

    @Test
    public void unfriendSomeone() {

    }

    @Test
    public void updateFriendAlias() {

    }

//    @Autowired
//    ImageRepo imageRepo;
//
//    @Test
//    @Rollback(false)
//    public void allImageHash() {
//        Iterable<Image> images = imageRepo.findAll();
//        images.forEach(e -> {
//            System.out.println(e.getId());
//            String hash = Cryptor.md5(e.getData());
//            System.out.println(hash);
//            e.setHash(hash);
//            imageRepo.save(e);
//        });
//    }

}
