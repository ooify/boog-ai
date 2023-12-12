package me.ooify.boogai.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import me.ooify.boogai.dto.author.AuthorDTO;
import me.ooify.boogai.entity.Author;
import me.ooify.boogai.entity.User;
import me.ooify.boogai.service.impl.AuthorServiceImpl;
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
@RequestMapping("/author")
public class AuthorController {
    @Resource
    private AuthorServiceImpl authorService;

    @Resource
    private ModelMapper modelMapper;

    @GetMapping("/{id}")
    public Result getAuthorById(@PathVariable Long id) {
        Author author = authorService.getById(id);
        AuthorDTO map = modelMapper.map(author, AuthorDTO.class);
        return Result.ok("查询成功")
                .setData(map);
    }

    @GetMapping
    public Result getAllAuthors(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                @RequestParam(value = "AuthorName", required = false) String AuthorName,
                                @RequestParam(value = "sortField", defaultValue = "created_at") String sortField,
                                @RequestParam(value = "sortOrder", defaultValue = "desc") String sortOrder) {
        pageNum = Math.max(pageNum, 1);
        pageSize = Math.min(pageSize, 100);
        if (!sortField.equals("created_at") && !sortField.equals("updated_at")) {
            sortField = "created_at";
        }
        if (!sortOrder.equals("asc") && !sortOrder.equals("desc")) {
            sortOrder = "desc";
        }
        Page<Author> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Author> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(AuthorName != null, "name", AuthorName)
                .orderByAsc("asc".equalsIgnoreCase(sortOrder), sortField)
                .orderByDesc("desc".equalsIgnoreCase(sortOrder), sortField);

        Page<Author> paged = authorService.page(page, queryWrapper);
        IPage<AuthorDTO> convert = paged.convert(author -> modelMapper.map(author, AuthorDTO.class));
        return Result.ok("查询成功")
                .setData(convert);
    }

    @SaCheckRole("admin")
    @PostMapping
    public Result createAuthor(@Valid @RequestBody AuthorDTO authorDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        Author author = modelMapper.map(authorDTO, Author.class);
        authorService.save(author);
        return Result.ok("创建成功")
                .setData(author);
    }

    @SaCheckRole("admin")
    @PutMapping
    public Result updateAuthor(@Valid @RequestBody AuthorDTO authorDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        Author author = modelMapper.map(authorDTO, Author.class);
        authorService.updateById(author);
        return Result.ok("更新成功")
                .setData(author);
    }

    @SaCheckRole("admin")
    @DeleteMapping("/{id}")
    public Result deleteAuthor(@PathVariable Long id) {
        authorService.removeById(id);
        return Result.ok("删除成功");
    }

}
