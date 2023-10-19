package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.validering;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;

class VedlegglistestørrelseValidatorTest {

    private static final VedlegglistestørrelseValidator vedlegglistestørrelseValidator = new VedlegglistestørrelseValidator();

    @Test
    void valideringFeilerVedForMangeSenereVedlegg() {
        assertThat(vedlegglistestørrelseValidator.isValid(validVedleggsliste(101, 0), null)).isFalse();
    }

    @Test
    void valideringFeilerVedForMangeOpplastedeVedlegg() {
        assertThat(vedlegglistestørrelseValidator.isValid(validVedleggsliste(0,  41), null)).isFalse();
    }

    @Test
    void forMangeSenereVedleggOGforMangeOpplastedeTriggerFeiletValidering() {
        assertThat(vedlegglistestørrelseValidator.isValid(validVedleggsliste(100,  100), null)).isFalse();
    }

    @Test
    void mangeSenereVedleggOgOppplastedeMenFremdelesInnenforGrensenOKValidering() {
        assertThat(vedlegglistestørrelseValidator.isValid(validVedleggsliste(50,  35), null)).isTrue();
    }

    @Test
    void ingenVedleggSendVedSkalValidereOK() {
        assertThat(vedlegglistestørrelseValidator.isValid(validVedleggsliste(0,  0), null)).isTrue();
    }



    private List<VedleggDto> validVedleggsliste(int sendSenereVedlegg, int opplastetVedlegg) {
        List<VedleggDto> sendSenere = new ArrayList<>();

        while (sendSenere.size() < sendSenereVedlegg) {
            var nyttVedlegg = new VedleggDto(null, "Beskrivelse", new MutableVedleggReferanseDto("Id"), "SEND_SENERE", "Skjemanummer", "xyz", null);
            sendSenere.add(nyttVedlegg);
        }

        List<VedleggDto> opplastet = new ArrayList<>();
        while (opplastet.size() < opplastetVedlegg) {
            var nyttVedlegg = new VedleggDto(null, "Beskrivelse", new MutableVedleggReferanseDto("Id"), null, "Skjemanummer", "xyz", null);
            opplastet.add(nyttVedlegg);
        }
        sendSenere.addAll(opplastet);
        return sendSenere;
    }

}
