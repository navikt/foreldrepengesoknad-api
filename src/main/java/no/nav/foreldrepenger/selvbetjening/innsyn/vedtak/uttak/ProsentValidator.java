package no.nav.foreldrepenger.selvbetjening.innsyn.vedtak.uttak;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ProsentValidator implements ConstraintValidator<Prosent, Double> {

    @Override
    public boolean isValid(Double prosent, ConstraintValidatorContext context) {
        return !((prosent == null) || (prosent.doubleValue() < 0D) || (prosent.doubleValue() > 100D));
    }
}
