package me.ooify.boogai.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentUserDTO {
    private String username;
    private String nickName;
    private String avatar;
}
