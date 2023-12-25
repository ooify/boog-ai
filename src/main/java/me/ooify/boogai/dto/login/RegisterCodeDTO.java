package me.ooify.boogai.dto.login;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterCodeDTO {
    @Email(message = "邮箱格式不正确")
    private String email;
}
