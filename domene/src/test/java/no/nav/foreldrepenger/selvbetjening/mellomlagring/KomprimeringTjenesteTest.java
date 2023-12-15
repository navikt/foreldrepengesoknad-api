package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter.megabytes;
import static no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggSjekkerTest.fraResource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.common.util.TokenUtil;

@ExtendWith(MockitoExtension.class)
class KomprimeringTjenesteTest {

    private static final Logger LOG = LoggerFactory.getLogger(KomprimeringTjenesteTest.class);
    @Mock
    TokenUtil util;

    @Test
    void name() {
        when(util.getSubject()).thenReturn("01010111111");
        var krypto = new MellomlagringKrypto("passphrase", util);
        var bytes = fraResource("pdf/spring-framework-reference.pdf");
        var compress = KomprimeringTjeneste.compress(bytes);
        var kryptert = krypto.encryptVedlegg(compress);
        var dekryptert = krypto.decryptVedlegg(kryptert);
        var decompressed = KomprimeringTjeneste.decompress(dekryptert);
        assertThat(bytes).isEqualTo(decompressed);

        LOG.info("Fra {} til {}", megabytes(decompressed), megabytes(compress));
    }

    @Test
    void name1() {
        var bytes = fraResource("pdf/spring-framework-reference.pdf");
        var compress = KomprimeringTjeneste.compress(bytes);
        var decompressed = KomprimeringTjeneste.decompress(compress);
        assertThat(bytes).isEqualTo(decompressed);

        LOG.info("Fra {} til {}", megabytes(decompressed), megabytes(compress));
    }
}
