package no.nav.foreldrepenger.selvbetjening.http;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.security.token.support.core.api.ProtectedWithClaims;

@RestController
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
@RequestMapping
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

public @interface ProtectedRestController {
    @AliasFor(annotation = RequestMapping.class)
    String[] path() default {};

    String[] value() default {};

    String[] produces() default { APPLICATION_JSON_VALUE };

}
