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

        var ikrafttredelse1 = LocalDate.of(2024, 3, 1);
        var ikrafttredelse2 = LocalDate.of(2024, 3, 1);
        var uttakCore2024 = new UttakCore2024(ikrafttredelse1, ikrafttredelse2, environment);

        assertThat(uttakCore2024.getIkrafttredelseDato1())
            .isNotEqualTo(ikrafttredelse1)
            .isEqualTo(UttakCore2024.DEFAULT_IKRAFTTREDELSEDATO_1);
        assertThat(uttakCore2024.getIkrafttredelseDato2())
            .isNotEqualTo(ikrafttredelse2)
            .isEqualTo(UttakCore2024.DEFAULT_IKRAFTTREDELSEDATO_2);
    }

    @Test
    void dagensDatoErFørIkrafttredelsesdatoene() {
        var environment = new MockEnvironment();
        environment.setActiveProfiles("dev-gcp");
        var ikrafttredelse1 = LocalDate.now().plusMonths(2);
        var ikrafttredelse2 = LocalDate.now().plusMonths(4);
        var uttakcore2024 = new UttakCore2024(ikrafttredelse1, ikrafttredelse2, environment);
        environment.setActiveProfiles("prod-gcp");

        var familiehendelseFør = ikrafttredelse1.minusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseFør)).isNull();

        var familiehendelseMellom = ikrafttredelse1.plusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseMellom)).isEqualTo(LocalDate.now()); // Overstyr regelvalgdato

        var familiehendelseEtter = ikrafttredelse2.plusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseEtter)).isEqualTo(LocalDate.now()); // Overstyr regelvalgdato
    }

    @Test
    void dagensDatoEtterFørsteIkrafttredelsesdatoMenFørAndre() {
        var environment = new MockEnvironment();
        environment.setActiveProfiles("dev-gcp");
        var ikrafttredelse1 = LocalDate.now().minusMonths(1);
        var ikrafttredelse2 = LocalDate.now().plusMonths(1);
        var uttakcore2024 = new UttakCore2024(ikrafttredelse1, ikrafttredelse2, environment);
        environment.setActiveProfiles("prod-gcp");

        var familiehendelseFør = ikrafttredelse1.minusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseFør)).isNull();

        var familiehendelseMellom = ikrafttredelse1.plusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseMellom)).isEqualTo(LocalDate.now()); // Overstyr regelvalgdato

        var familiehendelseEtter = ikrafttredelse2.plusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseEtter)).isEqualTo(LocalDate.now()); // Overstyr regelvalgdato
    }

    @Test
    void dagensDatoEtterIkrafttredelsesdatoene() {
        var environment = new MockEnvironment();
        environment.setActiveProfiles("dev-gcp");
        var ikrafttredelse1 = LocalDate.now().minusMonths(4);
        var ikrafttredelse2 = LocalDate.now().minusMonths(2);
        var uttakcore2024 = new UttakCore2024(ikrafttredelse1, ikrafttredelse2, environment);
        environment.setActiveProfiles("prod-gcp");

        var familiehendelseFør = ikrafttredelse1.minusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseFør)).isNull();

        var familiehendelseMellom = ikrafttredelse1.plusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseMellom)).isNull();

        var familiehendelseEtter = ikrafttredelse2.plusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseEtter)).isNull();
    }



    @Test
    void skalOverskriveVerdierIdev() {
        var environment = new MockEnvironment();
        environment.setActiveProfiles("dev-gcp");

        var ikrafttredelse1 = LocalDate.of(2024, 3, 1);
        var ikrafttredelse2 = LocalDate.of(2024, 3, 1);
        var uttakCore2024 = new UttakCore2024(ikrafttredelse1, ikrafttredelse1, environment);

        assertThat(uttakCore2024.getIkrafttredelseDato1()).isEqualTo(ikrafttredelse1);
        assertThat(uttakCore2024.getIkrafttredelseDato2()).isEqualTo(ikrafttredelse2);
    }


    @Test
    void dagensDatoErFørIkrafttredelsesdatoeneDEV() {
        var environment = new MockEnvironment();
        environment.setActiveProfiles("dev-gcp");

        var ikrafttredelse1 = LocalDate.now().plusMonths(2);
        var ikrafttredelse2 = LocalDate.now().plusMonths(4);
        var uttakcore2024 = new UttakCore2024(ikrafttredelse1, ikrafttredelse2, environment);

        var familiehendelseFør = ikrafttredelse1.minusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseFør)).isNull();

        var familiehendelseMellom = ikrafttredelse1.plusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseMellom)).isEqualTo(UttakCore2024.DEFAULT_IKRAFTTREDELSEDATO_1); // Overstyr regelvalgdato

        var familiehendelseEtter = ikrafttredelse2.plusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseEtter)).isEqualTo(UttakCore2024.DEFAULT_IKRAFTTREDELSEDATO_2); // Overstyr regelvalgdato
    }

    @Test
    void dagensDatoEtterFørsteIkrafttredelsesdatoMenFørAndreDEV() {
        var environment = new MockEnvironment();
        environment.setActiveProfiles("dev-gcp");

        var ikrafttredelse1 = LocalDate.now().minusMonths(1);
        var ikrafttredelse2 = LocalDate.now().plusMonths(1);
        var uttakcore2024 = new UttakCore2024(ikrafttredelse1, ikrafttredelse2, environment);

        var familiehendelseFør = ikrafttredelse1.minusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseFør)).isNull();

        var familiehendelseMellom = ikrafttredelse1.plusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseMellom)).isEqualTo(UttakCore2024.DEFAULT_IKRAFTTREDELSEDATO_1); // Overstyr regelvalgdato

        var familiehendelseEtter = ikrafttredelse2.plusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseEtter)).isEqualTo(UttakCore2024.DEFAULT_IKRAFTTREDELSEDATO_2); // Overstyr regelvalgdato
    }

    @Test
    void dagensDatoEtterIkrafttredelsesdatoeneDEV() {
        var environment = new MockEnvironment();
        environment.setActiveProfiles("dev-gcp");

        var ikrafttredelse1 = LocalDate.now().minusMonths(4);
        var ikrafttredelse2 = LocalDate.now().minusMonths(2);
        var uttakcore2024 = new UttakCore2024(ikrafttredelse1, ikrafttredelse2, environment);

        var familiehendelseFør = ikrafttredelse1.minusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseFør)).isNull();

        var familiehendelseMellom = ikrafttredelse1.plusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseMellom)).isEqualTo(UttakCore2024.DEFAULT_IKRAFTTREDELSEDATO_1);

        var familiehendelseEtter = ikrafttredelse2.plusMonths(1);
        assertThat(uttakcore2024.utledRegelvalgsdato(familiehendelseEtter)).isEqualTo(UttakCore2024.DEFAULT_IKRAFTTREDELSEDATO_2);
    }
}
