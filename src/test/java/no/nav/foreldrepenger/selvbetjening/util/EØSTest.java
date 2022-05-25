package no.nav.foreldrepenger.selvbetjening.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.neovisionaries.i18n.CountryCode;

class EØSTest {

    @Test
    void testNorge() {
        assertFalse(IkkeNordiskEØSLand.ikkeNordiskEøsLand(CountryCode.NO));
    }

    @Test
    void testUK() {
        assertTrue(IkkeNordiskEØSLand.ikkeNordiskEøsLand(CountryCode.UK));
    }

    @Test
    void testNull() {
        assertFalse(IkkeNordiskEØSLand.ikkeNordiskEøsLand(null));
    }

}
