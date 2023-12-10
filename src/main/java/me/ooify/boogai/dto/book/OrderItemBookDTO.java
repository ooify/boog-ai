package me.ooify.boogai.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemBookDTO {
    private Long id;
    private String title;
    private String imgPath;
    private String publisher;
    private String format;
    private String language;
    private String isbn;
    private Long authorId;
    private Long categoryId;
}
