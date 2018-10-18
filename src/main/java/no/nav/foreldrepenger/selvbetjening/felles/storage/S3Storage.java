package no.nav.foreldrepenger.selvbetjening.felles.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

public class S3Storage implements Storage {

    private static final String BUCKET_LAGRING_SOKNAD = "foreldrepengesoknad";
    private static final String BUCKET_MELLOMLAGRING_SOKNAD = "mellomlagring";
    private static final Logger LOG = LoggerFactory.getLogger(S3Storage.class);
    private AmazonS3 s3;

    @Inject
    public S3Storage(AmazonS3 s3) {
        this.s3 = s3;
        try {
            ensureBucketExists(BUCKET_LAGRING_SOKNAD, 60);
            ensureBucketExists(BUCKET_MELLOMLAGRING_SOKNAD, 1);
        } catch (Exception ex) {
            LOG.error("Could not create S3 bucket", ex);
        }
    }

    @Override
    public void put(String directory, String key, String value) {
        writeString(BUCKET_LAGRING_SOKNAD, directory, key, value);
    }

    @Override
    public void putTmp(String directory, String key, String value) {
        writeString(BUCKET_MELLOMLAGRING_SOKNAD, directory, key, value);
    }

    @Override
    public Optional<String> get(String directory, String key) {
        return Optional.ofNullable(readString(BUCKET_LAGRING_SOKNAD, directory, key));
    }


    @Override
    public Optional<String> getTmp(String directory, String key) {
        return Optional.ofNullable(readString(BUCKET_MELLOMLAGRING_SOKNAD, directory, key));
    }

    @Override
    public void delete(String directory, String key) {
        deleteString(BUCKET_LAGRING_SOKNAD, directory, key);
    }

    @Override
    public void deleteTmp(String directory, String key) {
        deleteString(BUCKET_MELLOMLAGRING_SOKNAD, directory, key);
    }


    private void ensureBucketExists(String bucketName, Integer expirationInDays) {
        boolean bucketExists = s3.listBuckets().stream()
                .anyMatch(b -> b.getName().equals(bucketName));
        if (!bucketExists) {
            createBucket(bucketName);
        }

        s3.setBucketLifecycleConfiguration(bucketName, objectExpiresInDays(expirationInDays));

    }

    private void createBucket(String bucketName) {
        s3.createBucket(new CreateBucketRequest(bucketName)
                .withCannedAcl(CannedAccessControlList.Private));
    }

    private void writeString(String bucketName, String directory, String key, String value) {
        s3.putObject(bucketName, fileName(directory, key), value);
    }

    private String readString(String bucketName, String directory, String key) {
        String path = fileName(directory, key);
        try {
            S3Object object = s3.getObject(bucketName, path);
            return new BufferedReader(new InputStreamReader(object.getObjectContent()))
                    .lines()
                    .collect(joining("\n"));
        } catch (AmazonS3Exception ex) {
            LOG.warn("Unable to retrieve " + path + ", it probably doesn't exist");
            return null;
        }

    }

    private BucketLifecycleConfiguration objectExpiresInDays(Integer days) {
        return new BucketLifecycleConfiguration().withRules(
                new BucketLifecycleConfiguration.Rule()
                        .withId("soknad-retention-policy-"+days)
                        .withFilter(new LifecycleFilter())
                        .withStatus(BucketLifecycleConfiguration.ENABLED)
                        .withExpirationInDays(days));
    }

    private void deleteString(String bucketName, String directory, String key) {
        s3.deleteObject(bucketName, fileName(directory, key));
    }

    private String fileName(String directory, String key) {
        return directory + "_" + key;
    }

}
