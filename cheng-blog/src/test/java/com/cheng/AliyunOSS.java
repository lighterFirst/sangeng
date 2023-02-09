package com.cheng;

import com.aliyun.oss.*;
import com.cheng.enums.AppHttpCodeEnum;
import com.cheng.handle.exception.SystemException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.FileInputStream;
import java.io.InputStream;

@SpringBootTest
public class AliyunOSS {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${oss.accessKeyId}")
    private String accessKeyId;
    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${oss.bucketName}")
    private String bucketName;

    @Test
    public void test(){

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
        String objectName = "2022/12/2.jpg";


        try {
            /*
            官网给的是上传一个字符串
            String content = "Hello OSS";
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content.getBytes()));*/
            InputStream inputStream = new FileInputStream("C:\\Users\\lenovo\\Desktop\\image\\百度贴吧\\d3b2f1c2324f3d24b03da834eaa7289.jpg");
            ossClient.putObject(bucketName, objectName, inputStream);
            System.out.println(12345);
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
    }

    @Test
    public void testpassword(){
        boolean key = passwordEncoder.matches("123_abc","$2a$10$4F6Vu1YVef88UgByYi4gguBZo5Dqj9iJHakLyg4Rhm2HASiDZkwGG");
        System.out.println(key);
    }

    @Test
    public void test02(){

       String originalFileName = "1.sql";

        if(((originalFileName.endsWith(".png")) || originalFileName.endsWith(".jpg"))){
            System.out.println("success");
        }else {
            System.out.println("fail");
        }
    }

}
