package online.hthang.truyenonline.validator;

import online.hthang.truyenonline.annotations.FormatFloat;
import online.hthang.truyenonline.controller.web.ChapterController;
import online.hthang.truyenonline.entity.Story;
import online.hthang.truyenonline.service.ChapterService;
import online.hthang.truyenonline.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

/**
 * @author Huy Thang on 13/10/2018
 * @project truyenmvc
 */

public class FormatFloatValidator implements ConstraintValidator< FormatFloat, Object > {

    @Autowired
    private ChapterService chapterService;

    Logger logger = LoggerFactory.getLogger(FormatFloatValidator.class);
    private String message;
    private String baseField;
    private String matchField;

    @Override
    public void initialize(FormatFloat constraint) {
        baseField = constraint.baseField();
        matchField = constraint.matchField();
        message = constraint.message();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        boolean valid = true;
        try {
            Story baseFieldValue = (Story) getFieldValue(object, baseField);
            Object matchFieldValue = getFieldValue(object, matchField);
            if (matchFieldValue != null) {
                logger.info("Vào rồi");
                if (WebUtils.checkFloatNumber(matchFieldValue.toString())) {
                    valid = false;
                    message = "Số thứ tự phải là số và lớn hơn 0";
                    logger.info("lỗi rồi");
                } else {
                    Float number = Float.parseFloat(matchFieldValue.toString());
                    if (chapterService.checkChapterBySerial(baseFieldValue.getId(), number)) {
                        valid = false;
                        message = "Đã tồn tại số thứ tự này!";
                        logger.info("tồn tại rồi");
                    }
                }
            }else {
                valid = false;
                message ="Số thứ tự Chương không được để trống";
                logger.info("null rồi");
            }
        } catch (Exception e) {
        }
        if (!valid) {
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(matchField)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return valid;
    }

    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Class< ? > clazz = object.getClass();
        Field passwordField = clazz.getDeclaredField(fieldName);
        passwordField.setAccessible(true);
        return passwordField.get(object);
    }
}
