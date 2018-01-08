package no.nav.foreldrepenger.selvbetjening.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

@RunWith(MockitoJUnitRunner.class)
public class StatusControllerTest {

    @InjectMocks
    private StatusController statusController;

    @Test
    public void isAlive() {
        ResponseEntity<String> response = statusController.isAlive();

        assertThat(response.getStatusCode()).isEqualByComparingTo(OK);
        assertThat(response.getBody()).isEqualTo("OK");
    }

}
