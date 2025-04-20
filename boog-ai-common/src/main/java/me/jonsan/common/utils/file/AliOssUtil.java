package me.jonsan.common.utils.file;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import me.jonsan.common.utils.StringUtils;
import me.jonsan.common.utils.bean.AliOSSProperties;
import me.jonsan.common.utils.uuid.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 阿里云 OSS 工具类
 */

@Component
public class AliOssUtil {
    @Autowired//将OSS的配置注入进来，后面需要用到
    private AliOSSProperties aliOSSProperties;

    /**
     * 以下是一个实现上传文件到OSS的方法
     */

    public String upload(MultipartFile file, String filename, String directory) throws IOException {
        // 获取阿里云OSS参数
        String endpoint = aliOSSProperties.getEndpoint();
        String accessKeyId = aliOSSProperties.getAccessKeyId();
        String accessKeySecret = aliOSSProperties.getAccessKeySecret();
        String bucketName = aliOSSProperties.getBucketName();

        // 获取上传的文件的输入流
        InputStream inputStream = file.getInputStream();

        // 避免文件覆盖，使用UUID和传入的文件名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 如果没有传入filename，则使用UUID生成
        if (StringUtils.isEmpty(filename)) {
            filename = UUID.randomUUID().toString();
        }

        // 组装最终的文件名，包括目录
        String finalFileName = directory + "/" + filename + fileExtension;

        // 上传文件到 OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, finalFileName, inputStream);

        // 文件访问路径
        String url = "https://" + bucketName + "." + endpoint + "/" + finalFileName;

        // 关闭ossClient
        ossClient.shutdown();

        // 把上传到oss的路径返回
        return url;
    }

}