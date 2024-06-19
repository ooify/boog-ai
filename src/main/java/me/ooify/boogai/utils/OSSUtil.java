package me.ooify.boogai.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class OSSUtil {

    private final String endpoint;
    private final String bucketName;
    private final String accessKeyId;
    private final String accessKeySecret;

    public OSSUtil(@Value("${oss.endpoint}") String endpoint,
                   @Value("${oss.bucketName}") String bucketName,
                   @Value("${oss.accessKeyId}") String accessKeyId,
                   @Value("${oss.accessKeySecret}") String accessKeySecret) {
        this.endpoint = endpoint;
        this.bucketName = bucketName;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
    }

    public String updateOss(String objectName, MultipartFile file) throws Exception {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            ossClient.putObject(bucketName, objectName, file.getInputStream());
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return "https://boog-ai.oss-cn-beijing.aliyuncs.com/" + objectName;
    }
}
