package online.hthang.truyenonline.annotations;

import online.hthang.truyenonline.validator.CheckUploadValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Huy Thang on 13/10/2018
 * @project truyenmvc
 */

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckUploadValidator.class)
@Documented
public @interface CheckUpload {

    String message() default "{hthang.truyenmvc.story.category.empty.message}";

    Class< ? >[] groups() default {};

    Class< ? extends Payload >[] payload() default {};

}
