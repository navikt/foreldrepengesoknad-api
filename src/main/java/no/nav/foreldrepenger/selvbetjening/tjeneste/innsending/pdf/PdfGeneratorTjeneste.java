package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.pdf;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.tilbakebetaling.TilbakebetalingUttalelse;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.OppslagTjeneste;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Person;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Søkerinfo;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        return new TilbakebetalingUttalelseDto(fulltnavn(person.getSøker()),
            person.getSøker().fnr,
            uttalelse.getSaksnummer(),
            uttalelse.getType(),
            LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE),
            uttalelse.getBrukerTekst().getTekst());
    }

    private String fulltnavn(Person person) {
        return Stream.of(person.fornavn, person.mellomnavn, person.etternavn)
            .filter(Objects::nonNull)
            .filter(s -> !s.isBlank())
            .collect(Collectors.joining(" "));
    }
}
