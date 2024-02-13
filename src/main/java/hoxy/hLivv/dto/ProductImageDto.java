package hoxy.hLivv.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductImageDto {
    private String imageUrl;

    public ProductImageDto(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
