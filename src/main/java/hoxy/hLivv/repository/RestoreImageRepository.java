package hoxy.hLivv.repository;

import hoxy.hLivv.entity.RestoreImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface RestoreImageRepository extends JpaRepository<RestoreImage, Long> {
}