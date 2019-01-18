package online.hthang.truyenonline.entity;

import lombok.Data;
import online.hthang.truyenonline.annotations.CheckUpload;
import online.hthang.truyenonline.annotations.ExtensionUpload;
import online.hthang.truyenonline.utils.ConstantsUtils;
import online.hthang.truyenonline.utils.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Huy Thang
 */
@Entity
@Table(name = "story")
@Data
public class Story implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @NotEmpty(message = "{hthang.truyenmvc.story.vnName.empty.message}")
    @Column(name = "vnName", nullable = false)
    private String vnName;
    @NotEmpty(message = "{hthang.truyenmvc.story.cnName.empty.message}")
    @Column(name = "cnName")
    private String cnName;
    @NotEmpty(message = "{hthang.truyenmvc.story.cnLink.empty.message}")
    @Column(name = "cnLink")
    private String cnLink;
    @Column(name = "images", nullable = false, length = 150)
    private String images;
    @NotEmpty(message = "{hthang.truyenmvc.story.author.empty.message}")
    @Column(name = "author", nullable = false)
    private String author;
    @NotEmpty(message = "{hthang.truyenmvc.story.infomation.empty.message}")
    @Column(name = "infomation", columnDefinition = "TEXT")
    private String infomation;
    @Column(name = "countAppoint")
    private Integer countAppoint;
    @Column(name = "countView")
    private Integer countView;
    @Column(name = "rating", precision = 12, scale = 0)
    private Float rating;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "createDate", length = 19)
    private Date createDate;
    @Column(name = "price", precision = 22, scale = 0)
    private Double price;
    @Column(name = "timeDeal")
    private Integer timeDeal;
    @Column(name = "dealStatus")
    private Integer dealStatus;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "updateDate", length = 19)
    private Date updateDate;
    @Column(name = "status")
    private Integer status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userPosted", referencedColumnName = "id", nullable = false)
    private User user;
    @NotEmpty(message = "{hthang.truyenmvc.story.category.empty.message}")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "story_category",
            joinColumns = {@JoinColumn(name = "storyId", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "categoryId", nullable = false)})
    private List< Category > categoryList;

    @CheckUpload
    @Transient
    private MultipartFile uploadfile;

    @ExtensionUpload
    @Transient
    private MultipartFile editfile;

    @PrePersist
    public void prePersist() {
        if (createDate == null) {
            createDate = DateUtils.getCurrentDate();
        }
        if (status == null) {
            status = ConstantsUtils.STORY_STATUS_GOING_ON;
        }
        if (rating == null) {
            rating = (float) 0;
        }
        if (countAppoint == null) {
            countAppoint = 0;
        }
        if (countView == null) {
            countAppoint = 0;
        }
        if (updateDate == null) {
            updateDate = DateUtils.getCurrentDate();
        }
        if (dealStatus == null) {
            dealStatus = ConstantsUtils.STORY_NOT_VIP;
        }
        if (timeDeal == null) {
            timeDeal = 0;
        }
        if (price == null) {
            price = (double) 0;
        }
    }
}
