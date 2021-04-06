package no.nav.foreldrepenger.selvbetjening.innsending.domain.tilrettelegging;

public class Arbeidsforhold {

    private String type;
    private String id;

    private String risikofaktorer;
    private String tilretteleggingstiltak;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRisikofaktorer() {
        return risikofaktorer;
    }

    public void setRisikofaktorer(String risikofaktorer) {
        this.risikofaktorer = risikofaktorer;
    }

    public String getTilretteleggingstiltak() {
        return tilretteleggingstiltak;
    }

    public void setTilretteleggingstiltak(String tilretteleggingstiltak) {
        this.tilretteleggingstiltak = tilretteleggingstiltak;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[type=" + type + ", id=" + id + ", risikofaktorer=" + risikofaktorer
                + ", tilretteleggingstiltak=" + tilretteleggingstiltak + "]";
    }

}
