package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static no.nav.foreldrepenger.selvbetjening.util.IkkeNordiskEØSLand.ikkeNordiskEøsLand;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.dto.PersonDto;
import no.nav.foreldrepenger.selvbetjening.util.Enabled;

@JsonInclude(NON_EMPTY)
public class Person {

    public String fnr;
    public String fornavn;
    public String mellomnavn;
    public String etternavn;
    public String kjønn;
    public LocalDate fødselsdato;
    public String målform;
    public CountryCode land;
    public Boolean ikkeNordiskEøsLand;
    public Bankkonto bankkonto;
    public List<Barn> barn;

    public Person(PersonDto dto) {
        this.fnr = dto.fnr;
        this.fornavn = dto.fornavn;
        this.etternavn = dto.etternavn;
        this.mellomnavn = dto.mellomnavn;
        this.kjønn = dto.kjønn;
        this.fødselsdato = dto.fødselsdato;
        this.målform = dto.målform;
        this.land = dto.landKode;
        this.ikkeNordiskEøsLand = ikkeNordiskEøsLand(dto.landKode);
        this.bankkonto = dto.bankkonto;

        if (Enabled.TPSBARN && dto.barn != null) {
            this.barn = new ArrayList<>();
            this.barn.addAll(dto.barn);
        }

    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [fnr=" + fnr + ", fornavn=" + fornavn + ", mellomnavn=" + mellomnavn
                + ", etternavn=" + etternavn
                + ", kjønn=" + kjønn + ", fødselsdato=" + fødselsdato + ", målform=" + målform + ", land=" + land
                + ", ikkeNordiskEøsLand=" + ikkeNordiskEøsLand + ", bankkonto=" + bankkonto + ", barn=" + barn + "]";
    }

}
