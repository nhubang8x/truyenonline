package online.hthang.truyenonline.annotations;

import online.hthang.truyenonline.validator.ExtensionUploadValidator;
import online.hthang.truyenonline.validator.UniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Huy Thang on 13/10/2018
 * @project truyenmvc
 */

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExtensionUploadValidator.class)
@Documented
public @interface ExtensionUpload {

    String message() default "{hthang.truyenmvc.user.uEmail.UniqueEmail.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
