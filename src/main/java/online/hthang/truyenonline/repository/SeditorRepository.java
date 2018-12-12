package online.hthang.truyenonline.repository;

import online.hthang.truyenonline.entity.Seditor;
import online.hthang.truyenonline.entity.SeditorPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Huy Thang
 */
@Repository
public interface SeditorRepository extends JpaRepository<Seditor, SeditorPK> {

}
