package me.ooify.boogai.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    //要求不为空，格式为字母数字下划线至少6位最多20位
    @NotNull(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]{6,20}$", message = "用户名格式不正确，应为长度6-20位的字母数字、或下划线")
    private String username;
    //要求不为空，至少6位最多20位
    @NotNull(message = "密码不能为空")
    @Pattern(regexp = "^.{6,20}$", message = "密码格式不正确，应为长度6-20位的任意字符")
    private String password;
    private String nickName;
    private Integer sex;
    @NotNull(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    //要求不为空，格式为1开头的11位数字
    @NotNull(message = "手机号不能为空")
    @Pattern(regexp = "^1\\d{10}$", message = "手机号格式不正确")
    private String phonenumber;

}
