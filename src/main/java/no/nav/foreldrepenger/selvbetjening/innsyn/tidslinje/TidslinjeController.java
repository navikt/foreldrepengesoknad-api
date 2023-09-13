package no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.innsyn.Innsyn;


@ProtectedRestController("/rest/innsyn/tidslinje")
public class TidslinjeController {
    private static final Logger LOG = LoggerFactory.getLogger(TidslinjeController.class);

    private final TidslinjeTjeneste tidslinjeTjeneste;
    private final Innsyn innsyn;

    @Autowired
    public TidslinjeController(TidslinjeTjeneste tidslinjeTjeneste, Innsyn innsyn) {
        this.tidslinjeTjeneste = tidslinjeTjeneste;
        this.innsyn = innsyn;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TidslinjeHendelseDto> hentTidslinje(@RequestParam @Valid Saksnummer saksnummer) {
        var tidslinjeHendelseDto = tidslinjeTjeneste.hentTidslinje(saksnummer);
        sammenlign(tidslinjeHendelseDto, saksnummer);
        return tidslinjeHendelseDto;
    }

    private void sammenlign(List<TidslinjeHendelseDto> historikk, Saksnummer saksnummer) {
        try {
            var oversikt = innsyn.tidslinje(saksnummer);
            if (historikk.equals(oversikt)) {
                LOG.info("Ingen avvik mellom tidslinje fra fpoversikt og fpinfo-historikk");
            } else {
                LOG.info("AVVIK [tidslinje]: Fpinfo-historikk returnerte {}, mens fpoversikt returnerte {}", historikk, oversikt);
            }
        } catch (Exception e) {
            LOG.info("Noe gikk galt med henting eller sammenligningav av tidlinjen", e);
        }
    }
}
