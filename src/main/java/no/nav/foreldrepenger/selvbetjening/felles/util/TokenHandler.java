package no.nav.foreldrepenger.selvbetjening.felles.util;

import static no.nav.foreldrepenger.selvbetjening.felles.Constants.ISSUER;
import static no.nav.security.oidc.OIDCConstants.OIDC_VALIDATION_CONTEXT;

import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.nimbusds.jwt.JWTClaimsSet;

import no.nav.foreldrepenger.selvbetjening.felles.error.ForbiddenException;
import no.nav.security.oidc.context.OIDCClaims;
import no.nav.security.oidc.context.OIDCRequestContextHolder;
import no.nav.security.oidc.context.OIDCValidationContext;

@Component
public class TokenHandler {

    private final OIDCRequestContextHolder ctxHolder;

    public TokenHandler(OIDCRequestContextHolder ctxHolder) {
        this.ctxHolder = ctxHolder;
    }

    public boolean erAutentisert() {
        return getSubject() != null;
    }

    public Date getExp() {
        return Optional.ofNullable(claimSet())
                .map(s -> s.getExpirationTime())
                .orElse(null);
    }

    public String getSubject() {
        return Optional.ofNullable(claimSet())
                .map(s -> s.getSubject())
                .orElse(null);
    }

    public String autentisertBruker() {
        return Optional.ofNullable(getSubject())
                .orElseThrow(() -> new ForbiddenException("Fant ikke subject"));
    }

    private JWTClaimsSet claimSet() {
        return Optional.ofNullable(claims())
                .map(s -> s.getClaimSet())
                .orElse(null);
    }

    private OIDCClaims claims() {
        return Optional.ofNullable(context())
                .map(s -> s.getClaims(ISSUER))
                .orElse(null);
    }

    private OIDCValidationContext context() {
        return Optional.ofNullable(ctxHolder.getRequestAttribute(OIDC_VALIDATION_CONTEXT))
                .map(s -> OIDCValidationContext.class.cast(s))
                .orElse(null);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [ctxHolder=" + ctxHolder + "]";
    }
}
