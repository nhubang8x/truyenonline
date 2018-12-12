package online.hthang.truyenonline.serviceImpl;

import online.hthang.truyenonline.entity.Srating;
import online.hthang.truyenonline.repository.SratingRepository;
import online.hthang.truyenonline.service.SratingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * @author Huy Thang
 */
@Service
public class SratingServiceImpl implements SratingService {

    @Autowired
    private SratingRepository sratingRepository;

    /**
     * Kiểm tra Tồn Tại Srating theo
     *
     * @param uID
     * @param sID
     * @return "true" nếu đã có bình chọn, "false" nếu chưa có bình chọn phù hợp
     */
    @Override
    public boolean checkRatingWithUser(Long sID, Long uID) {
        return sratingRepository
                .existsSratingByStory_sIDAndUser_uID(sID, uID);
    }

    /**
     * Kiểm tra Tồn Tại Srating theo
     *
     * @param sID
     * @param locationIP
     * @param startDate
     * @param endDate
     * @return Optional<Srating>
     */
    @Override
    public Optional< Srating > checkRatingWithLocationIP(Long sID, String locationIP, Date startDate, Date endDate) {
        return sratingRepository
                .findByStory_sIDAndLocationIPAndCreateDateBetween(sID, locationIP, startDate, endDate);
    }

    /**
     * Đếm số lượng đánh giá của Truyện
     *
     * @param sID
     * @return Long
     */
    @Override
    public Long getSumRaitingOfStory(Long sID) {
        return sratingRepository
                .countByStory_sID(sID);
    }

    /**
     * Thực Hiện Đánh giá
     *
     * @param uID
     * @param sID
     * @param locationIP
     * @param rating
     * @return true / false
     */
    @Override
    public Float saveRating(Long uID, Long sID, String locationIP, Integer rating) {
        return sratingRepository
                .saveRating(uID,sID,locationIP,rating);
    }
}
