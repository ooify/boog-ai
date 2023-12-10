package me.ooify.boogai.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ooify.boogai.dto.address.AddressDTO;

import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDTO {
    private List<OrderItemDTO> orderItems;
    private AddressDTO address;
    private Double totalAmount;
    private Integer status;
    private Date createdAt;
}
