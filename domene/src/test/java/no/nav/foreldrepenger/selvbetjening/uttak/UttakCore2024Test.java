package no.nav.foreldrepenger.selvbetjening.uttak;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

class UttakCore2024Test {


    @Test
    void skalIkkeOverskriveVerdierIProduksjon() {
        var environment = new MockEnvironment();
        environment.setActiveProfiles("prod-gcp");

        var uttakCore2024 = new UttakCore2024(LocalDate.now(), LocalDate.now(), environment);
        assertThat(uttakCore2024.getIkrafttredelseDato1()).isNotEqualTo(LocalDate.now());
        assertThat(uttakCore2024.getIkrafttredelseDato2()).isNotEqualTo(LocalDate.now());
    }

    @Test
    void skalOverskriveVerdierIdev() {
        var environment = new MockEnvironment();
        environment.setActiveProfiles("dev-gcp");

        var uttakCore2024 = new UttakCore2024(LocalDate.now(), LocalDate.now(), environment);
        assertThat(uttakCore2024.getIkrafttredelseDato1()).isEqualTo(LocalDate.now());
        assertThat(uttakCore2024.getIkrafttredelseDato2()).isEqualTo(LocalDate.now());
    }


    @Test
    void dagensDatoErFørIkrafttredelsesdatoene() {
        var ikrafttredelse1 = LocalDate.now().plusMonths(2);
        var ikrafttredelse2 = LocalDate.now().plusMonths(4);
        var uttakcore2024 = new UttakCore2024(ikrafttredelse1, ikrafttredelse2, new MockEnvironment());

        var familiehendelseFør = ikrafttredelse1.minusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseFør)).isNull();

        var familiehendelseMellom = ikrafttredelse1.plusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseMellom)).isEqualTo(LocalDate.now()); // Overstyr regelvalgdato

        var familiehendelseEtter = ikrafttredelse2.plusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseEtter)).isEqualTo(LocalDate.now()); // Overstyr regelvalgdato
    }

    @Test
    void dagensDatoEtterFørsteIkrafttredelsesdatoMenFørAndre() {
        var ikrafttredelse1 = LocalDate.now().minusMonths(1);
        var ikrafttredelse2 = LocalDate.now().plusMonths(1);
        var uttakcore2024 = new UttakCore2024(ikrafttredelse1, ikrafttredelse2, new MockEnvironment());

        var familiehendelseFør = ikrafttredelse1.minusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseFør)).isNull();

        var familiehendelseMellom = ikrafttredelse1.plusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseMellom)).isEqualTo(LocalDate.now()); // Overstyr regelvalgdato

        var familiehendelseEtter = ikrafttredelse2.plusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseEtter)).isEqualTo(LocalDate.now()); // Overstyr regelvalgdato
    }

    @Test
    void dagensDatoEtterIkrafttredelsesdatoene() {
        var ikrafttredelse1 = LocalDate.now().minusMonths(4);
        var ikrafttredelse2 = LocalDate.now().minusMonths(2);
        var uttakcore2024 = new UttakCore2024(ikrafttredelse1, ikrafttredelse2, new MockEnvironment());

        var familiehendelseFør = ikrafttredelse1.minusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseFør)).isNull();

        var familiehendelseMellom = ikrafttredelse1.plusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseMellom)).isNull();

        var familiehendelseEtter = ikrafttredelse2.plusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseEtter)).isNull();
    }
}
