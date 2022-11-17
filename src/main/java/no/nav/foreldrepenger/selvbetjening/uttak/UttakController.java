package no.nav.foreldrepenger.selvbetjening.uttak;

import static no.nav.foreldrepenger.selvbetjening.uttak.UttakControllerV2.beregnKonto;

import java.time.LocalDate;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.security.token.support.core.api.Unprotected;

@Validated
@Unprotected
@RestController
@RequestMapping(UttakController.UTTAK_PATH)
// TODO: Fjern denne etter frontend har endret til å gå mot rest/konto istedenfor /konto
public class UttakController {

    static final String UTTAK_PATH = "/konto";

    private static final String FMT = "yyyyMMdd";

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

        return beregnKonto(antallBarn, morHarRett, farHarRett, morHarAleneomsorg, farHarAleneomsorg, fødselsdato,
            termindato, omsorgsovertakelseDato, dekningsgrad, erMor, minsterett, morHarUføretrygd,
            harAnnenForelderTilsvarendeRettEØS, familieHendelseDatoNesteSak);
    }
}
