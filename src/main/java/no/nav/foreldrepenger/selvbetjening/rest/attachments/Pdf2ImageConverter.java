package no.nav.foreldrepenger.selvbetjening.rest.attachments;

import java.util.List;

public interface Pdf2ImageConverter {

    List<byte[]> convertToImages(List<byte[]> pdfPages);

}
