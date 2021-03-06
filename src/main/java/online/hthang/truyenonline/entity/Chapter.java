package online.hthang.truyenonline.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import online.hthang.truyenonline.annotations.FormatFloat;
import online.hthang.truyenonline.utils.ConstantsUtils;
import online.hthang.truyenonline.utils.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Huy Thang
 */
@Entity
@Table(name = "chapter")
@Data
@NoArgsConstructor
@FormatFloat(baseField = "story", idField="id", matchField = "serial")
public class Chapter implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @NotEmpty(message = "{hthang.truyenmvc.chapter.chapterNumber.empty.message}")
    @Column(name = "chapterNumber", nullable = false)
    private String chapterNumber;
    @Column(name = "serial", nullable = false, precision = 12, scale = 0)
    private Float serial;
    @NotEmpty(message = "{hthang.truyenmvc.chapter.name.empty.message}")
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "countView")
    private Integer countView;
    @NotEmpty(message = "{hthang.truyenmvc.chapter.content.empty.message}")
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createDate", length = 19)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createDate;
    @Column(name = "wordCount")
    private Integer wordCount;
    @Column(name = "price", precision = 22, scale = 0)
    private Double price;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dealine", length = 19)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date dealine;
    @Column(name = "status")
    private Integer status;
    @JoinColumn(name = "storyId", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Story story;
    @JoinColumn(name = "userPosted", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @PrePersist
    public void prePersist() {
        if (createDate == null) {
            createDate = DateUtils.getCurrentDate();
        }
        if (price == null) {
            price = (double) 0;
        }
        if (status == null) {
            status = ConstantsUtils.CHAPTER_ACTIVED;
        }
        if (wordCount == null) {
            wordCount = 0;
        }
        if(countView == null){
            countView = 0;
        }
    }

}
