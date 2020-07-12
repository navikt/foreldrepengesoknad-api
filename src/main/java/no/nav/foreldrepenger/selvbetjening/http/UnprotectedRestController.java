package no.nav.foreldrepenger.selvbetjening.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.boot.conditionals.ConditionalOnNotProd;
import no.nav.security.token.support.core.api.Unprotected;

@RestController
@Unprotected
@ConditionalOnNotProd
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

public @interface UnprotectedRestController {

}
