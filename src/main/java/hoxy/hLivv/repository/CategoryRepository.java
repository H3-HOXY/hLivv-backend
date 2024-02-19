package hoxy.hLivv.repository;

import hoxy.hLivv.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
