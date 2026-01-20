package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
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

    private final List<String> ALLOWED_TYPES = List.of("avatars", "stores", "menu");
    
    @PostMapping("/upload/{type}")
    public String upload(@PathVariable String type, @RequestParam("file") MultipartFile file) throws IOException {
    	
    	if (!ALLOWED_TYPES.contains(type)) {
    		throw new IllegalArgumentException("不支援的上傳類型");
    	}
        return imageService.uploadImage(file, type);
    }
}
