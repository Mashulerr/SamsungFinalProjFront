package com.example.spacex.ui.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryConfig {

    private static Cloudinary cloudinary;

    public static Cloudinary getInstance() {
        if (cloudinary == null) {
            cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "dljsgkjkg",
                    "api_key", "756594317299239",
                    "api_secret", "dVGKz3fihgCggWY4ulTInS14aAo"
            ));
        }
        return cloudinary;
    }
}
