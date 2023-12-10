package me.ooify.boogai.dto.order;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ooify.boogai.dto.address.AddressDTO;
import me.ooify.boogai.dto.book.OrderBookDTO;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class OrderDTO {
    @NotNull(message = "不能为空")
    private List<OrderBookDTO> books;
    @Digits(integer = 19, fraction = 0, message = "id不合法")
    @NotNull(message = "addressId不能为空")
    private Long addressId;
}
