package no.nav.foreldrepenger.selvbetjening.uttak;

import static no.nav.foreldrepenger.regler.uttak.beregnkontoer.grunnlag.Dekningsgrad.DEKNINGSGRAD_100;
import static no.nav.foreldrepenger.regler.uttak.beregnkontoer.grunnlag.Dekningsgrad.DEKNINGSGRAD_80;
import static no.nav.foreldrepenger.regler.uttak.konfig.StandardKonfigurasjon.SØKNADSDIALOG;

import java.time.LocalDate;

import javax.inject.Inject;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.regler.uttak.beregnkontoer.Minsterett;
import no.nav.foreldrepenger.regler.uttak.beregnkontoer.StønadskontoRegelOrkestrering;
import no.nav.foreldrepenger.regler.uttak.beregnkontoer.grunnlag.BeregnKontoerGrunnlag;
import no.nav.foreldrepenger.regler.uttak.beregnkontoer.grunnlag.BeregnMinsterettGrunnlag;
import no.nav.foreldrepenger.regler.uttak.beregnkontoer.grunnlag.Dekningsgrad;
import no.nav.security.token.support.core.api.Unprotected;

@Validated
@Unprotected
@RestController
@RequestMapping(UttakController.UTTAK_PATH)
public class UttakController {

    static final String UTTAK_PATH = "/konto";

    private static final String FMT = "yyyyMMdd";
    private final StønadskontoRegelOrkestrering regelOrkestrering;


    @Inject
    public UttakController() {
        this.regelOrkestrering = new StønadskontoRegelOrkestrering();
    }

    @GetMapping
    public KontoBeregning beregn(@RequestParam("antallBarn") int antallBarn,
                                 @RequestParam("morHarRett") boolean morHarRett,
                                 @RequestParam("farHarRett") boolean farHarRett,
                                 @RequestParam(name = "morHarAleneomsorg", required = false, defaultValue = "false") boolean morHarAleneomsorg,
                                 @RequestParam(name = "farHarAleneomsorg", required = false, defaultValue = "false") boolean farHarAleneomsorg,
                                 @RequestParam(name = "fødselsdato", required = false) @DateTimeFormat(pattern = FMT) LocalDate fødselsdato,
                                 @RequestParam(name = "termindato", required = false) @DateTimeFormat(pattern = FMT) LocalDate termindato,
                                 @RequestParam(name = "omsorgsovertakelseDato", required = false) @DateTimeFormat(pattern = FMT) LocalDate omsorgsovertakelseDato,
                                 @RequestParam("dekningsgrad") @Pattern(regexp = "^[\\p{Digit}\\p{L}]*$") String dekningsgrad,
                                 @RequestParam(value = "erMor", required = false) boolean erMor,
                                 @RequestParam(value = "minsterett", required = false) boolean minsterett,
                                 @RequestParam(value = "morHarUføretrygd", required = false) boolean morHarUføretrygd,
                                 @RequestParam(value = "familieHendelseDatoNesteSak", required = false) LocalDate familieHendelseDatoNesteSak) {

        guardFamiliehendelse(fødselsdato, termindato, omsorgsovertakelseDato);
        var dekningsgradOversatt = dekningsgrad(dekningsgrad);
        var grunnlag = new BeregnKontoerGrunnlag.Builder().antallBarn(antallBarn)
            .dekningsgrad(dekningsgradOversatt)
            .morAleneomsorg(morHarAleneomsorg)
            .farAleneomsorg(farHarAleneomsorg)
            .morRett(morHarRett)
            .farRett(farHarRett)
            .fødselsdato(fødselsdato)
            .omsorgsovertakelseDato(omsorgsovertakelseDato)
            .termindato(termindato)
            .build();
        var stønadskontoer = regelOrkestrering.beregnKontoer(grunnlag, SØKNADSDIALOG).getStønadskontoer();
        var bareFarHarRett = farHarRett && !morHarRett;
        var aleneomsorg = erMor && morHarAleneomsorg || !erMor && farHarAleneomsorg;
        var minsterettGrunnlag = new BeregnMinsterettGrunnlag.Builder()
            .minsterett(minsterett)
            .mor(erMor)
            .bareFarHarRett(bareFarHarRett)
            .aleneomsorg(aleneomsorg)
            .morHarUføretrygd(morHarUføretrygd)
            .dekningsgrad(dekningsgradOversatt)
            .familieHendelseDato(familiehendelse(fødselsdato, termindato, omsorgsovertakelseDato))
            .familieHendelseDatoNesteSak(familieHendelseDatoNesteSak)
            .build();
        var minsteretter = Minsterett.finnMinsterett(minsterettGrunnlag);
        return new KontoBeregning(stønadskontoer, Minsteretter.from(minsteretter));
    }

    private LocalDate familiehendelse(LocalDate fødselsdato, LocalDate termindato, LocalDate omsorgsovertakelseDato) {
        if (omsorgsovertakelseDato != null) {
            return omsorgsovertakelseDato;
        }
        if (fødselsdato != null) {
            return fødselsdato;
        }
        return termindato;
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
        return getClass().getSimpleName() + " [regelOrkestrering=" + regelOrkestrering + "]";
    }
}
