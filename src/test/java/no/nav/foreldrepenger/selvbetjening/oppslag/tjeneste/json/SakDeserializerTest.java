package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class SakDeserializerTest {

    @Test
    public void fromJson() {
        String json = "[{ \"sakId\": \"theid\", \"sakstype\": \"sakstypen\", \"fagomrade\": \"fagomr\", \"fagsystem\": \"systemet\", \"fagsystemSakId\": \"fsid\", \"opprettet\": \"2018-09-19\"}]";
        SakDeserializer deserializer = new SakDeserializer(new ObjectMapper());
        List<Sak> saker = deserializer.from(json);
        assertEquals(1, saker.size());
        assertFieldsContainTheRightValues(saker.get(0));
    }

    private void assertFieldsContainTheRightValues(Sak sak) {
        assertEquals("theid", sak.getSaksnummer());
        assertEquals("fagomr/sakstypen", sak.getBehandlingstema());
        assertEquals("ukjent", sak.getStatus());
        assertEquals(0, sak.getBehandlinger().size());
        assertEquals(0, sak.getAktørIdBarn().size());
        assertNull(sak.getAktørIdAnnenPart());
    }

}
