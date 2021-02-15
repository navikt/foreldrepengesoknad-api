package no.nav.foreldrepenger.selvbetjening.nedetid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import no.nav.foreldrepenger.selvbetjening.http.UnprotectedRestController;

@UnprotectedRestController(value = NedetidController.NEDETID)
public class NedetidController {

    static final String NEDETID = "/rest/nedetid";
    private final Nedetid nedetid;

    public NedetidController(Nedetid nedetid) {
        this.nedetid = nedetid;
    }

    @PostMapping("/registrer")
    public void FPsøknad(@RequestBody NedetidInfo info) {
        nedetid.registrer(info);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [nedetid=" + nedetid + "]";
    }
}
