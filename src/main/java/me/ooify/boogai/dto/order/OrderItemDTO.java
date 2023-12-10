package me.ooify.boogai.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ooify.boogai.dto.book.OrderItemBookDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private OrderItemBookDTO book;
    private Integer quantity;
    private Double price;
}
