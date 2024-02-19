package hoxy.hLivv.service;

import hoxy.hLivv.dto.CategoryDto;
import hoxy.hLivv.dto.product.ProductDto;
import hoxy.hLivv.entity.Category;
import hoxy.hLivv.repository.CategoryRepository;
import hoxy.hLivv.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryDto addCategory(CategoryDto categoryDto) {
        var saved = categoryRepository.save(new Category(categoryDto.getId(), categoryDto.getName()));
        return CategoryDto.from(saved);
    }

    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll()
                                 .stream()
                                 .map(CategoryDto::from)
                                 .toList();
    }

    public List<ProductDto> getProductsByCategory(String categoryId, int pageNo, int pageSize) throws Exception {
        pageSize = Math.min(pageSize, 100);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Category category = categoryRepository.findById(categoryId)
                                              .orElseThrow(Exception::new);
        return productRepository.getProductsByCategory(category, pageable)
                                .stream()
                                .map(ProductDto::from)
                                .toList();
    }
}