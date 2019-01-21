package online.hthang.truyenonline.repository;

import online.hthang.truyenonline.entity.Category;
import online.hthang.truyenonline.projections.CategorySummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Huy Thang
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findByStatus(Integer status);

    List< CategorySummary > findAllByStatus(Integer status);

    Optional<Category> findByIdAndStatus(Integer id, Integer status);

}
