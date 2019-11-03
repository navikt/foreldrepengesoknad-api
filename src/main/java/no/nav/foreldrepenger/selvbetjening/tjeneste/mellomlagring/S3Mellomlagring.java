package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;

public class S3Mellomlagring extends AbstractMellomlagringTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(S3Mellomlagring.class);

    private final AmazonS3 s3;

    public S3Mellomlagring(AmazonS3 s3, Bøtte søknadBøtte, Bøtte mellomlagringBøtte) {
        super(søknadBøtte, mellomlagringBøtte);
        this.s3 = s3;
        lagBøtteHvisIkkeFinnes(søknadBøtte);
        lagBøtteHvisIkkeFinnes(mellomlagringBøtte);
    }

    @Override
    protected void doDelete(String bøtte, String katalog, String key) {
        try {
            s3.deleteObject(bøtte, key(katalog, key));
        } catch (SdkClientException e) {
            throw new MellomlagringException(e);
        }
    }

    @Override
    protected void doStore(String bøtte, String katalog, String key, String value) {
        try {
            s3.putObject(bøtte, key(katalog, key), value);
        } catch (SdkClientException e) {
            throw new MellomlagringException(e);
        }
    }

    @Override
    protected String doRead(String bøtte, String katalog, String key) {
        try {
            S3Object object = s3.getObject(bøtte, key(katalog, key));
            return new BufferedReader(new InputStreamReader(object.getObjectContent()))
                    .lines()
                    .collect(joining("\n"));
        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == NOT_FOUND.value()) {
                LOG.info("Ikke funnet, finnes antagelig ikke");
                return null;
            }
        } catch (SdkClientException e) {
            throw new MellomlagringException(e);
        }
        return null;
    }

    @Override
    public String ping() {
        lagBøtteHvisIkkeFinnes(getMellomlagringBøtte());
        return "OK";
    }

    @Override
    public String name() {
        return "S3";
    }

    @Override
    public URI pingURI() {
        return URI.create(s3.getUrl(getMellomlagringBøtte().getNavn(), "42").toString());
    }

    private static BucketLifecycleConfiguration objectExpiresInDays(int days) {
        return new BucketLifecycleConfiguration().withRules(
                new BucketLifecycleConfiguration.Rule()
                        .withId("soknad-retention-policy-" + days)
                        .withFilter(new LifecycleFilter())
                        .withStatus(BucketLifecycleConfiguration.ENABLED)
                        .withExpirationInDays(days));
    }

    private void lagBøtteHvisIkkeFinnes(Bøtte bøtte) {
        LOG.info("Sjekker om bøtte {} eksisterer", bøtteNavn);
        if (s3.doesBucketExistV2(bøtteNavn)) {
            LOG.info("Bøtte {} eksisterer", bøtteNavn);
        } else {
            LOG.info("Bøtte {} eksisterer ikke", bøtteNavn);
            lagBøtte(bøtteNavn);
        }
    }

    private void lagBøtte(String bøtte) {
        try {
            LOG.info("Lager bøtte {}", bøtte);
            s3.createBucket(new CreateBucketRequest(bøtte)
                    .withCannedAcl(CannedAccessControlList.Private));
            s3.setBucketLifecycleConfiguration(bøtte.getNavn(),
                    objectExpiresInDays(Math.toIntExact(bøtte.getLevetid().toDays())));

        } catch (SdkClientException e) {
            throw new MellomlagringException(e);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[søknadBucket=" + getSøknadBøtte() + ", mellomlagringBucket="
                + getMellomlagringBøtte() + ", s3=" + s3 + "]";
    }

}
