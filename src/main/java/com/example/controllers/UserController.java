package com.example.controllers;

import com.example.Api;
import com.example.config.jsonviews.UserView;
import com.example.daos.AcademyRepo;
import com.example.daos.CollegeRepo;
import com.example.daos.UserRepo;
import com.example.models.*;
import com.example.services.ImageService;
import com.example.services.TokenService;
import com.example.services.UserService;
import com.example.services.VcodeService;
import com.fasterxml.jackson.annotation.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.example.Api.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class UserController {
    static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired UserRepo userRepo;

    @Autowired CollegeRepo collegeRepo;

    @Autowired AcademyRepo academyRepo;

    @Autowired UserService userService;

    @Autowired TokenService tokenService;

    @Autowired ImageService imageService;

    @Autowired VcodeService vcodeService;

    /////////////////////////////////////////////////////////////////
    //                                                             //
    //                    ~~~~~~~~~~~~~~~~~                        //
    //                         ACCOUNT                             //
    //                    =================                        //
    //                                                             //
    /////////////////////////////////////////////////////////////////

    //<editor-fold desc="=== Account ===">

    /**
     * 更新用户无效的 token，需要先向 Mob 验证手机，然后再给用户刷新 token
     *
     * @param phone
     * @return
     */
    @RequestMapping(value = "/api/token", method = GET)
    public Api.Result getToken(@RequestParam String phone, @RequestParam String vcode, @RequestParam String zone) {
        if (!vcodeService.verify(zone, phone, vcode)) return Api.result(VCODE_VERIFICATION_FAILED);
        boolean existed = userRepo.existsByPhone(phone);
        if (!existed) return Api.result(UPDATE_TOKEN_FAILED);
        return Api.result(SUCCESS).param("token").value(tokenService.generateTokenByPhone(phone));
    }

    /**
     * 检查可用性
     * type = phone|token
     */
    @RequestMapping(value = "/api/availability/{type}", method = GET)
    public boolean isAvailable(@PathVariable String type, @RequestParam(value = "q") String content) {
        switch (type) {
            case "phone":
                return !userRepo.existsByPhone(content);
            case "token":
                return tokenService.isAvailable(content);
        }
        return false;
    }

    /**
     * 注册新用户，通过向 Mob 发送手机验证码，来验证用户的身份，从而注册新用户
     *
     * @param phone
     * @return
     */
    @RequestMapping(value = "/api/register", method = POST)
    @ResponseBody
    public Api.Result register(@RequestParam String phone, @RequestParam String vcode, @RequestParam String zone,
                               HttpServletRequest request) {
        if (!vcodeService.verify(zone, phone, vcode)) return Api.result(VCODE_VERIFICATION_FAILED);
        boolean existed = userRepo.existsByPhone(phone);
        if (!existed) {
            User user = new User(phone);
            user.getRegInfo().setRegIp(request.getRemoteAddr());
            user.getProfile().setUsername(phone);
            user.getProfile().setRealName(phone);
            userRepo.save(user);
            return Api.result(SUCCESS).param("token").value(tokenService.generateToken(user));
        }
        return Api.result(ERR_PHONE_EXISTED);
    }

    /**
     * 获取当前用户的个人信息
     *
     * @return 用户的个人信息
     */
    @RequestMapping(value = "/api/profile", method = GET)
    @JsonView(UserView.Profile.class)
    public ResponseEntity<?> getMyProfile() {
        User user = userService.getCurrentUser();
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        updateAccesToken(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * 更新用户的个人信息
     *
     * @param updatedUser 新的用户个人信息，不含头像
     * @return HTTP状态码NO_CONTENT表示操作成功，BAD_REQUEST表示参数异常，未登录则返回UNAUTHORIZED
     */
    @RequestMapping(value = "/api/profile", method = PUT)
    @JsonView(UserView.Profile.class)
    public ResponseEntity<?> setMyProfile(@RequestBody User updatedUser) {
        //TODO 进行数据验证
        User user = userService.getCurrentUser();
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        updatedUser.setId(user.getId());
        updatedUser.setPhone(user.getPhone());
        updatedUser.setEnabled(updatedUser.isEnabled());
        updatedUser.setRegInfo(user.getRegInfo());
        College updatedCollege = updatedUser.getProfile().getCollege();
        College college = collegeRepo.findOneByName(updatedCollege.getName());
        Academy updatedAcademy = updatedUser.getProfile().getAcademy();
        Academy academy = academyRepo.findOneByName(updatedAcademy.getName());
        if (college != null && academy != null) {
            updatedUser.getProfile().setCollege(college);
            updatedUser.getProfile().setAcademy(academy);
            userRepo.save(updatedUser);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/api/reginfo", method = GET)
    public ResponseEntity<?> getMyRegInfo() {
        User user = userService.getCurrentUser();
        if (user != null) {
            RegistrationInfo registrationInfo = user.getRegInfo();
            return new ResponseEntity<>(registrationInfo, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //</editor-fold>

    /////////////////////////////////////////////////////////////////
    //                                                             //
    //                    ~~~~~~~~~~~~~~~~~                        //
    //                     OTHERS' PROFILE                         //
    //                    =================                        //
    //                                                             //
    /////////////////////////////////////////////////////////////////

    //<editor-fold desc="=== Others' Profile ===">

    /**
     * 查看其他用户的 Profile
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/api/users/{userId}/profile", method = GET)
    @JsonView(UserView.Profile.class)
    public ResponseEntity<?> getUserProfile(@PathVariable int userId) {
        User user = userRepo.findOne(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        updateAccesToken(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * 朋友的 profile
     *
     * @return
     */
    @RequestMapping(value = "/api/profile/friends", method = GET)
    @JsonView(UserView.UserSummary.class)
    public ResponseEntity<?> getMyFriendList() {
        final PageRequest pageable = new PageRequest(0, 10, Sort.Direction.ASC, "id");
        //TODO 将查询朋友的功能真正实现
        Page<User> users = userRepo.findAll(pageable);
        if (users.getTotalElements() <= 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<User> userList = users.getContent();
        User user = null;
        for (Iterator<User> iterator = userList.iterator(); iterator.hasNext(); ) {
            user = iterator.next();
            if (user != null) {
                updateAccesToken(user);
            }
        }
        return new ResponseEntity<>(userList, HttpStatus.OK);
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
     * 检查目标用户是否已经被关注了
     *
     * @param id 目标id
     * @return 已经关注过则返回NO_CONTENT，未关注则返回NOT_FOUND，未登录则返回UNAUTHORIZED
     */
    @RequestMapping(value = "/api/following/{id}", method = GET)
    public ResponseEntity<?> checkHasFollowed(@PathVariable Integer id) {
        if (id != null) {
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            Set<User> following = currentUser.getFocuses();
            for (Iterator<User> iterator = following.iterator(); iterator.hasNext(); ) {
                User checkUser = iterator.next();
                if (checkUser.getId().equals(id)) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * 获取当前用户的关注列表
     *
     * @return 如果未登录则返回UNAUTHORIZED，没有则返回NOT_FOUND，否则返回关注用户基本信息列表
     */
    @RequestMapping(value = "/api/following", method = GET)
    @JsonView(UserView.UserSummary.class)
    public ResponseEntity<?> getFollowList() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Set<User> following = currentUser.getFocuses();
        if (following.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        for (Iterator<User> iterator = following.iterator(); iterator.hasNext(); ) {
            User checkUser = iterator.next();
            updateAccesToken(checkUser);
        }
        return new ResponseEntity<>(following, HttpStatus.BAD_REQUEST);
    }

    /**
     * 关注一个用户
     *
     * @param id 要关注的对象的id
     * @return 操作成功返回NO_CONTENT，参数错误返回BAD_REQUEST，未登陆则返回UNAUTHORIZED
     */
    @RequestMapping(value = "/api/following/{id}", method = PUT)
    public ResponseEntity<?> doFollow(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.getCurrentUser();
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (user.getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User followedUser = userRepo.findOne(id);
        if (followedUser != null) {
            Set<User> following = user.getFocuses();
            boolean hasFollowed = false;
            for (Iterator<User> iterator = following.iterator(); iterator.hasNext(); ) {
                User checkUser = iterator.next();
                if (checkUser.getId().equals(id)) {
                    hasFollowed = true;
                    break;
                }
            }
            if (!hasFollowed) {
                following.add(followedUser);
                userRepo.save(user);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * 取消关注一个用户
     *
     * @param id 要取消关注的用户的id
     * @return 操作成功返回NO_CONTENT，参数错误返回BAD_REQUEST，未登陆则返回UNAUTHORIZED
     */
    @RequestMapping(value = "/api/following/{id}", method = DELETE)
    public ResponseEntity<?> unFollow(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.getCurrentUser();
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User followedUser = userRepo.findOne(id);
        if (followedUser != null) {
            Set<User> following = user.getFocuses();
            for (Iterator<User> iterator = following.iterator(); iterator.hasNext(); ) {
                User checkUser = iterator.next();
                if (checkUser.getId().equals(id)) {
                    iterator.remove();
                    break;
                }
            }
            userRepo.save(user);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * 获取关注当前用户的用户列表
     *
     * @return 未登录则返回UNAUTHORIZED，如果列表为空则返回NOT_FOUND，否则返回用户列表
     */
    @RequestMapping(value = "/api/followed", method = GET)
    @JsonView(UserView.UserSummary.class)
    public ResponseEntity getFollowedList() {
        User currentUser = userService.getCurrentUser();
        Set<User> fans = currentUser.getFans();
        if (fans.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        fans.forEach(this::updateAccesToken);
        return new ResponseEntity<>(fans, HttpStatus.OK);
    }

    /**
     * 获取关注对象的数据
     *
     * @return
     */
    @RequestMapping(value = "/api/followings/count", method = GET)
    public ResponseEntity<?> getFollowNumber() {
        Api.Result result = Api.result(SUCCESS).param("num").value(200);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 获取粉丝的数目
     *
     * @return
     */
    @RequestMapping(value = "/api/fans/count", method = GET)
    public ResponseEntity<?> getFanNumber() {
        Api.Result result = Api.result(SUCCESS).param("num").value(456);
        return new ResponseEntity<>(result, HttpStatus.OK);
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
     * 获取朋友的数目
     *
     * @return
     */
    @RequestMapping(value = "/api/friends/count", method = GET)
    @JsonView(UserView.Profile.class)
    public ResponseEntity<?> getFriendNumber() {
        Api.Result result = Api.result(SUCCESS).param("num").value(123);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //</editor-fold>


    /**
     * 获取赞的数目
     *
     * @return
     */
    @RequestMapping(value = "/api/likes/count", method = GET)
    public ResponseEntity<?> getLikeNumber() {
        Api.Result result = Api.result(SUCCESS).param("num").value(789);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private void updateAccesToken(User user) {
        Profile profile = user.getProfile();
        if (profile != null) {
            if (profile.getAvatar() != null) {
                profile.setAvatarAccessToken(imageService.generateAccessToken(profile.getAvatar()));
            }
            if (profile.getBackgroundImage() != null) {
                profile.setBackgroundImageAccessToken(imageService.generateAccessToken(profile.getBackgroundImage()));
            }
        }
    }

}
