package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.validering;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = VedlegglistestørrelseValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface VedlegglistestørrelseConstraint {
    String message() default "Vedleggslisten kan ikke inneholde flere enn 40 opplastede vedlegg eller 100 vedlegg som skal sendes senere.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
