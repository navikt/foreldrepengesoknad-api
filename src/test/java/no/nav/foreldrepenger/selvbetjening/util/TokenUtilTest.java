package no.nav.foreldrepenger.selvbetjening.util;

import static no.nav.foreldrepenger.selvbetjening.util.Constants.ISSUER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.nimbusds.jwt.JWTClaimsSet;

import no.nav.security.oidc.context.OIDCClaims;
import no.nav.security.oidc.context.OIDCRequestContextHolder;
import no.nav.security.oidc.context.OIDCValidationContext;
import no.nav.security.oidc.context.TokenContext;
import no.nav.security.oidc.exceptions.OIDCTokenValidatorException;

@ExtendWith(MockitoExtension.class)
public class TokenUtilTest {

    private static final String FNR = "42";
    @Mock
    private OIDCRequestContextHolder holder;
    @Mock
    private OIDCValidationContext context;
    @Mock
    private OIDCClaims claims;
    @Mock
    private TokenContext tokenContext;

    private TokenUtil tokenHandler;

    @BeforeEach
    public void before() {
        lenient().when(holder.getOIDCValidationContext()).thenReturn(context);
        lenient().when(context.getClaims(eq(ISSUER))).thenReturn(claims);
        tokenHandler = new TokenUtil(holder);
    }

    @Test
    public void testTokenExpiry() {
        when(claims.getClaimSet()).thenReturn(new JWTClaimsSet.Builder()
                .subject(FNR)
                .expirationTime(toDate(LocalDateTime.now().plusHours(1)))
                .build());
        assertNotNull(tokenHandler.getExpiryDate());

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

    @Test
    public void testNoContext() throws OIDCTokenValidatorException {
        when(holder.getOIDCValidationContext()).thenReturn(null);
        assertFalse(tokenHandler.erAutentisert());
        assertNull(tokenHandler.getSubject());
        assertNull(tokenHandler.getExpiryDate());
        assertThrows(OIDCTokenValidatorException.class, () -> tokenHandler.autentisertBruker());
    }

    @Test
    public void testNoClaims() throws OIDCTokenValidatorException {
        when(context.getClaims(eq(ISSUER))).thenReturn(null);
        assertFalse(tokenHandler.erAutentisert());
        assertNull(tokenHandler.getSubject());
        assertThrows(OIDCTokenValidatorException.class, () -> tokenHandler.autentisertBruker());
    }

    @Test
    public void testNoClaimset() throws OIDCTokenValidatorException {
        assertNull(tokenHandler.getSubject());
        assertFalse(tokenHandler.erAutentisert());
        assertThrows(OIDCTokenValidatorException.class, () -> tokenHandler.autentisertBruker());
    }

    @Test
    public void testNoSubject() throws OIDCTokenValidatorException {
        when(claims.getClaimSet()).thenReturn(new JWTClaimsSet.Builder().build());
        assertNull(tokenHandler.getSubject());
        assertFalse(tokenHandler.erAutentisert());
        assertThrows(OIDCTokenValidatorException.class, () -> tokenHandler.autentisertBruker());
    }
}
