package no.nav.foreldrepenger.selvbetjening.innsyn;

import static java.time.LocalDate.now;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;

import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.selvbetjening.innsyn.saker.Sak;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class InnsynConnectionTest {

    @Mock
    RestOperations rest;

    private static final InnsynConfig CFG = new InnsynConfig(URI.create("http://www.innsyn.no"),
            URI.create("http://www.oppslag.no"), true);

    InnsynConnection connection;

    @BeforeEach
    public void beforeEach() {
        connection = new InnsynConnection(rest, CFG);
    }

    @Test
    public void testUthentingAvSakerMedKunSakerFraFpsak() {
        lenient().when(rest.getForObject(any(), eq(Sak[].class))).thenReturn(sakerFraFpsak());
        List<Sak> saker = connection.hentSaker();
        assertThat(saker).hasSize(1);
        Sak sak = saker.get(0);
        assertThat(sak.getType()).isEqualTo("FPSAK");
    }

    @Test
    public void testUthentingAvSakerMedKunSakerFraInfotrygd() {
        lenient().when(rest.getForObject(any(), eq(Sak[].class))).thenReturn(new Sak[0])
                .thenReturn(sakerFraInfotrygd());
        List<Sak> saker = connection.hentSaker();
        assertThat(saker).hasSize(1);
        Sak sak = saker.get(0);
        assertThat(sak.getType()).isEqualTo("SAK");
    }

    @Test
    public void testUthentingAvSakerMedSakerFraFpsakOgInfotrygd() {
        lenient().when(rest.getForObject(any(), eq(Sak[].class))).thenReturn(sakerFraFpsak())
                .thenReturn(sakerFraInfotrygd());
        List<Sak> saker = connection.hentSaker();
        assertThat(saker).hasSize(1);
        Sak sak = saker.get(0);
        assertThat(sak.getType()).isEqualTo("FPSAK");
    }

    private static Sak[] sakerFraFpsak() {
        return new Sak[] { new Sak("FPSAK", "1", "UBEH", now().minusMonths(1), null, null, "FORP", emptyList()) };
    }

    private static Sak[] sakerFraInfotrygd() {
        return new Sak[] { new Sak("SAK", "9", null, now().minusMonths(2), "LA8PV", null, "FORP", emptyList()) };
    }

}
