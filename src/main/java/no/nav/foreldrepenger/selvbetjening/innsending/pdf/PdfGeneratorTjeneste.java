package no.nav.foreldrepenger.selvbetjening.innsending.pdf;

import static java.util.stream.Collectors.joining;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.tilbakebetaling.TilbakebetalingUttalelse;
import no.nav.foreldrepenger.selvbetjening.oppslag.OppslagTjeneste;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.PersonFrontend;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Søkerinfo;

@Component
public class PdfGeneratorTjeneste implements PdfGenerator {

    private final PdfGeneratorConnection connection;
    private final OppslagTjeneste oppslagTjeneste;

    public PdfGeneratorTjeneste(PdfGeneratorConnection connection, OppslagTjeneste oppslagTjeneste) {
        this.connection = connection;
        this.oppslagTjeneste = oppslagTjeneste;
    }

    @Override
    public byte[] generate(TilbakebetalingUttalelse uttalelse) {
        return connection.genererPdf(fra(uttalelse));
    }

    private TilbakebetalingUttalelseDto fra(TilbakebetalingUttalelse uttalelse) {
        Søkerinfo person = oppslagTjeneste.hentSøkerinfo();
        return new TilbakebetalingUttalelseDto(fulltnavn(person.søker()),
            person.søker().fnr(),
            uttalelse.saksnummer(),
            uttalelse.type(),
            LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
            uttalelse.brukerTekst().tekst());
    }

    private static String fulltnavn(PersonFrontend person) {
        return Stream.of(person.fornavn(), person.mellomnavn(), person.etternavn())
            .filter(Objects::nonNull)
            .filter(s -> !s.isBlank())
            .collect(joining(" "));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[connection=" + connection + ", oppslagTjeneste=" + oppslagTjeneste + "]";
    }

    @Override
    public String ping() {
        return connection.ping();
    }
}
