package no.nav.foreldrepenger.selvbetjening.http;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.nimbusds.jwt.util.DateUtils;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.util.AuthenticationLevel;
import no.nav.foreldrepenger.common.util.TimeUtil;
import no.nav.security.token.support.core.context.TokenValidationContext;
import no.nav.security.token.support.core.context.TokenValidationContextHolder;
import no.nav.security.token.support.core.exceptions.JwtTokenValidatorException;
import no.nav.security.token.support.core.jwt.JwtToken;
import no.nav.security.token.support.core.jwt.JwtTokenClaims;

@Configuration
public class TokenUtil {
    public static final String IDPORTEN = "idporten";
    public static final String CLAIMS = "acr=Level4";
    public static final String IDPORTENV2_CLAIMS = "acr=idporten-loa-high";
    public static final String BEARER = "Bearer ";
    public static final String NAV_AUTH_LEVEL = "Nav-auth-level";
    public static final String NAV_TOKEN_EXPIRY_ID = "Nav-Token-Expiry";

    private final TokenValidationContextHolder ctxHolder;
    private final List<String> issuers = List.of(IDPORTEN);

    @Autowired
    public TokenUtil(TokenValidationContextHolder ctxHolder) {
        this.ctxHolder = ctxHolder;
    }

    public Fødselsnummer autentisertBrukerOrElseThrowException() {
        return new Fødselsnummer(fødselsnummerFraToken());
    }

    public boolean erAutentisert() {
        return getSubject() != null;
    }

    public String getSubject() {
        return Optional.ofNullable(claimSet())
                .map(this::getSubjectFromPidOrSub)
                .orElse(null);
    }

    private String getSubjectFromPidOrSub(JwtTokenClaims claims) {
        return Optional.ofNullable(claims.getStringClaim("pid"))
                .orElseGet(claims::getSubject);
    }

    public String getJti() {
        return Optional.ofNullable(claimSet())
                .map(c -> c.getStringClaim("jti"))
                .orElse("");
    }

    public boolean erUtløpt() {
        return Optional.ofNullable(getExpiration())
                .filter(d -> d.isBefore(LocalDateTime.now()))
                .isPresent();
    }

    public LocalDateTime getExpiration() {
        return Optional.ofNullable(claimSet())
                .map(c -> c.get("exp"))
                .map(this::getDateClaim)
                .map(TimeUtil::fraDato)
                .orElse(null);
    }

    public AuthenticationLevel getLevel() {
        return Optional.ofNullable(claimSet())
                .map(c -> c.get("acr"))
                .map(String.class::cast)
                .map(AuthenticationLevel::of)
                .orElse(AuthenticationLevel.NONE);
    }

    private String fødselsnummerFraToken() {
        return Optional.ofNullable(getSubject())
            .orElseThrow(unauthenticated("Fant ikke subject, antagelig ikke autentisert"));
    }

    private static Supplier<? extends JwtTokenValidatorException> unauthenticated(String msg) {
        return () -> new JwtTokenValidatorException(msg);
    }

    public String getToken() {
        return issuers.stream()
                .map(this::getToken)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(unauthenticated("Fant ikke ID-token"));
    }

    private String getToken(String issuer) {
        return Optional.ofNullable(context())
                .map(c -> c.getJwtToken(issuer))
                .map(JwtToken::getTokenAsString)
                .orElse(null);
    }

    private JwtTokenClaims claimSet() {
        return issuers.stream()
                .map(this::claimSet)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private JwtTokenClaims claimSet(String issuer) {
        return Optional.ofNullable(context())
                .map(s -> s.getClaims(issuer))
                .orElse(null);
    }

    private TokenValidationContext context() {
        return ctxHolder.getTokenValidationContext();
    }

    private Date getDateClaim(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Date d) {
            return d;
        }
        if (value instanceof Number n) {
            return DateUtils.fromSecondsSinceEpoch(n.longValue());
        }
        return null;
    }

    @Override
    public String toString() {
        return "TokenUtil{" + "issuers=" + issuers + '}';
    }
}
