package me.ooify.boogai.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import me.ooify.boogai.entity.Notice;
import me.ooify.boogai.service.impl.NoticeServiceImpl;
import me.ooify.boogai.utils.Result;
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
@RequestMapping("/notice")
public class NoticeController {
    @Resource
    NoticeServiceImpl noticeService;


    @GetMapping("/{id}")
    public Result getNoticeById(@PathVariable Long id) {
        return Result.ok("查询成功")
                .setData(noticeService.getById(id));
    }

    @GetMapping("/all")
    public Result getAllNotices() {
        return Result.ok("查询成功")
                .setData(noticeService.list(new QueryWrapper<Notice>().orderByDesc("hot")));
    }

    @SaCheckRole("admin")
    @PostMapping
    public Result addNotice(@RequestBody Notice notice) {
        if (noticeService.save(notice)) {
            return Result.ok("添加成功");
        } else {
            return Result.error("添加失败");
        }
    }

    @SaCheckRole("admin")
    @PutMapping
    public Result updateNotice(@RequestBody Notice notice) {
        if (noticeService.updateById(notice)) {
            return Result.ok("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }

    @SaCheckRole("admin")
    @DeleteMapping("/{id}")
    public Result deleteNotice(@PathVariable Long id) {
        if (noticeService.removeById(id)) {
            return Result.ok("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
}
