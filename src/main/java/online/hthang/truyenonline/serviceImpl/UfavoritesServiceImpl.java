package online.hthang.truyenonline.serviceImpl;

import online.hthang.truyenonline.entity.Chapter;
import online.hthang.truyenonline.entity.Ufavorites;
import online.hthang.truyenonline.entity.User;
import online.hthang.truyenonline.repository.UfavoritesRepository;
import online.hthang.truyenonline.service.UfavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Huy Thang
 */

@Service
public class UfavoritesServiceImpl implements UfavoritesService {

    @Autowired
    private UfavoritesRepository ufavoritesRepository;

    /**
     * Kiểm tra tồn tại Ufavorites trong khoảng
     *
     * @param chID
     * @param uID
     * @param startDate
     * @param endDate
     * @return boolean
     */
    @Override
    public boolean checkChapterAndUserInTime(Long chID, Long uID, Date startDate, Date endDate) {
        return ufavoritesRepository
                .existsUfavoritesByChapter_ChIDAndUser_uIDAndDateViewBetween(chID, uID, startDate, endDate);
    }

    /**
     * Kiểm tra tồn tại Ufavorites trong khoảng
     *
     * @param chID
     * @param locationIP
     * @param startDate
     * @param endDate
     * @return boolean
     */
    @Override
    public boolean checkChapterAndLocationIPInTime(Long chID, String locationIP, Date startDate, Date endDate) {
        return ufavoritesRepository
                .existsUfavoritesByChapter_ChIDAndLocationIPAndDateViewBetween(chID, locationIP, startDate, endDate);
    }

    /**
     * Lưu Lịch Sử đọc truyện
     *
     * @param chapter
     * @param user
     * @param LocationIP
     * @param uView
     * @return void
     */
    @Override
    public void saveUfavorite(Chapter chapter, User user, String LocationIP, Integer uView) {
        Ufavorites ufavorites = new Ufavorites();
        ufavorites.setUfView(uView);
        ufavorites.setUser(user);
        ufavorites.setChapter(chapter);
        ufavorites.setLocationIP(LocationIP);
        ufavoritesRepository.save(ufavorites);
        System.out.println("Save UFavorite");
    }


}
