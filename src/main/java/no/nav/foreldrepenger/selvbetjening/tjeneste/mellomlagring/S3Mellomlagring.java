package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import static java.util.stream.Collectors.joining;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;

public class S3Mellomlagring extends AbstractMellomlagringTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(S3Mellomlagring.class);

    private final AmazonS3 s3;

    public S3Mellomlagring(AmazonS3 s3, @Value("${storage.søknad:foreldrepengesoknad}") String søknadBøtte,
            @Value("${storage.mellomlagring:mellomlagring}") String mellomlagringBøtte, boolean enabled) {
        super(søknadBøtte, mellomlagringBøtte, enabled);
        this.s3 = s3;
        ensureBucketExists(søknadBøtte, 365);
        ensureBucketExists(mellomlagringBøtte, 1);
    }

    @Override
    protected void slett(String bøtte, String katalog, String key) {
        try {
            s3.deleteObject(bøtte, key(katalog, key));
        } catch (SdkClientException e) {
            throw new MellomlagringException(e);
        }
    }

    @Override
    protected void lagre(String bøtte, String katalog, String key, String value) {
        try {
            s3.putObject(bøtte, key(katalog, key), value);
        } catch (SdkClientException e) {
            throw new MellomlagringException(e);
        }
    }

    @Override
    protected String les(String bøtte, String katalog, String key) {
        try {
            String path = key(katalog, key);
            S3Object object = s3.getObject(bøtte, path);
            var v = new BufferedReader(new InputStreamReader(object.getObjectContent()))
                    .lines()
                    .collect(joining("\n"));
            LOG.trace("debugging v {}", v);
            return v;
        } catch (Exception e) {
            LOG.trace("debugging", e);
            return null;
        }
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
