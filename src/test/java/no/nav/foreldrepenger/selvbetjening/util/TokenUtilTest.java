package no.nav.foreldrepenger.selvbetjening.util;

import static no.nav.foreldrepenger.common.util.Constants.ISSUER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import no.nav.security.token.support.core.context.TokenValidationContext;
import no.nav.security.token.support.core.context.TokenValidationContextHolder;
import no.nav.security.token.support.core.exceptions.JwtTokenValidatorException;
import no.nav.security.token.support.core.jwt.JwtTokenClaims;

@ExtendWith(MockitoExtension.class)
class TokenUtilTest {

    private static final String FNR = "42";
    @Mock
    private TokenValidationContextHolder holder;
    @Mock
    private TokenValidationContext context;
    @Mock
    private JwtTokenClaims claims;

    private TokenUtil tokenHandler;

    @BeforeEach
    public void before() {
        lenient().when(holder.getTokenValidationContext()).thenReturn(context);
        lenient().when(context.getClaims(eq(ISSUER))).thenReturn(claims);
        tokenHandler = new TokenUtil(holder);
    }

    @Test
    void testTokenExpiry() {
        when(claims.get(eq("exp")))
                .thenReturn(
                        toDate(LocalDateTime.now().minusSeconds(3)).toInstant().getEpochSecond());
        assertTrue(tokenHandler.erUtløpt());
    }

    private static Date toDate(LocalDateTime date) {
        return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Test
    void testOK() {
        when(claims.get(eq("exp"))).thenReturn(toDate(LocalDateTime.now().minusHours(1)).toInstant().getEpochSecond());
        when(claims.getSubject()).thenReturn(FNR);
        assertEquals(FNR, tokenHandler.autentisertBruker());
        assertEquals(FNR, tokenHandler.getSubject());
        assertTrue(tokenHandler.erAutentisert());
        assertTrue(tokenHandler.erUtløpt());

    }

    @Test
    void testNoContext() {
        when(holder.getTokenValidationContext()).thenReturn(null);
        assertFalse(tokenHandler.erAutentisert());
        assertNull(tokenHandler.getSubject());
        assertNull(tokenHandler.getExpiryDate());
        assertThrows(JwtTokenValidatorException.class, () -> tokenHandler.autentisertBruker());
    }

    @Test
    void testNoClaims() {
        when(context.getClaims(eq(ISSUER))).thenReturn(null);
        assertFalse(tokenHandler.erAutentisert());
        assertNull(tokenHandler.getSubject());
        assertThrows(JwtTokenValidatorException.class, () -> tokenHandler.autentisertBruker());
    }

    @Test
    void testNoClaimset() {
        assertNull(tokenHandler.getSubject());
        assertFalse(tokenHandler.erAutentisert());
        assertThrows(JwtTokenValidatorException.class, () -> tokenHandler.autentisertBruker());
    }

    @Test
    void testNoSubject() {
        when(claims.getSubject()).thenReturn(null);
        assertNull(tokenHandler.getSubject());
        assertFalse(tokenHandler.erAutentisert());
        assertThrows(JwtTokenValidatorException.class, () -> tokenHandler.autentisertBruker());
    }
}
