package me.ooify.boogai.dto.category;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO{
    private Long bookCategoryId;
    private String name;
    private String mainCategory;
    private Integer hot;
    private Date createdAt;
    private Date updatedAt;
}
