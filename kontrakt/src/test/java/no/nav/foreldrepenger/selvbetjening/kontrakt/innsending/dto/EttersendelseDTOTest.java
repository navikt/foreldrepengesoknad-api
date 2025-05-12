package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import static no.nav.foreldrepenger.common.domain.felles.DokumentType.I000044;
import static no.nav.foreldrepenger.common.domain.felles.DokumentType.I000119;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.common.mapper.DefaultJsonMapper;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.BrukerTekstDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.EttersendelseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.YtelseType;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggInnsendingType;


class EttersendelseDTOTest {

    private static final ObjectMapper MAPPER = DefaultJsonMapper.MAPPER;

    @Test
    void ettersendelseUttalelsePåTilbakekrevingKonsistensTest() throws IOException {
        var ettersendelse = new EttersendelseDto(LocalDate.now(), YtelseType.FORELDREPENGER, new Saksnummer("123456789"),
            new BrukerTekstDto(I000119, "En tekst tror jeg?", "Ooj oj oj en overskrift!"), "dialogidSomIndikererUttalelseOmTilbakekreving",
            List.of());

        assertThat(ettersendelse.erTilbakebetalingUttalelse()).isTrue();
        assertThat(ettersendelse.brukerTekst().dokumentType().erUttalelseOmTilbakekreving()).isTrue();
        test(ettersendelse);
    }

    @Test
    void ettersendelseSeraliseringVirkerTest() throws IOException {
        var vedleggI000044 = new VedleggDto(UUID.randomUUID(), I000044, VedleggInnsendingType.LASTET_OPP, "what what", null);
        var ettersendelse = new EttersendelseDto(LocalDate.now(), YtelseType.FORELDREPENGER, new Saksnummer("123456789"), null, null,
            List.of(vedleggI000044));


        assertThat(vedleggI000044.referanse().verdi()).isNotNull().startsWith("V");
        test(ettersendelse);
    }

    private void test(Object object) throws IOException {
        assertEquals(object, MAPPER.readValue(write(object), object.getClass()));
    }

    private String write(Object obj) throws JsonProcessingException {
        return MAPPER.writeValueAsString(obj);
    }
}
