package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Søker;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.AnnenInntekt;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.SelvstendigNæringsdrivendeInformasjon;

@JsonInclude(NON_EMPTY)
public class OpptjeningDto {

    public FrilansDto frilans;
    public List<EgenNæringDto> egenNæring = new ArrayList<>();
    public List<AnnenOpptjeningDto> annenOpptjening = new ArrayList<>();
    public List<ArbeidsforholdDto> arbeidsforhold = new ArrayList<>();

    public OpptjeningDto(Søker søker) {
        if (søker.getFrilansInformasjon() != null) {
            this.frilans = new FrilansDto(søker.getFrilansInformasjon());
        }
        for (SelvstendigNæringsdrivendeInformasjon selvstendig : søker.getSelvstendigNæringsdrivendeInformasjon()) {
            this.egenNæring.add(new EgenNæringDto(selvstendig));
        }
        for (AnnenInntekt annenInntekt : søker.getAndreInntekterSiste10Mnd()) {
            if (annenInntekt.getType().equals("JOBB_I_UTLANDET")) {
                this.arbeidsforhold.add(new ArbeidsforholdDto(annenInntekt));
            } else {
                this.annenOpptjening.add(new AnnenOpptjeningDto(annenInntekt));
            }
        }
    }

    @Override
    public String toString() {
        return "OpptjeningDto [frilans=" + frilans + ", egenNæring=" + egenNæring + ", annenOpptjening="
                + annenOpptjening + ", arbeidsforhold=" + arbeidsforhold + "]";
    }
}
