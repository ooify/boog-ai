package me.ooify.boogai.utils;

import cn.dev33.satoken.stp.StpInterface;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import me.ooify.boogai.entity.User;
import me.ooify.boogai.service.impl.UserServiceImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限加载接口实现类
 */
@Component    // 保证此类被 SpringBoot 扫描，完成 Sa-Token 的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {
    @Resource
    UserServiceImpl userService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return null;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> list = new ArrayList<String>();
        if (userService.getOne(new QueryWrapper<User>().eq("id", loginId)).getStatus() == 2) {
            list.add("admin");
        }
        if (userService.getOne(new QueryWrapper<User>().eq("id", loginId)).getStatus() == 1) {
            list.add("user");
        }
        return list;
    }

}
