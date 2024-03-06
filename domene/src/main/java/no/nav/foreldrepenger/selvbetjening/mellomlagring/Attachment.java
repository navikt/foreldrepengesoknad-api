package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import java.net.URI;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public record Attachment(byte[] bytes, String uuid) {
    public static Attachment of(byte[] bytes) {
        return new Attachment(bytes, UUID.randomUUID().toString());
    }

    public URI uri() {
        return ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{uuid}")
                .buildAndExpand(this.uuid).toUri();
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(bytes));
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
        var other = (Attachment) obj;
        return Arrays.equals(bytes(), other.bytes());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [uuid=" + uuid + "]";
    }
}
