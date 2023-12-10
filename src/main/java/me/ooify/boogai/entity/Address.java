package me.ooify.boogai.entity;

import lombok.*;

/**
 * <p>
 *
 * </p>
 *
 * @author jonsan
 * @since 2023-12-07 07:11:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private Long id;

    private Long userId;

    private String recipientName;

    private String street;

    private String county;

    private String city;

    private String state;

    private String phonenumber;

    private Integer isDefault;
}
