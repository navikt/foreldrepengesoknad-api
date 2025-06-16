package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class BarnDtoValideringTest {
    private Validator validator;

    @BeforeEach
    void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void terminDatoPastOrPresentValidationError() {
        var terminDto = new TerminDto(1, LocalDate.now(), LocalDate.now().plusDays(1));
        var validate = validator.validate(terminDto);
        assertThat(validate).hasSize(1);
    }

    @Test
    void dagensDatoSkalIkkeHiveValideringsfeil() {
        var terminDto = new TerminDto(1, LocalDate.now(), LocalDate.now());
        var validate = validator.validate(terminDto);
        assertThat(validate).hasSize(0);
    }
}
