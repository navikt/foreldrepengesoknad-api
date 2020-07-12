package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static no.nav.foreldrepenger.selvbetjening.util.StringUtil.limit;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import no.nav.foreldrepenger.selvbetjening.error.AttachmentTooLargeException;

public class Attachment {

    public static final DataSize MAX_VEDLEGG_SIZE = DataSize.of(8, DataUnit.MEGABYTES);

    public final String filename;
    public final byte[] bytes;
    public final MediaType contentType;
    public final DataSize size;
    public final String uuid;

    private Attachment(String filename, byte[] bytes, MediaType contentType) {
        this.filename = filename;
        this.bytes = bytes;
        this.contentType = contentType;
        this.size = DataSize.ofBytes(bytes.length);
        this.uuid = UUID.randomUUID().toString();
        if (size.toBytes() > MAX_VEDLEGG_SIZE.toBytes()) {
            throw new AttachmentTooLargeException(size, MAX_VEDLEGG_SIZE);
        }
    }

    public static Attachment of(MultipartFile file) {
        return of(file.getOriginalFilename(), getBytes(file), MediaType.valueOf(file.getContentType()));
    }

    private static Attachment of(String fileName, byte[] bytes, MediaType mediaType) {
        return new Attachment(fileName, bytes, mediaType);
    }

    private static byte[] getBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public URI uri() {
        return ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{uuid}")
                .buildAndExpand(this.uuid).toUri();
    }

    public String getFilename() {
        return filename;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public MediaType getContentType() {
        return contentType;
    }

    public DataSize getSize() {
        return size;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bytes, contentType, filename, size, uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Attachment other = Attachment.class.cast(obj);
        return Objects.equals(getContentType(), other.getContentType())
                && Objects.equals(getSize(), other.getSize())
                && Objects.equals(getFilename(), other.getFilename())
                && Arrays.equals(getBytes(), other.getBytes());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [filename=" + filename + ", bytes=" + limit(bytes) + ", contentType="
                + contentType + ", size=" + size + ", uuid=" + uuid + "]";
    }
}
