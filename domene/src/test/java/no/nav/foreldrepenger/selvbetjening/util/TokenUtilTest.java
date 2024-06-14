package no.nav.foreldrepenger.selvbetjening.util;

import static no.nav.foreldrepenger.selvbetjening.http.TokenUtil.TOKENX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.selvbetjening.http.TokenUtil;
import no.nav.security.token.support.core.context.TokenValidationContext;
import no.nav.security.token.support.core.context.TokenValidationContextHolder;
import no.nav.security.token.support.core.exceptions.JwtTokenValidatorException;
import no.nav.security.token.support.core.jwt.JwtTokenClaims;

class TokenUtilTest {

    private static final Fødselsnummer FNR = new Fødselsnummer("42");
    private static final String PID = "pid";

    private TokenValidationContextHolder holder;
    private TokenValidationContext context;
    private JwtTokenClaims claims;

    private TokenUtil tokenHelper;


    @BeforeEach
    void before() {
        holder = mock(TokenValidationContextHolder.class);
        context = mock(TokenValidationContext.class);
        claims = mock(JwtTokenClaims.class);

        when(holder.getTokenValidationContext()).thenReturn(context);
        when(context.getClaims(TOKENX)).thenReturn(claims);
        tokenHelper = new TokenUtil(holder);
    }

    @Test
    void testOK() {
        when(claims.get("exp")).thenReturn(toDate(LocalDateTime.now().minusHours(1)).toInstant().getEpochSecond());
        when(claims.getSubject()).thenReturn(FNR.value());
        assertEquals(FNR, tokenHelper.innloggetBrukerOrElseThrowException());
        assertTrue(tokenHelper.erInnloggetBruker());
    }

    @Test
    void testNoContext() {
        when(holder.getTokenValidationContext()).thenReturn(null);
        assertFalse(tokenHelper.erInnloggetBruker());
        assertThrows(JwtTokenValidatorException.class, () -> tokenHelper.innloggetBrukerOrElseThrowException());
    }

    @Test
    void testNoClaims() {
        when(context.getClaims(TOKENX)).thenReturn(null);
        assertFalse(tokenHelper.erInnloggetBruker());
        assertThrows(JwtTokenValidatorException.class, () -> tokenHelper.innloggetBrukerOrElseThrowException());
    }

    @Test
    void testNoClaimset() {
        when(context.getClaims(TOKENX)).thenReturn(null);
        assertFalse(tokenHelper.erInnloggetBruker());
        assertThrows(JwtTokenValidatorException.class, () -> tokenHelper.innloggetBrukerOrElseThrowException());
    }

    @Test
    void testNoSubject() {
        when(claims.getSubject()).thenReturn(null);
        when(claims.getStringClaim(PID)).thenReturn(null);
        assertFalse(tokenHelper.erInnloggetBruker());
        assertThrows(JwtTokenValidatorException.class, () -> tokenHelper.innloggetBrukerOrElseThrowException());
    }

    @Test
    void nårSubjectIkkeFinnesISubSåHentesDetFraPidTest() {
        when(claims.getSubject()).thenReturn(null);
        when(claims.getStringClaim(PID)).thenReturn(FNR.value());
        assertTrue(tokenHelper.erInnloggetBruker());
        assertEquals(FNR, tokenHelper.innloggetBrukerOrElseThrowException());
    }

    @Test
    void nårSubjectIkkeFinnesIPidSåHentesDetFraSubTest() {
        when(claims.getSubject()).thenReturn(FNR.value());
        when(claims.getStringClaim(PID)).thenReturn(null);
        assertTrue(tokenHelper.erInnloggetBruker());
        assertEquals(FNR, tokenHelper.innloggetBrukerOrElseThrowException());
    }

    private static Date toDate(LocalDateTime date) {
        return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    }
}
