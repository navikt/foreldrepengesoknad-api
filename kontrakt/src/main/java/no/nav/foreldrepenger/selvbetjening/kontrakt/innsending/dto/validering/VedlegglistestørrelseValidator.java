package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.validering;

import static no.nav.foreldrepenger.common.domain.felles.InnsendingsType.SEND_SENERE;

import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;

public class VedlegglistestørrelseValidator implements ConstraintValidator<VedlegglistestørrelseConstraint, List<VedleggDto>> {

    @Override
    public boolean isValid(List<VedleggDto> values, ConstraintValidatorContext context) {
        var antallSendSenere = values.stream().filter(vf -> SEND_SENERE.equals(vf.innsendingsType())).count();
        return antallSendSenere < 101 && (values.size() - antallSendSenere) < 41;
    }

}


