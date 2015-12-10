package org.team10424102.blackserver.services;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.team10424102.blackserver.config.security.UserAuthentication;
import org.team10424102.blackserver.daos.UserRepo;
import org.team10424102.blackserver.exceptions.PersistEntityException;
import org.team10424102.blackserver.exceptions.UserNotFoundException;
import org.team10424102.blackserver.models.*;
import org.team10424102.blackserver.utils.DateUtils;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    public static final int HALF_AN_HOUR_IN_MILLISECONDS = 30 * 60 * 1000;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final Cache tokenCache = CacheManager.getInstance().getCache("userTokenCache");
    @Autowired UserRepo userRepo;
    @Autowired ImageService imageService;
    @Autowired EntityManager em;

    @Scheduled(fixedRate = HALF_AN_HOUR_IN_MILLISECONDS)
    public void evictExpiredTokens() {
        tokenCache.evictExpiredElements();
    }

    @NotNull
    @Override
    public String generateToken(User user) {
        UserAuthentication auth = new UserAuthentication(user);
        String token = UUID.randomUUID().toString();
        tokenCache.put(new Element(token, auth));
        return token;
    }

    @Nullable
    @Override
    public UserAuthentication getUserAuthenticationFromToken(String token) {
        Element element = tokenCache.get(token);
        if (element == null) return null;
        return (UserAuthentication) element.getObjectValue();
    }

    @Override
    public boolean isTokenValid(String token) {
        Element element = tokenCache.get(token);
        return element != null;
    }

    @NotNull
    @Override
    public User getCurrentUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO 搭配使用 Ehcache 和 Hibernate
        return userRepo.findOne(user.getId());
    }

    @Override
    public int getCurrentUserId() {
        return getCurrentUser().getId();
    }

    @Override
    public boolean isSecondsFriend(int firstUid, int secondUid) {
        return userRepo.friendOf(firstUid, secondUid);
    }

    @Override
    public boolean isSecondsFan(int firstUid, int secondUid) {
        return userRepo.fanOf(firstUid, secondUid);
    }

    @Override
    public boolean isSecondsFocus(int firstUid, int secondUid) {
        return userRepo.focusOf(firstUid, secondUid);
    }

    @NotNull
    @Override
    public User createAndSaveUser(String phone, HttpServletRequest request) {
        User user = new User();
        user.setUsername(phone);
        user.setEnabled(true);

        Profile p = user.getProfile();
        p.setPhone(phone);
        p.setGender(Gender.SECRET);
        p.setAvatar(imageService.getDefault().avatar());
        p.setBackgroundImage(imageService.getDefault().background());

        RegInfo r = user.getRegInfo();
        r.setRegIp(request.getRemoteAddr());
        r.setRegTime(DateUtils.now());

        User savedUser = userRepo.save(user);
        if (savedUser == null) throw new PersistEntityException(User.class);
        return savedUser;
    }

    @NotNull
    @Override
    public User updateUser(User newUser) {
        // 首先传入进来的 user 是数据格式合法的 user, 这里检测更新的逻辑合法性
        filterUnchangableFields(newUser);
        User existedUser = getCurrentUser();
        mergeToExistedUser(newUser, existedUser);
        User updatedUser = userRepo.save(existedUser);
        if (updatedUser == null) throw new PersistEntityException(User.class);
        return updatedUser;
    }

    private void mergeToExistedUser(User newUser, User existedUser) {
        String username = newUser.getUsername();
        String email = newUser.getEmail();

        Profile p = newUser.getProfile();
        String nickname = p.getNickname();
        Gender gender = p.getGender();
        Image avatar = p.getAvatar();
        Image background = p.getBackgroundImage();
        Date birthday = p.getBirthday();
        String signature = p.getSignature();
        String hometown = p.getHometown();
        String phone = p.getPhone();
        String highschool = p.getHighschool();
        College college = p.getCollege();
        Academy academy = p.getAcademy();
        String grade = p.getGrade();

        if (username != null) existedUser.setUsername(username);
        if (email != null) existedUser.setEmail(email);

        Profile profile = existedUser.getProfile();
        if (nickname != null) profile.setNickname(nickname);
        if (gender != null) profile.setGender(gender);
        if (avatar != null) profile.setAvatar(avatar);
        if (background != null) profile.setBackgroundImage(background);
        if (birthday != null) profile.setBirthday(birthday);
        if (signature != null) profile.setSignature(signature);
        if (hometown != null) profile.setHometown(hometown);
        if (phone != null) profile.setPhone(phone);
        if (highschool != null) profile.setHighschool(highschool);
        if (college != null) profile.setCollege(college);
        if (academy != null) profile.setAcademy(academy);
        if (grade != null) profile.setGrade(grade);
    }

    private void filterUnchangableFields(User u) {
        // id 虽然不可改变, 但是 hibernate 保存的时候设置的 id 是无效的, 所以这里可以不用过滤
        u.setRegInfo(null);
        // TODO 用户设置邮箱需要验证邮箱真伪, 所以这里暂且过滤
        u.setEmail(null);
        u.setEnabled(null);
        u.setFocuses(null);
        u.setFans(null);
        u.setFriendshipSet(null);
        u.setMembershipSet(null);

        Profile p = u.getProfile();
        // TODO 更改手机号码需要再次短信验证, 这里暂且过滤
        p.setPhone(null);
        // TODO 更改 idcard 和 realname 需要实名验证系统的完善, 这里暂且过滤
        p.setRealName(null);
        p.setIdCard(null);
        // 头像和背景在 jackson 转换的时候已经获取, 非法的token会导致这里的头像和背景是 null, 不影响更新
        // 大学和学院也是在 jackson 转换的时候确认的合法性
    }

    @Override
    public void deleteCurrentUser() {
        int id = getCurrentUserId();
        userRepo.delete(id);
    }

    @NotNull
    @Override
    public User getUserById(int id) {
        User user = userRepo.findOne(id);
        if (user == null) throw new UserNotFoundException(id);
        return user;
    }

    @Override
    public boolean isPhoneExisted(String phone) {
        return userRepo.existsByPhone(phone);
    }

    @Override
    public User findByPhone(String phone) {
        return userRepo.findByPhone(phone);
    }
}
