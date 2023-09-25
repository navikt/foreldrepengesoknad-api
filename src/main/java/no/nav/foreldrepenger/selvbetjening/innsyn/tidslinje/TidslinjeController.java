package no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje;

import static no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje.TidslinjeHendelseDto.TidslinjeHendelseType.ENDRINGSSØKNAD;
import static no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje.TidslinjeHendelseDto.TidslinjeHendelseType.ETTERSENDING;
import static no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje.TidslinjeHendelseDto.TidslinjeHendelseType.FØRSTEGANGSSØKNAD;
import static no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje.TidslinjeHendelseDto.TidslinjeHendelseType.FØRSTEGANGSSØKNAD_NY;
import static no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje.TidslinjeHendelseDto.TidslinjeHendelseType.VEDTAK;

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

    private final Innsyn innsyn;

    @Autowired
    public TidslinjeController(Innsyn innsyn) {
        this.innsyn = innsyn;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TidslinjeHendelseDto> hentTidslinje(@RequestParam @Valid Saksnummer saksnummer) {
        var oversikt = innsyn.tidslinje(saksnummer);
        tidslinjeKonsistensSjekk(oversikt);
        return oversikt;
    }

    private static void tidslinjeKonsistensSjekk(List<TidslinjeHendelseDto> tidslinjeHendelseDto) {
        try {
            for (var innslag : tidslinjeHendelseDto) {
                if (FØRSTEGANGSSØKNAD.equals(innslag.tidslinjeHendelseType())) {
                    if (finnesHendelseTypeTidligereITidslinjen(VEDTAK, innslag, tidslinjeHendelseDto)) {
                        LOG.info("Det finnes vedtak uten førstegangssøknad: {}", tidslinjeHendelseDto);
                    }
                }

                if (FØRSTEGANGSSØKNAD_NY.equals(innslag.tidslinjeHendelseType())) {
                    if (!finnesHendelseTypeTidligereITidslinjen(FØRSTEGANGSSØKNAD, innslag, tidslinjeHendelseDto)) {
                        LOG.info("Det finnes ingen førstegangssøknad før ny førstegangssøknad: {}", tidslinjeHendelseDto);
                    }
                }

                if (ETTERSENDING.equals(innslag.tidslinjeHendelseType())) {
                    if (!finnesHendelseTypeTidligereITidslinjen(FØRSTEGANGSSØKNAD, innslag, tidslinjeHendelseDto)) {
                        LOG.info("Det finnes ikke søknad før ettersendelse: {}", tidslinjeHendelseDto);
                    }
                }

                if (ENDRINGSSØKNAD.equals(innslag.tidslinjeHendelseType())) {
                    if (!finnesHendelseTypeTidligereITidslinjen(FØRSTEGANGSSØKNAD, innslag, tidslinjeHendelseDto) || !finnesHendelseTypeTidligereITidslinjen(VEDTAK, innslag, tidslinjeHendelseDto)) {
                        LOG.info("Det finnes ikke førstegangssøknad og/eller vedtak før endringssøknad: {}", tidslinjeHendelseDto);
                    }
                }
            }
        } catch (Exception e) {
            LOG.info("Konsistens sjekk feilet for tidslinje {}", tidslinjeHendelseDto, e);
        }
    }

    private static boolean finnesHendelseTypeTidligereITidslinjen(TidslinjeHendelseDto.TidslinjeHendelseType tidslinjeHendelseType,
                                                                  TidslinjeHendelseDto hendelse,
                                                                  List<TidslinjeHendelseDto> tidslinjeHendelseDto) {
        return tidslinjeHendelseDto.stream()
            .filter(t -> t.tidslinjeHendelseType().equals(tidslinjeHendelseType))
            .anyMatch(t -> t.opprettet().isBefore(hendelse.opprettet()));
    }
}
