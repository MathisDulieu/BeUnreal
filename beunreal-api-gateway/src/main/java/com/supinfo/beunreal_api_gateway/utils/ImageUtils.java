package com.supinfo.beunreal_api_gateway.utils;

import com.cloudinary.Cloudinary;
import com.supinfo.beunreal_api_gateway.configuration.EnvConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ImageUtils {

    private final Cloudinary cloudinary;

    @Autowired
    public ImageUtils(EnvConfiguration envConfiguration) {
        this.cloudinary = new Cloudinary(envConfiguration.getCloudinaryConfig());
    }

    public String uploadMedia(MultipartFile mediaFile) throws IOException {
        Map<String, Object> uploadParams = new HashMap<>();

        String contentType = mediaFile.getContentType();
        if (contentType.startsWith("video/")) {
            uploadParams.put("resource_type", "video");
        }

        uploadParams.put("secure", true);

        uploadParams.put("folder", "beunreal_media");

        Map uploadResult = cloudinary.uploader().upload(mediaFile.getBytes(), uploadParams);
        return (String) uploadResult.get("secure_url");
    }

}
