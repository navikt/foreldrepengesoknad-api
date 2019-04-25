package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.gson.Gson;

import no.nav.foreldrepenger.selvbetjening.error.AttachmentsTooLargeException;

public class Attachment {

    private static final long MB = 1024L * 1024L;
    public static final long MAX_TOTAL_VEDLEGG_SIZE = 32 * MB;
    public static final long MAX_VEDLEGG_SIZE = 8 * MB;

    public final String filename;
    public final byte[] bytes;
    public final MediaType contentType;
    public final long size;
    public final String uuid;

    private Attachment(String filename, byte[] bytes, MediaType contentType, long size) {
        this.filename = filename;
        this.bytes = bytes;
        this.contentType = contentType;
        this.size = size;
        this.uuid = UUID.randomUUID().toString();
    }

    public static Attachment of(MultipartFile file) {
        long fileSize = file.getSize();
        if (fileSize > MAX_VEDLEGG_SIZE) {
            throw new AttachmentsTooLargeException(fileSize, MAX_VEDLEGG_SIZE);
        }
        return new Attachment(file.getOriginalFilename(), getBytes(file), MediaType.valueOf(file.getContentType()),
                fileSize);
    }

    private static byte[] getBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Attachment fromJson(String json) {
        return new Gson().fromJson(json, Attachment.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public URI uri() {
        return ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{uuid}")
                .buildAndExpand(this.uuid).toUri();
    }

    public ResponseEntity<byte[]> asOKHTTPEntity() {
        return ResponseEntity.ok()
                .contentType(contentType)
                .contentLength(size)
                .body(bytes);
    }

    private String bytes() {
        String vedleggAsString = Arrays.toString(bytes);
        if (vedleggAsString.length() >= 50) {
            return vedleggAsString.substring(0, 49) + ".... " + (vedleggAsString.length() - 50) + " more bytes";
        }
        return vedleggAsString;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [filename=" + filename + ", bytes=" + bytes()
                + ", contentType=" + contentType
                + ", size=" + size + ", uuid=" + uuid + "]";
    }
}
