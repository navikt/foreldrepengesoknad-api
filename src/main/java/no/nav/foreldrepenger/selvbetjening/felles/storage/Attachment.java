package no.nav.foreldrepenger.selvbetjening.felles.storage;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.gson.Gson;

public class Attachment {
    public final String filename;
    public final byte[] bytes;
    public final String contentType;
    public final long size;
    public final String uuid;

    private Attachment(String filename, byte[] bytes, String contentType, long size){
        this.filename = filename;
        this.bytes = bytes;
        this.contentType = contentType;
        this.size = size;
        this.uuid = UUID.randomUUID().toString();
    }

    public static Attachment of(MultipartFile file){
        return new Attachment(file.getOriginalFilename(), getBytes(file), file.getContentType(), file.getSize());
    }

    private static byte[] getBytes(MultipartFile file){
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Attachment fromJson(String json){
        return new Gson().fromJson(json, Attachment.class );
    }

    public String toJson(){
        return new Gson().toJson(this);
    }

    public URI uri(){
        return ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{uuid}")
                .buildAndExpand(this.uuid).toUri();
    }

    public ResponseEntity<byte[]> asOKHTTPEntity(){
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .contentLength(size)
                .body(bytes);
    }
}
