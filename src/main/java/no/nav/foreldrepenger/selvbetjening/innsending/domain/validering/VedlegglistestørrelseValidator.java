package no.nav.foreldrepenger.selvbetjening.innsending.domain.validering;

import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.VedleggDto;

public class VedlegglistestørrelseValidator implements ConstraintValidator<VedlegglistestørrelseConstraint, List<VedleggDto>> {

    @Override
    public boolean isValid(List<VedleggDto> values, ConstraintValidatorContext context) {
        var antallSendSenere = values.stream().filter(vf -> "SEND_SENERE".equals(vf.getInnsendingsType())).count();
        return antallSendSenere < 101 && (values.size() - antallSendSenere) < 41;
    }

}


