package no.nav.foreldrepenger.selvbetjening.innsending;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.net.URI;

import javax.ws.rs.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import no.nav.foreldrepenger.selvbetjening.error.UnexpectedInputException;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Søknad;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.KryptertMellomlagring;
import no.nav.foreldrepenger.selvbetjening.util.TokenUtil;
import no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter;
import no.nav.foreldrepenger.selvbetjening.vedlegg.PDFEncryptionChecker;
import no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggSjekker;
import no.nav.foreldrepenger.selvbetjening.virusscan.VirusScanner;
import no.nav.security.token.support.spring.SpringTokenValidationContextHolder;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = { "spring.cloud.vault.enabled=false" })
@ContextConfiguration(classes = { NotFoundException.class, Image2PDFConverter.class,
        TokenUtil.class, SpringTokenValidationContextHolder.class })
@RestClientTest

@ActiveProfiles("test")
public class InnsendingTest {

    private static final DataSize MAX_TOTAL = DataSize.of(32, DataUnit.MEGABYTES);
    private static final DataSize MAX_ENKEL = DataSize.of(8, DataUnit.MEGABYTES);

    @Mock
    TokenUtil tokenHandler;

    @Mock
    KryptertMellomlagring storage;
    @Mock
    VirusScanner scanner;

    @Mock
    PDFEncryptionChecker encryptionChecker;
    private static final InnsendingConfig CFG = cfg();

    private static InnsendingConfig cfg() {
        return new InnsendingConfig(URI.create("http://www.innsending.no"), "key", true);
    }

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private RestTemplateBuilder builder;

    private InnsendingTjeneste innsending;
    @Autowired
    private Image2PDFConverter converter;

    @BeforeEach
    public void init() {
        if (innsending == null) {
            innsending = new InnsendingTjeneste(new InnsendingConnection(builder
                    .build(), CFG, converter), storage,
                    new VedleggSjekker(MAX_TOTAL, MAX_ENKEL, scanner, encryptionChecker), null);
        }
    }

    @Test
    public void unknownType() {
        server.expect(ExpectedCount.once(), requestTo(CFG.innsendingURI()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK));
        assertThrows(UnexpectedInputException.class, () -> innsending.sendInn(new Søknad()));

    }
}
