package me.ooify.boogai.mapper;

import me.ooify.boogai.dto.order.OrderItemDTO;
import me.ooify.boogai.entity.OrderItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author jonsan
 * @since 2023-12-07 07:11:14
 */
public interface OrderItemMapper extends BaseMapper<OrderItem> {
    @Select("SELECT oi.*, b.* FROM order_item oi LEFT JOIN book b ON oi.book_id = b.id WHERE oi.order_id = #{orderId}")
    @Results({
            @Result(column = "quantity", property = "quantity"),
            @Result(column = "price", property = "price"),
            @Result(column = "book_id", property = "book.id"),
            @Result(column = "title", property = "book.title"),
            @Result(column = "img_path", property = "book.imgPath"),
            @Result(column = "publisher", property = "book.publisher"),
            @Result(column = "format", property = "book.format"),
            @Result(column = "language", property = "book.language"),
            @Result(column = "isbn", property = "book.isbn"),
            @Result(column = "author_id", property = "book.authorId"),
            @Result(column = "category_id", property = "book.categoryId")
    })
    List<OrderItemDTO> findOrderItemsByOrderId(Long orderId);

}
