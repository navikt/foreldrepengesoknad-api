package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class PDFKomprimeringsTjeneste {

    public static byte[] komprimer(byte[] pdf) {
        var arrayOutputStream = new ByteArrayOutputStream();
        try (var outputStream = new GZIPOutputStream(arrayOutputStream)){
            outputStream.write(pdf);
        } catch (IOException e) {
            throw new IllegalStateException("Noe gikk galt med komprimering av vedlegg ved mellomlagring", e);
        }
        return arrayOutputStream.toByteArray();
    }

    public static byte[] dekomprimer(byte[] compressedPDF) {
        var arrayInputStream = new ByteArrayInputStream(compressedPDF);
        try (var inputStream = new GZIPInputStream(arrayInputStream)){
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new IllegalStateException("Noe gikk galt med dekomprimering av vedlegg ved mellomlagring",e);
        }
    }
}
