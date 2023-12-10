package me.ooify.boogai.service.impl;

import me.ooify.boogai.entity.Book;
import me.ooify.boogai.mapper.BookMapper;
import me.ooify.boogai.service.BookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 图书表 服务实现类
 * </p>
 *
 * @author jonsan
 * @since 2023-12-07 07:11:14
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

}
