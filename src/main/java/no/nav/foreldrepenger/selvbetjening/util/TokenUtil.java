package no.nav.foreldrepenger.selvbetjening.util;

import static java.time.Instant.now;
import static no.nav.foreldrepenger.selvbetjening.util.Constants.ISSUER;

import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Component;

import no.nav.security.token.support.core.context.TokenValidationContext;
import no.nav.security.token.support.core.context.TokenValidationContextHolder;
import no.nav.security.token.support.core.exceptions.JwtTokenValidatorException;
import no.nav.security.token.support.core.jwt.JwtTokenClaims;

@Component
public class TokenUtil {

    private final TokenValidationContextHolder ctxHolder;

    public TokenUtil(TokenValidationContextHolder ctxHolder) {
        this.ctxHolder = ctxHolder;
    }

    public boolean erUtløpt() {
        return Optional.ofNullable(getExpiryDate())
                .filter(d -> d.before(Date.from(now())))
                .isPresent();
    }

    public boolean erAutentisert() {
        return getSubject() != null;
    }

    public Date getExpiryDate() {
        return Optional.ofNullable(claimSet())
                .map(c -> c.get("exp"))
                .map(this::getDateClaim)
                .orElse(null);
    }

    public String getSubject() {
        return Optional.ofNullable(claimSet())
                .map(JwtTokenClaims::getSubject)
                .orElse(null);
    }

    public String autentisertBruker() {
        return Optional.ofNullable(getSubject())
                .orElseThrow(() -> new JwtTokenValidatorException("Fant ikke subject", getExpiryDate()));
    }

    private JwtTokenClaims claimSet() {
        return Optional.ofNullable(context())
                .map(s -> s.getClaims(ISSUER))
                .orElse(null);
    }

    private TokenValidationContext context() {
        return Optional.ofNullable(ctxHolder.getTokenValidationContext())
                .orElse(null);
    }

    public Date getDateClaim(Object value) {
        if (value instanceof Date d) {
            return d;
        }
        if (value instanceof Number n) {
            return new Date(n.longValue() * 1000L);
        }
        return null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [ctxHolder=" + ctxHolder + "]";
    }
}
