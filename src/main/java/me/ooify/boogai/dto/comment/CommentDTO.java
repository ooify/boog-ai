package me.ooify.boogai.dto.comment;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    @Digits(integer = 19, fraction = 0, message = "id不合法")
    @NotNull(message = "id不能为空")
    private String bookId;
    @Min(value = 0, message = "评分不能小于0")
    @Max(value = 5, message = "评分不能大于5")
    @NotNull(message = "评分不能为空")
    private Double star;
    @NotNull(message = "内容不能为空")
    @Size(min = 1, max = 150, message = "内容长度必须在1到150之间")
    private String content;
}
