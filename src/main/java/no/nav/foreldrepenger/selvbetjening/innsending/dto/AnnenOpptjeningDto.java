package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.AnnenInntekt;

public class AnnenOpptjeningDto {
    @Override
    public String toString() {
        return "AnnenOpptjeningDto [type=" + type + ", periode=" + periode + "]";
    }

    public String type;
    public PeriodeDto periode;
    public List<String> vedlegg;

    public AnnenOpptjeningDto(AnnenInntekt annenInntekt) {
        this.type = annenInntekt.getType();
        this.periode = new PeriodeDto(annenInntekt.getTidsperiode().getFom(), annenInntekt.getTidsperiode().getTom());
        this.vedlegg = annenInntekt.getVedlegg();
    }
}