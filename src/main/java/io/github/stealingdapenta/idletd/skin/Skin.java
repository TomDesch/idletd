package io.github.stealingdapenta.idletd.skin;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Builder
@Data
@RequiredArgsConstructor
public class Skin {
    private final long id;
    private final String name;
    private final String description;
    private final String dataToken;
    private final String signatureToken;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skin skin = (Skin) o;
        return id == skin.id || Objects.equals(dataToken, skin.dataToken) || Objects.equals(signatureToken, skin.signatureToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataToken, signatureToken);
    }
}
