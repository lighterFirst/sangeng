package com.cheng.service;

import com.cheng.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    ResponseResult getImg(MultipartFile img);
}
