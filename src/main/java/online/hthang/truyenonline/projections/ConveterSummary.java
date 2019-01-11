package online.hthang.truyenonline.projections;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * @author Huy Thang on 23/12/2018
 * @project truyenonline
 */
public interface ConveterSummary {

    Long getId();

    String getUsername();

    String getDisplayName();

    String getAvatar();

    Double getGold();

    Date getCreateDate();

    @Value("#{@myComponent.countChapterOfUser(target.id)}")
    Long getCountChapter();

    @Value("#{@myComponent.countStoryOfUser(target.id)}")
    Long getCountStory();
}
