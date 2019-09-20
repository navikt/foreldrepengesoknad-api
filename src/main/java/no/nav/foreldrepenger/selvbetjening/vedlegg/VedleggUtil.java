package no.nav.foreldrepenger.selvbetjening.vedlegg;

import org.apache.tika.Tika;
import org.springframework.http.MediaType;

public class VedleggUtil {

    public static MediaType mediaType(byte[] bytes) {
        return MediaType.valueOf(new Tika().detect(bytes));
    }

}
