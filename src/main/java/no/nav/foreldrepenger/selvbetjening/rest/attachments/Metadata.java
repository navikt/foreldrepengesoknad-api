
package no.nav.foreldrepenger.selvbetjening.rest.attachments;

public class Metadata {

    private org.apache.tika.metadata.Metadata metadata;

    public Metadata(org.apache.tika.metadata.Metadata metadata) {
        this.metadata = metadata;
    }

    public int pages() {
        return get("xmpTPg:NPages", 1);
    }

    private int get(String key, int defaultValue) {
        String value = metadata.get(key);
        return value != null ? Integer.valueOf(value) : defaultValue;
    }
}
