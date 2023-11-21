package io.github.stealingdapenta.idletd.towerdefense;

import io.github.stealingdapenta.idletd.Idletd;
import lombok.Getter;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.Random;

import static org.bukkit.entity.EntityType.SKELETON;
import static org.bukkit.entity.EntityType.SPIDER;
import static org.bukkit.entity.EntityType.ZOMBIE;

@Getter
public enum WaveConfiguration {
    WAVE_1(1, List.of(ZOMBIE)),
    WAVE_2(2, List.of(ZOMBIE)),
    WAVE_3(4, List.of(ZOMBIE)),
    WAVE_4(6, List.of(ZOMBIE)),
    WAVE_5(5, List.of(ZOMBIE, SKELETON)), // hardcode specific
    WAVE_6(5, List.of(ZOMBIE)),
    WAVE_7(5, List.of(ZOMBIE)),
    WAVE_8(5, List.of(ZOMBIE)),
    WAVE_9(8, List.of(ZOMBIE)),
    WAVE_10(8, List.of(ZOMBIE)),  // hardcode specific
    WAVE_11(8, List.of(ZOMBIE, SKELETON)),
    WAVE_12(8, List.of(ZOMBIE, SKELETON)),
    WAVE_13(8, List.of(ZOMBIE, SKELETON)),
    WAVE_14(8, List.of(ZOMBIE, SKELETON)),
    WAVE_15(8, List.of(ZOMBIE, SKELETON)), // hardcode specific
    WAVE_16(8, List.of(ZOMBIE, SKELETON)),
    WAVE_17(8, List.of(ZOMBIE, SKELETON)),
    WAVE_18(8, List.of(ZOMBIE, SKELETON)),
    WAVE_19(8, List.of(ZOMBIE, SKELETON)),
    WAVE_20(8, List.of(ZOMBIE, SKELETON)), // hardcode specific
    WAVE_21(8, List.of(ZOMBIE, SKELETON, SPIDER)),
    WAVE_22(8, List.of(ZOMBIE, SKELETON, SPIDER)),
    WAVE_23(8, List.of(ZOMBIE, SKELETON, SPIDER)),
    WAVE_24(8, List.of(ZOMBIE, SKELETON, SPIDER)),
    WAVE_25(8, List.of(ZOMBIE, SKELETON, SPIDER)); // hardcode specific

    // idee: randomized waves binnen bepaalde preset parameters op basis van stage level
    // later dan enkele specifieke benchmark levels (e.g. 1, 5, 10, 20 , 30 , ...) hardcoden me specifieke challenge

    private final int numMobs;
    private final List<EntityType> mobTypes;
    private final Random random = new Random();

    WaveConfiguration(int numMobs, List<EntityType> mobTypes) {
        this.numMobs = numMobs;
        this.mobTypes = mobTypes;
    }

    public static WaveConfiguration getByLevel(int level) {
        if (level >= 1 && level <= values().length) {
            return values()[level - 1]; // -1 because ordinal counter starts from 0
        }
        Idletd.getInstance().getLogger().severe("Error trying to get wave by level.");
        return WAVE_1;
    }

    public static WaveConfiguration getNext(WaveConfiguration wave) {
        return getByLevel(wave.getLevel() + 1);
    }

    public int getLevel() {
        return this.ordinal() + 1; // +1 because ordinal counter starts from 0
    }

    public EntityType chooseMobType() {
        return mobTypes.get(random.nextInt(mobTypes.size()));
    }

}
