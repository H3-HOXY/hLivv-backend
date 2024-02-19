package hoxy.hLivv.dto;

import hoxy.hLivv.entity.Category;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDto {
    private String id;
    private String name;

    public Category toEntity() {
        return Category.builder()
                       .id(id)
                       .name(name)
                       .build();
    }

    public static CategoryDto from(Category category) {
        return CategoryDto.builder()
                          .id(category.getId())
                          .name(category.getName())
                          .build();
    }
}
