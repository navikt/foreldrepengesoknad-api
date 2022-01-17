package no.nav.foreldrepenger.selvbetjening.uttak;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class UttakControllerTest {

    @Test
    void skal_kaste_exception_hvis_manglende_familiehendelse() {
        var controller = new UttakController();
        LocalDate fødselsdato = null;
        LocalDate termindato = null;
        LocalDate omsorgsovertakelseDato = null;

        assertThrows(ManglendeFamiliehendelseException.class,
            () -> controller.kontoer(1, true, true, false, false, fødselsdato, termindato, omsorgsovertakelseDato, "80"));
    }

}
