package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static no.nav.foreldrepenger.selvbetjening.util.IkkeNordiskEØSLand.ikkeNordiskEøsLand;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.neovisionaries.i18n.CountryCode;

import lombok.EqualsAndHashCode;
import no.nav.foreldrepenger.selvbetjening.innsyn.saker.Kjønn;
import no.nav.foreldrepenger.selvbetjening.innsyn.saker.Navn;
import no.nav.foreldrepenger.selvbetjening.oppslag.dto.PersonDto;
import no.nav.foreldrepenger.selvbetjening.util.StringUtil;

@JsonInclude(NON_EMPTY)
@EqualsAndHashCode
public class Person {

    public String fnr;
    public String fornavn;
    public String mellomnavn;
    public String etternavn;
    public String kjønn;
    public LocalDate fødselsdato;
    public CountryCode land;
    public Boolean ikkeNordiskEøsLand;
    public Bankkonto bankkonto;
    public List<Barn> barn;

    public Person(PersonDto dto) {
        this.fnr = dto.fnr;
        this.fornavn = Optional.ofNullable(dto.navn).map(Navn::getFornavn).orElse(null);
        this.etternavn = Optional.ofNullable(dto.navn).map(Navn::getEtternavn).orElse(null);
        this.mellomnavn = Optional.ofNullable(dto.navn).map(Navn::getMellomnavn).orElse(null);
        this.kjønn = Optional.ofNullable(dto.navn).map(Navn::getKjønn).map(Kjønn::name).orElse(null);
        this.fødselsdato = dto.fødselsdato;
        this.land = dto.landKode;
        this.ikkeNordiskEøsLand = ikkeNordiskEøsLand(dto.landKode);
        this.bankkonto = dto.bankkonto;

        if (dto.barn != null) {
            this.barn = new ArrayList<>(dto.barn);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [fnr=" + StringUtil.maskFnr(fnr) + ", fornavn=" + fornavn + ", mellomnavn=" + mellomnavn
                + ", etternavn=" + etternavn + ", kjønn=" + kjønn
                + ", fødselsdato=" + fødselsdato + ", land=" + land + ", ikkeNordiskEøsLand=" + ikkeNordiskEøsLand + ", bankkonto=" + bankkonto
                + ", barn=" + barn + "]";
    }
}
