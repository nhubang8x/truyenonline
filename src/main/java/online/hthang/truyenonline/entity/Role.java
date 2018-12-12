package online.hthang.truyenonline.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author Huy Thang
 */
@Entity
@Table(name = "role", schema = "")
@Data
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rID", unique = true, nullable = false)
    private Integer rID;
    @Column(name = "rName", nullable = false, length = 150)
    private String rName;

}
