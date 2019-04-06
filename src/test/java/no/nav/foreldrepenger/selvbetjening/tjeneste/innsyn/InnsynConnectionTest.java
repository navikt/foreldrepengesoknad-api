package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import static java.time.LocalDate.now;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.saker.Sak;

@RunWith(MockitoJUnitRunner.class)
public class InnsynConnectionTest {

    @Mock
    RestOperations rest;

    @Mock
    InnsynConfig config;

    @InjectMocks
    InnsynConnection connection;

    @Test
    public void testUthentingAvSakerMedKunSakerFraFpsak() {
        when(rest.getForObject(any(), eq(Sak[].class))).thenReturn(sakerFraFpsak());
        List<Sak> saker = connection.hentSaker();
        assertThat(saker).hasSize(1);
        Sak sak = saker.get(0);
        assertThat(sak.getType()).isEqualTo("FPSAK");
    }

    @Test
    public void testUthentingAvSakerMedKunSakerFraInfotrygd() {
        when(rest.getForObject(any(), eq(Sak[].class))).thenReturn(new Sak[0]).thenReturn(sakerFraInfotrygd());
        List<Sak> saker = connection.hentSaker();
        assertThat(saker).hasSize(1);
        Sak sak = saker.get(0);
        assertThat(sak.getType()).isEqualTo("SAK");
    }

    @Test
    public void testUthentingAvSakerMedSakerFraFpsakOgInfotrygd() {
        when(rest.getForObject(any(), eq(Sak[].class))).thenReturn(sakerFraFpsak()).thenReturn(sakerFraInfotrygd());
        List<Sak> saker = connection.hentSaker();
        assertThat(saker).hasSize(1);
        Sak sak = saker.get(0);
        assertThat(sak.getType()).isEqualTo("FPSAK");
    }

    private Sak[] sakerFraFpsak() {
        return new Sak[] { new Sak("FPSAK", "1", "UBEH", now().minusMonths(1), null, null, emptyList()) };
    }

    private Sak[] sakerFraInfotrygd() {
        return new Sak[] { new Sak("SAK", "9", null, now().minusMonths(2), "LA8PV", null, emptyList()) };
    }

}
