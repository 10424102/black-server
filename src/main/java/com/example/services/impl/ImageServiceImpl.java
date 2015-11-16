package com.example.services.impl;

import com.example.daos.ImageRepo;
import com.example.exceptions.IllegalTokenException;
import com.example.exceptions.TokenExpiredException;
import com.example.models.Image;
import com.example.models.UserGroup;
import com.example.services.GroupService;
import com.example.services.ImageService;
import com.example.services.UserService;
import com.example.utils.Cryptor;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ImageServiceImpl implements ImageService {
    public static final int ACCESS_TOKEN_EXPIRE = 1000 * 60 * 5; // 有效时间 5 分钟，单位毫秒
    static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);
    @Autowired
    ImageRepo imageRepo;

    @Autowired
    UserService userService;

    @Autowired
    GroupService groupService;

    /**
     * 判断当前用户是否有足够的权限来访问这张图片
     */
    public boolean canAccessImage(Image image) {
        int flags = image.getFlags();
        if ((flags & Image.FLAG_PRIVATE) == 0) return true; // 公共图片
        int uid = userService.getCurrentUserId();
        if ((flags & Image.FLAG_USER) > 0) {
            if (uid == image.getUid()) {
                // 就是用户自己的图片
                return true;
            }
        }
        if ((flags & Image.FLAG_USER_GROUP) > 0) {
            long gid = image.getGid();
            if (gid >= UserGroup.GID_BASE) {
                // 检查当前用户知否是这个组的成员
                if (groupService.isUserInGroup(uid, gid)) return true;
            } else {
                int id = (int) gid;
                int imageUid = image.getUid();
                switch (id) {
                    case UserGroup.GID_FRIENDS:
                        // 检查当前用户是否是图片 uid 的朋友
                        if (userService.currentUserIsHisFriend(imageUid)) return true;
                        break;
                    case UserGroup.GID_FOLLOWINGS:
                        // 检查当前用户是否是图片 uid 的粉丝
                        if (userService.currentUserIsHisFan(imageUid)) return true;
                        break;
                    case UserGroup.GID_FANS:
                        // 检查当前用户是否是图片 uid 关注的人
                        if (userService.currentUserIsHisFollwing(imageUid)) return true;
                        break;
                }
            }
        }
        return false;
    }

    /**
     * 获得这一张图片的访问 token 字符串
     *
     * @param image
     * @return
     */
    public String generateAccessToken(Image image) {
        return encrypt(image);
    }

    /**
     * 转换用户提交的图片数据
     *
     * @param bytes
     * @return
     */
    public byte[] convertToPNGImageData(byte[] bytes) {
        // TODO convert to png image data
        return bytes;
    }

    /**
     * token 的组成
     * - flags  int32
     * - uid    int32
     * - gid    int64
     * - expire int64
     * - id     int64
     * 总共 40 个字节
     */
    private String encrypt(Image image) {
        byte[] data = new byte[16];
        int flags = image.getFlags();
        int uid = image.getUid();
        long gid = image.getGid();
        long expire = new Date().getTime() + ACCESS_TOKEN_EXPIRE;
        long id = image.getId();
        for (int i = 0; i < 4; i++) {
            data[i] = (byte) flags;
            flags >>= 8;
        }
        for (int i = 4; i < 8; i++) {
            data[i] = (byte) uid;
            uid >>= 8;
        }
        for (int i = 8; i < 16; i++) {
            data[i] = (byte) gid;
            gid >>= 8;
        }
        for (int i = 16; i < 32; i++) {
            data[i] = (byte) expire;
            expire >>= 8;
        }
        for (int i = 32; i < 40; i++) {
            data[i] = (byte) id;
            id >>= 8;
        }
        return Cryptor.encrypt(data) + "~" + image.getHash();
    }

    private Image decrypt(String accessToken) throws IllegalTokenException {
        String[] parts = accessToken.split("~");
        if (parts.length < 2) throw new IllegalTokenException();
        String token = parts[0];
        String hash = parts[1];

        byte[] data = Cryptor.decrypt(accessToken);
        if (data == null) throw new IllegalTokenException();
        int flags = 0, uid = 0;
        long gid = 0, expire = 0, id = 0;
        for (int i = 39; i >= 32; i--) {
            id = id << 8 | data[i];
        }
        for (int i = 31; i >= 16; i--) {
            expire = expire << 8 | data[i];
        }
        if (expire > new Date().getTime()) throw new TokenExpiredException();

        for (int i = 15; i >= 8; i--) {
            gid = gid << 8 | data[i];
        }
        for (int i = 7; i >= 4; i--) {
            uid = uid << 8 | data[i];
        }
        for (int i = 3; i >= 0; i--) {
            flags = flags << 8 | data[i];
        }
        Image image = new Image();
        image.setId(id);
        image.setFlags(flags);
        image.setUid(uid);
        image.setGid(gid);
        return image;
    }

    /**
     * 本函数用户更新数据的时候关联图片
     * 如果 accessToken 失效，或者权限不够，都会返回 null
     */
    @Nullable
    public Long getImageIdFromAccessToken(String accessToken) {
        try {
            Image image = decrypt(accessToken);
            if (canAccessImage(image)) return image.getId();
        } catch (IllegalTokenException e) {
            logger.debug("非法的图片访问 token", e);
            return null;
        }
        return null;
    }

}
