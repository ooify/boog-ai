package me.ooify.boogai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

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
@TableName(value = "`order`")
public class Order {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long addressId;

    private Double totalAmount;

    private Integer status;

    private Date createdAt;
}
