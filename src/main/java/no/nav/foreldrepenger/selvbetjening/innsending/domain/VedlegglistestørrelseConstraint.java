package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = VedlegglistestørrelseValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface VedlegglistestørrelseConstraint {
    String message() default "Vedleggslisten kan ikke inneholde flere enn 40 opplastede vedlegg eller 100 vedlegg som skal sendes senere.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
