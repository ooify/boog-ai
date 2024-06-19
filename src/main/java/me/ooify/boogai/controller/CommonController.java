package me.ooify.boogai.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.annotation.Resource;
import me.ooify.boogai.entity.User;
import me.ooify.boogai.service.UserService;
import me.ooify.boogai.utils.OSSUtil;
import me.ooify.boogai.utils.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@Transactional
@RestController
@RequestMapping("/common")
public class CommonController {
    @Resource
    private UserService userService;
    @Resource
    private OSSUtil ossUtil;
    @Value("${image.avatar.path}")
    private String avatar;

    @PostMapping("/feedback")
    public Result feedback(@PathVariable String content) {
        return Result.ok("反馈成功").setData(content);
    }


    @SaCheckLogin
    @PostMapping("/upload/avatar")
    public Result uploadAvater(@RequestParam("imageFile") MultipartFile imageFile) throws Exception {
        if (!imageFile.isEmpty()) {
            String fileName = imageFile.getOriginalFilename();
            // 构建保存文件的路径，使用用户ID作为文件名或路径的一部分
            String savePath = UriComponentsBuilder.newInstance()
                    .scheme("file")
                    .path(avatar)
                    .path(StpUtil.getLoginIdAsString())
                    .path("/")
                    .path(fileName)
                    .build()
                    .toUriString();

            // 保存文件
            Path path = Paths.get(new URI(savePath));
            Files.createDirectories(path.getParent()); // 创建父目录（用户ID目录）
            Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            User user = new User();
            user.setId(StpUtil.getLoginIdAsLong());
            user.setAvatar("/static/image/avatar/" + StpUtil.getLoginIdAsString() + "/" + fileName);
            userService.updateById(user);
            return Result.ok("上传成功").setData("/static/image/avatar/" + StpUtil.getLoginIdAsString() + "/" + fileName);
        } else {
            return Result.error("未选择文件");
        }
    }

    @SaCheckRole("admin")
    @PostMapping("/admin/upload/avatar")
    public Result adminUploadAvater(@RequestParam("imageFile") MultipartFile imageFile,
                                    @RequestParam(value = "userId") Long userId) throws Exception {
        if (imageFile.isEmpty()) {
            return Result.error("未选择文件");
        }
        String random = UUID.randomUUID().toString().replace("-", "").substring(0, 3);
        String fileType = imageFile.getContentType().split("/")[1];  // 比如 image/jpeg -> jpeg
        String fileName = "avatar" + random + "." + fileType;
        String url = ossUtil.updateOss(userId.toString() + "/" + fileName, imageFile);
        User user = new User();
        user.setId(userId);
        user.setAvatar(url);
        boolean b = userService.updateById(user);
        System.out.println(b);
        Map<String, Object> data = Map.of("url", url);
        System.out.println(data);
        System.out.println(user);
        System.out.println(userService.getById(userId));
        return Result.ok("上传成功").setData(data);
    }

}
