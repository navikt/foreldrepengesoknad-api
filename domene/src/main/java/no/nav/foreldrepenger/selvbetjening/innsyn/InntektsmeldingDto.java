package no.nav.foreldrepenger.selvbetjening.innsyn;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record InntektsmeldingDto(
    int versjon,
    boolean erAktiv,
    BigDecimal inntektPrMnd,
                              BigDecimal refusjonPrMnd,
                              String arbeidsgiverNavn,
                              String journalpostId,
    String kontaktpersonNavn,
    String kontaktpersonNummer,
                              LocalDateTime innsendingstidspunkt,
                              LocalDateTime mottattTidspunkt,
                              LocalDate startDatoPermisjon,
                              List<NaturalYtelse> bortfalteNaturalytelser,
                              List<Refusjon> refusjonsperioder
) {
    public record NaturalYtelse(
        LocalDate fomDato,
        LocalDate tomDato,
        BigDecimal beloepPerMnd,
        String type
    ) {}

    public record Refusjon(
        BigDecimal refusjonsbeløpMnd,
        LocalDate fomDato
    ) {}
}
