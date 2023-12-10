package me.ooify.boogai.dto.cart;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    @NotNull(message = "bookId不能为空")
    @Digits(integer = 19, fraction = 0, message = "id不合法")
    private Long bookId;
    @NotNull(message = "quantity不能为空")
    @Digits(integer = 19, fraction = 0, message = "数量不合法")
    private Integer quantity;

}
