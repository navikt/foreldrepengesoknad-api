package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

public class Svar {
    private final String id;
    private final String saksnr;
    private final String svar;

    public Svar(String id, String saksnr, String svar) {
        this.id = id;
        this.saksnr = saksnr;
        this.svar = svar;
    }

    public String getId() {
        return id;
    }

    public String getSaksnr() {
        return saksnr;
    }

    public String getSvar() {
        return svar;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id=" + id + ", saksnr=" + saksnr + ", svar=" + svar + "]";
    }

}
