package online.hthang.truyenonline.repository;

import online.hthang.truyenonline.entity.Pay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author Huy Thang
 */

@Repository
public interface PayRepository extends JpaRepository<Pay, Long> {

    /**
     * Kiểm Tra đã tồn tại thanh toán trong khoảng
     *
     * @param chID
     * @param uID
     * @param startDate
     * @param endDate
     * @param payStatus
     * @return true - nếu tồn tại/false - nếu không tồn tại
     */
    boolean existsByChapter_IdAndUserSendAndCreateDateBetweenAndTypeAndStatus(Long chID,
                                                                              Long uID,
                                                                              Date startDate,
                                                                              Date endDate,
                                                                              Integer type,
                                                                              Integer payStatus);

    /**
     * Thực Hiện Thanh Toán Chapter Vip
     *
     * @param payerID
     * @param receiverID
     * @param chapterID
     * @param price
     * @param payType
     * @return true - nếu thanh toán thành công / false - nếu thanh toán thất bại và roll back dữ liệu
     */
    @Procedure("payChapter")
    boolean transferPayChapter(@Param("userSend") Long payerID,
                               @Param("userReceived") Long receiverID,
                               @Param("chapterID") Long chapterID,
                               @Param("storyID") Long storyID,
                               @Param("price") Double price,
                               @Param("payType") Integer payType);

    Long countByStory_IdOrChapter_Story_Id(Long id, Long storyId);

    Page<Pay> findByUserSendOrUserReceivedAndTypeInOrderByCreateDateDesc(Long userSend, Long userReceived, List<Integer> integerList, Pageable pageable);
}
