package no.nav.foreldrepenger.selvbetjening.innsyn;


import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.foreldrepenger.selvbetjening.http.UnprotectedRestController;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;

@UnprotectedRestController("/rest/token-mock")
@Profile("local")
public class VtpTokenMock {
    private static final String vtpTokenUri = "http://localhost:8060/rest/AzureAd/loginservice/oauth2/v2.0/token";
    private static final String tokenUri = "http://localhost:9002/rest/token-mock/token";
    private static final RestOperations client = new RestTemplate();

    @GetMapping(value = "/hent-token", produces = MediaType.TEXT_HTML_VALUE)
    public String hent(@RequestParam(name= "redirect") URI redirectUri) {
        String tmpl = """
            <!DOCTYPE html>
            <html>
            <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Velg bruker</title>
            </head>
                <body>
                <div style="text-align:center;width:100%%;">
                   <caption><h3>Fødselsnummer:</h3></caption>
                    <form action="%s" method="post">
                      <input type="hidden" name="redirect_uri" value="%s" />
                      <input type="text" name="fnr" />
                      <input type="submit" value="Token, takk!" />
                    </form>
                </div>
            </body>
            </html>
            """;
        return String.format(tmpl, tokenUri, redirectUri);
    }

    @PostMapping("/token")
    public ResponseEntity<?> token(@RequestParam String fnr,
                                   @RequestParam(name = "redirect_uri") URI redirectUri) throws IOException, InterruptedException {
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var reqFormData = new LinkedMultiValueMap<String, String>();
        reqFormData.add("scope", "openid");
        reqFormData.add("audience", "lokal");
        reqFormData.add("code", fnr);
        reqFormData.add("redirect_uri", redirectUri.toString());
        var request = new HttpEntity<MultiValueMap<String, String>>(reqFormData, reqHeaders);

        var tokenResponse = client.postForEntity(vtpTokenUri, request, Token.class);

        var cookieTemplate = "selvbetjening-idtoken=%s;Path=/;Domain=localhost";
        var respHeader = new HttpHeaders();
        respHeader.setLocation(redirectUri);
        respHeader.add("Set-Cookie", String.format(cookieTemplate, tokenResponse.getBody().idToken()));
        return new ResponseEntity<>(respHeader, HttpStatus.SEE_OTHER);
    }

    record Token(@JsonProperty("id_token") String idToken) {}
}
