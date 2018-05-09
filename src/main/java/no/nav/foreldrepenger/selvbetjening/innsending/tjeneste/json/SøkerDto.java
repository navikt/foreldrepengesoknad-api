package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class SøkerDto {

    public String fornavn;
    public String mellomnavn;
    public String etternavn;
    public String fnr;
    public String aktør;
    public String søknadsRolle;

}
