package no.nav.foreldrepenger.selvbetjening.http;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static no.nav.foreldrepenger.common.util.TokenUtil.CLAIMS;
import static no.nav.foreldrepenger.common.util.TokenUtil.SELVBETJENING;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.security.token.support.core.api.ProtectedWithClaims;

@RestController
@Documented
@ProtectedWithClaims(issuer = SELVBETJENING)
@Target(TYPE)
@Retention(RUNTIME)
@RequestMapping
public @interface ProtectedRestController {
    @AliasFor(annotation = RequestMapping.class, attribute = "value")
    String[] value() default {};

    @AliasFor(annotation = ProtectedWithClaims.class, attribute = "claimMap")
    String[] claimMap() default CLAIMS;

}
