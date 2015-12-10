package org.team10424102.blackserver.services;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.team10424102.blackserver.models.Image;

public interface ImageService {

    /**
     * 为当前用户生成一张图片的访问 token
     */
    @NotNull
    String generateAccessToken(Image image);

    /**
     * 通过图片的访问 token, 为当前用户获得图片的真实 id
     * 如果当前用户无权限来访问这一张图片则返回 null
     */
    @Nullable
    Long getImageIdFromAccessToken(String accessToken);

    @Nullable
    Image getImageFromAccessToken(String accessToken);

    /**
     * 获得系统默认图片
     */
    @NotNull
    DefaultImage getDefault();

    /**
     * 为当前用户保存一张图片, 并保存默认的访问权限
     * (即只有当前用户才能访问的私有图片)
     * 当 API 再次将图片和其他实体(例如活动)关联的时候, 会修改相应的访问权限
     * 例如: 允许组内成员访问的图片
     */
    @NotNull
    Image saveImage(byte[] data, String tags);
}
