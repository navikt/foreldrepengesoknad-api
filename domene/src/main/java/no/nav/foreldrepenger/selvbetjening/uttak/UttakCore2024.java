package no.nav.foreldrepenger.selvbetjening.uttak;


import static no.nav.boot.conditionals.EnvUtil.isProd;

import java.time.LocalDate;
import java.time.Month;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class UttakCore2024 {

    private static LocalDate DEFAULT_IKRAFTTREDELSEDATO_1 = LocalDate.of(3024, Month.JULY,1); // LA STÅ etter endring til 2024
    private static LocalDate DEFAULT_IKRAFTTREDELSEDATO_2 = LocalDate.of(3024, Month.AUGUST,2); // LA STÅ.

    private LocalDate ikrafttredelseDato1;
    private LocalDate ikrafttredelseDato2;

    public UttakCore2024(@Value("${dato.for.aatti.prosent:#{null}}") LocalDate overstyrtIkrafttredelsedato1,
                         @Value("${dato.for.minsterett.andre:#{null}}") LocalDate overstyrtIkrafttredelsedato2,
                         Environment env) {
        ikrafttredelseDato1 = isProd(env) || overstyrtIkrafttredelsedato1 == null ? DEFAULT_IKRAFTTREDELSEDATO_1 : overstyrtIkrafttredelsedato1;
        ikrafttredelseDato2 = isProd(env) || overstyrtIkrafttredelsedato2 == null ? DEFAULT_IKRAFTTREDELSEDATO_2 : overstyrtIkrafttredelsedato2;
    }

    public LocalDate getIkrafttredelseDato1() {
        return ikrafttredelseDato1;
    }

    public LocalDate getIkrafttredelseDato2() {
        return ikrafttredelseDato2;
    }

    public LocalDate utledRegelvalgsdato(LocalDate familieHendelseDato) {
        if (familieHendelseDato == null || familieHendelseDato.isBefore(ikrafttredelseDato1)) return null;
        return LocalDate.now().isBefore(ikrafttredelseDato2) ? LocalDate.now() : null;
    }
}

