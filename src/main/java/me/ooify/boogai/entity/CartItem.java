package me.ooify.boogai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author jonsan
 * @since 2023-12-07 07:11:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("cart_item")
public class CartItem {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long cartId;

    private Long bookId;

    private Integer quantity;
}
