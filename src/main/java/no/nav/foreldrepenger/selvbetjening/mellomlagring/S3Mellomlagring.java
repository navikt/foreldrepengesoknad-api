package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static java.util.stream.Collectors.joining;
import static no.nav.foreldrepenger.common.util.StringUtil.flertall;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration.Rule;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;

public class S3Mellomlagring extends AbstractMellomlagringTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(S3Mellomlagring.class);

    private final AmazonS3 s3;

    public S3Mellomlagring(AmazonS3 s3, Bøtte søknadBøtte, Bøtte mellomlagringBøtte) {
        super(søknadBøtte, mellomlagringBøtte);
        this.s3 = s3;
    }

    @Override
    protected void doSlett(String bøtte, String katalog, String key) {
        s3.deleteObject(bøtte, key(katalog, key));
    }

    @Override
    protected void doLagre(String bøttenavn, String katalog, String key, String value) {
        s3.putObject(bøttenavn, key(katalog, key), value);
    }

    @Override
    protected Optional<String> doLes(String bøtte, String katalog, String key) {
        try {
            return Optional.of(
                    new BufferedReader(new InputStreamReader(s3.getObject(bøtte, key(katalog, key)).getObjectContent()))
                            .lines()
                            .collect(joining("\n")));
        } catch (AmazonS3Exception e) {
            if (SC_NOT_FOUND == e.getStatusCode()) {
                return Optional.empty();
            }
            LOG.info("Katalog {} ikke funnet, ({})", katalog, e.getErrorCode());
            throw e;
        }
    }

    @Override
    public URI pingURI() {
        return URI.create("http://www.vg.no");
    }

    @Override
    protected void validerBøtte(Bøtte bøtte) {
        LOG.info("Validerer bøtte {}", bøtte);
        if (s3.doesBucketExistV2(bøtte.getNavn())) {
            LOG.info("Bøtte {} eksisterer", bøtte);
            visRegler(s3.getBucketLifecycleConfiguration(bøtte.getNavn()).getRules());
        } else {
            LOG.info("Bøtte {} eksisterer ikke", bøtte);
            lagBøtte(bøtte);
        }
    }

    private static void visRegler(List<Rule> rules) {
        rules.forEach(r -> LOG.info("Regel har expiry om {} dag{}", r.getExpirationInDays(), flertall(r.getExpirationInDays())));
    }

    private void lagBøtte(Bøtte bøtte) {

        LOG.info("Lager bøtte {}", bøtte);
        s3.createBucket(new CreateBucketRequest(bøtte.getNavn())
                .withCannedAcl(CannedAccessControlList.Private));
        s3.setBucketLifecycleConfiguration(bøtte.getNavn(),
                objectExpiresInDays(Math.toIntExact(bøtte.getLevetid().toDays())));
        LOG.info("Laget bøtte {}", bøtte);
    }

    private static BucketLifecycleConfiguration objectExpiresInDays(int days) {
        return new BucketLifecycleConfiguration().withRules(
                new BucketLifecycleConfiguration.Rule()
                        .withId("soknad-retention-policy-" + days)
                        .withFilter(new LifecycleFilter())
                        .withStatus(BucketLifecycleConfiguration.ENABLED)
                        .withExpirationInDays(days));
    }

}
