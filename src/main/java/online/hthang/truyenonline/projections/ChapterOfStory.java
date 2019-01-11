package online.hthang.truyenonline.projections;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * @author Huy Thang on 04/12/2018
 * @project truyenonline
 */
public interface ChapterOfStory {

    Long getId();

    Integer getChapterNumber();

    String getName();

    @Value("#{target.story.id}")
    Long getStoryId();

    @Value("#{target.user.name}")
    String getUserName();

    @Value("#{target.user.displayName}")
    String getUserDisplayName();

    Date getCreateDate();

    @Value("#{@myComponent.getBetewwen(target.createDate)}")
    String getTimeUpdate();

    Integer getStatus();

}
