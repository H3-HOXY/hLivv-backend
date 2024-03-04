package hoxy.hLivv.dto.product;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPagingDTO {
    @Min(1)
    @NotNull
    private Integer pageNo = 1;

    @Min(10)
    @Max(20)
    @NotNull
    private Integer pageSize = 20;

    private ProductSortCriteria sortCriteria = ProductSortCriteria.NEWEST;
}
