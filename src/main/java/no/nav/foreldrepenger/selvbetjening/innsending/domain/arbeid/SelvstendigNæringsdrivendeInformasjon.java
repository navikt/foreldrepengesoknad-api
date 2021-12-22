package no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Tidsperiode;

public record SelvstendigNæringsdrivendeInformasjon(boolean harBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene,
                                                    boolean hattVarigEndringAvNæringsinntektSiste4Kalenderår,
                                                    boolean registrertINorge,
                                                    Double stillingsprosent,
                                                    int næringsinntekt,
                                                    List<String> næringstyper,
                                                    List<String> vedlegg,
                                                    LocalDate oppstartsdato,
                                                    NæringsinntektInformasjon endringAvNæringsinntektInformasjon,
                                                    String navnPåNæringen,
                                                    String organisasjonsnummer,
                                                    String registrertILand,
                                                    Tidsperiode tidsperiode,
                                                    TilknyttetPerson regnskapsfører,
                                                    TilknyttetPerson revisor) {
}
