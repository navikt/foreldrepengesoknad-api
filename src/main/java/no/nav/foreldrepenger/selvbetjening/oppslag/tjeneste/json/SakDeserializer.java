package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.foreldrepenger.selvbetjening.oppslag.json.Sak;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

public class SakDeserializer {

    private ObjectMapper mapper;

    public SakDeserializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public List<Sak> from(String json) {
        try {
            JsonNode rootNode = mapper.readTree(json);

            return StreamSupport.stream(rootNode.spliterator(), false)
                    .map(this::fromNode)
                    .collect(toList());
        } catch (IOException ex) {
            throw new RuntimeException("Error while deserializing gsak response", ex);
        }

    }

    private Sak fromNode(JsonNode node) {
        try {
            return new Sak(node.get("sakId").asText(), node.get("fagsystemSakId").asText(),
                    "ukjent", "FORP_FODS", mapper.readValue(node.get("opprettet").asText(), LocalDate.class),
                    Collections.emptyList());
        } catch (IOException e) {
            return null;
        }
    }

}
