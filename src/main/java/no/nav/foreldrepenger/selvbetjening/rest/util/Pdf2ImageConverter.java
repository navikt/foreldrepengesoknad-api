package no.nav.foreldrepenger.selvbetjening.rest.util;

import java.util.List;

public interface Pdf2ImageConverter {

    List<byte[]> convertToImages(List<byte[]> pdfPages);

}
