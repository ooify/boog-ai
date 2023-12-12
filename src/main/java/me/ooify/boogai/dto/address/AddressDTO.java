package me.ooify.boogai.dto.address;

import jakarta.validation.constraints.Digits;
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
public class AddressDTO {
    @NotNull(message = "state不为空")
    @Size(min = 1, max = 20, message = "state不合法")
    private String state;
    @NotNull(message = "city不为空")
    @Size(min = 1, max = 20, message = "city不合法")
    private String city;
    @NotNull(message = "county不为空")
    @Size(min = 1, max = 20, message = "county不合法")
    private String county;
    @NotNull(message = "street不为空")
    @Size(min = 1, max = 50, message = "street不合法")
    private String street;
    @NotNull(message = "recipientName不为空")
    @Size(min = 1, max = 100, message = "recipientName不合法")
    private String recipientName;
    @NotNull(message = "phonenumber不为空")
    @Pattern(regexp = "^1\\d{10}$", message = "手机号格式不正确")
    private String phonenumber;
    @NotNull(message = "isDefault不为空")
    @Range(min = 0, max = 1, message = "isDefault不合法")
    private Integer isDefault;
}
