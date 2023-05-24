package no.nav.foreldrepenger.selvbetjening.util;

import static no.nav.foreldrepenger.selvbetjening.util.StringUtils.escapeHtml;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class StringUtilsTest {

    @Test
    void escapeHtmlMedStreng() {
        var streng = "<script> noe øtetete taasdasc asZ#$%&</script>";
        var escapedString = escapeHtml(streng);
        assertThat(escapedString).doesNotContain("<");
    }

    @Test
    void escapeHtmlMedNullSomInput() {
        var escapedString = escapeHtml(null);
        assertThat(escapedString).isNotNull();
    }
}
