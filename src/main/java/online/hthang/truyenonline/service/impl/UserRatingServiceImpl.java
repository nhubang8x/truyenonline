package online.hthang.truyenonline.service.impl;

import online.hthang.truyenonline.entity.UserRating;
import online.hthang.truyenonline.repository.UserRatingRepository;
import online.hthang.truyenonline.service.UserRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * @author Huy Thang
 */
@Service
public class UserRatingServiceImpl implements UserRatingService {

    private final UserRatingRepository userRatingRepository;

    @Autowired
    public UserRatingServiceImpl(UserRatingRepository userRatingRepository) {
        this.userRatingRepository = userRatingRepository;
    }

    /**
     * Kiểm tra Tồn Tại UserRating theo
     *
     * @param uID
     * @param sID
     * @return "true" nếu đã có bình chọn, "false" nếu chưa có bình chọn phù hợp
     */
    @Override
    public boolean checkRatingWithUser(Long sID, Long uID) {
        return userRatingRepository
                .existsSratingByStory_IdAndUser_Id(sID, uID);
    }

    /**
     * Kiểm tra Tồn Tại UserRating theo
     *
     * @param sID
     * @param locationIP
     * @param startDate
     * @param endDate
     * @return Optional<UserRating>
     */
    @Override
    public Optional< UserRating > checkRatingWithLocationIP(Long sID, String locationIP, Date startDate, Date endDate) {
        return userRatingRepository
                .findByStory_IdAndLocationIPAndCreateDateBetween(sID, locationIP, startDate, endDate);
    }

    /**
     * Đếm số lượng đánh giá của Truyện
     *
     * @param sID
     * @return Long
     */
    @Override
    public Long getSumRaitingOfStory(Long sID) {
        return userRatingRepository
                .countByStory_Id(sID);
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
        return userRatingRepository
                .saveRating(uID,sID,locationIP,rating);
    }
}
