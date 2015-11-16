package com.example.services;

import com.example.models.Image;

public interface ImageService {
    boolean canAccessImage(Image image);
    String generateAccessToken(Image image);
    byte[] convertToPNGImageData(byte[] bytes);
    Long getImageIdFromAccessToken(String accessToken);
}
