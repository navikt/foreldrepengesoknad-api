package no.nav.foreldrepenger.selvbetjening.uttak;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;
import static no.nav.foreldrepenger.regler.uttak.beregnkontoer.grunnlag.Dekningsgrad.DEKNINGSGRAD_100;
import static no.nav.foreldrepenger.regler.uttak.beregnkontoer.grunnlag.Dekningsgrad.DEKNINGSGRAD_80;
import static no.nav.foreldrepenger.regler.uttak.konfig.StandardKonfigurasjon.SØKNADSDIALOG;

import java.time.LocalDate;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.regler.uttak.beregnkontoer.StønadskontoRegelOrkestrering;
import no.nav.foreldrepenger.regler.uttak.beregnkontoer.grunnlag.BeregnKontoerGrunnlag;
import no.nav.foreldrepenger.regler.uttak.beregnkontoer.grunnlag.Dekningsgrad;
import no.nav.foreldrepenger.regler.uttak.felles.grunnlag.Stønadskontotype;
import no.nav.security.token.support.core.api.Unprotected;

@Validated
@Unprotected
@RestController
@RequestMapping(UttakController.UTTAK_PATH)
public class UttakController {

    static final String UTTAK_PATH = "/konto";

    private static final String FMT = "yyyyMMdd";
    private final StønadskontoRegelOrkestrering kalkulator;


    @Inject
    public UttakController() {
        this.kalkulator = new StønadskontoRegelOrkestrering();
    }

    @GetMapping
    @CrossOrigin(origins = "*", allowCredentials = "false")
    public Map<String, Map<Stønadskontotype, Integer>> kontoer(
            @RequestParam("antallBarn") int antallBarn,
            @RequestParam("morHarRett") boolean morHarRett,
            @RequestParam("farHarRett") boolean farHarRett,
            @RequestParam(name = "morHarAleneomsorg", required = false, defaultValue = "false") boolean morHarAleneomsorg,
            @RequestParam(name = "farHarAleneomsorg", required = false, defaultValue = "false") boolean farHarAleneomsorg,
            @RequestParam(name = "fødselsdato", required = false) @DateTimeFormat(pattern = FMT) LocalDate fødselsdato,
            @RequestParam(name = "termindato", required = false) @DateTimeFormat(pattern = FMT) LocalDate termindato,
            @RequestParam(name = "omsorgsovertakelseDato", required = false) @DateTimeFormat(pattern = FMT) LocalDate omsorgsovertakelseDato,
            @RequestParam("dekningsgrad") @Pattern(regexp = FRITEKST)  String dekningsgrad) {

        guardFamiliehendelse(fødselsdato, termindato, omsorgsovertakelseDato);
        return Map.of("kontoer", kalkulator.beregnKontoer(new BeregnKontoerGrunnlag.Builder()
            .antallBarn(antallBarn)
            .dekningsgrad(dekningsgrad(dekningsgrad))
            .morAleneomsorg(morHarAleneomsorg)
            .farAleneomsorg(farHarAleneomsorg)
            .morRett(morHarRett)
            .farRett(farHarRett)
            .fødselsdato(fødselsdato)
            .omsorgsovertakelseDato(omsorgsovertakelseDato)
            .termindato(termindato)
            .build(), SØKNADSDIALOG).getStønadskontoer());
    }

    private void guardFamiliehendelse(LocalDate fødselsdato, LocalDate termindato, LocalDate omsorgsovertakelseDato) {
        if (fødselsdato == null && termindato == null && omsorgsovertakelseDato == null) {
            throw new ManglendeFamiliehendelseException("Mangler dato for familiehendelse");
        }
    }

    private static Dekningsgrad dekningsgrad(String dekningsgrad) {
        return switch (dekningsgrad) {
            case "100" -> DEKNINGSGRAD_100;
            case "80" -> DEKNINGSGRAD_80;
            default -> throw new IllegalArgumentException("Ugyldig dekningsgrad " + dekningsgrad);
        };
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [kalkulator=" + kalkulator + "]";
    }
}
