package online.hthang.truyenonline.entity;

import lombok.Data;
import online.hthang.truyenonline.utils.ConstantsUtils;
import online.hthang.truyenonline.utils.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Huy Thang
 */
@Entity
@Table(name = "chapter", schema = "")
@Data
public class Chapter implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chID", unique = true, nullable = false)
    private Long chID;
    @Column(name = "chNumber", nullable = false)
    private Integer chNumber;
    @Column(name = "chSerial", nullable = false, precision = 12, scale = 0)
    private Float chSerial;
    @Column(name = "chName", nullable = false)
    private String chName;
    @Column(name = "chView")
    private Integer chView;
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createDate", length = 19)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createDate;
    @Column(name = "wordNumber")
    private Integer wordNumber;
    @Column(name = "price", precision = 22, scale = 0)
    private Double price;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dealine", length = 19)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date dealine;
    @Column(name = "chStatus")
    private Integer chStatus;
    @JoinColumn(name = "sID", referencedColumnName = "sID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Story story;
    @JoinColumn(name = "uID", referencedColumnName = "uID")
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
        if (chStatus == null) {
            chStatus = ConstantsUtils.CHAPTER_ACTIVED;
        }
    }

}
