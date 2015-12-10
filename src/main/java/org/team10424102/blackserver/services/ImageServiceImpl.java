package org.team10424102.blackserver.services;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.team10424102.blackserver.App;
import org.team10424102.blackserver.daos.ImageRepo;
import org.team10424102.blackserver.exceptions.PersistEntityException;
import org.team10424102.blackserver.models.Image;
import org.team10424102.blackserver.utils.Cryptor;

import java.io.IOException;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService, DefaultImage {
    public static final int HALF_AN_HOUR_IN_MILLISECONDS = 30 * 60 * 1000;
    static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);
    private static final Cache tokenCache = CacheManager.getInstance().getCache("imageTokenCache");
    @Autowired ImageRepo imageRepo;
    @Autowired UserService userService;
    @Autowired GroupService groupService;
    @Autowired ApplicationContext context;

    private Image avatar;
    private Image background;
    private Image cover;

    @Autowired
    public ImageServiceImpl(ApplicationContext context, ImageRepo imageRepo) throws IOException {
        this.imageRepo = imageRepo;

        // TODO 检查路径在 Windows 上是否起作用
        avatar = imageRepo.findOneByTags(App.DEFAULT_AVATAR_TAG);
        if (avatar == null) {
            byte[] data = IOUtils.toByteArray(
                    context.getResource("classpath:default/default_avatar.png").getInputStream());
            avatar = saveDefaultImage(data, App.DEFAULT_AVATAR_TAG);
        }

        background = imageRepo.findOneByTags(App.DEFAULT_BACKGROUND_TAG);
        if (background == null) {
            byte[] data = IOUtils.toByteArray(
                    context.getResource("classpath:default/default_background.png").getInputStream());
            background = saveDefaultImage(data, App.DEFAULT_BACKGROUND_TAG);
        }

        cover = imageRepo.findOneByTags(App.DEFAULT_COVER_TAG);
        if (cover == null) {
            byte[] data = IOUtils.toByteArray(
                    context.getResource("classpath:default/default_cover.png").getInputStream());
            cover = saveDefaultImage(data, App.DEFAULT_COVER_TAG);
        }
    }

    @Scheduled(fixedRate = HALF_AN_HOUR_IN_MILLISECONDS)
    public void evictExpiredTokens() {
        tokenCache.evictExpiredElements();
    }

    /**
     * 为当前用户生成一张图片的访问 token
     */
    @NotNull
    public String generateAccessToken(Image image) {
        String token = UUID.randomUUID().toString();
        long id = image.getId();
        // TODO 改进 Ehcache 和 Hibernate 的协作关系, 这里获取 id 是为了通过 token 拿出来的 image 可以获取到 id
        // 避免出现延迟加载没有 session 的问题
        // logger.debug(String.format("生成图片 (id=%d) 的 token: %s", id, token));
        tokenCache.put(new Element(token, image));
        return token;
    }

    /**
     * 通过图片的访问 token, 为当前用户获得图片的真实 id
     * 如果当前用户无权限来访问这一张图片则返回 null
     */
    @Nullable
    public Long getImageIdFromAccessToken(String accessToken) {
        Element element = tokenCache.get(accessToken);
        if (element == null) return null;
        Image image = (Image) element.getObjectValue();
        return image.getId();
    }

    @Nullable
    @Override
    public Image getImageFromAccessToken(String accessToken) {
        Long id = getImageIdFromAccessToken(accessToken);
        if (id == null) return null;
        return imageRepo.findOne(id);
    }

    /**
     * 获得系统默认图片
     */
    @NotNull
    public DefaultImage getDefault() {
        return this;
    }

    /**
     * 为当前用户保存一张图片, 并保存默认的访问权限
     * (即只有当前用户才能访问的私有图片)
     * 当 API 再次将图片和其他实体(例如活动)关联的时候, 会修改相应的访问权限
     * 例如: 允许组内成员访问的图片
     */
    @NotNull
    public Image saveImage(byte[] data, String tags) {
        data = convertToPNGImageData(data);
        String hash = hash(data);
        Image image = imageRepo.findOneByHash(hash);
        Image savedImage = null;
        if (image == null) {
            image = new Image();
            image.setData(data);
            image.setHash(hash);
            image.setTags(tags);
            savedImage = imageRepo.save(image);
        }

        if (savedImage == null) throw new PersistEntityException(Image.class);

        return savedImage;
    }

    private Image saveDefaultImage(byte[] data, String tags) {
        data = convertToPNGImageData(data);
        String hash = hash(data);
        Image savedImage;
        Image image;
        image = new Image();
        image.setData(data);
        image.setHash(hash);
        image.setTags(tags);
        savedImage = imageRepo.save(image);

        if (savedImage == null) throw new PersistEntityException(Image.class);

        return savedImage;
    }

    /**
     * 转换用户提交的图片数据
     */
    private byte[] convertToPNGImageData(byte[] bytes) {
        // TODO convert to png image data
        return bytes;
    }

    private String hash(byte[] data) {
        return Cryptor.md5(data);
    }

    @Override
    public Image avatar() {
        return avatar;
    }

    @Override
    public Image background() {
        return background;
    }

    @Override
    public Image cover() {
        return cover;
    }
}
