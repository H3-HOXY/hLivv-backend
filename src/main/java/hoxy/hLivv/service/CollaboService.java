package hoxy.hLivv.service;

import hoxy.hLivv.dto.product.CollaboDto;
import hoxy.hLivv.dto.product.ProductImageDto;
import hoxy.hLivv.repository.CollaboRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CollaboService {

    private final CollaboRepository collaboRepository;
    private final ProductService productService;

    @Transactional
    public CollaboDto saveCollaboProduct(CollaboDto collaboDto) {

        // 콜라보 상품의 가격합계를 계산
        var price = collaboDto.getCollaboProduct()
                              .stream()
                              .mapToInt(productCollaboDto -> {
                                  var product = productService.getProductWith(productCollaboDto.getProductId());
                                  return product.getPrice() * productCollaboDto.getQuantity();
                              })
                              .sum();

        //할인률을 적용한 가격을 설정
        float percent = (collaboDto.getDiscountPercent() == 0 ? 1 : ((100.f - collaboDto.getDiscountPercent()) / 100));
        collaboDto.setPrice((int) (price * percent));

        var imageDtos = collaboDto.getCollaboProduct()
                                  .stream()
                                  .map(productCollaboDto -> productService.getProductWith(productCollaboDto.getProductId())
                                                                          .getProductImages())
                                  .flatMap(List::stream)
                                  .map(ProductImageDto::getImageUrl)
                                  .distinct()
                                  .map(ProductImageDto::new)
                                  .toList();

        collaboDto.setProductImages(imageDtos);

        var collabo = collaboRepository.save(collaboDto.toEntity());

        return CollaboDto.from(collabo);
    }

    public CollaboDto getCollaboProductWith(Long id) {
        return CollaboDto.from(collaboRepository.getReferenceById(id));
    }

    public List<CollaboDto> getAllCollaboProduct() {
        return collaboRepository.findAll()
                                .stream()
                                .map(CollaboDto::from)
                                .toList();
    }

    @Transactional
    public CollaboDto updateCollaboProduct(CollaboDto collaboDto) {
        return null;
    }

    @Transactional
    public void removeCollaboProduct() {

    }

}
