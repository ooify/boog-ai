package me.ooify.boogai.dto.author;


import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO {
    @NotNull(message = "id不能为空")
    @Digits(integer = 19, fraction = 0, message = "id不合法")
    private Long id;
    @NotNull(message = "name不能为空")
    @Size(min = 1, max = 100, message = "name长度不合法")
    private String name;
}
