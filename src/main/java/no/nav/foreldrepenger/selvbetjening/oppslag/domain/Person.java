package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static no.nav.foreldrepenger.selvbetjening.util.IkkeNordiskEØSLand.ikkeNordiskEøsLand;
import static no.nav.foreldrepenger.selvbetjening.util.StreamUtil.safeStream;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOG = LoggerFactory.getLogger(Person.class);
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
        this.fornavn = Optional.ofNullable(dto.navn).map(Navn::fornavn).orElse(null);
        this.etternavn = Optional.ofNullable(dto.navn).map(Navn::etternavn).orElse(null);
        this.mellomnavn = Optional.ofNullable(dto.navn).map(Navn::mellomnavn).orElse(null);
        this.kjønn = Optional.ofNullable(dto.navn).map(Navn::kjønn).map(Kjønn::name).orElse(null);
        this.fødselsdato = dto.fødselsdato;
        this.land = dto.landKode;
        this.ikkeNordiskEøsLand = ikkeNordiskEøsLand(dto.landKode);
        this.bankkonto = dto.bankkonto;
        this.barn = sort(dto.barn);
    }

    private static List<Barn> sort(List<Barn> barn) {
        try {
            return safeStream(barn)
                    .sorted(comparing(p -> p.getFødselsdato()))
                    .collect(toList());
        } catch (Exception e) {
            LOG.warn("Feil ved sortering", e);
            return barn;

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
