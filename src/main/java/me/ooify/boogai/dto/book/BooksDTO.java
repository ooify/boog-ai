package me.ooify.boogai.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ooify.boogai.dto.author.AuthorDTO;
import me.ooify.boogai.entity.Category;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BooksDTO {
    private Long id;
    private String title;
    private String imgPath;
    private Double price;
    private Double priceOld;
    private String isbn;
    private String language;
    private Double averageRating;
    private String ratingDist;
    private Integer ratingsCount;
    private Integer commentCount;
    private Date originalPublicationDate;
    private Date publicationDate;
    private String format;
    private String publisher;
    private Integer numPages;
    private String description;
    private Integer hot;
    private AuthorDTO author;
    private Category category;
    private Date createdAt;
    private Date updatedAt;
}
