package com.example.demo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.ImageService;

@RestController
@RequestMapping("/image")
@CrossOrigin(origins = "http://localhost:4200")
public class ImageController {

    @Autowired
    private ImageService imageService;

 // 使用者大頭照
    @PostMapping("/upload/avatar")
    public String uploadAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        return imageService.uploadImage(file, "avatars");
    }

    // 商店照片
    @PostMapping("/upload/stores")
    public String uploadStoresImage(@RequestParam("file") MultipartFile file) throws IOException {
        return imageService.uploadImage(file, "stores");
    }

    // 菜單照片
    @PostMapping("/upload/menu")
    public String uploadMenuImage(@RequestParam("file") MultipartFile file) throws IOException {
        return imageService.uploadImage(file, "menu");
    }
}
