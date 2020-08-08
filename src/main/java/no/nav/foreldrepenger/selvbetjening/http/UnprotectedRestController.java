package no.nav.foreldrepenger.selvbetjening.http;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.boot.conditionals.ConditionalOnNotProd;
import no.nav.security.token.support.core.api.Unprotected;

@RestController
@RequestMapping
@Unprotected
@Documented
@ConditionalOnNotProd
@Target(TYPE)
@Retention(RUNTIME)
public @interface UnprotectedRestController {
    @AliasFor(annotation = RequestMapping.class, attribute = "value")
    String[] value() default {};

}
