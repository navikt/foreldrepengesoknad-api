package no.nav.foreldrepenger.selvbetjening.http;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Parameter;
import java.util.Set;

import jakarta.validation.Valid;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.selvbetjening.uttak.UttakController;
import no.nav.foreldrepenger.selvbetjening.uttak.UttakControllerV2;

class RestApiInputValideringAnnoteringTest extends RestApiTestUtil {
    private static final Set<Class<?>> UNNTATT_RESTCONTROLLER = Set.of(UttakController.class, UttakControllerV2.class);
    private static final Set<Class<?>> UNNTATT_PARAMETER_TYPE = Set.of(boolean.class, String.class);


    @Test
    void sjekkAtAlleEndepunktHarValidAnnoteringPåInputParametrene() {
        for (var method : finnAlleRestMetoder()) {
            for (var i = 0; i < method.getParameterCount(); i++) {
                if (UNNTATT_RESTCONTROLLER.contains(method.getDeclaringClass())) {
                    continue;
                }
                assertThat(harNødvendigValidAnnotering(method.getParameters()[i]))
                    .as("Alle parameter for REST-metoder skal være annotert med @Valid. Var ikke det for "
                        + printKlasseOgMetodeNavn.apply(method))
                    .withFailMessage("Fant parametere som mangler @Valid annotation '" + method.getParameters()[i].toString() + "'").isTrue();
            }
        }
    }

    private boolean harNødvendigValidAnnotering(Parameter parameter) {
        if (UNNTATT_PARAMETER_TYPE.contains(parameter.getType())) {
            return true;
        }
        return parameter.isAnnotationPresent(Valid.class);
    }
}
