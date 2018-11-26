package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

@Constraint(validatedBy = ProsentValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Prosent {

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

    String message() default "{prosent.ugyldig}";
}
