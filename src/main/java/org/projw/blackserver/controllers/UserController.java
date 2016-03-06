package org.projw.blackserver.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.projw.blackserver.App;
import org.projw.blackserver.config.json.Views;
import org.projw.blackserver.config.CurrentUser;
import org.projw.blackserver.config.SpringSecurityUserAdapter;
import org.projw.blackserver.models.*;
import org.projw.blackserver.controllers.exceptions.VcodeVerificationException;
import org.projw.blackserver.notifications.AddingFriendHandler;
import org.projw.blackserver.notifications.RemovingFriendHandler;
import org.projw.blackserver.services.TokenService;
import org.projw.blackserver.services.VcodeService;
import org.projw.blackserver.utils.Api;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = App.API_USER)
@Transactional
public class UserController {

    @Autowired UserRepo userRepo;
    @Autowired FriendshipRepo friendshipRepo;
    @Autowired FriendshipApplicationRepo friendshipApplicationRepo;
    @Autowired NotificationRepo notificationRepo;
    @Autowired ImageRepo imageRepo;

    @Autowired VcodeService vcodeService;
    @Autowired TokenService tokenService;

    @Autowired AddingFriendHandler addingFriendHandler;
    @Autowired RemovingFriendHandler removingFriendHandler;

    @Autowired ObjectMapper objectMapper;


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
     * @return
     * {
     *     "token": token-string
     * }
     * <p>
     * 如果手机号码不存在, 这里就是创建用户
     */
    @RequestMapping(value = "/token", method = GET)
    @ResponseBody
    public String getToken(@RequestParam String phone, @RequestParam String vcode, HttpServletRequest request)
            throws JsonProcessingException{
        // TODO 使用 User 数据模型中 phone 属性的注解来对这里的 phone 做验证
        if (!vcodeService.verify("86", phone, vcode)) {
            throw new VcodeVerificationException(phone, vcode);
        }
        User user;
        if (!userRepo.isPhoneExists(phone)) {
            user = new User();
            user.setPhone(phone);
            user.setUsername(phone);
            user.setEnabled(true);
            user.setGender(Gender.UNKNOWN);
            // TODO 使用查询缓存来优化下面这两句话
            user.setAvatar(imageRepo.findOneByTags(App.DEFAULT_AVATAR_TAG));
            user.setBackground(imageRepo.findOneByTags(App.DEFAULT_BACKGROUND_TAG));

            RegInfo regInfo = new RegInfo();
            regInfo.setRegIp(request.getRemoteAddr());
            regInfo.setRegTime(new Date());

            user = userRepo.save(user);

        } else {
            user = userRepo.findByPhone(phone);
        }
        return objectMapper.writeValueAsString(Api.result().param("token", generateToken(user)));
    }

    private String generateToken(User user) {
        SpringSecurityUserAdapter securityUser = new SpringSecurityUserAdapter(user);
        return tokenService.generateToken(securityUser);
    }

    /**
     * 检查可用性
     * type = phone|token
     *
     * @return
     * {
     *     "result": true|false
     * }
     */
    @RequestMapping(value = "/{type}", method = HEAD)
    public ResponseEntity isAvailable(@PathVariable String type, String q) {
        switch (type) {
            case "phone":
                if (userRepo.isPhoneExists(q)) return ResponseEntity.ok().build();
                break;
            case "token":
                if (tokenService.isTokenValid(q)) return ResponseEntity.ok().build();
                break;
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 注销当前用户
     */
    @RequestMapping(method = DELETE)
    public void unregister(@CurrentUser User user) {
        userRepo.delete(user);
    }

    /**
     * 获取当前用户的个人信息
     */
    @RequestMapping(method = GET)
    @Transactional(readOnly = true)
    public String getCurrentUsersProfile(@CurrentUser User user) throws JsonProcessingException{
        return objectMapper.writerWithView(Views.UserDetails.class).writeValueAsString(userRepo.findOne(user.getId()));
    }

    /**
     * 查看其他用户的 Profile
     */
    @RequestMapping(value = "/{id}", method = GET)
    @Transactional(readOnly = true)
    public ResponseEntity<String> getOthersProfile(@PathVariable long id) throws JsonProcessingException {
        User user = userRepo.findOne(id);
        if (user == null) new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(objectMapper.writerWithView(Views.UserSummary.class).writeValueAsString(user));
    }

//    /**
//     * 更新当前用户的个人信息
//     */
//    @RequestMapping(method = PUT)
//    @JsonView(Views.UserDetails.class)
//    public User updateCurrentUsersProfile(@Valid User user) {
//        return userRepo.save(user);
//    }

    /**
     * 更新昵称
     * @param val
     * @param user
     * @return
     */
    @RequestMapping(value = "/nickname", method = PATCH)
    @Transactional
    public void updateNickName(@RequestParam String val, @CurrentUser User user) {
        user.setNickname(val);
        userRepo.save(user);
    }

    /**
     * 更新个性签名, 140个字符, 后面的内容会被截掉
     * @param val
     * @param user
     * @return
     */
    @RequestMapping(value = "/signature", method = PATCH)
    @Transactional
    public void updateSignature(@RequestParam String val, @CurrentUser User user) {
        val = val.substring(0, 140);
        user.setSignature(val);
        userRepo.save(user);
    }

    /**
     * 更新个人生日
     * @param val
     * @param user
     * @return
     */
    @RequestMapping(value = "/birthday", method = PATCH)
    @Transactional
    public ResponseEntity<Void> updateBirthday(@RequestParam String val, @CurrentUser User user) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date birthday = format.parse(val);
            user.setBirthday(birthday);
            userRepo.save(user);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 更新性别
     * @param val
     * @param user
     * @return
     */
    @RequestMapping(value = "/gender", method = PATCH)
    @Transactional
    public ResponseEntity<Void> updateGender(@RequestParam String val, @CurrentUser User user) {
        try {
            Gender gender = Gender.valueOf(val);
            user.setGender(gender);
            userRepo.save(user);
        } catch (IllegalArgumentException|NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 更新用户名, 冲突会返回 409
     * @param val
     * @param user
     * @return
     */
    @RequestMapping(value = "/username", method = PATCH)
    @Transactional
    public ResponseEntity<Void> updateUsername(@RequestParam String val, @CurrentUser User user) {
        try {
            user.setUsername(val);
            userRepo.save(user);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok().build();
    }
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
    @RequestMapping(value = "/focuses", method = GET)
    @JsonView(Views.UserSummary.class)
    public Set<User> getCurrentUsersFocuses(@CurrentUser User user) {
        return user.getFocuses();
    }

    /**
     * 获取当前用户的粉丝列表
     */
    @RequestMapping(value = "/fans", method = GET)
    @JsonView(Views.UserSummary.class)
    public Set<User> getCurrentUsersFans(@CurrentUser User user) {
        return user.getFans();
    }

    /**
     * 关注一个用户
     */
    @RequestMapping(value = "/focuses/{id}", method = POST)
    public void focusSomeone(@PathVariable long id, @CurrentUser User user) {
        if (user.getId() == id) return; // 不能关注自己

        User focusUser = userRepo.findOne(id);
        if (focusUser == null) return;

        user.getFocuses().add(focusUser);
        userRepo.save(user);
    }

    /**
     * 取消关注一个用户
     */
    @RequestMapping(value = "/focuses/{id}", method = DELETE)
    public void unfocusSomeone(@PathVariable long id, @CurrentUser User user) {
        user.getFocuses().removeIf(u -> u.getId() == id);
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
    @RequestMapping(value = "/friends", method = GET)
    @JsonView(Views.UserSummary.class)
    public Collection<User> getCurrentUsersFriends(@CurrentUser User user) {
        return user.getFriends();
    }

    /**
     * 添加一个朋友
     */
    @RequestMapping(value = "/friends/{id}", method = POST)
    @Transactional
    public void friendSomeone(@PathVariable long id, @RequestParam(required = false) String attachment, @CurrentUser User user) {
        addingFriendHandler.fireRequest(user, userRepo.findOne(id), attachment);
    }

    /**
     * 删除一个朋友
     */
    @RequestMapping(value = "/friends/{id}", method = DELETE)
    @Transactional
    public void unfriendSomeone(@PathVariable long id, @CurrentUser User user) {
        removingFriendHandler.unfriend(user, userRepo.findOne(id));
    }

    /**
     * 修改一个朋友的备注名称
     */
    @RequestMapping(value = "/friends/{id}", method = PUT)
    public void updateFriendAlias(@PathVariable long id, @RequestParam String alias, @CurrentUser User user) {
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
