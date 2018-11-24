package no.nav.foreldrepenger.selvbetjening.felles.util;

import static no.nav.foreldrepenger.selvbetjening.felles.Constants.ISSUER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.nimbusds.jwt.JWTClaimsSet;

import no.nav.foreldrepenger.selvbetjening.felles.error.UnauthorizedException;
import no.nav.foreldrepenger.selvbetjening.tjeneste.TokenHandler;
import no.nav.security.oidc.context.OIDCClaims;
import no.nav.security.oidc.context.OIDCRequestContextHolder;
import no.nav.security.oidc.context.OIDCValidationContext;
import no.nav.security.oidc.context.TokenContext;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TokenHandlerTest {

    private static final String FNR = "42";
    @Mock
    private OIDCRequestContextHolder holder;
    @Mock
    private OIDCValidationContext context;
    @Mock
    private OIDCClaims claims;
    @Mock
    private TokenContext tokenContext;

    private TokenHandler tokenHandler;

    @Before
    public void before() {
        when(holder.getOIDCValidationContext()).thenReturn(context);
        when(context.getClaims(eq(ISSUER))).thenReturn(claims);
        tokenHandler = new TokenHandler(holder);
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
    public void testOK() {
        when(claims.getClaimSet()).thenReturn(new JWTClaimsSet.Builder().subject(FNR).build());
        assertEquals(FNR, tokenHandler.autentisertBruker());
        assertEquals(FNR, tokenHandler.getSubject());
        assertTrue(tokenHandler.erAutentisert());
    }

    @Test(expected = UnauthorizedException.class)
    public void testNoContext() {
        when(holder.getOIDCValidationContext()).thenReturn(null);
        assertFalse(tokenHandler.erAutentisert());
        assertNull(tokenHandler.getSubject());
        assertNull(tokenHandler.getExp());
        tokenHandler.autentisertBruker();
    }

    @Test(expected = UnauthorizedException.class)
    public void testNoClaims() {
        when(context.getClaims(eq(ISSUER))).thenReturn(null);
        assertFalse(tokenHandler.erAutentisert());
        assertNull(tokenHandler.getSubject());
        tokenHandler.autentisertBruker();
    }

    @Test(expected = UnauthorizedException.class)
    public void testNoClaimset() {
        assertNull(tokenHandler.getSubject());
        assertFalse(tokenHandler.erAutentisert());
        tokenHandler.autentisertBruker();
    }

    @Test(expected = UnauthorizedException.class)
    public void testNoToken() {
        when(context.getToken(eq(ISSUER))).thenReturn(null);
        tokenHandler.getToken();
    }

    @Test(expected = UnauthorizedException.class)
    public void testNoSubject() {
        when(claims.getClaimSet()).thenReturn(new JWTClaimsSet.Builder().build());
        assertNull(tokenHandler.getSubject());
        assertFalse(tokenHandler.erAutentisert());
        tokenHandler.autentisertBruker();
    }
}
