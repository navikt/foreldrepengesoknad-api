package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.validering;

import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggInnsendingType;

public class VedlegglistestørrelseValidator implements ConstraintValidator<VedlegglistestørrelseConstraint, List<VedleggDto>> {

    @Override
    public boolean isValid(List<VedleggDto> values, ConstraintValidatorContext context) {
        var antallSendSenere = values.stream().filter(vf -> VedleggInnsendingType.SEND_SENERE.equals(vf.innsendingsType())).count();
        return antallSendSenere < 101 && (values.size() - antallSendSenere) < 41;
    }

}


