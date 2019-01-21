package no.nav.foreldrepenger.selvbetjening.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neovisionaries.i18n.CountryCode;

public class EØSTest {

    @Test
    public void testNorge() {
        assertFalse(IkkeNordiskEØSLand.ikkeNordiskEøsLand(CountryCode.NO));
    }

    @Test
    public void testUK() {
        assertTrue(IkkeNordiskEØSLand.ikkeNordiskEøsLand(CountryCode.UK));
    }

    @Test
    public void testNull() {
        assertFalse(IkkeNordiskEØSLand.ikkeNordiskEøsLand(null));
    }
}
