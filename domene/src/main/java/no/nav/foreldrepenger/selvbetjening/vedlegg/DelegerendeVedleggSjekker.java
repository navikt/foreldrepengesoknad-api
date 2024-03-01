package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.mellomlagring.Attachment;

@Component
@Qualifier(DelegerendeVedleggSjekker.DELEGERENDE)
public class DelegerendeVedleggSjekker implements VedleggSjekker {

    public static final String DELEGERENDE = "Delegerende";

    private final VedleggSjekker[] sjekkere;

    public DelegerendeVedleggSjekker(VedleggSjekker... sjekkere) {
        this.sjekkere = sjekkere;
    }

    @Override
    public void sjekk(Attachment... vedlegg) {
        safeStream(sjekkere).forEach(s -> s.sjekk(vedlegg));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [sjekkere=" + Arrays.toString(sjekkere) + "]";
    }
}
