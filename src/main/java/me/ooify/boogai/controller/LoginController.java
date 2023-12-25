package me.ooify.boogai.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import me.ooify.boogai.dto.user.LoginDTO;
import me.ooify.boogai.dto.user.RegisterDTO;
import me.ooify.boogai.entity.Cart;
import me.ooify.boogai.entity.User;
import me.ooify.boogai.service.impl.CartServiceImpl;
import me.ooify.boogai.service.impl.UserServiceImpl;
import me.ooify.boogai.utils.Result;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Transactional
@RestController
public class LoginController {
    @Resource
    private UserServiceImpl userService;
    @Resource
    private CartServiceImpl cartService;
    @Resource
    private ModelMapper modelMapper;

    @PostMapping("/login")
    public Result login(@Valid @RequestBody(required = false) LoginDTO loginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        User user = modelMapper.map(loginDTO, User.class);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername())
                .eq("password", user.getPassword());
        User u = userService.getOne(queryWrapper);
        if (u != null) {
            StpUtil.login(u.getId());
            return Result.ok("登录成功")
                    .setToken(StpUtil.getTokenValue());
        } else {
            return Result.error("用户不存在/密码错误");
        }
    }
    @PostMapping("/logout")
    public Result logout() {
        StpUtil.logoutByTokenValue(StpUtil.getTokenValue());
        return Result.ok("登出成功");
    }

    @PostMapping("/register")
    public Result register(@Valid @RequestBody RegisterDTO registerDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        User user = modelMapper.map(registerDTO, User.class);
        if (userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername())) != null) {
            return Result.error("新增用户'" + user.getUsername() + "'失败，登录账号已存在");
        } else if (userService.getOne(new QueryWrapper<User>().eq("email", user.getEmail())) != null) {
            return Result.error("新增用户'" + user.getUsername() + "'失败，邮箱已存在");
        } else if (userService.getOne(new QueryWrapper<User>().eq("phonenumber", user.getPhonenumber())) != null) {
            return Result.error("新增用户'" + user.getUsername() + "'失败，手机号已存在");
        } else {
            if (userService.save(user)) {
                User one = userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername()).eq("password", user.getPassword()));
                Cart cart = new Cart();
                cart.setUserId(one.getId());
                cartService.save(cart);
                return Result.ok("新增用户'" + user.getUsername() + "'成功");
            } else {
                return Result.error("新增用户'" + user.getUsername() + "'失败");
            }
        }

    }
//    @PostMapping("/register/code")
//    public Result getRegisterCode(@Valid @RequestBody RegisterCodeDTO registerCodeDTO, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return Result.error(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
//        }
//
//    }

}
