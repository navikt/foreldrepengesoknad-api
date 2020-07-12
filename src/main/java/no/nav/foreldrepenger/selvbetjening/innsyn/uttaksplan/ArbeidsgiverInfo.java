package no.nav.foreldrepenger.selvbetjening.innsyn.uttaksplan;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ArbeidsgiverInfo {
    public String getId() {
        return id;
    }

    public String getNavn() {
        return navn;
    }

    public ArbeidsgiverType getType() {
        return type;
    }

    private final String id;
    private final String navn;
    private final ArbeidsgiverType type;

    public ArbeidsgiverInfo(@JsonProperty("id") String id, @JsonProperty("type") ArbeidsgiverType type,
            @JsonProperty("navn") String navn) {
        this.id = id;
        this.type = type;
        this.navn = navn;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [id=" + id + ", navn=" + navn + ", type=" + type + "]";
    }
}
