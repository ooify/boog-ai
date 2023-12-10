package me.ooify.boogai.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import me.ooify.boogai.entity.Category;
import me.ooify.boogai.service.impl.CategoryServiceImpl;
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
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private CategoryServiceImpl categoryService;
    @GetMapping("/{id}")
    public Result getCategoryById(@PathVariable Long id) {
        return Result.ok("查询成功")
                .setData(categoryService.getById(id));
    }

    @GetMapping
    public Result getAllCategories(
            @RequestParam(value = "sortField", defaultValue = "created_at") String sortField,
            @RequestParam(value = "sortOrder", defaultValue = "desc") String sortOrder) {
        if (!sortField.equals("created_at") && !sortField.equals("hot")) {
            sortField = "created_at";
        }
        if (!sortOrder.equals("asc") && !sortOrder.equals("desc")) {
            sortOrder = "desc";
        }
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("asc".equalsIgnoreCase(sortOrder), sortField)
                .orderByDesc("desc".equalsIgnoreCase(sortOrder), sortField);

        return Result.ok("查询成功")
                .setData(categoryService.list(queryWrapper));
    }

    @SaCheckRole("admin")
    @PostMapping
    public Result createCategory(@RequestBody Category category) {
        categoryService.save(category);
        return Result.ok("创建成功")
                .setData(category);
    }

    @SaCheckRole("admin")
    @PutMapping("/{id}")
    public Result updateCategory(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        categoryService.updateById(category);
        return Result.ok("更新成功")
                .setData(category);
    }

    @SaCheckRole("admin")
    @DeleteMapping("/{id}")
    public Result deleteCategory(@PathVariable Long id) {
        categoryService.removeById(id);
        return Result.ok("删除成功");
    }

}
