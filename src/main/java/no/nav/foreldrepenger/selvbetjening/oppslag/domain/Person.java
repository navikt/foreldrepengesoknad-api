package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static no.nav.foreldrepenger.selvbetjening.util.IkkeNordiskEØSLand.ikkeNordiskEøsLand;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.selvbetjening.oppslag.dto.PersonDto;

@JsonInclude(NON_EMPTY)
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
        this.fornavn = dto.fornavn;
        this.etternavn = dto.etternavn;
        this.mellomnavn = dto.mellomnavn;
        this.kjønn = dto.kjønn;
        this.fødselsdato = dto.fødselsdato;
        this.land = dto.landKode;
        this.ikkeNordiskEøsLand = ikkeNordiskEøsLand(dto.landKode);
        this.bankkonto = dto.bankkonto;

        if (dto.barn != null) {
            this.barn = new ArrayList<>(dto.barn);
        }

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bankkonto == null) ? 0 : bankkonto.hashCode());
        result = prime * result + ((barn == null) ? 0 : barn.hashCode());
        result = prime * result + ((etternavn == null) ? 0 : etternavn.hashCode());
        result = prime * result + ((fnr == null) ? 0 : fnr.hashCode());
        result = prime * result + ((fornavn == null) ? 0 : fornavn.hashCode());
        result = prime * result + ((fødselsdato == null) ? 0 : fødselsdato.hashCode());
        result = prime * result + ((ikkeNordiskEøsLand == null) ? 0 : ikkeNordiskEøsLand.hashCode());
        result = prime * result + ((kjønn == null) ? 0 : kjønn.hashCode());
        result = prime * result + ((land == null) ? 0 : land.hashCode());
        result = prime * result + ((mellomnavn == null) ? 0 : mellomnavn.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Person other = (Person) obj;
        if (bankkonto == null) {
            if (other.bankkonto != null)
                return false;
        } else if (!bankkonto.equals(other.bankkonto))
            return false;
        if (barn == null) {
            if (other.barn != null)
                return false;
        } else if (!barn.equals(other.barn))
            return false;
        if (etternavn == null) {
            if (other.etternavn != null)
                return false;
        } else if (!etternavn.equals(other.etternavn))
            return false;
        if (fnr == null) {
            if (other.fnr != null)
                return false;
        } else if (!fnr.equals(other.fnr))
            return false;
        if (fornavn == null) {
            if (other.fornavn != null)
                return false;
        } else if (!fornavn.equals(other.fornavn))
            return false;
        if (fødselsdato == null) {
            if (other.fødselsdato != null)
                return false;
        } else if (!fødselsdato.equals(other.fødselsdato))
            return false;
        if (ikkeNordiskEøsLand == null) {
            if (other.ikkeNordiskEøsLand != null)
                return false;
        } else if (!ikkeNordiskEøsLand.equals(other.ikkeNordiskEøsLand))
            return false;
        if (kjønn == null) {
            if (other.kjønn != null)
                return false;
        } else if (!kjønn.equals(other.kjønn))
            return false;
        if (land != other.land)
            return false;
        if (mellomnavn == null) {
            if (other.mellomnavn != null)
                return false;
        } else if (!mellomnavn.equals(other.mellomnavn))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [fnr=" + fnr + ", fornavn=" + fornavn + ", mellomnavn=" + mellomnavn
                + ", etternavn=" + etternavn
                + ", kjønn=" + kjønn + ", fødselsdato=" + fødselsdato + ", land=" + land
                + ", ikkeNordiskEøsLand=" + ikkeNordiskEøsLand + ", bankkonto=" + bankkonto + ", barn=" + barn + "]";
    }

}
