package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import no.nav.foreldrepenger.common.domain.Orgnummer;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Virksomhetstype;

class NæringDtoTest {

    @Test
    void næringOppgittVarigEndringUtenVarigEndringinformasjonSkalFeile() {
        var validator = Validation.buildDefaultValidatorFactory().getValidator();
        var næring = new NæringDto(LocalDate.MIN,
            LocalDate.MAX,
            Virksomhetstype.DAGMAMMA,
            "navnet",
            Orgnummer.MAGIC_ORG,
            null,
            true,
            null,
            false,
            null,
            true,
            null,
            null,
            null);
        var validate = validator.validate(næring);
        assertThat(validate).hasSize(1);
    }
}
