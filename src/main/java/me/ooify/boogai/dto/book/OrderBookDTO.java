package me.ooify.boogai.dto.book;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderBookDTO {
    @Digits(integer = 19, fraction = 0, message = "id不合法")
    @NotNull(message = "bookId不能为空")
    private Long bookId;
    @Min(message = "数值不合法", value = 1)
    @NotNull(message = "quantity不能为空")
    private Integer quantity;

}
