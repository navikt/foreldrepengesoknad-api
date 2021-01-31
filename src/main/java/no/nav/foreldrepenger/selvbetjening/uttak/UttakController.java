package no.nav.foreldrepenger.selvbetjening.uttak;

import static no.nav.foreldrepenger.regler.uttak.konfig.StandardKonfigurasjon.SØKNADSDIALOG;

import java.time.LocalDate;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.regler.uttak.beregnkontoer.StønadskontoRegelOrkestrering;
import no.nav.foreldrepenger.regler.uttak.beregnkontoer.grunnlag.BeregnKontoerGrunnlag;
import no.nav.foreldrepenger.regler.uttak.beregnkontoer.grunnlag.Dekningsgrad;
import no.nav.foreldrepenger.regler.uttak.felles.grunnlag.Stønadskontotype;
import no.nav.security.token.support.core.api.Unprotected;

@RestController
@RequestMapping(UttakController.UTTAK)
@Unprotected
public class UttakController {

    private static final String FMT = "yyyyMMdd";

    private static final Logger LOG = LoggerFactory.getLogger(UttakController.class);

    private final StønadskontoRegelOrkestrering kalkulator;

    static final String UTTAK = "/konto";

    @Inject
    public UttakController() {
        this.kalkulator = new StønadskontoRegelOrkestrering();
    }

    @GetMapping
    public Map<String, Map<Stønadskontotype, Integer>> kontoer(
            @RequestParam(name = "antallBarn", required = true, defaultValue = "1") int antallBarn,
            @RequestParam(name = "morHarRett", required = true) boolean morHarRett,
            @RequestParam(name = "farHarRett", required = true) boolean farHarRett,
            @RequestParam(name = "morHarAleneomsorg", required = false, defaultValue = "false") boolean morHarAleneomsorg,
            @RequestParam(name = "farHarAleneomsorg", required = false, defaultValue = "false") boolean farHarAleneomsorg,
            @DateTimeFormat(pattern = FMT) @RequestParam(name = "fødselsdato", required = false) LocalDate fødselsdato,
            @DateTimeFormat(pattern = FMT) @RequestParam(name = "termindato", required = false) LocalDate termindato,
            @DateTimeFormat(pattern = FMT) @RequestParam(name = "omsorgsovertakelseDato", required = false) LocalDate omsorgsovertakelseDato,
            @DateTimeFormat(pattern = FMT) @RequestParam(name = "startdatoUttak", required = false) LocalDate startdatoUttak,
            @DateTimeFormat(pattern = FMT) @RequestParam(name = "dekningsgrad", required = true) String dekningsgrad) {

        LOG.info(
                "Beregner konti barn={},dekning={},morHarAlene={},farHarAlene={},morHarRett={},farHarRett={},fødselsdato={},termindato={},omsorgsdato={}",
                antallBarn, dekningsgrad(dekningsgrad), morHarAleneomsorg, farHarAleneomsorg, morHarRett,
                fødselsdato, termindato, omsorgsovertakelseDato);
        var b = new BeregnKontoerGrunnlag.Builder()
                .medAntallBarn(antallBarn)
                .medDekningsgrad(dekningsgrad(dekningsgrad))
                .morAleneomsorg(morHarAleneomsorg)
                .farAleneomsorg(farHarAleneomsorg)
                .morRett(morHarRett)
                .farRett(farHarRett)
                .medFødselsdato(fødselsdato)
                .medOmsorgsovertakelseDato(omsorgsovertakelseDato)
                .medTermindato(termindato)
                .build();
        var konti = kalkulator.beregnKontoer(b, SØKNADSDIALOG).getStønadskontoer();
        LOG.info("Beregnet konti {}", konti);
        return Map.of("kontoer", konti);
    }

    private Dekningsgrad dekningsgrad(String dekningsgrad) {
        return switch (dekningsgrad) {
            case "100" -> Dekningsgrad.DEKNINGSGRAD_100;
            case "80" -> Dekningsgrad.DEKNINGSGRAD_80;
            default -> throw new IllegalArgumentException(dekningsgrad);
        };
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [kalkulator=" + kalkulator + "]";
    }
}
