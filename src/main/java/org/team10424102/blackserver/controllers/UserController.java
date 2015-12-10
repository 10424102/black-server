package org.team10424102.blackserver.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.team10424102.blackserver.Api;
import org.team10424102.blackserver.App;
import org.team10424102.blackserver.config.json.Views;
import org.team10424102.blackserver.config.security.CurrentUser;
import org.team10424102.blackserver.daos.FriendshipRepo;
import org.team10424102.blackserver.exceptions.VcodeVerificationException;
import org.team10424102.blackserver.models.Friendship;
import org.team10424102.blackserver.models.User;
import org.team10424102.blackserver.services.UserService;
import org.team10424102.blackserver.services.VcodeService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = App.API_USER, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {

    @Autowired UserService userService;

    @Autowired FriendshipRepo friendshipRepo;

    @Autowired VcodeService vcodeService;

    /////////////////////////////////////////////////////////////////
    //                                                             //
    //                    ~~~~~~~~~~~~~~~~~                        //
    //                          USER                               //
    //                    =================                        //
    //                                                             //
    /////////////////////////////////////////////////////////////////

    //<editor-fold desc="=== Account ===">

    /**
     * 获取 token, 如果手机号没有注册则注册新用户
     *
     * @return {
     * "token": "3b92e87b-69c1-41fe-9b3d-923e2138c00b"
     * }
     */
    @RequestMapping(value = App.API_TOKEN, method = GET)
    public Api.Result getToken(@RequestParam String phone, @RequestParam String vcode, HttpServletRequest request) {
        if (!vcodeService.verify("86", phone, vcode)) {
            throw new VcodeVerificationException(phone, vcode);
        }
        User user;
        if (!userService.isPhoneExisted(phone)) {
            user = userService.createAndSaveUser(phone, request);
        } else {
            user = userService.findByPhone(phone);
        }
        return Api.result().param("token", userService.generateToken(user));
    }

    /**
     * 检查可用性
     * type = phone|token
     *
     * @return {
     * "result": true|false
     * }
     */
    @RequestMapping(value = App.API_AVAILABILITY + "/{type}", method = GET)
    public Api.Result isAvailable(@PathVariable String type, @RequestParam(value = "q") String content) {
        switch (type) {
            case "phone":
                return Api.result().param("result", !userService.isPhoneExisted(content));
            case "token":
                return Api.result().param("result", userService.isTokenValid(content));
        }
        return Api.result().param("result", false);
    }

    /**
     * 注销当前用户
     */
    @RequestMapping(value = App.API_USER, method = DELETE)
    public void unregister() {
        userService.deleteCurrentUser();
    }

    /**
     * 获取当前用户的个人信息
     */
    @RequestMapping(value = App.API_USER, method = GET)
    @JsonView(Views.UserDetails.class)
    public User getCurrentUsersProfile() {
        return userService.getCurrentUser();
    }

    /**
     * 更新当前用户的个人信息
     */
    @RequestMapping(value = App.API_USER, method = PUT)
    public void updateCurrentUsersProfile(@JsonView(Views.UserDetails.class) User newUser) {
        userService.updateUser(newUser);
    }

//    @RequestMapping(value = App.API_USER, method = PATCH)
//    public void updateNickName(@RequestParam String val, @CurrentUser User user) {
//        user.getProfile().setNickname(val);
//        userService.updateUser(user);
//    }
//
//    @RequestMapping(value = App.API_USER, method = PATCH)
//    public void updateUserName(@RequestParam String val, @CurrentUser User user) {
//
//    }
//
//    @RequestMapping(value = App.API_USER, method = PATCH)
//    public void updateBirthday(@RequestParam String val, @CurrentUser User user) {
//
//    }
//
//    @RequestMapping(value = App.API_USER, method = PATCH)
//    public void updateGender(@RequestParam String val, @CurrentUser User user) {
//
//    }
//
//    @RequestMapping(value = App.API_USER, method = PATCH)
//    public void updateSignature(@RequestParam String val, @CurrentUser User user) {
//
//    }
//
//    @RequestMapping(value = App.API_USER, method = PATCH)
//    public void updateHighschool(@RequestParam String val, @CurrentUser User user) {
//
//    }
//
//    @RequestMapping(value = App.API_USER, method = PATCH)
//    public void updateHometown(@RequestParam String val, @CurrentUser User user) {
//
//    }
//
//    @RequestMapping(value = App.API_USER, method = PATCH)
//    public void updateCollege(@RequestParam String val, @CurrentUser User user) {
//
//    }
//
//    @RequestMapping(value = App.API_USER, method = PATCH)
//    public void updateAcademy(@RequestParam String val, @CurrentUser User user) {
//
//    }
//
//    @RequestMapping(value = App.API_USER, method = PATCH)
//    public void updateGrade(@RequestParam String val, @CurrentUser User user) {
//
//    }



    /**
     * 查看其他用户的 Profile
     */
    @RequestMapping(value = App.API_USER + "/{id}", method = GET)
    @JsonView(Views.UserDetails.class)
    public User getOthersProfile(@PathVariable int id) {
        return userService.getUserById(id);
    }

    //</editor-fold>

    /////////////////////////////////////////////////////////////////
    //                                                             //
    //                    ~~~~~~~~~~~~~~~~~                        //
    //                       SUBCRIPTION                           //
    //                    =================                        //
    //                                                             //
    /////////////////////////////////////////////////////////////////

    //<editor-fold desc="=== Subscription ===">

    /**
     * 获取当前用户的关注列表
     */
    @RequestMapping(value = App.API_USER + "/focuses", method = GET)
    @JsonView(Views.UserSummary.class)
    public Set<User> getCurrentUsersFocuses() {
        return userService.getCurrentUser().getFocuses();
    }

    /**
     * 获取当前用户的粉丝列表
     */
    @RequestMapping(value = App.API_USER + "/fans", method = GET)
    @JsonView(Views.UserSummary.class)
    public Set<User> getCurrentUsersFans() {
        return userService.getCurrentUser().getFocuses();
    }

    /**
     * 关注一个用户
     */
    @RequestMapping(value = App.API_USER + "/focuses/{id}", method = POST)
    public void focusSomeone(@PathVariable int id) {
        User user = userService.getCurrentUser();
        // 不能关注自己
        if (user.getId() == id) return;
        User focus = userService.getUserById(id);
        user.getFocuses().add(focus);
        userService.updateUser(user);
        // 关注另一个人的同时你也成为了他的粉丝
        // TODO 检查实体 cascade 是否成立
    }

    /**
     * 取消关注一个用户
     */
    @RequestMapping(value = App.API_USER + "/focuses/{id}", method = DELETE)
    public void unfocusSomeone(@PathVariable int id) {
        User user = userService.getCurrentUser();
        user.getFocuses().removeIf(u -> u.getId() == id);
        // 取消关注一个人的同时你也会从他的粉丝列表里消失
        // TODO 检查实体 cascade 是否成立
    }

    //</editor-fold>

    /////////////////////////////////////////////////////////////////
    //                                                             //
    //                    ~~~~~~~~~~~~~~~~~                        //
    //                        FRIENDSHIP                           // 
    //                    =================                        //
    //                                                             //
    /////////////////////////////////////////////////////////////////

    //<editor-fold desc="=== Friendship ===">

    /**
     * 获取当前用户的朋友
     */
    @RequestMapping(value = App.API_USER + "/friends", method = GET)
    @JsonView(Views.UserSummary.class)
    public Set<Friendship> getCurrentUsersFriends() {
        return userService.getCurrentUser().getFriendshipSet();
    }

    /**
     * 添加一个朋友
     */
    @RequestMapping(value = App.API_USER + "/friends/{id}", method = POST)
    public void friendSomeone(@PathVariable int id, @RequestParam(required = false) String alias) {
        User user = userService.getCurrentUser();
        // 不能和自己成为朋友
        if (user.getId() == id) return;
        User friend = userService.getUserById(id);

        Friendship friendship = new Friendship();
        friendship.setFriend(friend);
        friendship.setFriendAlias(alias);
        friendship.setUser(user);
        friendshipRepo.save(friendship);

        // 保存这个朋友关系的同时, 双方的朋友列表里都会有对方
        // TODO 检查实体 cascade 是否成立
    }

    /**
     * 删除一个朋友
     */
    @RequestMapping(value = App.API_USER + "/friends/{id}", method = DELETE)
    public void unfriendSomeone(@PathVariable int id) {
        User user = userService.getCurrentUser();
        user.getFriendshipSet().removeIf(f -> f.getUser().getId() == id);

        // 删除这个朋友关系的同时, 双方的朋友列表里都会删除对方
        // TODO 检查实体 cascade 是否成立
    }

    /**
     * 修改一个朋友的备注名称
     */
    @RequestMapping(value = App.API_USER + "/friends/{id}", method = PUT)
    public void updateFriendAlias(@PathVariable int id, @RequestParam String alias) {
        User user = userService.getCurrentUser();
        Optional<Friendship> friendship = user.getFriendshipSet().stream()
                .filter(f -> f.getFriend().getId() == id).findFirst();
        if (friendship.isPresent()) {
            Friendship f = friendship.get();
            f.setFriendAlias(alias);
            friendshipRepo.save(f);
        }
    }

    //</editor-fold>

}
