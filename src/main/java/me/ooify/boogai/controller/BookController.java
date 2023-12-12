package me.ooify.boogai.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import me.ooify.boogai.dto.author.AuthorDTO;
import me.ooify.boogai.dto.book.BooksDTO;
import me.ooify.boogai.entity.Book;
import me.ooify.boogai.entity.Category;
import me.ooify.boogai.service.impl.AuthorServiceImpl;
import me.ooify.boogai.service.impl.BookServiceImpl;
import me.ooify.boogai.service.impl.CategoryServiceImpl;
import me.ooify.boogai.utils.Result;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 图书表 前端控制器
 * </p>
 *
 * @author jonsan
 * @since 2023-12-07 07:11:14
 */
@Transactional
@RestController
@RequestMapping("/book")
public class BookController {

    @Resource
    private BookServiceImpl bookService;
    @Resource
    private CategoryServiceImpl categoryService;
    @Resource
    private AuthorServiceImpl authorService;
    @Resource
    private ModelMapper modelMapper;

    @GetMapping("/{id}")
    public Result getBookById(@PathVariable Long id) {
        Book book = bookService.getById(id);
        BooksDTO bookDTO = modelMapper.map(book, BooksDTO.class);
        bookDTO.setCategory(categoryService.getById(book.getCategoryId()));
        bookDTO.setAuthor(modelMapper.map(authorService.getById(book.getAuthorId()), AuthorDTO.class));
        return Result.ok("查询成功")
                .setData(bookDTO);
    }

    @GetMapping
    public Result getBooks(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                           @RequestParam(value = "categoryId", required = false) Long categoryId,
                           @RequestParam(value = "authorId", required = false) Long authorId,
                           @RequestParam(value = "title", required = false) String title,
                           @RequestParam(value = "isbn", required = false) String isbn,
                           @RequestParam(value = "language", required = false) String language,
                           @RequestParam(value = "format", required = false) String format,
                           @RequestParam(value = "publisher", required = false) String publisher,
                           @RequestParam(value = "sortField", defaultValue = "hot") String sortField,
                           @RequestParam(value = "sortOrder", defaultValue = "desc") String sortOrder) {
        pageNum = Math.max(1, pageNum);
        pageSize = Math.max(1, pageSize);
        if (!sortField.equals("created_at") && !sortField.equals("hot") && !sortField.equals("price") && !sortField.equals("price_old") && !sortField.equals("average_rating") && !sortField.equals("ratings_count") && !sortField.equals("comment_count") && !sortField.equals("original_publication_date") && !sortField.equals("publication_date") && !sortField.equals("num_pages")) {
            sortField = "hot";
        }
        if (!sortOrder.equals("asc") && !sortOrder.equals("desc")) {
            sortOrder = "desc";
        }
        Page<Book> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(categoryId != null, "category_id", categoryId)
                .eq(authorId != null, "author_id", authorId)
                .like(title != null, "title", title)
                .like(isbn != null, "isbn", isbn)
                .like(language != null, "language", language)
                .like(format != null, "format", format)
                .like(publisher != null, "publisher", publisher)
                .orderByAsc(sortOrder.equals("asc"), sortField)
                .orderByDesc(sortOrder.equals("desc"), sortField);
        Page<Book> bookPage = bookService.page(page, queryWrapper);
        IPage<BooksDTO> convert = bookPage.convert(book -> {
            BooksDTO bookDTO = modelMapper.map(book, BooksDTO.class);
            bookDTO.setCategory(categoryService.getById(book.getCategoryId()));
            bookDTO.setAuthor(modelMapper.map(authorService.getById(book.getAuthorId()), AuthorDTO.class));
            return bookDTO;
        });
        return Result.ok("查询成功").setData(convert);
    }

    @SaCheckRole("admin")
    @GetMapping("/list")
    public Result getBookList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                              @RequestParam(value = "categoryId", required = false) Long categoryId,
                              @RequestParam(value = "authorId", required = false) Long authorId,
                              @RequestParam(value = "title", required = false) String title,
                              @RequestParam(value = "isbn", required = false) String isbn,
                              @RequestParam(value = "language", required = false) String language,
                              @RequestParam(value = "format", required = false) String format,
                              @RequestParam(value = "publisher", required = false) String publisher,
                              @RequestParam(value = "sortField", defaultValue = "hot") String sortField,
                              @RequestParam(value = "sortOrder", defaultValue = "desc") String sortOrder) {
        pageNum = Math.max(1, pageNum);
        pageSize = Math.max(1, pageSize);
        if (!sortField.equals("created_at") && !sortField.equals("hot") && !sortField.equals("price") && !sortField.equals("price_old") && !sortField.equals("average_rating") && !sortField.equals("ratings_count") && !sortField.equals("comment_count") && !sortField.equals("original_publication_date") && !sortField.equals("publication_date") && !sortField.equals("num_pages")) {
            sortField = "hot";
        }
        if (!sortOrder.equals("asc") && !sortOrder.equals("desc")) {
            sortOrder = "desc";
        }
        Page<Book> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(categoryId != null, "category_id", categoryId)
                .eq(authorId != null, "author_id", authorId)
                .like(title != null, "title", title)
                .like(isbn != null, "isbn", isbn)
                .like(language != null, "language", language)
                .like(format != null, "format", format)
                .like(publisher != null, "publisher", publisher)
                .orderByAsc(sortOrder.equals("asc"), sortField)
                .orderByDesc(sortOrder.equals("desc"), sortField);
        return Result.ok("查询成功")
                .setData(bookService.page(page, queryWrapper));
    }

    @SaCheckRole("admin")
    @PostMapping
    public Result addBook(@RequestBody Book book) {
        if (bookService.save(book)) {
            return Result.ok("添加成功");
        }
        return Result.error("添加失败");
    }


    @SaCheckRole("admin")
    @PutMapping
    public Result updateBook(@RequestBody Book book) {
        if (bookService.updateById(book)) {
            return Result.ok("更新成功");
        }
        return Result.error("更新失败");
    }

    @SaCheckRole("admin")
    @DeleteMapping("/{id}")
    public Result deleteBook(@PathVariable Long id) {
        if (bookService.removeById(id)) {
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }


}
