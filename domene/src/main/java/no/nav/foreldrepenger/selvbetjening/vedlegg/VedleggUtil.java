package no.nav.foreldrepenger.selvbetjening.vedlegg;

import java.util.Optional;

import org.apache.tika.Tika;
import org.springframework.http.MediaType;

public final class VedleggUtil {

    private VedleggUtil() {

    }

    public static MediaType mediaType(byte[] bytes) {
        return Optional.ofNullable(bytes)
                .filter(b -> b.length > 0)
                .map(b -> MediaType.valueOf(new Tika().detect(b)))
                .orElse(null);
    }
}
