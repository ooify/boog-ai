package me.ooify.boogai.mapper;

import me.ooify.boogai.dto.cart.CartItemsDTO;
import me.ooify.boogai.entity.CartItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Result;
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
public interface CartItemMapper extends BaseMapper<CartItem> {
    @Select("SELECT \n" +
            "    ci.quantity AS cart_quantity, \n" +
            "    b.quantity AS book_quantity, \n" +
            "    ci.*, \n" +
            "    b.*, \n" +
            "    CASE WHEN b.quantity - ci.quantity >= 0 THEN 0 ELSE 1 END AS isStockout\n" +
            "FROM \n" +
            "    cart_item ci \n" +
            "LEFT JOIN \n" +
            "    book b ON ci.book_id = b.id \n" +
            "WHERE \n" +
            "    ci.cart_id =#{cartId}")
    @Result(column = "cart_item_quantity", property = "quantity")
    @Result(column = "isStockout", property = "isStockout")
    @Result(column = "book_id", property = "book.id")
    @Result(column = "title", property = "book.title")
    @Result(column = "img_path", property = "book.imgPath")
    @Result(column = "publisher", property = "book.publisher")
    @Result(column = "format", property = "book.format")
    @Result(column = "language", property = "book.language")
    @Result(column = "isbn", property = "book.isbn")
    @Result(column = "price", property = "book.price")
    @Result(column = "price_old", property = "book.priceOld")
    @Result(column = "author_id", property = "book.authorId")
    @Result(column = "category_id", property = "book.categoryId")
    List<CartItemsDTO> findCartItemsByCartId(Long cartId);

}
