package me.ooify.boogai.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ooify.boogai.dto.book.CartItemBookDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemsDTO {
    private CartItemBookDTO book;
    private Boolean isStockout;
    private Integer quantity;
}
