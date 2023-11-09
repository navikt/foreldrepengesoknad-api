package no.nav.foreldrepenger.selvbetjening.innsending;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.Innsending;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;

public final class VedleggsHåndteringTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(VedleggsHåndteringTjeneste.class);

    private VedleggsHåndteringTjeneste() {
        // Skal ikke instansieres
    }

    public static void fjernDupliserteVedleggFraInnsending(Innsending innsending) {
        var antallVedleggFørDuplikatsjekk = innsending.vedlegg().size();
        finnOgfjernDupliserteVedlegg(innsending.vedlegg());
        LOG.info("Fjernet {} dupliserte vedlegg av totalt {} mottatt",  antallVedleggFørDuplikatsjekk - innsending.vedlegg().size(), antallVedleggFørDuplikatsjekk);
    }

    private static void finnOgfjernDupliserteVedlegg(List<VedleggDto> vedlegg) {
        if (vedlegg.isEmpty()) {
            return;
        }
        var duplikatTilEksisterende = finnDupliserteVedlegg(vedlegg);
        vedlegg.removeIf(vedleggDto -> duplikatTilEksisterende.contains(vedleggDto.getId()));
    }

    private static Set<VedleggDto.Referanse> finnDupliserteVedlegg(List<VedleggDto> vedlegg) {
        var kopi = new ArrayList<>(vedlegg);
        var duplikater = new HashSet<VedleggDto.Referanse>();
        for (var gjeldendeVedlegg : vedlegg) {
            var eksisterende = kopi.stream()
                .filter(v -> !Objects.equals(gjeldendeVedlegg.getId(), v.getId()))
                .filter(v -> Objects.equals(gjeldendeVedlegg.getInnsendingsType(), v.getInnsendingsType()))
                .filter(v -> Objects.equals(gjeldendeVedlegg.getSkjemanummer(), v.getSkjemanummer()))
                .filter(v -> sizeEquals(gjeldendeVedlegg, v))
                .findFirst();
            if (eksisterende.isPresent()) {
                duplikater.add(eksisterende.get().getId());
                kopi.remove(gjeldendeVedlegg);
            }
        }
        return duplikater;
    }

    private static boolean sizeEquals(VedleggDto vedlegg, VedleggDto kandidat) {
        var vedleggLength = vedlegg.getContent() != null ? vedlegg.getContent().length : 0;
        var kandidatLength = kandidat.getContent() != null ? kandidat.getContent().length : 0;
        return Objects.equals(vedleggLength, kandidatLength);
    }
}
