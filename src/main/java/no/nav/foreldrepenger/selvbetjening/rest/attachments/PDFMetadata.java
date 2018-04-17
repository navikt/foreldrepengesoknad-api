
package no.nav.foreldrepenger.selvbetjening.rest.attachments;

public class PDFMetadata {

    private org.apache.tika.metadata.Metadata metadata;

    public PDFMetadata(org.apache.tika.metadata.Metadata metadata) {
        this.metadata = metadata;
    }

    public int pages() {
        return get("xmpTPg:NPages", 1);
    }

    private int get(String key, int defaultValue) {
        String value = metadata.get(key);
        return value != null ? Integer.valueOf(value) : defaultValue;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [metadata=" + metadata + "]";
    }

}
