package no.nav.foreldrepenger.selvbetjening.consumer.json;

public class VedleggDto {
    public String type;
    public VedleggMetadataDto metadata;
    public byte[] vedlegg;

    public VedleggDto() {
        metadata = new VedleggMetadataDto();
    }

    public class VedleggMetadataDto {
        public String beskrivelse;
        public String type;
        public String skjemanummer;
    }
}
