package no.nav.foreldrepenger.selvbetjening.felles.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

public class S3Storage implements Storage {

    private AmazonS3 s3;

    private static final String BUCKET_NAME = "foreldrepengesoknad";
    private static final Logger LOG = LoggerFactory.getLogger(S3Storage.class);

    @Inject
    public S3Storage(AmazonS3 s3) {
        this.s3 = s3;
        ensureBucketExists();
    }

    @Override
    public void put(String directory, String key, String value) {
        writeString(directory, key, value);
    }

    @Override
    public Optional<String> get(String directory, String key) {
        return Optional.ofNullable(readString(directory, key));
    }

    @Override
    public void delete(String directory, String key) {
        deleteString(directory, key);
    }

    private void ensureBucketExists() {
        boolean bucketExists = s3.listBuckets().stream()
                .anyMatch(b -> b.getName().equals(BUCKET_NAME));
        if (!bucketExists) {
            createBucket();
        }
    }

    private void createBucket() {
        s3.createBucket(new CreateBucketRequest(BUCKET_NAME)
                 .withCannedAcl(CannedAccessControlList.Private));
    }

    private void writeString(String directory, String key, String value) {
        s3.putObject(BUCKET_NAME, fileName(directory, key), value);
    }

    private String readString(String directory, String key) {
        String path = fileName(directory, key);
        try {
            S3Object object = s3.getObject(BUCKET_NAME, path);
            return new BufferedReader(new InputStreamReader(object.getObjectContent()))
                    .lines()
                    .collect(joining("\n"));
        } catch (AmazonS3Exception ex) {
            LOG.warn("Unable to retrieve " + path + ", it probably doesn't exist");
            return null;
        }

    }

    private void deleteString(String directory, String key) {
        s3.deleteObject(BUCKET_NAME, fileName(directory, key));
    }

    private String fileName(String directory, String key) {
        return directory + "_" + key;
    }

}
