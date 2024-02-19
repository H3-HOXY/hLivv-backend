package hoxy.hLivv.repository;

import hoxy.hLivv.entity.Category;
import hoxy.hLivv.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> getProductsByCategory(Category category, Pageable pageable);
}
