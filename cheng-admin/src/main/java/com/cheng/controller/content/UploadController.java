package com.cheng.controller.content;

import com.cheng.domain.ResponseResult;
import com.cheng.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    /**
     * 上传图片
     * @param img
     * @return
     */

    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    public ResponseResult upload(MultipartFile img){
        return uploadService.getImg(img);
    }

}
