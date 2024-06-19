package me.ooify.boogai.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import me.ooify.boogai.dto.user.RegisterDTO;
import me.ooify.boogai.dto.user.UserInfoDTO;
import me.ooify.boogai.entity.Cart;
import me.ooify.boogai.entity.User;
import me.ooify.boogai.service.impl.CartServiceImpl;
import me.ooify.boogai.service.impl.UserServiceImpl;
import me.ooify.boogai.utils.Result;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.Objects;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jonsan
 * @since 2023-12-07 07:11:14
 */
@Transactional
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserServiceImpl userService;
    @Resource
    private CartServiceImpl cartService;
    @Resource
    private ModelMapper modelMapper;

    @SaCheckRole("admin")
    @GetMapping("/{id}")
    public Result getUserById(@PathVariable Long id) {
        User user = userService.getOne(new QueryWrapper<User>().eq("user_id", id));
        return Result.ok("查询成功")
                .setData(user);
    }

    @GetMapping("/info")
    public Result getUserInfo() {
        User user = userService.getOne(new QueryWrapper<User>().eq("id", StpUtil.getLoginIdAsLong()));
        UserInfoDTO userInfo = modelMapper.map(user, UserInfoDTO.class);
        if (user.getStatus() == 2) {
            userInfo.setRoles(new String[]{"R_SUPER"});
        }
        return Result.ok("查询成功")
                .setData(userInfo);
    }

    @SaCheckRole("admin")
    @GetMapping("/list")
    public Result getUsers(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                           @RequestParam(value = "username", required = false) String username,
                           @RequestParam(value = "nickName", required = false) String nickName,
                           @RequestParam(value = "sex", required = false) Integer sex,
                           @RequestParam(value = "email", required = false) String email,
                           @RequestParam(value = "phonenumber", required = false) String phonenumber,
                           @RequestParam(value = "sortField", defaultValue = "created_at") String sortField,
                           @RequestParam(value = "sortOrder", defaultValue = "desc") String sortOrder) {

        pageNum = Math.max(1, pageNum);
        pageSize = Math.max(1, pageSize);

        if (!sortOrder.equals("desc") && !sortOrder.equals("asc")) {
            sortOrder = "desc";
        }

        Page<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.like(StringUtils.isNotBlank(username), "username", username)
                .like(StringUtils.isNotBlank(nickName), "nick_name", nickName)
                .eq(sex != null, "sex", sex)
                .like(StringUtils.isNotBlank(email), "email", email)
                .like(StringUtils.isNotBlank(phonenumber), "phonenumber", phonenumber)
                .orderByAsc("asc".equalsIgnoreCase(sortOrder), sortField)
                .orderByDesc("desc".equalsIgnoreCase(sortOrder), sortField);

        return Result.ok("查询成功").setData(userService.page(page, queryWrapper));
    }

    @SaCheckRole("admin")
    @PostMapping
    public Result createUser(@RequestBody User user) {
        System.out.println(user);
        if (userService.save(user)) {
            return Result.ok("新增成功");
        } else {
            return Result.error("新增失败");
        }
    }

    @SaCheckRole("admin")
    @PutMapping
    public Result updateUser(@RequestBody User user) {
        if (userService.updateById(user)) {
            return Result.ok("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }

    @PutMapping("/info")
    public Result updateUserInfo(@Valid @RequestBody RegisterDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        User user = modelMapper.map(userDTO, User.class);
        user.setId(StpUtil.getLoginIdAsLong());
        if (userService.updateById(user)) {
            return Result.ok("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }

    @SaCheckRole("admin")
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable Long id) {
        if (userService.removeById(id)) {
            cartService.remove(new QueryWrapper<Cart>().eq("user_id", id));
            return Result.ok("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }

}
