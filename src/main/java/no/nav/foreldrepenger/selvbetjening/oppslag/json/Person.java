package no.nav.foreldrepenger.selvbetjening.oppslag.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.neovisionaries.i18n.CountryCode;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.PersonDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static no.nav.foreldrepenger.selvbetjening.felles.util.IkkeNordiskEØSLandVelger.erIkkenordiskEØSLand;

@JsonInclude(NON_NULL)
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
        this.kjønn = dto.kjonn;
        this.fødselsdato = dto.fodselsdato;
        this.målform = dto.målform;
        this.land = dto.landKode;
        this.ikkeNordiskEøsLand = erIkkenordiskEØSLand(dto.landKode);
        this.bankkonto = dto.bankkonto;

        if (dto.barn != null) {
            this.barn = new ArrayList<>();
            this.barn.addAll(dto.barn);
        }

    }
}
