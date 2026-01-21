package com.example.demo.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// ----------------
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;

@Service
public class ImageService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile file, String folder) throws IOException {
    	Transformation<?> transformation = new Transformation<>();
        
    	switch (folder) {
        case "avatars":
            // 使用者頭像：縮放並裁切成 250x250 正方形，自動對焦人臉
            transformation.width(250).height(250).crop("thumb").gravity("face");
            break;

        case "stores":
            // 店家主圖：寬度固定 800，高度等比例縮放 (fit)，確保門面完整不被裁切
            transformation.width(800).crop("fit");
            break;

        case "products":
            // 菜單品項圖：寬度 500，正方形裁切 (fill)，適合列表展示
            transformation.width(500).height(500).crop("fill");
            break;

        default:
            // 預設值：限制最大寬度 1024，避免過大檔案
            transformation.width(1024).crop("limit");
            break;
    }

    Map <String, Object> options = Map.of(
        "folder", "myapp/" + folder,
        "transformation", transformation,
        "secure", true
    );

        // 執行上傳
    Map <?, ?>uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
    return uploadResult.get("secure_url").toString();
    }
    
    public void deleteImage(String imageUrl) {
        // 安全檢查：如果網址為空，或不是我們自己 myapp 資料夾下的圖，則不處理
        if (imageUrl == null || imageUrl.isEmpty() || !imageUrl.contains("myapp/")) {
            return;
        }

        try {
            // 1. 提取 Public ID
            // 邏輯：找到 "myapp/" 的起始位置，並截取到最後一個 "." 之前
            int startIndex = imageUrl.indexOf("myapp/");
            int endIndex = imageUrl.lastIndexOf(".");

            if (startIndex != -1 && endIndex > startIndex) {
                String publicId = imageUrl.substring(startIndex, endIndex);

                // 2. 執行 Cloudinary 刪除指令
                // destroy 方法需要 public_id，options 通常傳空 Map
                Map<?, ?> result = cloudinary.uploader().destroy(publicId, com.cloudinary.utils.ObjectUtils.emptyMap());
                
                // 3. 檢查結果 (印出 log 方便除錯)
                if ("ok".equals(result.get("result"))) {
                    System.out.println("Cloudinary 圖片刪除成功: " + publicId);
                } else {
                    System.err.println("Cloudinary 圖片刪除失敗，原因: " + result.get("result"));
                }
            }
        } catch (Exception e) {
            // 捕捉 IOException 或其他異常，避免因為刪圖失敗導致整個業務流程中斷
            System.err.println("刪除雲端圖片時發生異常: " + e.getMessage());
        }
    }
    
}