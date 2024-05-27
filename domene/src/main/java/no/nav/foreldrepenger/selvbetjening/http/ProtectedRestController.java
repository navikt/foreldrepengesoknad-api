package no.nav.foreldrepenger.selvbetjening.http;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static no.nav.foreldrepenger.selvbetjening.http.TokenUtil.CLAIMS;
import static no.nav.foreldrepenger.selvbetjening.http.TokenUtil.IDPORTEN;
import static no.nav.foreldrepenger.selvbetjening.http.TokenUtil.IDPORTENV2_CLAIMS;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.security.token.support.core.api.ProtectedWithClaims;
import no.nav.security.token.support.core.api.RequiredIssuers;

@RestController
@Documented
@RequiredIssuers(
        @ProtectedWithClaims(issuer = IDPORTEN, claimMap = { IDPORTENV2_CLAIMS, CLAIMS }, combineWithOr = true)
)
@Validated
@Target(TYPE)
@Retention(RUNTIME)
@RequestMapping
public @interface ProtectedRestController {
    @AliasFor(annotation = RequestMapping.class, attribute = "value")
    String[] value() default {};

}
