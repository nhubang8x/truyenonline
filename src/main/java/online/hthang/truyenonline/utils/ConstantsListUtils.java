package online.hthang.truyenonline.utils;

import online.hthang.truyenonline.entity.Status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Huy Thang on 16/11/2018
 * @project truyenonline
 */
public class ConstantsListUtils {

    public static final List< Status > LIST_STORY_STATUS_CONVERTER = Collections.unmodifiableList(
            new ArrayList< Status >() {{
                add(new Status(ConstantsUtils.STORY_STATUS_GOING_ON, "Đang ra"));
                add(new Status(ConstantsUtils.STORY_STATUS_COMPLETED, "Hoàn Thành"));
                add(new Status(ConstantsUtils.STORY_STATUS_STOP, "Tạm Dừng"));
            }}
    );
    public static final List<Integer> LIST_STORY_DISPLAY = Collections.unmodifiableList(
            new ArrayList<Integer>() {{
                add(ConstantsUtils.STORY_STATUS_COMPLETED);
                add(ConstantsUtils.STORY_STATUS_GOING_ON);
                add(ConstantsUtils.STORY_STATUS_STOP);
            }}
    );

    public static final List<Integer> LIST_CHAPTER_DISPLAY = Collections.unmodifiableList(
            new ArrayList<Integer>() {{
                add(ConstantsUtils.CHAPTER_ACTIVED);
                add(ConstantsUtils.CHAPTER_VIP_ACTIVED);
            }}
    );
}
