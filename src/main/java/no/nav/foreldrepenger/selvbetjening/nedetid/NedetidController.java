package no.nav.foreldrepenger.selvbetjening.nedetid;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import no.nav.foreldrepenger.selvbetjening.http.UnprotectedRestController;

@UnprotectedRestController(value = NedetidController.NEDETID_PATH)
public class NedetidController {

    static final String NEDETID_PATH = "/rest/nedetid";
    private final Nedetid nedetid;

    public NedetidController(Nedetid nedetid) {
        this.nedetid = nedetid;
    }

    @PostMapping("/registrer")
    public void FPsøknad(@Valid @RequestBody NedetidInfo info) {
        nedetid.registrer(info);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [nedetid=" + nedetid + "]";
    }
}
