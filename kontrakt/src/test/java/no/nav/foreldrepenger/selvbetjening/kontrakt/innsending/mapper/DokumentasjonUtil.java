package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import no.nav.foreldrepenger.common.domain.felles.DokumentType;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ÅpenPeriodeDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggInnsendingType;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.ArbeidsforholdDto;

public class DokumentasjonUtil {


    public static VedleggDto.Dokumenterer opptjening(LocalDate fom, LocalDate tom) {
        return new VedleggDto.Dokumenterer(VedleggDto.Dokumenterer.DokumentererType.OPPTJENING, null, List.of(new ÅpenPeriodeDtoOLD(fom, tom)));
    }

    public static VedleggDto.Dokumenterer opptjening(ÅpenPeriodeDtoOLD periode) {
        return new VedleggDto.Dokumenterer(VedleggDto.Dokumenterer.DokumentererType.OPPTJENING, null, List.of(periode));
    }

    public static VedleggDto.Dokumenterer uttaksperiode(LocalDate fom, LocalDate tom) {
        return new VedleggDto.Dokumenterer(VedleggDto.Dokumenterer.DokumentererType.UTTAK, null, List.of(new ÅpenPeriodeDtoOLD(fom, tom)));
    }

    public static VedleggDto.Dokumenterer uttaksperioder(List<ÅpenPeriodeDtoOLD> perioder) {
        return new VedleggDto.Dokumenterer(VedleggDto.Dokumenterer.DokumentererType.UTTAK, null, perioder);
    }

    public static VedleggDto.Dokumenterer barn() {
        return new VedleggDto.Dokumenterer(VedleggDto.Dokumenterer.DokumentererType.BARN, null, null);
    }

    public static VedleggDto.Dokumenterer tilrettelegging(ArbeidsforholdDto arbeidsforhold) {
        return new VedleggDto.Dokumenterer(VedleggDto.Dokumenterer.DokumentererType.TILRETTELEGGING, arbeidsforhold, null);
    }

    public static VedleggDto vedlegg(VedleggDto.Dokumenterer dokumenterer) {
        return new VedleggDto(UUID.randomUUID(), DokumentType.I000051, VedleggInnsendingType.LASTET_OPP, null, dokumenterer);
    }

    public static VedleggDto vedlegg(DokumentType dokumentType, VedleggDto.Dokumenterer dokumenterer) {
        return new VedleggDto(UUID.randomUUID(), dokumentType, VedleggInnsendingType.LASTET_OPP, null, dokumenterer);
    }
}
