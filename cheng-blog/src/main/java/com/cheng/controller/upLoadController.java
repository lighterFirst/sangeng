package com.cheng.controller;

import com.cheng.domain.ResponseResult;
import com.cheng.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传头像
 */

@RestController
public class upLoadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    public ResponseResult upLoadImg(MultipartFile img){
        return uploadService.getImg(img);

    }

}
