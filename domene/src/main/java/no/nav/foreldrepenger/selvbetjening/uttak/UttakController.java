package no.nav.foreldrepenger.selvbetjening.uttak;


import static no.nav.foreldrepenger.selvbetjening.uttak.KontoBeregningDtoMapper.tilKontoberegning;
import static no.nav.foreldrepenger.stønadskonto.regelmodell.grunnlag.Dekningsgrad.DEKNINGSGRAD_100;
import static no.nav.foreldrepenger.stønadskonto.regelmodell.grunnlag.Dekningsgrad.DEKNINGSGRAD_80;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import no.nav.foreldrepenger.stønadskonto.regelmodell.StønadskontoRegelOrkestrering;
import no.nav.foreldrepenger.stønadskonto.regelmodell.grunnlag.BeregnKontoerGrunnlag;
import no.nav.foreldrepenger.stønadskonto.regelmodell.grunnlag.Brukerrolle;
import no.nav.foreldrepenger.stønadskonto.regelmodell.grunnlag.Dekningsgrad;
import no.nav.foreldrepenger.stønadskonto.regelmodell.grunnlag.Rettighetstype;
import no.nav.security.token.support.core.api.Unprotected;

@Validated
@Unprotected
@RestController
@RequestMapping(UttakController.UTTAK_PATH)
public class UttakController {

    static final String UTTAK_PATH = "/rest/konto";

    private static final String FMT = "yyyyMMdd";
    private static final StønadskontoRegelOrkestrering REGEL_ORKESTRERING = new StønadskontoRegelOrkestrering();

    private final UttakCore2024 uttakCore2024;

    public UttakController(UttakCore2024 uttakCore2024) {
        this.uttakCore2024 = uttakCore2024;
    }


    @Deprecated
    @GetMapping
    @CrossOrigin(origins = "*", allowCredentials = "false")
    public KontoBeregning beregnMedDekningsgrad(@RequestParam("antallBarn") @Digits(integer = 2, fraction = 0) int antallBarn,
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
        var brukerrolle = tilBrukerrolle(erMor);
        var familiehendelsedato = familiehendelsedato(fødselsdato, termindato, omsorgsovertakelseDato);
        var grunnlag = new BeregnKontoerGrunnlag.Builder()
            .regelvalgsdato(uttakCore2024.utledRegelvalgsdato(familiehendelsedato))
            .dekningsgrad(dekningsgradOversatt)
            .rettighetType(tilRettighetstype(brukerrolle, morHarRett, farHarRett, harAnnenForelderTilsvarendeRettEØS, morHarAleneomsorg, farHarAleneomsorg))
            .brukerRolle(brukerrolle)
            .antallBarn(antallBarn)
            .fødselsdato(fødselsdato)
            .termindato(termindato)
            .omsorgsovertakelseDato(omsorgsovertakelseDato)
            .morHarUføretrygd(morHarUføretrygd)
            .familieHendelseDatoNesteSak(familieHendelseDatoNesteSak)
            .build();
        var stønadskontoer = REGEL_ORKESTRERING.beregnKontoer(grunnlag).getStønadskontoer();
        return KontoBeregning.fra(stønadskontoer, brukerrolle);
    }

    private Rettighetstype tilRettighetstype(Brukerrolle brukerrolle,
                                             boolean morHarRett,
                                             boolean farHarRett,
                                             boolean harAnnenForelderTilsvarendeRettEØS,
                                             boolean morHarAleneomsorg,
                                             boolean farHarAleneomsorg) {
        if (brukerrolle.equals(Brukerrolle.MOR)) {
            if (morHarRett && (farHarRett || harAnnenForelderTilsvarendeRettEØS)) {
                return Rettighetstype.BEGGE_RETT;
            } else if (morHarAleneomsorg) {
                return Rettighetstype.ALENEOMSORG;
            } else {
                return Rettighetstype.BARE_SØKER_RETT;
            }
        } else {
            if (farHarRett && (morHarRett || harAnnenForelderTilsvarendeRettEØS)) {
                return Rettighetstype.BEGGE_RETT;
            } else if (farHarAleneomsorg) {
                return Rettighetstype.ALENEOMSORG;
            } else {
                return Rettighetstype.BARE_SØKER_RETT;
            }
        }
    }

    private Brukerrolle tilBrukerrolle(boolean erMor) {
        if (erMor) {
            return Brukerrolle.MOR;
        } else {
            return Brukerrolle.FAR;
        }
    }

    private static void guardFamiliehendelse(LocalDate fødselsdato, LocalDate termindato, LocalDate omsorgsovertakelseDato) {
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


    @PostMapping
    @CrossOrigin(origins = "*", allowCredentials = "false")
    public Map<String, KontoBeregningDto> beregn(@Valid @NotNull @RequestBody KontoBeregningGrunnlagDto grunnlag) {
        guardFamiliehendelse(grunnlag);
        var kontoberegning80 = kontoberegningFra(grunnlag, DEKNINGSGRAD_80);
        var kontoberegning100 = kontoberegningFra(grunnlag, DEKNINGSGRAD_100);
        return Map.of(
            "80", kontoberegning80,
            "100", kontoberegning100
        );
    }

    private KontoBeregningDto kontoberegningFra(KontoBeregningGrunnlagDto grunnlag, Dekningsgrad dekningsgrad) {
        var stønadskontoer = REGEL_ORKESTRERING.beregnKontoer(tilBeregnKontoGrunnlag(grunnlag, dekningsgrad)).getStønadskontoer();
        return tilKontoberegning(stønadskontoer, grunnlag.brukerrolle());
    }

    private BeregnKontoerGrunnlag tilBeregnKontoGrunnlag(KontoBeregningGrunnlagDto grunnlag, Dekningsgrad dekningsgrad) {
        var familiehendelsedato = familiehendelsedato(grunnlag.fødselsdato(), grunnlag.termindato(), grunnlag.omsorgsovertakelseDato());
        return new BeregnKontoerGrunnlag.Builder()
            .regelvalgsdato(uttakCore2024.utledRegelvalgsdato(familiehendelsedato))
            .dekningsgrad(dekningsgrad)
            .rettighetType(grunnlag.rettighetstype())
            .brukerRolle(grunnlag.brukerrolle())
            .antallBarn(grunnlag.antallBarn())
            .fødselsdato(grunnlag.fødselsdato())
            .termindato(grunnlag.termindato())
            .omsorgsovertakelseDato(grunnlag.omsorgsovertakelseDato())
            .morHarUføretrygd(grunnlag.morHarUføretrygd())
            .familieHendelseDatoNesteSak(grunnlag.familieHendelseDatoNesteSak())
            .build();
    }


    private static LocalDate familiehendelsedato(LocalDate fødselsdato, LocalDate termindato, LocalDate omsorgsovertakelseDato) {
        if (omsorgsovertakelseDato != null) return omsorgsovertakelseDato;
        if (fødselsdato != null) return fødselsdato;
        return termindato;
    }

    private static void guardFamiliehendelse(KontoBeregningGrunnlagDto grunnlag) {
        if (grunnlag.fødselsdato() == null && grunnlag.termindato() == null && grunnlag.omsorgsovertakelseDato() == null) {
            throw new ManglendeFamiliehendelseException("Mangler dato for familiehendelse");
        }
    }

}
