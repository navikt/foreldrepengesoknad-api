package no.nav.foreldrepenger.selvbetjening.uttak;


import static no.nav.foreldrepenger.stønadskonto.regelmodell.grunnlag.Dekningsgrad.DEKNINGSGRAD_100;
import static no.nav.foreldrepenger.stønadskonto.regelmodell.grunnlag.Dekningsgrad.DEKNINGSGRAD_80;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import no.nav.foreldrepenger.stønadskonto.regelmodell.Minsterett;
import no.nav.foreldrepenger.stønadskonto.regelmodell.StønadskontoRegelOrkestrering;
import no.nav.foreldrepenger.stønadskonto.regelmodell.grunnlag.BeregnKontoerGrunnlag;
import no.nav.foreldrepenger.stønadskonto.regelmodell.grunnlag.BeregnMinsterettGrunnlag;
import no.nav.foreldrepenger.stønadskonto.regelmodell.grunnlag.Dekningsgrad;
import no.nav.security.token.support.core.api.Unprotected;

@Validated
@Unprotected
@RestController
@RequestMapping(UttakController.UTTAK_PATH)
public class UttakController {

    static final String UTTAK_PATH = "/rest/konto";

    private static final String FMT = "yyyyMMdd";
    private static final StønadskontoRegelOrkestrering REGEL_ORKESTRERING = new StønadskontoRegelOrkestrering();

    @GetMapping
    @CrossOrigin(origins = "*", allowCredentials = "false")
    public KontoBeregning beregn(@RequestParam("antallBarn") @Digits(integer = 2, fraction = 0) int antallBarn,
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
                                 @RequestParam(value = "harAnnenForelderTilsvarendeRettEØS", required = false) boolean harAnnenForelderTilsvarendeRettEØS,
                                 @RequestParam(value = "familieHendelseDatoNesteSak", required = false) @DateTimeFormat(pattern = FMT) LocalDate familieHendelseDatoNesteSak) {
        guardFamiliehendelse(fødselsdato, termindato, omsorgsovertakelseDato);
        var dekningsgradOversatt = dekningsgrad(dekningsgrad);
        var grunnlag = new BeregnKontoerGrunnlag.Builder().antallBarn(antallBarn)
            .dekningsgrad(dekningsgradOversatt)
            .morAleneomsorg(morHarAleneomsorg)
            .farAleneomsorg(farHarAleneomsorg)
            .morRett(morHarRett)
            .farRett(farHarRett)
            .morHarRettEØS(!erMor && harAnnenForelderTilsvarendeRettEØS)
            .farHarRettEØS(erMor && harAnnenForelderTilsvarendeRettEØS)
            .fødselsdato(fødselsdato)
            .omsorgsovertakelseDato(omsorgsovertakelseDato)
            .termindato(termindato)
            .minsterett(minsterett)
            .build();
        var stønadskontoer = REGEL_ORKESTRERING.beregnKontoer(grunnlag).getStønadskontoer();
        var morHarRettEllerEØS = morHarRett || (!erMor && harAnnenForelderTilsvarendeRettEØS);
        var bareFarHarRett = farHarRett && !morHarRettEllerEØS;
        var aleneomsorg = (erMor && morHarAleneomsorg) || (!erMor && farHarAleneomsorg);
        var gjelderFødsel = omsorgsovertakelseDato == null;
        var minsterettGrunnlag = new BeregnMinsterettGrunnlag.Builder().antallBarn(antallBarn)
            .minsterett(minsterett)
            .mor(erMor)
            .bareFarHarRett(bareFarHarRett)
            .aleneomsorg(aleneomsorg)
            .morHarUføretrygd(morHarUføretrygd)
            .dekningsgrad(dekningsgradOversatt)
            .gjelderFødsel(gjelderFødsel)
            .familieHendelseDato(familiehendelse(fødselsdato, termindato, omsorgsovertakelseDato))
            .familieHendelseDatoNesteSak(familieHendelseDatoNesteSak)
            .build();
        var minsteretter = Minsterett.finnMinsterett(minsterettGrunnlag);
        return new KontoBeregning(stønadskontoer, Minsteretter.from(minsteretter));
    }


    private static LocalDate familiehendelse(LocalDate fødselsdato,
                                             LocalDate termindato,
                                             LocalDate omsorgsovertakelseDato) {
        if (omsorgsovertakelseDato != null) {
            return omsorgsovertakelseDato;
        }
        if (fødselsdato != null) {
            return fødselsdato;
        }
        return termindato;
    }

    private static void guardFamiliehendelse(LocalDate fødselsdato,
                                             LocalDate termindato,
                                             LocalDate omsorgsovertakelseDato) {
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
        return getClass().getSimpleName() + " [regelOrkestrering=" + REGEL_ORKESTRERING + "]";
    }
}
