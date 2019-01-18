package online.hthang.truyenonline.utils;

/**
 * @author Huy Thang
 */

public class ConstantsQueryUtils {

    public static final String STORY_NEW_UPDATE = "SELECT s.id, s.vnName, s.images, s.author, s.updateDate,"
            + " c.id as chapterId, c.chapterNumber,"
            + " u.displayName, u.username, s.dealStatus"
            + " FROM Story s LEFT JOIN (SELECT c.* FROM Chapter c INNER JOIN"
            + " (SELECT MAX(c.id) AS maxChapterID FROM Story s"
            + " LEFT JOIN Chapter c"
            + " ON s.id = c.storyId GROUP BY s.id) d"
            + " ON c.id = d.maxChapterID "
            + " WHERE c.status IN :chapterStatus) c "
            + " ON s.id = c.storyID "
            + " LEFT JOIN user u on c.userPosted = u.id"
            + " WHERE s.status IN :storyStatus"
            + " ORDER BY s.updateDate DESC";

    public static final String COUNT_STORY_NEW_UPDATE = "SELECT COUNT(*)"
            + " FROM Story s LEFT JOIN (SELECT c.* FROM Chapter c INNER JOIN"
            + " (SELECT MAX(c.id) AS maxChapterID FROM Story s"
            + " LEFT JOIN Chapter c"
            + " ON s.id = c.storyId GROUP BY s.id) d"
            + " ON c.id = d.maxChapterID "
            + " WHERE c.status IN :chapterStatus) c "
            + " ON s.id = c.storyID "
            + " LEFT JOIN user u on c.userPosted = u.id"
            + " WHERE s.status IN :storyStatus"
            + " ORDER BY s.updateDate DESC";

    public static final String STORY_NEW_UPDATE_BY_CATEGORY = "SELECT s.id, s.vnName, s.images, s.author, s.updateDate,"
            + " c.id as chapterId, c.chapterNumber,"
            + " u.displayName, u.username, s.dealStatus"
            + " FROM Story s LEFT JOIN (SELECT c.* FROM Chapter c INNER JOIN"
            + " (SELECT MAX(c.id) AS maxChapterID FROM Story s"
            + " LEFT JOIN Chapter c"
            + " ON s.id = c.storyId GROUP BY s.id) d"
            + " ON c.id = d.maxChapterID "
            + " WHERE c.status IN :chapterStatus) c "
            + " ON s.id = c.storyID "
            + " LEFT JOIN user u on c.userPosted = u.id"
            + " LEFT JOIN `story_category` sc on s.id = sc.storyId"
            + " WHERE  sc.categoryId = :categoryId AND s.status IN :storyStatus"
            + " GROUP BY s.id"
            + " ORDER BY s.updateDate DESC";

    public static final String COUNT_STORY_NEW_UPDATE_BY_CATEGORY = "SELECT COUNT(*)"
            + " FROM Story s LEFT JOIN (SELECT c.* FROM Chapter c INNER JOIN"
            + " (SELECT MAX(c.id) AS maxChapterID FROM Story s"
            + " LEFT JOIN Chapter c"
            + " ON s.id = c.storyId GROUP BY s.id) d"
            + " ON c.id = d.maxChapterID "
            + " WHERE c.status IN :chapterStatus) c "
            + " ON s.id = c.storyID "
            + " LEFT JOIN user u on c.userPosted = u.id"
            + " LEFT JOIN `story_category` sc on s.id = sc.storyId"
            + " WHERE  sc.categoryId = :categoryId AND s.status IN :storyStatus"
            + " GROUP BY s.id"
            + " ORDER BY s.updateDate DESC";

    public static final String VIP_STORY_NEW_UPDATE = "SELECT s.id, s.vnName, s.images, s.author, s.updateDate, s.infomation,"
            + " c.id as chapterId, c.chapterNumber,"
            + " u.displayName, u.username, s.dealStatus"
            + " FROM Story s LEFT JOIN (SELECT c.* FROM Chapter c INNER JOIN"
            + " (SELECT MAX(c.id) AS maxChapterID FROM Story s"
            + " LEFT JOIN Chapter c"
            + " ON s.id = c.storyId GROUP BY s.id) d"
            + " ON c.id = d.maxChapterID "
            + " WHERE c.status IN :chapterStatus) c "
            + " ON s.id = c.storyID "
            + " LEFT JOIN user u on c.userPosted = u.id"
            + " LEFT JOIN `story_category` sc on s.id = sc.storyId"
            + " LEFT JOIN Category ca on sc.categoryId = ca.id"
            + " WHERE s.status IN :storyStatus AND s.dealStatus = :storyDealStatus"
            + " GROUP BY s.id"
            + " ORDER BY s.updateDate DESC";

    public static final String COUNT_VIP_STORY_NEW_UPDATE = "SELECT COUNT(*)"
            + " FROM Story s LEFT JOIN (SELECT c.* FROM Chapter c INNER JOIN"
            + " (SELECT MAX(c.id) AS maxChapterID FROM Story s"
            + " LEFT JOIN Chapter c"
            + " ON s.id = c.storyId GROUP BY s.id) d"
            + " ON c.id = d.maxChapterID "
            + " WHERE c.status IN :chapterStatus) c "
            + " ON s.id = c.storyID "
            + " LEFT JOIN user u on c.userPosted = u.id"
            + " WHERE s.status IN :storyStatus AND s.dealStatus = :storyDealStatus"
            + " GROUP BY s.id"
            + " ORDER BY s.updateDate DESC";

    public static final String STORY_TOP_VIEW = "SELECT s.id, s.vnName, s.images, s.author, s.infomation, s.dealStatus, "
            + " COALESCE(d.countTopView,0) AS cnt, ca.id as categoryId, ca.name as categoryName"
            + " FROM Story s "
            + " LEFT JOIN (SELECT c.id as chapterId, c.storyId, COUNT(c.storyId) AS countTopView FROM Chapter c"
            + " LEFT JOIN Favorites f ON c.id = f.chapterId"
            + " LEFT JOIN Story st on c.storyId = st.id"
            + " WHERE st.status IN :storyStatus"
            + " AND f.dateView BETWEEN :startDate AND :endDate"
            + " AND f.status = :favoritesStatus"
            + " GROUP BY c.storyId) d ON s.id = d.storyId"
            + " LEFT JOIN `story_category` sc on s.id = sc.storyId"
            + " LEFT JOIN Category ca on sc.categoryId = ca.id"
            + " WHERE  s.status IN :storyStatus"
            + " GROUP BY s.id"
            + " ORDER BY cnt DESC, s.countView DESC";

    public static final String COUNT_STORY_TOP_VIEW = "SELECT COUNT(*) FROM (SELECT s.id, COALESCE(d.countTopView,0) AS cnt FROM Story s"
            + " LEFT JOIN (SELECT c.storyId, COUNT(c.storyId) AS countTopView FROM Chapter c"
            + " LEFT JOIN Favorites f ON c.id = f.chapterId"
            + " LEFT JOIN Story st on c.storyId = st.id"
            + " WHERE st.status IN :storyStatus"
            + " AND f.dateView BETWEEN :startDate AND :endDate"
            + " AND f.status = :favoritesStatus"
            + " GROUP BY c.storyId) d ON s.id = d.storyId"
            + " LEFT JOIN `story_category` sc on s.id = sc.storyId"
            + " LEFT JOIN Category ca on sc.categoryId = ca.id"
            + " WHERE  s.status IN :storyStatus"
            + " GROUP BY s.id"
            + " ORDER BY cnt DESC, s.countView DESC) rs";

    public static final String STORY_TOP_VIEW_BY_CATEGORY = "SELECT s.id, s.vnName, s.images, s.infomation, s.dealStatus, s.author,"
            + " COALESCE(d.countTopView,0) AS cnt, ca.id as categoryId, ca.name as categoryName FROM Story s"
            + " LEFT JOIN (SELECT c.storyId, COUNT(c.storyId) AS countTopView FROM Chapter c"
            + " LEFT JOIN `favorites` f ON  c.id = f.chapterId"
            + " LEFT JOIN Story st on c.storyId = st.id"
            + " WHERE st.status IN :storyStatus"
            + " AND f.dateView BETWEEN :startDate AND :endDate"
            + " AND f.status = :favoritesStatus"
            + " GROUP BY c.storyId) d ON s.id = d.storyId"
            + " LEFT JOIN `story_category` sc on s.id = sc.storyId"
            + " LEFT JOIN Category ca on sc.categoryId = ca.id"
            + " WHERE  s.status IN :storyStatus AND sc.categoryId = :categoryID"
            + " GROUP BY s.id"
            + " ORDER BY cnt DESC, s.countView DESC";

    public static final String COUNT_STORY_TOP_VIEW_BY_CATEGORY = "SELECT COUNT(*) FROM (SELECT s.sID, COALESCE(d.countView,0) AS cnt FROM Story s"
            + " LEFT JOIN (SELECT c.storyId, COUNT(c.storyId) AS countTopView FROM Chapter c"
            + " LEFT JOIN `favorites` f ON  c.id = f.chapterId"
            + " LEFT JOIN Story st on c.storyId = st.id"
            + " WHERE st.status IN :storyStatus"
            + " AND f.dateView BETWEEN :startDate AND :endDate"
            + " AND f.status = :favoritesStatus"
            + " GROUP BY c.storyId) d ON s.id = d.storyId"
            + " LEFT JOIN `story_category` sc on s.id = sc.storyId"
            + " LEFT JOIN Category ca on sc.categoryId = ca.id"
            + " WHERE  s.status IN :storyStatus AND sc.categoryId = :categoryID"
            + " GROUP BY s.id"
            + " ORDER BY cnt DESC, s.countView DESC) rs";

    public static final String STORY_VIP_TOP_VIEW = "SELECT s.id, s.vnName, s.images, s.author, COALESCE(g.scnt,0) as cnt, s.infomation, s.dealStatus, "
            + " ca.name as categoryName, ca.id as categoryId FROM Story s"
            + " LEFT JOIN (SELECT c.storyId, c.id, SUM(p.money) AS scnt FROM Chapter c"
            + " LEFT JOIN pay p on c.id = p.chapterId"
            + " WHERE p.createDate BETWEEN :startDate AND :endDate"
            + " GROUP BY c.storyId) g ON g.storyId = s.id"
            + " LEFT JOIN `story_category` sc on s.id = sc.storyId"
            + " LEFT JOIN Category ca on sc.categoryId = ca.id"
            + " WHERE s.dealStatus = :storyDealStatus AND s.status IN :storyStatus"
            + " GROUP BY s.id"
            + " ORDER BY COALESCE(g.scnt,0) DESC";

    public static final String COUNT_STORY_VIP_TOP_VIEW = "SELECT COUNT(*) FROM Story s"
            + " LEFT JOIN (SELECT  c.storyId, c.id, SUM(p.money) AS scnt FROM Chapter c"
            + " LEFT JOIN pay p on c.id = p.chapterId"
            + " WHERE p.createDate BETWEEN :startDate AND :endDate"
            + " GROUP BY c.storyId) g ON g.storyId = s.id"
            + " LEFT JOIN `story_category` sc on s.id = sc.storyId"
            + " LEFT JOIN Category ca on sc.categoryId = ca.id"
            + " WHERE s.dealStatus = :storyDealStatus AND s.status IN :storyStatus"
            + " GROUP BY s.id"
            + " ORDER BY COALESCE(g.scnt,0) DESC";

    public static final String TOP_CONVERTER = "SELECT u.id, u.username, u.displayName, u.avatar,"
            + " COALESCE(d.cntc ,0) AS cnt, COALESCE(e.cnts ,0) AS scnt"
            + " FROM User u"
            + " LEFT JOIN (SELECT c.userPosted as userChapterPosted, COUNT(c.userPosted) as cntc FROM Chapter c"
            + " WHERE c.status IN :chapterStatus"
            + " GROUP BY c.userPosted) d ON u.id = d.userChapterPosted"
            + " LEFT JOIN (SELECT s.userPosted as userStoryPosted, COUNT(s.userPosted) as cnts FROM Story s"
            + " WHERE s.status IN :storyStatus"
            + " GROUP BY s.userPosted) e ON u.id = e.userStoryPosted"
            + " WHERE u.status = :userStatus"
            + " ORDER BY cnt DESC, scnt DESC";

    public static final String COUNT_TOP_CONVERTER = "SELECT COUNT(*) FROM User u"
            + " LEFT JOIN (SELECT c.userPosted as userChapterPosted, COALESCE(COUNT(c.userPosted),0) as cnt FROM Chapter c"
            + " WHERE c.status IN :chapterStatus"
            + " GROUP BY c.userPosted) d ON u.id = d.userChapterPosted"
            + " LEFT JOIN (SELECT s.userPosted as userStoryPosted, COALESCE(COUNT(s.userPosted),0) as scnt FROM Story s"
            + " WHERE s.status IN :storyStatus"
            + " GROUP BY s.userPosted) e ON u.id = e.userStoryPosted"
            + " WHERE u.status = :userStatus"
            + " ORDER BY cnt DESC, scnt DESC";

    public static final String LIST_ALL_FAVORITES_CHAPTER = "SELECT c.* FROM Chapter c"
            + " INNER JOIN (SELECT MAX(f.ufID), uf.chID FROM `favorites` f"
            + " LEFT JOIN Chapter c ON uf.chID = c.chID"
            + " WHERE uf.uID= :uID AND uf.ufStatus = :ufStatus AND c.chStatus IN :chStatus"
            + " AND uf.ufView = 1"
            + " GROUP BY c.sID) d ON d.chID = c.chID"
            + " INNER JOIN Story s on c.sID = s.sID"
            + " WHERE S.sStatus != :sStatus"
            + " ORDER BY s.updateDate DESC";

    public static final String STORY_COMPLETE = "SELECT s.id, s.vnName, s.images, s.author, s.updateDate, "
            + " c.id as chapterId, c.chapterNumber, "
            + " u.displayName, u.username, s.dealStatus"
            + " FROM Story s LEFT JOIN (SELECT c.* FROM Chapter c INNER JOIN"
            + " (SELECT MAX(c.id) AS maxChapterID FROM Story s"
            + " LEFT JOIN Chapter c"
            + " ON s.id = c.storyId GROUP BY s.id) d"
            + " ON c.id = d.maxChapterID "
            + " WHERE c.status IN :chapterStatus) c "
            + " ON s.id = c.storyID "
            + " LEFT JOIN user u on c.userPosted = u.id"
            + " WHERE s.status = :storyStatus"
            + " ORDER BY s.updateDate DESC";

    public static final String COUNT_STORY_COMPLETE = "SELECT COUNT(*)"
            + " FROM Story s LEFT JOIN (SELECT c.* FROM Chapter c INNER JOIN"
            + " (SELECT MAX(c.id) AS maxChapterID FROM Story s"
            + " LEFT JOIN Chapter c"
            + " ON s.id = c.storyId GROUP BY s.id) d"
            + " ON c.id = d.maxChapterID "
            + " WHERE c.status IN :chapterStatus) c "
            + " ON s.id = c.storyID "
            + " LEFT JOIN user u on c.userPosted = u.id"
            + " WHERE s.status = :storyStatus"
            + " ORDER BY s.updateDate DESC";

    public static final String COMPLETE_STORY_TOP_VIEW_SWAPPER = "SELECT s.* FROM Story s"
            + " LEFT JOIN (SELECT c.storyId, COUNT(c.storyId) AS countStoryView FROM Chapter c"
            + " LEFT JOIN `favorites` f ON c.id = f.chapterId"
            + " LEFT JOIN Story st on c.storyId = st.id"
            + " WHERE st.status = :storyStatus"
            + " AND f.dateView BETWEEN :startDate AND :endDate"
            + " AND f.status = :favoritesStatus"
            + " GROUP BY c.storyId) d ON s.id = d.storyId"
            + " WHERE s.status = :storyStatus"
            + " ORDER BY COALESCE(d.countStoryView,0) DESC, s.countView DESC";

    public static final String COUNT_COMPLETE_STORY_TOP_VIEW_SWAPPER = "SELECT COUNT(*) FROM (SELECT s.* FROM Story s"
            + " LEFT JOIN (SELECT c.storyId, COUNT(c.storyId) AS countStoryView FROM Chapter c"
            + " LEFT JOIN `favorites` f ON c.id = f.chapterId"
            + " LEFT JOIN Story st on c.storyId = st.id"
            + " WHERE st.status = :storyStatus"
            + " AND f.dateView BETWEEN :startDate AND :endDate"
            + " AND f.status = :favoritesStatus"
            + " GROUP BY c.storyId) d ON s.id = d.storyId"
            + " WHERE s.status = :storyStatus"
            + " ORDER BY COALESCE(d.countStoryView,0) DESC, s.countView DESC) rs";

    public static final String NEXT_CHAPTER = "SELECT ch.id FROM chapter ch"
            + " WHERE ch.serial > :chapterSerial"
            + " AND ch.storyId = :storyId"
            + " AND ch.status IN :chapterStatus"
            + " ORDER BY ch.serial ASC"
            + " LIMIT 1";

    public static final String PREVIOUS_CHAPTER = "SELECT ch.id FROM chapter ch"
            + " WHERE ch.serial < :chapterSerial"
            + " AND ch.storyId = :storyId"
            + " AND ch.status IN :chapterStatus"
            + " ORDER BY ch.serial DESC"
            + " LIMIT 1";

    public static final String CHAPTER_HEAD = "SELECT ch.* FROM chapter ch"
            + " WHERE ch.storyId = :storyId"
            + " AND ch.status IN :chapterStatus"
            + " ORDER BY ch.serial ASC"
            + " LIMIT 1";

    public static final String CHAPTER_NEW = "SELECT ch.* FROM chapter ch"
            + " WHERE ch.storyId = :storyId"
            + " AND ch.status IN :chapterStatus"
            + " ORDER BY ch.serial DESC"
            + " LIMIT 1";

    public static final String SEARCH_STORY = "SELECT s.id, s.vnName, s.images, s.author, s.updateDate, "
            + " c.id as chapterId, c.chapterNumber,"
            + " u.displayName, u.username, s.dealStatus"
            + " FROM Story s LEFT JOIN (SELECT c.* FROM Chapter c INNER JOIN"
            + " (SELECT MAX(c.id) AS maxChapterId FROM Story s"
            + " LEFT JOIN Chapter c"
            + " ON s.id = c.storyId"
            + " WHERE c.status IN :chapterStatus"
            + " AND LOWER(s.vnName) LIKE %:search%"
            + " AND s.status IN :storyStatus"
            + " GROUP BY s.id) d"
            + " ON c.id = d.maxChapterId ) c "
            + " ON s.id = c.storyId "
            + " LEFT JOIN user u on c.userPosted = u.id"
            + " WHERE s.status IN :storyStatus"
            + " AND LOWER(s.vnName) LIKE %:search%"
            + " ORDER BY s.updateDate DESC";

    public static final String COUNT_SEARCH_STORY = "SELECT COUNT(*)"
            + " FROM Story s LEFT JOIN (SELECT c.* FROM Chapter c INNER JOIN"
            + " (SELECT MAX(c.id) AS maxChapterId FROM Story s"
            + " LEFT JOIN Chapter c"
            + " ON s.id = c.storyId"
            + " WHERE c.status IN :chapterStatus"
            + " AND LOWER(s.vnName) LIKE %:search%"
            + " AND s.status IN :storyStatus"
            + " GROUP BY s.id) d"
            + " ON c.id = d.maxChapterId ) c "
            + " ON s.id = c.storyId "
            + " LEFT JOIN user u on c.userPosted = u.id"
            + " WHERE s.status IN :storyStatus"
            + " AND LOWER(s.vnName) LIKE %:search%"
            + " ORDER BY s.updateDate DESC";

    public static final String STORY_TOP_APPOIND = "SELECT s.id, s.vnName, s.images, s.author, s.infomation, s.dealStatus, "
            + " COALESCE(s.countAppoint,0) AS cnt, ca.id as categoryId, ca.name as categoryName"
            + " FROM Story s "
            + " LEFT JOIN `story_category` sc on s.id = sc.storyId"
            + " LEFT JOIN Category ca on sc.categoryId = ca.id"
            + " WHERE  s.status IN :storyStatus"
            + " GROUP BY s.id"
            + " ORDER BY cnt DESC, s.countView DESC";

    public static final String COUNT_STORY_TOP_APPOIND = "SELECT COUNT(*) FROM (SELECT s.id, COALESCE(d.countTopView,0) AS cnt FROM Story s"
            + " LEFT JOIN (SELECT c.storyId, COUNT(c.storyId) AS countTopView FROM Chapter c"
            + " LEFT JOIN Favorites f ON c.id = f.chapterId"
            + " LEFT JOIN Story st on c.storyId = st.id"
            + " WHERE st.status IN :storyStatus"
            + " AND f.dateView BETWEEN :startDate AND :endDate"
            + " AND f.status = :favoritesStatus"
            + " GROUP BY c.storyId) d ON s.id = d.storyId"
            + " LEFT JOIN `story_category` sc on s.id = sc.storyId"
            + " LEFT JOIN Category ca on sc.categoryId = ca.id"
            + " WHERE  s.status IN :storyStatus"
            + " GROUP BY s.id"
            + " ORDER BY cnt DESC, s.countView DESC) rs";
}
