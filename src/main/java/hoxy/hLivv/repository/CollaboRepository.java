package hoxy.hLivv.repository;

import hoxy.hLivv.entity.Category;
import hoxy.hLivv.entity.Collabo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollaboRepository extends JpaRepository<Collabo, Long> {
    public List<Collabo> getCollabosByCategory(Category category, Pageable pageable);
}
