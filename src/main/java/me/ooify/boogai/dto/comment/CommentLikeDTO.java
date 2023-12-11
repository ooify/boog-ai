package me.ooify.boogai.dto.comment;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentLikeDTO {
    @Digits(integer = 19, fraction = 0, message = "id不合法")
    @NotNull(message = "id不能为空")
    private Long commentId;
    @Digits(integer = 19, fraction = 0, message = "id不合法")
    @NotNull(message = "id不能为空")
    private Long userId;
    @NotNull(message = "点赞状态不能为空")
    @Range(min = 0, max = 1, message = "点赞状态不合法")
    private Integer isLike;

}
