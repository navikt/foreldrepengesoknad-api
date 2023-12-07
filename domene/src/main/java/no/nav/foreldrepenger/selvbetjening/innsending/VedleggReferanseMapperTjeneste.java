package no.nav.foreldrepenger.selvbetjening.innsending;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.AnnenInntektDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.Innsending;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.engangsstønad.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.UttaksplanPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.SvangerskapspengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.TilretteleggingDto;

public class VedleggReferanseMapperTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(VedleggReferanseMapperTjeneste.class);

    private VedleggReferanseMapperTjeneste() {
        // Statisk implementasjon
    }

    public static void leggVedleggsreferanserTilSøknad(Innsending innsending) {
        if (innsending.vedlegg().isEmpty()) {
            return;
        }

        if (innsending.vedlegg().stream().allMatch(v -> v.getDokumenterer() == null)) {
            LOG.info("Mottok søknad med vedlegg på gammelt format. Forsetter uten endringer.");
            return;
        }

        LOG.info("Mottok søknad med vedlegg som inneholder dokumenterer-felt. Legger til vedleggsreferanser.");
        innsending.vedlegg().forEach(vedlegg -> vedlegg.setId(new MutableVedleggReferanseDto(UUID.randomUUID().toString())));
        if (innsending instanceof ForeldrepengesøknadDto foreldrepengesøknad) {
            leggVedleggsreferanserTilSøknad(foreldrepengesøknad);
        }
        if (innsending instanceof EngangsstønadDto es) {
            leggVedleggsreferanserTilSøknad(es);
        }
        if (innsending instanceof no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.engangsstønad.EngangsstønadDto es) {
            leggVedleggsreferanserTilSøknad(es);
        }
        if (innsending instanceof SvangerskapspengesøknadDto svp) {
            leggVedleggsreferanserTilSøknad(svp);
        }
    }

    private static void leggVedleggsreferanserTilSøknad(SvangerskapspengesøknadDto foreldrepenger) {
        for (var vedlegg : foreldrepenger.vedlegg()) {
            switch (vedlegg.getDokumenterer().type()) {
                case BARN -> leggVedleggTilBarn(foreldrepenger.barn(), vedlegg);
                case OPPTJENING -> leggVedleggTilAnnenInntekt(foreldrepenger.søker().andreInntekterSiste10Mnd(), vedlegg);
                case UTTAK -> throw new UnsupportedOperationException("Utviklerfeil: Svangerskapspenger har ikke uttak!");
                case TILRETTELEGGING -> leggVedleggTilTilrettelegging(foreldrepenger.tilrettelegging(), vedlegg);
            }
        }
    }



    private static void leggVedleggsreferanserTilSøknad(no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.engangsstønad.EngangsstønadDto foreldrepenger) {
        for (var vedlegg : foreldrepenger.vedlegg()) {
            switch (vedlegg.getDokumenterer().type()) {
                case BARN -> leggVedleggTilBarn(foreldrepenger.barn(), vedlegg);
                case OPPTJENING -> throw new UnsupportedOperationException("Utviklerfeil: Engangstønad har ikke annen inntektskilde!");
                case UTTAK -> throw new UnsupportedOperationException("Utviklerfeil: Engangstønad har ikke uttak!");
                case TILRETTELEGGING -> throw new UnsupportedOperationException("Utviklerfeil: Engangstønad har ikke tilrettelegging!");
            }
        }
    }

    private static void leggVedleggsreferanserTilSøknad(EngangsstønadDto foreldrepenger) {
        for (var vedlegg : foreldrepenger.vedlegg()) {
            switch (vedlegg.getDokumenterer().type()) {
                case BARN -> leggVedleggTilBarn(foreldrepenger.barn(), vedlegg);
                case OPPTJENING -> leggVedleggTilAnnenInntekt(foreldrepenger.søker().andreInntekterSiste10Mnd(), vedlegg);
                case UTTAK -> throw new UnsupportedOperationException("Utviklerfeil: Engangstønad har ikke uttak!");
                case TILRETTELEGGING -> throw new UnsupportedOperationException("Utviklerfeil: Engangstønad har ikke tilrettelegging!");
            }
        }
    }

    private static void leggVedleggsreferanserTilSøknad(ForeldrepengesøknadDto foreldrepenger) {
        for (var vedlegg : foreldrepenger.vedlegg()) {
            switch (vedlegg.getDokumenterer().type()) {
                case BARN -> leggVedleggTilBarn(foreldrepenger.barn(), vedlegg);
                case OPPTJENING -> leggVedleggTilAnnenInntekt(foreldrepenger.søker().andreInntekterSiste10Mnd(), vedlegg);
                case UTTAK -> leggVedleggTilUttak(foreldrepenger.uttaksplan(), vedlegg);
                case TILRETTELEGGING -> throw new UnsupportedOperationException("Utviklerfeil: Foreldrepenger har ikke tilrettelegging!");

            }
        }
    }

    private static void leggVedleggTilTilrettelegging(List<TilretteleggingDto> tilrettelegging, VedleggDto vedlegg) {
        var gjeldendeTilrettelegging = tilrettelegging.stream()
            .filter(t -> t.arbeidsforhold().equals(vedlegg.getDokumenterer().arbeidsforhold()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Utviklerfeil: klarte ikke finne arbeidsforhold i tilrettelegginger"));
        gjeldendeTilrettelegging.vedlegg().add(vedlegg.getId());
    }

    private static void leggVedleggTilUttak(List<UttaksplanPeriodeDto> uttaksplan, VedleggDto vedlegg) {
        vedlegg.getDokumenterer().perioder()
            .forEach(periode -> uttaksplan.stream()
                .filter(u -> periode.equals(u.tidsperiode()))
                .forEach(u -> u.vedlegg().add(vedlegg.getId())));
    }

    private static void leggVedleggTilAnnenInntekt(List<AnnenInntektDto> annenInntektDtos, VedleggDto vedlegg) {
        vedlegg.getDokumenterer().perioder()
            .forEach(periode -> annenInntektDtos.stream()
                .filter(u -> periode.equals(u.tidsperiode()))
                .forEach(u -> u.vedlegg().add(vedlegg.getId())));
    }

    private static void leggVedleggTilBarn(BarnDto barn, VedleggDto vedlegg) {
        barn.terminbekreftelse().add(vedlegg.getId());
    }
    private static void leggVedleggTilBarn(no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.BarnDto barn, VedleggDto vedlegg) {
        barn.vedleggreferanser().add(vedlegg.getId());
    }
}
