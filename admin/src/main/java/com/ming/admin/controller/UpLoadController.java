package com.ming.admin.controller;

import com.ming.admin.service.UpLoadService;
import com.ming.admin.util.Ajax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/upload")
public class UpLoadController {
    @Autowired
    private UpLoadService loadService;

    @PostMapping("/image")
    public Ajax image(MultipartFile file, HttpServletRequest request) {
        return loadService.upLoadImage(file, request);
    }
}
