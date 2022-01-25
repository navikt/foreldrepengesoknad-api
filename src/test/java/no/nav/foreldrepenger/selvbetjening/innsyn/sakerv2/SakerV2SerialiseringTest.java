package no.nav.foreldrepenger.selvbetjening.innsyn.sakerv2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.innsyn.sakerv2.persondetaljer.AktørId;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
class SakerV2SerialiseringTest {

    @Inject
    private ObjectMapper mapper;

    private final static AnnenPart annenPart = new AnnenPart(new AktørId("42"));
    private final static AktørId barn = new AktørId("1");


    @Test
    void testSerialiseringAnnenPart() throws Exception {
        test(annenPart);
    }

    @Test
    void SakerMedForeldrepengesakRoundtrip() throws Exception {
        var saksnummer = new Saksnummer("123");
        var familieHendelse = new Familiehendelse(LocalDate.of(2021, 12, 6),
            LocalDate.of(2021, 12, 5), 1, LocalDate.of(2021, 12, 12));
        var vedtakPerioder = new VedtakPeriode(LocalDate.of(2021, 12, 1),
            LocalDate.of(2022, 3, 31),
            KontoType.FORELDREPENGER,
            new VedtakPeriodeResultat(true),
            UtsettelseÅrsak.BARN_INNLAGT,
            OppholdÅrsak.MØDREKVOTE_ANNEN_FORELDER,
            OverføringÅrsak.ALENEOMSORG,
            new Gradering(BigDecimal.valueOf(50L)),
            MorsAktivitet.INNLAGT,
            new SamtidigUttak(BigDecimal.valueOf(30L)),
            false);
        var åpenBehandling = new FpÅpenBehandling(BehandlingTilstand.UNDER_BEHANDLING, Set.of(new Søknadsperiode(LocalDate.of(2021, 11, 1),
            LocalDate.of(2021, 11, 13), KontoType.FORELDREPENGER)));
        var fpVedtak = new FpVedtak(List.of(vedtakPerioder));
        var fpSak = new FpSak(saksnummer, false, false, false, false, false,
            RettighetType.ALENEOMSORG, annenPart, familieHendelse, fpVedtak, åpenBehandling, Set.of(barn),
            Dekningsgrad.ÅTTI);
        var saker = new Saker(Set.of(fpSak), Set.of(), Set.of());

        test(saker);
    }

    private void test(Object object) throws IOException {
        assertEquals(object, mapper.readValue(write(object), object.getClass()));
    }

    private String write(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }



}
