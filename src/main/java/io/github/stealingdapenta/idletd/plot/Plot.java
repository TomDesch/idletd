package io.github.stealingdapenta.idletd.plot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Plot {
    private int id;
    private int startX;
    private int startZ;
    private UUID playerUUID;

    public String getPlayerUUID() {
        return playerUUID.toString();
    }
}