package me.ooify.boogai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.Date;

import lombok.*;

/**
 * <p>
 * 图书表
 * </p>
 *
 * @author jonsan
 * @since 2023-12-07 07:11:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String title;

    private Long authorId;

    private String imgPath;

    private Double price;

    private Double priceOld;

    private Integer quantity;

    private Long categoryId;

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

    private Date createdAt;

    private Date updatedAt;
}
