package no.nav.foreldrepenger.selvbetjening.innsyn.uttaksplan;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JacksonConfiguration.class)
class UttaksplanSerliseringRoundtripTest {

    @Autowired
    ObjectMapper mapper;

    @Test
    void uttaksperiodeRoundtripSeraliseringsTest() throws IOException {
        var uttaksperiode = getUttaksPeriode();
        test(uttaksperiode);
        assertThat(uttaksperiode.periode()).isNotNull();
        assertThat(uttaksperiode.periode().fom()).isNotNull();
        assertThat(uttaksperiode.periode().tom()).isNotNull();
    }

    @Test
    void uttaksplanUtenPerioderRoundtripSeraliseringTest() throws IOException {
        var grunnlag = getGrunnlag();
        var uttaksplanUtenPerioder = new Uttaksplan(grunnlag, List.of());
        test(uttaksplanUtenPerioder);
    }

    @Test
    void uttaksplanMedPerioderRoundtripSeraliseringTest() throws IOException {
        var uttaksperiode = getUttaksPeriode();
        var grunnlag = getGrunnlag();
        var uttaksplanUtenPerioder = new Uttaksplan(grunnlag, List.of(uttaksperiode, uttaksperiode));
        test(uttaksplanUtenPerioder);
    }

    private SøknadsGrunnlag getGrunnlag() {
        return new SøknadsGrunnlag(null, LocalDate.now(), null, 100,
            1, true, true, true, null,
            true, false, true);
    }

    private UttaksPeriode getUttaksPeriode() {
        return new UttaksPeriode(
            OppholdÅrsak.UTTAK_FEDREKVOTE_ANNEN_FORELDER,
            OverføringÅrsak.ALENEOMSORG,
            GraderingAvslagÅrsak.AVSLAG_PGA_100_PROSENT_ARBEID,
            UtsettelsePeriodeType.ARBEID,
            PeriodeResultatType.AVSLÅTT,
            true,
            true,
            LocalDate.now().minusMonths(4),
            LocalDate.now(),
            null,
            StønadskontoType.FELLESPERIODE,
            50.0,
            100,
            100,
            true,
            MorsAktivitet.UFØRE,
            true,
            true,
            0,
            UttakArbeidType.FRILANS,
            new ArbeidsgiverInfo("123", "Privat Arbeidsgiver", ArbeidsgiverType.PRIVAT),
            "ÅRSAK");
    }

    private void test(Object object) throws IOException {
        assertEquals(object, mapper.readValue(write(object), object.getClass()));
    }

    private String write(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }
}
