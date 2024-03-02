package hoxy.hLivv;

import hoxy.hLivv.dto.CategoryDto;
import hoxy.hLivv.dto.product.CollaboDto;
import hoxy.hLivv.dto.product.ProductCollaboDto;
import hoxy.hLivv.service.CollaboService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class CollaboProductTest {

    @Autowired
    private CollaboService collaboService;

    @Test
    public void testCreateCollaboProduct() {
        // given
        ArrayList<ProductCollaboDto> dtos = new ArrayList<>();
        dtos.add(new ProductCollaboDto(23L, 1));
        dtos.add(new ProductCollaboDto(15L, 1));

        var collaboDto = CollaboDto.builder()
                                   .name("testCollaboProduct")
                                   .productDesc("test")
                                   .category(CategoryDto.builder()
                                                        .id("C200000061")
                                                        .build())
                                   .price(1000)
                                   .productImages(new ArrayList<>())
                                   .startDate(new Date())
                                   .endDate(new Date())
                                   .stockQuantity(100)
                                   .collaboProduct(dtos)
                                   .discountPercent(10)
                                   .isArSupported(true)
                                   .isQrSupported(true)
                                   .isRestore(true)
                                   .isEco(true)
                                   .productBrand("test")
                                   .build();

        // when
        var savedProduct = collaboService.saveCollaboProduct(collaboDto);

        // then
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo(collaboDto.getName());
        assertThat(savedProduct.getProductDesc()).isEqualTo(collaboDto.getProductDesc());
        assertThat(savedProduct.getPrice()).isEqualTo(collaboDto.getPrice());
        assertThat(savedProduct.getStartDate()).isEqualTo(collaboDto.getStartDate());
        assertThat(savedProduct.getEndDate()).isEqualTo(collaboDto.getEndDate());
        assertThat(savedProduct.getStockQuantity()).isEqualTo(collaboDto.getStockQuantity());
        assertThat(savedProduct.getDiscountPercent()).isEqualTo(collaboDto.getDiscountPercent());
        assertThat(savedProduct.isArSupported()).isEqualTo(collaboDto.isArSupported());
        assertThat(savedProduct.isQrSupported()).isEqualTo(collaboDto.isQrSupported());
        assertThat(savedProduct.isRestore()).isEqualTo(collaboDto.isRestore());
        assertThat(savedProduct.isEco()).isEqualTo(collaboDto.isEco());
        assertThat(savedProduct.getProductBrand()).isEqualTo(collaboDto.getProductBrand());
    }

}


