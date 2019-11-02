package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import static java.util.stream.Collectors.joining;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;

public class S3Mellomlagring extends AbstractMellomlagringTjeneste implements EnvironmentAware {

    private static final Logger LOG = LoggerFactory.getLogger(S3Mellomlagring.class);

    private final AmazonS3 s3;

    private Environment env;

    public S3Mellomlagring(AmazonS3 s3, String søknadBøtte, String mellomlagringBøtte) {
        super(søknadBøtte, mellomlagringBøtte);
        this.s3 = s3;
        ensureBucketExists(søknadBøtte, 365);
        ensureBucketExists(mellomlagringBøtte, 1);
    }

    @Override
    @Retryable(value = { SdkClientException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    protected boolean slett(String bøtte, String katalog, String key) {
        s3.deleteObject(bøtte, fileName(katalog, key));
        return true;
    }

    @Recover
    protected boolean recoverySlett(String bøtte, String katalog, String key) {
        LOG.trace("(Recovery) Kunne ikke slette {} fra bøtte {}, finnes sannsynligvis ikke", katalog, bøtte);
        return false;
    }

    @Override
    @Retryable(value = { SdkClientException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    protected boolean lagre(String bøtte, String katalog, String key, String value) {
        s3.putObject(bøtte, fileName(katalog, key), value);
        return true;
    }

    @Recover
    protected boolean recoveryLagre(SdkClientException e, String bøtte, String katalog, String key,
            String value) {
        LOG.trace("(Recovery) Kunne ikke lagre {} i bøtte {}, finnes sannsynligvis ikke", katalog, bøtte);
        return false;
    }

    @Override
    @Retryable(value = { SdkClientException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    protected String les(String bøtte, String katalog, String key) {
        String path = fileName(katalog, key);
        S3Object object = s3.getObject(bøtte, path);
        return new BufferedReader(new InputStreamReader(object.getObjectContent()))
                .lines()
                .collect(joining("\n"));
    }

    @Recover
    protected String recoveryLes(SdkClientException e, String bøtte, String directory, String key) {
        LOG.trace("(Recovery) Kunne ikke lese {} fra bøtte {}, finnes sannsynligvis ikke", directory, bøtte);
        return null;
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }

    @Override
    public String ping() {
        ensureBucketExists(getMellomlagringBøtte());
        return "OK";
    }

    @Override
    public String name() {
        return "S3";
    }

    @Override
    public URI pingURI() {
        return URI.create(s3.getUrl(getMellomlagringBøtte(), "42").toString());
    }

    @Override
    public boolean isEnabled() {
        return env.getProperty("mellomlagring.s3.enabled", boolean.class, true);
    }

    private static BucketLifecycleConfiguration objectExpiresInDays(Integer days) {
        return new BucketLifecycleConfiguration().withRules(
                new BucketLifecycleConfiguration.Rule()
                        .withId("soknad-retention-policy-" + days)
                        .withFilter(new LifecycleFilter())
                        .withStatus(BucketLifecycleConfiguration.ENABLED)
                        .withExpirationInDays(days));
    }

    private void ensureBucketExists(String bøtte) {
        LOG.info("Sjekker om bøtte {} eksisterer", bøtte);
        boolean finnes = s3.listBuckets().stream()
                .anyMatch(b -> b.getName().equals(bøtte));
        if (!finnes) {
            createBucket(bøtte);
        } else {
            LOG.info("Bøtte {} eksisterer", bøtte);
        }
    }

    private void ensureBucketExists(String bøtte, Integer expirationInDays) {
        ensureBucketExists(bøtte);
        setLifeCycleConfig(bøtte, objectExpiresInDays(expirationInDays));
    }

    private void setLifeCycleConfig(String bøtte, BucketLifecycleConfiguration expiry) {
        try {
            LOG.info("Setter lifecycle config for bøtte {}", bøtte);
            s3.setBucketLifecycleConfiguration(bøtte, expiry);
        } catch (Exception e) {
            LOG.info("Kunne ikke setter lifecycle config for bøtte {}", bøtte, e);
        }
    }

    private void createBucket(String bøtte) {
        LOG.info("Lager bøtte {}", bøtte);
        s3.createBucket(new CreateBucketRequest(bøtte)
                .withCannedAcl(CannedAccessControlList.Private));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[søknadBucket=" + getSøknadBøtte() + ", mellomlagringBucket="
                + getMellomlagringBøtte() + ", s3=" + s3 + "]";
    }

}
