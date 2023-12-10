package me.ooify.boogai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

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
public class Category {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String main;

    private Integer hot;

    private Date createdAt;

    private Date updatedAt;
}
