package no.nav.foreldrepenger.selvbetjening.http;

import no.nav.security.token.support.core.api.ProtectedWithClaims;
import no.nav.security.token.support.core.api.RequiredIssuers;
import org.springframework.core.annotation.AliasFor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static no.nav.foreldrepenger.common.util.TokenUtil.CLAIMS;
import static no.nav.foreldrepenger.common.util.TokenUtil.SELVBETJENING;

@RestController
@Documented
@RequiredIssuers({
        @ProtectedWithClaims(issuer = SELVBETJENING, claimMap = CLAIMS),
        @ProtectedWithClaims(issuer = "idporten", claimMap = { CLAIMS, "acr=idporten-loa-high" })
})
@Validated
@Target(TYPE)
@Retention(RUNTIME)
@RequestMapping
public @interface ProtectedRestController {
    @AliasFor(annotation = RequestMapping.class, attribute = "value")
    String[] value() default {};

}
