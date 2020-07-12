package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import java.time.Duration;

import no.nav.foreldrepenger.selvbetjening.http.Togglable;

public class Bøtte implements Togglable {

    public static final String TMP = "tmp";
    public static final String SØKNAD = "søknad";
    private final String navn;
    private final Duration levetid;
    private final boolean enabled;

    public Bøtte(String navn, Duration levetid, boolean enabled) {
        this.navn = navn;
        this.levetid = levetid;
        this.enabled = enabled;
    }

    public String getNavn() {
        return navn;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Duration getLevetid() {
        return levetid;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[navn=" + navn + ", levetid=" + levetid.toDays() + " dag(er)"
                + ", enabled="
                + enabled + "]";
    }

}
