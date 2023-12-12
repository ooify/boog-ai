package me.ooify.boogai.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import me.ooify.boogai.entity.Balance;
import me.ooify.boogai.entity.User;
import me.ooify.boogai.service.impl.BalanceServiceImpl;
import me.ooify.boogai.service.impl.UserServiceImpl;
import me.ooify.boogai.utils.Result;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

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
@RequestMapping("/balance")
public class BalanceController {

    @Resource
    private BalanceServiceImpl balanceService;
    @Resource
    private UserServiceImpl userService;
    @Resource
    private ModelMapper modelMapper;


    @SaCheckRole("admin")
    @GetMapping("/{id}")
    public Result getBalanceById(@PathVariable Long id) {
        return Result.ok("查询成功")
                .setData(balanceService.getById(id));
    }

    @GetMapping("/user")
    public Result getBalanceByUser(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                   @RequestParam(value = "changeType", required = false) String changeType,
                                   @RequestParam(value = "sortField", defaultValue = "created_at") String sortField,
                                   @RequestParam(value = "sortOrder", defaultValue = "desc") String sortOrder) {
        pageNum = Math.max(1, pageNum);
        pageSize = Math.max(1, pageSize);
        if (!sortField.equals("created_at") && !sortField.equals("change_amount")) {
            sortField = "created_at";
        }
        if (!sortOrder.equals("asc") && !sortOrder.equals("desc")) {
            sortOrder = "desc";
        }
        Page<Balance> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Balance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", StpUtil.getLoginIdAsLong())
                .like(changeType != null, "change_type", changeType)
                .orderByDesc(sortOrder.equals("desc"), sortField)
                .orderByAsc(sortOrder.equals("asc"), sortField);
        return Result.ok("查询成功")
                .setData(balanceService.page(page, queryWrapper));

    }

    @PostMapping("/recharge")
    public Result addBalanceByUser(@RequestParam(value = "amount") Integer amount,
                                   @RequestParam(value = "type", defaultValue = "0") Integer type) {
        type = 0;
        if (amount <= 0) {
            return Result.error("充值金额必须大于0");
        }
        if (amount > 10000) {
            return Result.error("充值金额不能大于10000");
        }
        User user = userService.getById(StpUtil.getLoginIdAsLong());
        Balance balance = new Balance();
        balance.setUserId(StpUtil.getLoginIdAsLong());
        balance.setChangeAmount(Double.valueOf(amount));
        balance.setChangeType("充值");
        balance.setBalance(user.getBalance() + amount);
        return Result.ok("充值成功")
                .setData(balanceService.save(balance));
    }

}
