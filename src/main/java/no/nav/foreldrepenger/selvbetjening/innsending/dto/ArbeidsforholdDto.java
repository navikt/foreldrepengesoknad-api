package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.AnnenInntekt;

public class ArbeidsforholdDto {
    @Override
    public String toString() {
        return "ArbeidsforholdDto [arbeidsgiverNavn=" + arbeidsgiverNavn + ", periode=" + periode + ", land=" + land
                + ", vedlegg=" + vedlegg + "]";
    }

    public String arbeidsgiverNavn;
    public PeriodeDto periode;
    public String land;
    public List<String> vedlegg;

    public ArbeidsforholdDto(AnnenInntekt annenInntekt) {
        this.arbeidsgiverNavn = annenInntekt.getArbeidsgiverNavn();
        this.land = annenInntekt.getLand();
        this.periode = new PeriodeDto(annenInntekt.getTidsperiode().getFom(), annenInntekt.getTidsperiode().getTom());
        this.vedlegg = annenInntekt.getVedlegg();
    }
}