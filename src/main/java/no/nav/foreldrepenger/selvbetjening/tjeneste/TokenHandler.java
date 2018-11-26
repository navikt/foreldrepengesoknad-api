package no.nav.foreldrepenger.selvbetjening.tjeneste;

import static no.nav.foreldrepenger.selvbetjening.util.Constants.ISSUER;

import java.util.Date;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.stereotype.Component;

import com.nimbusds.jwt.JWTClaimsSet;

import no.nav.foreldrepenger.selvbetjening.error.UnauthenticatedException;
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
                .map(JWTClaimsSet::getExpirationTime)
                .orElse(null);
    }

    public String getSubject() {
        return Optional.ofNullable(claimSet())
                .map(JWTClaimsSet::getSubject)
                .orElse(null);
    }

    public String autentisertBruker() {
        return Optional.ofNullable(getSubject())
                .orElseThrow(unauthenticated("Fant ikke subject"));
    }

    private static Supplier<? extends UnauthenticatedException> unauthenticated(String msg) {
        return () -> new UnauthenticatedException(msg);
    }

    private JWTClaimsSet claimSet() {
        return Optional.ofNullable(claims())
                .map(OIDCClaims::getClaimSet)
                .orElse(null);
    }

    private OIDCClaims claims() {
        return Optional.ofNullable(context())
                .map(s -> s.getClaims(ISSUER))
                .orElse(null);
    }

    private OIDCValidationContext context() {
        return Optional.ofNullable(ctxHolder.getOIDCValidationContext())
                .orElse(null);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [ctxHolder=" + ctxHolder + "]";
    }
}
