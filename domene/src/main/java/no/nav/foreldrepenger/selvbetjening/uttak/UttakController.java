package no.nav.foreldrepenger.selvbetjening.uttak;


import static no.nav.foreldrepenger.selvbetjening.uttak.KontoBeregningDtoMapper.tilKontoberegning;
import static no.nav.foreldrepenger.stønadskonto.regelmodell.grunnlag.Dekningsgrad.DEKNINGSGRAD_100;
import static no.nav.foreldrepenger.stønadskonto.regelmodell.grunnlag.Dekningsgrad.DEKNINGSGRAD_80;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.foreldrepenger.stønadskonto.regelmodell.StønadskontoRegelOrkestrering;
import no.nav.foreldrepenger.stønadskonto.regelmodell.grunnlag.BeregnKontoerGrunnlag;
import no.nav.foreldrepenger.stønadskonto.regelmodell.grunnlag.Dekningsgrad;
import no.nav.security.token.support.core.api.Unprotected;

@Validated
@Unprotected
@RestController
@RequestMapping(UttakController.UTTAK_PATH)
public class UttakController {

    static final String UTTAK_PATH = "/rest/konto";

    private static final StønadskontoRegelOrkestrering REGEL_ORKESTRERING = new StønadskontoRegelOrkestrering();

    private final UttakCore2024 uttakCore2024;

    public UttakController(UttakCore2024 uttakCore2024) {
        this.uttakCore2024 = uttakCore2024;
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
        return tilKontoberegning(stønadskontoer, grunnlag);
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
