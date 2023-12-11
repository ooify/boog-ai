package me.ooify.boogai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
@TableName("comment_like")
public class CommentLike {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long commentId;

    private Long userId;

    private Date createdAt;
}
