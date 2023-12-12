package me.ooify.boogai.dto.address;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddresssDTO {
    private Long id;
    private String state;
    private String city;
    private String county;
    private String street;
    private String recipientName;
    private String phonenumber;
    private Integer isDefault;
}
