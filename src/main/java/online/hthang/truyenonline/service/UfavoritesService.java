package online.hthang.truyenonline.service;

import online.hthang.truyenonline.entity.Chapter;
import online.hthang.truyenonline.entity.User;

import java.util.Date;

/**
 * @author Huy Thang
 */
public interface UfavoritesService {

    /**
     * Kiểm tra tồn tại Ufavorites trong khoảng
     *
     * @param chID
     * @param uID
     * @param startDate
     * @param endDate
     *
     * @return boolean
     */
    boolean checkChapterAndUserInTime(Long chID, Long uID, Date startDate, Date endDate);

    /**
     * Kiểm tra tồn tại Ufavorites trong khoảng
     *
     * @param chID
     * @param locationIP
     * @param startDate
     * @param endDate
     *
     * @return boolean
     */
    boolean checkChapterAndLocationIPInTime(Long chID, String locationIP, Date startDate, Date endDate);
    /**
     * Lưu Lịch Sử đọc truyện
     *
     * @param chapter
     * @param user
     * @param LocationIP
     * @param uView
     *
     * @return void
     */
    void saveUfavorite(Chapter chapter, User user, String LocationIP, Integer uView);
}
