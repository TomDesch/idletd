package io.github.stealingdapenta.idletd.custommob;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AttackType {
    MELEE("melee"),
    RANGED("ranged");

    private final String attackTypeTag;

    public static AttackType fromString(String value) {
        return Arrays.stream(values())
                     .filter(attackType -> attackType.attackTypeTag.equalsIgnoreCase(value))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("Unknown AttackType: " + value));
    }
}
