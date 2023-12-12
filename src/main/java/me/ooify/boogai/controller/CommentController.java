package me.ooify.boogai.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import me.ooify.boogai.dto.comment.CommentDTO;
import me.ooify.boogai.dto.comment.CommentLikeDTO;
import me.ooify.boogai.dto.comment.CommentsDTO;
import me.ooify.boogai.dto.user.CommentUserDTO;
import me.ooify.boogai.entity.Book;
import me.ooify.boogai.entity.Comment;

import me.ooify.boogai.entity.CommentLike;
import me.ooify.boogai.service.impl.BookServiceImpl;
import me.ooify.boogai.service.impl.CommentLikeServiceImpl;
import me.ooify.boogai.service.impl.CommentServiceImpl;
import me.ooify.boogai.service.impl.UserServiceImpl;
import me.ooify.boogai.utils.Result;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentServiceImpl commentService;
    @Resource
    private UserServiceImpl userService;
    @Resource
    private BookServiceImpl bookService;
    @Resource
    private CommentLikeServiceImpl commentLikeService;
    @Resource
    private ModelMapper modelMapper;


    @SaCheckRole("admin")
    @GetMapping("/{id}")
    public Result getCommentById(@PathVariable Long id) {
        return Result.ok("查询成功")
                .setData(commentService.getById(id));
    }

    @GetMapping("/book")
    public Result getCommentsByBookId(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                      @RequestParam(value = "sortField", defaultValue = "created_at") String sortField,
                                      @RequestParam(value = "bookId") Long bookId) {
        pageNum = Math.max(1, pageNum);
        pageSize = Math.max(1, pageSize);
        if (!sortField.equals("created_at") && !sortField.equals("likes")) {
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
            if (StpUtil.isLogin()) {
                commentsDTO.setIsLiked(commentLikeService.getOne(new QueryWrapper<CommentLike>().eq("user_id", StpUtil.getLoginIdAsLong()).eq("comment_id", comment.getId())) != null);
            }
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
        if (!sortField.equals("created_at") && !sortField.equals("likes")) {
            sortField = "created_at";
        }
        Page<Comment> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", StpUtil.getLoginIdAsLong());
        queryWrapper.orderByDesc(sortField);
        return Result.ok("查询成功")
                .setData(commentService.page(page, queryWrapper));
    }

    @SaCheckRole("admin")
    @GetMapping("/list")
    public Result getComments(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                              @RequestParam(value = "bookId", required = false) Long bookId,
                              @RequestParam(value = "userId", required = false) Long userId,
                              @RequestParam(value = "star", required = false) Integer star,
                              @RequestParam(value = "sortField", defaultValue = "created_at") String sortField,
                              @RequestParam(value = "sortOrder", defaultValue = "desc") String sortOrder) {
        pageNum = Math.max(1, pageNum);
        pageSize = Math.max(1, pageSize);
        if (!sortField.equals("created_at") && !sortField.equals("likes") && !sortField.equals("star")) {
            sortField = "created_at";
        }
        if (!sortOrder.equals("desc") && !sortOrder.equals("asc")) {
            sortOrder = "desc";
        }
        Page<Comment> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(bookId != null, "book_id", bookId)
                .eq(userId != null, "user_id", userId)
                .eq(star != null, "star", star)
                .orderByAsc("asc".equalsIgnoreCase(sortOrder), sortField)
                .orderByDesc("desc".equalsIgnoreCase(sortOrder), sortField);
        return Result.ok("查询成功")
                .setData(commentService.page(page, queryWrapper));
    }

    @PostMapping
    public Result addComment(@Valid @RequestBody CommentDTO commentDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        if (bookService.getOne(new QueryWrapper<Book>().eq("id", comment.getBookId())) == null) {
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

    @PutMapping("/like")
    public Result likeComment(@Valid @RequestBody CommentLikeDTO commentLikeDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        CommentLike commentLike = modelMapper.map(commentLikeDTO, CommentLike.class);
        commentLike.setUserId(StpUtil.getLoginIdAsLong());
        if (commentService.getOne(new QueryWrapper<Comment>().eq("id", commentLike.getCommentId())) == null) {
            return Result.error("评论不存在");
        }
        if (commentLikeDTO.getIsLike() == 0) {
            if (commentLikeService.remove(new QueryWrapper<CommentLike>().eq("user_id", commentLike.getUserId()).eq("comment_id", commentLike.getCommentId()))) {
                return Result.ok("取消点赞成功");
            } else {
                return Result.error("取消点赞失败");
            }
        } else {
            if (commentLikeService.save(commentLike)) {
                return Result.ok("点赞成功");
            } else {
                return Result.error("点赞失败");
            }
        }
    }

    @SaCheckRole("admin")
    @DeleteMapping("/{id}")
    public Result deleteComment(@PathVariable Long id) {
        if (commentService.removeById(id)) {
            return Result.ok("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }


}
