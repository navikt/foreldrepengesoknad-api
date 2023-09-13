package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.common.domain.Saksnummer;

public final class EttersendingFrontend {
    private final @Pattern(regexp = FRITEKST) @NotNull String type;
    private final @Valid Saksnummer saksnummer;
    private final @Valid BrukerTekst brukerTekst;
    private final @Pattern(regexp = FRITEKST) String dialogId;
    private @Valid @Size(max = 40) List<VedleggFrontend> vedlegg;


    public EttersendingFrontend(String type, Saksnummer saksnummer, BrukerTekst brukerTekst, String dialogId,  List<VedleggFrontend> vedlegg) {
        this.type = type;
        this.saksnummer = saksnummer;
        this.brukerTekst = brukerTekst;
        this.dialogId = dialogId;
        this.vedlegg = Optional.ofNullable(vedlegg).orElse(new ArrayList<>());
    }

    public String type() {
        return type;
    }

    public Saksnummer saksnummer() {
        return saksnummer;
    }

    public List<VedleggFrontend> vedlegg() {
        return vedlegg;
    }

    public void vedlegg(List<VedleggFrontend> vedlegg) {
        this.vedlegg = vedlegg;
    }

    public BrukerTekst brukerTekst() {
        return brukerTekst;
    }

    public String dialogId() {
        return dialogId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (EttersendingFrontend) obj;
        return Objects.equals(this.type, that.type) &&
                Objects.equals(this.saksnummer, that.saksnummer) &&
                Objects.equals(this.vedlegg, that.vedlegg) &&
                Objects.equals(this.brukerTekst, that.brukerTekst) &&
                Objects.equals(this.dialogId, that.dialogId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, saksnummer, vedlegg, brukerTekst, dialogId);
    }

    @Override
    public String toString() {
        return "EttersendingFrontend[" +
                "type=" + type + ", " +
                "saksnummer=" + saksnummer + ", " +
                "vedlegg=" + vedlegg + ", " +
                "brukerTekst=" + brukerTekst + ", " +
                "dialogId=" + dialogId + ']';
    }

}
