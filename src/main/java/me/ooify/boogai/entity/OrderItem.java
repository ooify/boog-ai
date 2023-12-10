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
@TableName("order_item")
public class OrderItem {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private Long bookId;

    private Integer quantity;

    private Double price;
}
