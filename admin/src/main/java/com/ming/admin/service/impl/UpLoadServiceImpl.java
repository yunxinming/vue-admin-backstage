package com.ming.admin.service.impl;

import com.ming.admin.service.UpLoadService;
import com.ming.admin.util.Ajax;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class UpLoadServiceImpl implements UpLoadService {
    @Value("${upload.save.file.path}")
    private String savePath;
    @Override
    public Ajax upLoadImage(MultipartFile file, HttpServletRequest request) {
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        filename = UUID.randomUUID() + suffix;
        url = url + "/images/" +filename;
        try {
            file.transferTo(new File(savePath, filename));
        } catch (IOException e) {
            return Ajax.error("上传失败");
        }
        return Ajax.success("上传成功", url);
    }
}
