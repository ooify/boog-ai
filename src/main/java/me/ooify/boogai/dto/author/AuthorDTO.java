package me.ooify.boogai.dto.author;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO{
    private Long authorId;
    private String authorName;
    private Date createdAt;
    private Date updatedAt;
}
