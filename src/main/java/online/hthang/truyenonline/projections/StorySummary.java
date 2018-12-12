package online.hthang.truyenonline.projections;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * @author Huy Thang on 20/11/2018
 * @project truyenonline
 */
public interface StorySummary {

    Long getsID();

    String getvnName();

    String getcnName();

    String getcnLink();

    String getsImages();

    String getsAuthor();


    String getsInfo();

    Float getsRating();

    @Value("#{target.sConverter.uID}")
    Long getuID();

    @Value("#{@myComponent.getChapterHead(target.sID)}")
    ChapterSummary getchHead();

    @Value("#{@myComponent.getNewChapter(target.sID)}")
    ChapterSummary getchNew();

    List<CatogorySummary> getCategoryList();

    Integer getsStatus();
}
