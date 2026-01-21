package com.example.demo.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// --- 修正匯入 ---
import com.cloudinary.utils.ObjectUtils; 
import com.cloudinary.Transformation;
// ----------------
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;

@Service
public class ImageService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile file, String type) throws IOException {
        String folderPath = "myapp/" + type;
        
        // 此處的 ObjectUtils 現在指向 com.cloudinary.utils.ObjectUtils
        Map options = ObjectUtils.asMap(
            "folder", folderPath,
            "use_filename", true,
            "unique_filename", true
        );

        if ("avatars".equals(type)) {
            // 已經 import 了 Transformation，代碼會更乾淨
            options.put("transformation", new Transformation<>()
                .width(250).height(250).crop("thumb").gravity("face"));
        }

        // 執行上傳
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
        return uploadResult.get("secure_url").toString();
    }
}