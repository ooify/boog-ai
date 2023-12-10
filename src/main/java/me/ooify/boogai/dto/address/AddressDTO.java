package me.ooify.boogai.dto.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private String state;
    private String city;
    private String county;
    private String street;
    private String recipientName;
    private String phonenumber;
}
