package no.nav.foreldrepenger.selvbetjening.felles.attachments;

import java.util.List;

public interface PDF2ImageConverter {

    List<byte[]> convertToImages(List<byte[]> pdfPages);

}
