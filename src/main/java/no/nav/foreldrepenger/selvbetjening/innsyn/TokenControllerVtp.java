package no.nav.foreldrepenger.selvbetjening.innsyn;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Joiner;
import no.nav.foreldrepenger.common.mapper.DefaultJsonMapper;
import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.http.UnprotectedRestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestOperations;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@UnprotectedRestController(InnsynController.INNSYN)
public class TokenControllerVtp {

    private static final HttpClient client = HttpClient.newHttpClient();

    @GetMapping(value = "/hent-token", produces = MediaType.TEXT_HTML_VALUE)
    public String hent(@RequestParam URI redirect) {
        String tmpl = """
            <!DOCTYPE html>
            <html>
            <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Velg bruker</title>
            </head>
                <body>
                <div style="text-align:center;width:100%%;">
                   <caption><h3>Fødselsnummer takk:</h3></caption>
                    <form action="http://localhost:9002/rest/innsyn/tokensmoken" method="post">
                      <input type="hidden" name="redirect_uri" value="%s" />
                      <input type="text" name="fnr" />
                      <input type="submit" value="SUbmit" />
                    </form>
                </div>
            </body>
            </html>
            """;
        return String.format(tmpl, redirect);
    }

    @PostMapping("/tokensmoken")
    public ResponseEntity<?> token(@RequestParam String fnr, @RequestParam URI redirect_uri) throws IOException, InterruptedException {
        var data = Map.of("grant_type", "client_credentials",
            "scope", "openid",
            "audience", "lokal",
            "code", fnr,
            "redirect_uri", redirect_uri);
        var formData = Joiner.on("&").withKeyValueSeparator("=").join(data);
        var req = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8060/rest/AzureAd/loginservice/oauth2/v2.0/token"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(formData))
            .build();
        var resf = client.send(req, HttpResponse.BodyHandlers.ofString());
        var body = DefaultJsonMapper.MAPPER.readValue(resf.body(), Token.class);
        var headers = new HttpHeaders();
        headers.setLocation(redirect_uri);
        var tmpl = "selvbetjening-idtoken=%s;Path=/;Domain=localhost";
        headers.add("Set-Cookie", String.format(tmpl, body.idToken));
        var r = new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        return r;
    }

    record Token(
        @JsonProperty("id_token")
        String idToken
//        String refreshToken,
//        String accessToken,
//        int expiresIn,
//        String tokenType
    ) {}
}
