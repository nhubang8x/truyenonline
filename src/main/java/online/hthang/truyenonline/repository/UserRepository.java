package online.hthang.truyenonline.repository;

import online.hthang.truyenonline.entity.User;
import online.hthang.truyenonline.projections.ConveterSummary;
import online.hthang.truyenonline.projections.TopConverter;
import online.hthang.truyenonline.utils.ConstantsQueryUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Huy Thang
 */
@Repository
public interface UserRepository extends JpaRepository< User, Long > {

    User findByUsername(String username);

    @Query(value = ConstantsQueryUtils.TOP_CONVERTER,
            countQuery = ConstantsQueryUtils.COUNT_TOP_CONVERTER,
            nativeQuery = true)
    Page< TopConverter > getTopConverter(@Param("chapterStatus") List< Integer > listChapterStatus,
                                         @Param("storyStatus") List< Integer > listStoryStatus,
                                         @Param("userStatus") Integer uStatus, Pageable pageable);

    boolean existsUserByEmail(String uEmail);

    boolean existsUserByUsername(String uName);

    User findByUsernameAndEmail(String uName, String uEmail);

    /**
     * Kiểm Tra User Có tồn tại không
     *
     * @param uID
     * @return true - nếu tồn tại user/ false - nếu không tồn tại user
     */
    boolean existsById(Long uID);

    /**
     * Kiểm Tra Có tồn tại DName với điều kiện Khác uID không
     *
     * @param uID
     * @param uDName
     * @return true - nếu tồn tại user/ false - nếu không tồn tại user
     */
    boolean existsByIdNotAndDisplayName(Long uID, String uDName);

    /**
     * Lấy Thông Tin Converter
     *
     * @param uID
     * @return Converter
     */
    ConveterSummary findUserById(Long uID);
}
