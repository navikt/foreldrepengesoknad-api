package no.nav.foreldrepenger.selvbetjening.felles.attachments;

import java.io.Closeable;
import java.io.IOException;

final class PDFUtils {

    private PDFUtils() {

    }

    static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
        }
    }

}
