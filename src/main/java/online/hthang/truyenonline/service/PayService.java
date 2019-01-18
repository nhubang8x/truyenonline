package online.hthang.truyenonline.service;

import online.hthang.truyenonline.entity.Chapter;
import online.hthang.truyenonline.entity.Story;
import online.hthang.truyenonline.entity.User;

import java.util.Date;

/**
 * @author Huy Thang
 */
public interface PayService {

    /**
     * Kiểm Tra đã tồn tại thanh toán trong khoảng
     *
     * @param chID
     * @param uID
     * @param startDate
     * @param endDate
     * @return true/false
     */
    boolean checkDealStoryVip(Long chID, Long uID, Date startDate, Date endDate);

    boolean savePay(Story story, Chapter chapter, User userSend, User userReceived, Double money, Integer payType);


    Long countPay(Long id);
}
