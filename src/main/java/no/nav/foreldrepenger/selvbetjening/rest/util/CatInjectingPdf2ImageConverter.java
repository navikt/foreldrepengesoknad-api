package no.nav.foreldrepenger.selvbetjening.rest.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

@Component
public class CatInjectingPdf2ImageConverter implements Pdf2ImageConverter {

    private static final Logger LOG = LoggerFactory.getLogger(CatInjectingPdf2ImageConverter.class);
    private final byte[] cat;

    public CatInjectingPdf2ImageConverter() {
        this.cat = katt();
    }

    @Override
    public List<byte[]> convertToImages(List<byte[]> pdfPages) {
        List<byte[]> images = new ArrayList<>();
        int i = 0;
        for (byte[] page : pdfPages) {
            LOG.info("legger til en søt katt i mellomtiden");
            images.add(cat);
        }
        return images;
    }

    private static byte[] katt() {
        try {
            return StreamUtils.copyToByteArray(new ClassPathResource("katt.jpeg").getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
