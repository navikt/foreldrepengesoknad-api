package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import static java.util.stream.Collectors.joining;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;

public class S3Mellomlagring extends AbstractStorage {

    private static final Logger LOG = LoggerFactory.getLogger(S3Mellomlagring.class);

    private final AmazonS3 s3;

    public S3Mellomlagring(AmazonS3 s3, String søknadBucket, String mellomlagringBucket) {
        super(søknadBucket, mellomlagringBucket);
        this.s3 = s3;
        ensureBucketExists(søknadBucket, 365);
        ensureBucketExists(mellomlagringBucket, 1);
    }

    @Override
    public String ping() {
        ensureBucketExists(getMellomlagringBøtte());
        return "OK";
    }

    @Override
    public URI pingURI() {
        return URI.create(s3.getUrl(getMellomlagringBøtte(), "42").toString());
    }

    @Override
    protected boolean deleteString(String bucketName, String directory, String key) {
        try {
            s3.deleteObject(bucketName, fileName(directory, key));
            return true;
        } catch (SdkClientException e) {
            LOG.warn("Feil ved fjerning", e);
            return false;
        }
    }

    protected boolean writeString(String bucketName, String directory, String key, String value) {
        try {
            s3.putObject(bucketName, fileName(directory, key), value);
            return true;
        } catch (SdkClientException e) {
            LOG.warn("Feil ved lagring", e);
            return false;
        }
    }

    @Override
    protected String readString(String bucketName, String directory, String key) {
        String path = fileName(directory, key);
        try {
            S3Object object = s3.getObject(bucketName, path);
            return new BufferedReader(new InputStreamReader(object.getObjectContent()))
                    .lines()
                    .collect(joining("\n"));
        } catch (Exception e) {
            LOG.trace("Kunne ikke hente {}, finnes sannsynligvis ikke", path, e.toString());
            return null;
        }
    }

    private static BucketLifecycleConfiguration objectExpiresInDays(Integer days) {
        return new BucketLifecycleConfiguration().withRules(
                new BucketLifecycleConfiguration.Rule()
                        .withId("soknad-retention-policy-" + days)
                        .withFilter(new LifecycleFilter())
                        .withStatus(BucketLifecycleConfiguration.ENABLED)
                        .withExpirationInDays(days));
    }

    private void ensureBucketExists(String bucketName) {
        LOG.info("Sjekker om bøtte {} eksisterer", bucketName);
        boolean bucketExists = s3.listBuckets().stream()
                .anyMatch(b -> b.getName().equals(bucketName));
        if (!bucketExists) {
            createBucket(bucketName);
        } else {
            LOG.info("Bøtte {} eksisterer", bucketName);
        }
    }

    private void ensureBucketExists(String bucketName, Integer expirationInDays) {
        ensureBucketExists(bucketName);
        setLifeCycleConfig(bucketName, objectExpiresInDays(expirationInDays));
    }

    private void setLifeCycleConfig(String bucketName, BucketLifecycleConfiguration expiry) {
        try {
            LOG.info("Setter lifecycle config for bøtte {}", bucketName);
            s3.setBucketLifecycleConfiguration(bucketName, expiry);
        } catch (Exception e) {
            LOG.info("Kunne ikke setter lifecycle config for bøtte {}", bucketName, e);
        }
    }

    private void createBucket(String bucketName) {
        LOG.info("Lager bøtte {}", bucketName);
        s3.createBucket(new CreateBucketRequest(bucketName)
                .withCannedAcl(CannedAccessControlList.Private));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[søknadBucket=" + getSøknadBøtte() + ", mellomlagringBucket="
                + getMellomlagringBøtte() + ", s3=" + s3 + "]";
    }

}
