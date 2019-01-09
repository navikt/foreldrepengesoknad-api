package no.nav.foreldrepenger.selvbetjening.util;

import com.nimbusds.jwt.JWTClaimsSet;
import no.nav.security.oidc.context.OIDCClaims;
import no.nav.security.oidc.context.OIDCRequestContextHolder;
import no.nav.security.oidc.context.OIDCValidationContext;
import no.nav.security.oidc.context.TokenContext;
import no.nav.security.oidc.exceptions.OIDCTokenValidatorException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static no.nav.foreldrepenger.selvbetjening.util.Constants.ISSUER;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TokenHelperTest {

    private static final String FNR = "42";
    @Mock
    private OIDCRequestContextHolder holder;
    @Mock
    private OIDCValidationContext context;
    @Mock
    private OIDCClaims claims;
    @Mock
    private TokenContext tokenContext;

    private TokenHelper tokenHandler;

    @Before
    public void before() {
        when(holder.getOIDCValidationContext()).thenReturn(context);
        when(context.getClaims(eq(ISSUER))).thenReturn(claims);
        tokenHandler = new TokenHelper(holder);
    }

    @Test
    public void testTokenExpiry() {
        when(claims.getClaimSet()).thenReturn(new JWTClaimsSet.Builder()
                .subject(FNR)
                .expirationTime(toDate(LocalDateTime.now().plusHours(1)))
                .build());
        assertNotNull(tokenHandler.getExp());

    }

    private static Date toDate(LocalDateTime date) {
        return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Test
    public void testOK() throws OIDCTokenValidatorException {
        when(claims.getClaimSet()).thenReturn(new JWTClaimsSet.Builder().subject(FNR).build());
        assertEquals(FNR, tokenHandler.autentisertBruker());
        assertEquals(FNR, tokenHandler.getSubject());
        assertTrue(tokenHandler.erAutentisert());
    }

    @Test(expected = OIDCTokenValidatorException.class)
    public void testNoContext() throws OIDCTokenValidatorException {
        when(holder.getOIDCValidationContext()).thenReturn(null);
        assertFalse(tokenHandler.erAutentisert());
        assertNull(tokenHandler.getSubject());
        assertNull(tokenHandler.getExp());
        tokenHandler.autentisertBruker();
    }

    @Test(expected = OIDCTokenValidatorException.class)
    public void testNoClaims() throws OIDCTokenValidatorException {
        when(context.getClaims(eq(ISSUER))).thenReturn(null);
        assertFalse(tokenHandler.erAutentisert());
        assertNull(tokenHandler.getSubject());
        tokenHandler.autentisertBruker();
    }

    @Test(expected = OIDCTokenValidatorException.class)
    public void testNoClaimset() throws OIDCTokenValidatorException {
        assertNull(tokenHandler.getSubject());
        assertFalse(tokenHandler.erAutentisert());
        tokenHandler.autentisertBruker();
    }

    @Test(expected = OIDCTokenValidatorException.class)
    public void testNoSubject() throws OIDCTokenValidatorException {
        when(claims.getClaimSet()).thenReturn(new JWTClaimsSet.Builder().build());
        assertNull(tokenHandler.getSubject());
        assertFalse(tokenHandler.erAutentisert());
        tokenHandler.autentisertBruker();
    }
}
