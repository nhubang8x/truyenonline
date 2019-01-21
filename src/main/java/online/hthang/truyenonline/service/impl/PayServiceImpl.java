package online.hthang.truyenonline.service.impl;

import online.hthang.truyenonline.entity.Chapter;
import online.hthang.truyenonline.entity.Pay;
import online.hthang.truyenonline.entity.Story;
import online.hthang.truyenonline.entity.User;
import online.hthang.truyenonline.repository.PayRepository;
import online.hthang.truyenonline.service.PayService;
import online.hthang.truyenonline.utils.ConstantsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Huy Thang
 */
@Service
public class PayServiceImpl implements PayService {

    private final PayRepository payRepository;

    @Autowired
    public PayServiceImpl(PayRepository payRepository) {
        this.payRepository = payRepository;
    }

    /**
     * Kiểm Tra đã tồn tại thanh toán trong khoảng
     *
     * @param chID
     * @param uID
     * @param startDate
     * @param endDate
     * @return true/false
     */
    @Override
    public boolean checkDealStoryVip(Long chID, Long uID, Date startDate, Date endDate) {
        return payRepository
                .existsByChapter_IdAndUserSendAndCreateDateBetweenAndTypeAndStatus(chID, uID,
                        startDate, endDate, ConstantsUtils.PAY_CHAPTER_VIP_TYPE, ConstantsUtils.PAY_STATUS);
    }

    @Override
    public boolean savePay(Story story, Chapter chapter, User userSend, User userReceived, Double money, Integer payType) {
        Long chapterID = null;
        Long storyID = null;
        if (chapter != null)
            chapterID = chapter.getId();
        if (story != null)
            storyID = story.getId();
        return payRepository
                .transferPayChapter(userSend.getId(),
                        userReceived.getId(),
                        chapterID,
                        storyID,
                        money,
                        payType);
    }

    @Override
    public Long countPay(Long id) {
        return payRepository.countByStory_IdOrChapter_Story_Id(id, id);
    }

    @Override
    public Page< Pay > getPagePayByUser(User user, List< Integer > listType, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return payRepository.findByUserSendOrUserReceivedAndTypeInOrderByCreateDateDesc(user.getId(), user.getId(), listType, pageable);
    }
}