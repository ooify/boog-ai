package me.ooify.boogai.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ooify.boogai.dto.user.CommentUserDTO;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentsDTO {
    private Long id;
    private String bookId;
    private CommentUserDTO user;
    private String content;
    private Integer like;
    private Date createdAt;
}
