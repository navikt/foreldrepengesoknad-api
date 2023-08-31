package no.nav.foreldrepenger.selvbetjening.config;

import static no.nav.foreldrepenger.common.util.TokenUtil.IDPORTEN;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import no.nav.foreldrepenger.common.util.TokenUtil;
import no.nav.security.token.support.core.context.TokenValidationContextHolder;

@Configuration
public class TokenUtilConfiguration {

    @Bean
    public TokenUtil tokenUtil(TokenValidationContextHolder contextHolder) {

        return new TokenUtil(contextHolder, IDPORTEN);
    }
}
