package no.nav.foreldrepenger.selvbetjening.tjeneste.virusscan;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ScanResult {

    @JsonAlias("Filename")
    private final String filename;
    @JsonAlias("Result")
    private final Result result;

    @JsonCreator
    public ScanResult(@JsonProperty("filename") String filename, @JsonProperty("result") Result result) {
        this.filename = filename;
        this.result = result;
    }

    public String getFilename() {
        return filename;
    }

    public Result getResult() {
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [filename=" + filename + ", result=" + result + "]";
    }
}
