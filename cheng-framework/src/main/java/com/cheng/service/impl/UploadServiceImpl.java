package com.cheng.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.cheng.domain.ResponseResult;
import com.cheng.enums.AppHttpCodeEnum;
import com.cheng.handle.exception.SystemException;
import com.cheng.service.UploadService;
import com.cheng.utils.PathUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class UploadServiceImpl implements UploadService {

    @Value("${oss.accessKeyId}")
    private String accessKeyId;
    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${oss.bucketName}")
    private String bucketName;

    @Override
    public ResponseResult getImg(MultipartFile img) {


        //1：判断文件类型或者文件大小

        //获取原始文件名
        String originalFileName = img.getOriginalFilename();
        //对原始文件名进行判断
        if(!((originalFileName.endsWith(".png")) || originalFileName.endsWith(".jpg"))){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }

        //如果判断通过，上传文件, filePath为拼接的文件名
        String filePath = PathUtils.generateFilePath(originalFileName);

        String upLoad = getUpLoad(img,filePath);
        return ResponseResult.okResult(upLoad);


    }

    private String getUpLoad(MultipartFile multipartFile, String filePath){

        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-beijing.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
       /* String accessKeyId = "LTAI5t8kB6gS11oofpww78wy";
        String accessKeySecret = "Sw8LfQMtCkEU55HFt3pMZVxeJUKOpX";*/
        // 填写Bucket名称，例如examplebucket。
        /*  String bucketName = "first-bucker";*/

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
        String objectName = filePath;


        try {
            /*
            官网给的是上传一个字符串
            String content = "Hello OSS";
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content.getBytes()));*/
            InputStream inputStream = multipartFile.getInputStream();
            ossClient.putObject(bucketName, objectName, inputStream);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (Exception ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        return "https://image.hudaye.cn/"+objectName;
    }




}
