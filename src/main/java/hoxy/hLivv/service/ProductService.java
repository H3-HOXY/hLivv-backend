package hoxy.hLivv.service;


import hoxy.hLivv.dto.ProductDto;
import hoxy.hLivv.entity.Product;
import hoxy.hLivv.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // C
    @Transactional
    public ProductDto saveProduct(ProductDto productDto) {
        var product = productRepository.save(productDto.toEntity());
        return product.toDto();
    }


    // R
    public ProductDto getProductWith(Long id) {
        return productRepository.getReferenceById(id)
                                .toDto();
    }

    public List<ProductDto> getAllProduct() {
        return productRepository.findAll()
                                .stream()
                                .map(Product::toDto)
                                .toList();
    }


    // U
    @Transactional
    public ProductDto updateProduct(Long productId, ProductDto productDto) {
        return null;
    }

    // D
    @Transactional
    public void removeProduct() {

    }

}
