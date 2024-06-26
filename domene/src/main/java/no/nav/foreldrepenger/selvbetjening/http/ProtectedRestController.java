package no.nav.foreldrepenger.selvbetjening.http;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static no.nav.foreldrepenger.selvbetjening.http.TokenUtil.ACR_CLAIM_LEGACY;
import static no.nav.foreldrepenger.selvbetjening.http.TokenUtil.ARC_CLAIM;
import static no.nav.foreldrepenger.selvbetjening.http.TokenUtil.TOKENX;

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
@RequiredIssuers({@ProtectedWithClaims(issuer = TOKENX, claimMap = { ARC_CLAIM, ACR_CLAIM_LEGACY }, combineWithOr = true)})
@Validated
@Target(TYPE)
@Retention(RUNTIME)
@RequestMapping
public @interface ProtectedRestController {
    @AliasFor(annotation = RequestMapping.class, attribute = "value")
    String[] value() default {};

}
