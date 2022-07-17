package com.ming.admin.service;

import com.ming.admin.util.Ajax;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface UpLoadService {
    Ajax upLoadImage(MultipartFile file, HttpServletRequest request);
}
