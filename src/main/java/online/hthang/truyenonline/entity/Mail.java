package online.hthang.truyenonline.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Huy Thang
 */

@NoArgsConstructor
@Data
public class Mail {

    private String from;
    private String fromDisplay;
    private String to;
    private String subject;
    private Map<String, Object> model;


}
