package no.nav.foreldrepenger.selvbetjening.http;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import no.nav.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.selvbetjening.uttak.UttakController;
import no.nav.foreldrepenger.selvbetjening.uttak.UttakControllerV2;
import no.nav.security.token.support.core.api.ProtectedWithClaims;
import no.nav.security.token.support.core.api.Unprotected;

class RestApiSikredeEndepunktTest extends RestApiTestUtil {

    private static final List<Class<?>> UNNTATT_RESTCONTROLLER = List.of(UttakController.class, UttakControllerV2.class);
    private static final String ENDEPUNKT_SOM_KAN_VÆRE_UNPROTECTED = "ping";

    @Test
    void sjekkAtProtectedRestControllerIkkeHarUbeskyttetAnnotering() {
        assertThat(ProtectedRestController.class).hasAnnotations(RestController.class, ProtectedWithClaims.class, Validated.class);
        assertThat(ProtectedRestController.class.isAnnotationPresent(Unprotected.class))
            .as("Sørg for at @ProtectedRestController ikke er annotert med @Unprotected!")
            .isFalse();
    }

    @Test
    void sjekkAtAlleEndepunktErBeskyttet() {
        for (var metode : finnAlleRestMetoder()) {
            assertThat(erEndepunktUnprotected(metode))
                .as("Følgende endepunkt er ikke beskyttet " + printKlasseOgMetodeNavn.apply(metode))
                .isFalse();
        }
    }

    @Test
    void sjekkAtRestControllerErBeskyttet() {
        for (var klasse : hentAlleRestControllerKlasser()) {
            if (UNNTATT_RESTCONTROLLER.contains(klasse)) {
                continue;
            }
            assertThat(erRestControllerUbeskyttet(klasse))
                .as("RestController er annotert med @Unprotected eller mangler @ProtectedRestController og er ubeskyttet: " + klasse.getName())
                .isFalse();
        }
    }

    private boolean erRestControllerUbeskyttet(Class<?> klasse) {
        if (klasse.isAnnotationPresent(Unprotected.class) || !klasse.isAnnotationPresent(ProtectedRestController.class)) {
            if (klasse.isAnnotationPresent(ConditionalOnNotProd.class)) {
                // Endepunkt som ikke er eksponsert mot prod trenger ikke å være protected
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean erEndepunktUnprotected(Method metode) {
        if (metode.isAnnotationPresent(ConditionalOnNotProd.class) || metode.getName().equalsIgnoreCase(ENDEPUNKT_SOM_KAN_VÆRE_UNPROTECTED)) {
            return false;
        }
        return metode.isAnnotationPresent(Unprotected.class);
    }
}
