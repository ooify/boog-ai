package me.ooify.boogai.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
    private String username;
    private String nickName;
    private Integer sex;
    private String email;
    private String phonenumber;
    private String avatar;
    private Date createdAt;
    private Date updatedAt;
}
