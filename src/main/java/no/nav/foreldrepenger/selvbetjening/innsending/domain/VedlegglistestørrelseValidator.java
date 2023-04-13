package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class VedlegglistestørrelseValidator implements ConstraintValidator<VedlegglistestørrelseConstraint, List<VedleggFrontend>> {

    @Override
    public boolean isValid(List<VedleggFrontend> values, ConstraintValidatorContext context) {
        var antallSendSenere = values.stream().filter(vf -> "SEND_SENERE".equals(vf.getInnsendingsType())).count();
        return antallSendSenere < 101 && (values.size() - antallSendSenere) < 41;
    }

}


