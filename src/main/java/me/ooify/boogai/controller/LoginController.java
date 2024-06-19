package me.ooify.boogai.controller;


import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import me.ooify.boogai.dto.login.RegisterCodeDTO;
import me.ooify.boogai.dto.user.LoginDTO;
import me.ooify.boogai.dto.user.RegisterDTO;
import me.ooify.boogai.entity.Cart;
import me.ooify.boogai.entity.Code;
import me.ooify.boogai.entity.User;
import me.ooify.boogai.service.impl.CartServiceImpl;
import me.ooify.boogai.service.impl.CodeServiceImpl;
import me.ooify.boogai.service.impl.UserServiceImpl;
import me.ooify.boogai.utils.Email;
import me.ooify.boogai.utils.Result;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Transactional
@RestController
public class LoginController {
    @Resource
    private UserServiceImpl userService;
    @Resource
    private CartServiceImpl cartService;
    @Resource
    private CodeServiceImpl codeService;
    @Resource
    private Email email;
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
            Map<String, Object> map = new HashMap<>();
            map.put("token", StpUtil.getTokenValue());
            return Result.ok("登录成功").setData(map);
//            return Result.ok("登录成功")
//                    .setToken(StpUtil.getTokenValue());
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
        Code code = codeService.getOne(new QueryWrapper<Code>().eq("email", registerDTO.getEmail()));
        if (code == null) {
            return Result.error("验证码错误");
        } else {
            if (!code.getCode().equals(registerDTO.getCode())) {
                return Result.error("验证码错误");
            }
        }
        codeService.removeById(code.getId());
        User user = modelMapper.map(registerDTO, User.class);
        if (userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername())) != null) {
            return Result.error("新增用户'" + user.getUsername() + "'失败，登录账号已存在");
        } else if (userService.getOne(new QueryWrapper<User>().eq("phonenumber", user.getPhonenumber())) != null) {
            return Result.error("新增用户'" + user.getUsername() + "'失败，手机号已存在");
        } else {
            String UUID = IdUtil.simpleUUID();
            user.setNickName("新用户" + UUID.substring(0, 5));
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

    @SaIgnore
    @PostMapping("/register/code")
    public Result getRegisterCode(@Valid @RequestBody RegisterCodeDTO registerCodeDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        if (userService.getOne(new QueryWrapper<User>().eq("email", registerCodeDTO.getEmail())) != null) {
            return Result.error("邮箱已存在");
        }
        Code code = new Code();
        code.setEmail(registerCodeDTO.getEmail());
        code.setCode((int) ((Math.random() * 9 + 1) * 100000));
        Code c = codeService.getOne(new QueryWrapper<Code>().eq("email", code.getEmail()));
        if (c != null) {
            if (System.currentTimeMillis() - c.getCreatedAt().getTime() > 60000) {
                codeService.removeById(c.getId());
            } else {
                return Result.error("发送失败，一分钟内只能发送一次");
            }
        }
        codeService.save(code);
        email.sentEmail(code.getEmail(), code.getCode());
        return Result.ok("发送成功");
    }

}
