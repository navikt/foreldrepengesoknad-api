package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

public class GSakDeserializer {

    private ObjectMapper mapper;

    public GSakDeserializer(ObjectMapper mapper) {
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
        return new Sak(node.get("sakId").asText(),
                "ukjent",
                node.get("fagomrade").asText() + "/" + node.get("sakstype").asText(),
                null,
                null,
                Collections.emptyList(),
                Collections.emptyList());
    }

}
