package me.ooify.boogai.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import me.ooify.boogai.dto.comment.CommentDTO;
import me.ooify.boogai.dto.comment.CommentsDTO;
import me.ooify.boogai.dto.user.CommentUserDTO;
import me.ooify.boogai.entity.Comment;

import me.ooify.boogai.service.impl.CommentServiceImpl;
import me.ooify.boogai.service.impl.UserServiceImpl;
import me.ooify.boogai.utils.Result;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static net.sf.jsqlparser.parser.feature.Feature.comment;


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
@RequestMapping("/comment")
public class CommentController {
    @Resource
    CommentServiceImpl commentService;
    @Resource
    UserServiceImpl userService;
    @Resource
    private ModelMapper modelMapper;


    @GetMapping("/{id}")
    public Result getCommentById(@PathVariable Long id) {
        return Result.ok("查询成功")
                .setData(commentService.getById(id));
    }

    @GetMapping("/book")
    public Result getCommentsByBookId(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                      @RequestParam(value = "sortField", defaultValue = "created_at") String sortField,
                                      @RequestParam(value = "bookId") String bookId) {
        pageNum = Math.max(1, pageNum);
        pageSize = Math.max(1, pageSize);
        if (!sortField.equals("created_at") && !sortField.equals("hot")) {
            sortField = "created_at";
        }
        Page<Comment> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_id", bookId);
        queryWrapper.orderByDesc(sortField);
        Page<Comment> commentPage = commentService.page(page, queryWrapper);
        commentPage.convert(comment -> {
            CommentsDTO commentsDTO = modelMapper.map(comment, CommentsDTO.class);
            commentsDTO.setUser(modelMapper.map(userService.getById(comment.getUserId()), CommentUserDTO.class));
            return commentsDTO;
        });
        return Result.ok("查询成功")
                .setData(commentPage);
    }

    @GetMapping("/user")
    public Result getCommentsByUserId(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                      @RequestParam(value = "sortField", defaultValue = "created_at") String sortField) {
        pageNum = Math.max(1, pageNum);
        pageSize = Math.max(1, pageSize);
        if (!sortField.equals("created_at") && !sortField.equals("hot")) {
            sortField = "created_at";
        }
        Page<Comment> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", StpUtil.getLoginIdAsLong());
        queryWrapper.orderByDesc(sortField);
        return Result.ok("查询成功")
                .setData(commentService.page(page, queryWrapper));
    }

    @PostMapping
    public Result addComment(@Valid @RequestBody CommentDTO commentDTO) {
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        if (commentService.getOne(new QueryWrapper<Comment>().eq("book_id", comment.getBookId())) == null) {
            return Result.error("书籍不存在");
        }
        if (commentService.getOne(new QueryWrapper<Comment>().eq("user_id", StpUtil.getLoginIdAsLong()).eq("book_id", comment.getBookId())) != null) {
            return Result.error("您已经评论过了");
        }
        comment.setUserId(StpUtil.getLoginIdAsLong());
        if (commentService.save(comment)) {
            return Result.ok("添加成功");
        } else {
            return Result.error("添加失败");
        }
    }

    @DeleteMapping("/{id}")
    public Result deleteComment(@PathVariable Long id) {
        if (commentService.removeById(id)) {
            return Result.ok("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }


}
