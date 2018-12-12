package online.hthang.truyenonline.repository;

import online.hthang.truyenonline.entity.Srating;
import online.hthang.truyenonline.entity.SratingPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

/**
 * @author Huy Thang
 */
@Repository
public interface SratingRepository extends JpaRepository< Srating, SratingPK > {


    /**
     * Kiểm tra Tồn Tại Srating theo
     *
     * @param sID
     * @param locationIP
     * @param startDate
     * @param endDate
     * @return
     */
    Optional<Srating> findByStory_sIDAndLocationIPAndCreateDateBetween(Long sID,
                                                              String locationIP,
                                                              Date startDate,
                                                              Date endDate);

    /**
     * Kiểm tra Tồn Tại Srating theo
     *
     * @param sID
     * @param uID
     * @return "true" nếu đã có bình chọn, "false: nếu chưa có bình chọn phù hợp
     */
    boolean existsSratingByStory_sIDAndUser_uID(Long sID, Long uID);

    /**
     * Đếm số đánh giá
     *
     * @param sID
     * @return "true" nếu đã có bình chọn, "false: nếu chưa có bình chọn phù hợp
     */
    Long countByStory_sID(Long sID);

    /**
     * Thực Hiện Đánh giá
     *
     * @param userID
     * @param storyID
     * @param myLocationIP
     * @param myRating
     * @return Float
     */
    @Procedure("saveRating")
    Float saveRating(@Param("userID") Long userID,
                       @Param("storyID") Long storyID,
                       @Param("myLocationIP") String myLocationIP,
                       @Param("myRating") Integer myRating);
}
