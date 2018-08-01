package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

public class VedleggDto {
    public String type;
    public VedleggMetadataDto metadata;
    public byte[] vedlegg;

    public VedleggDto() {
        metadata = new VedleggMetadataDto();
    }

    public class VedleggMetadataDto {
        public String id;
        public String beskrivelse;
    }
}
