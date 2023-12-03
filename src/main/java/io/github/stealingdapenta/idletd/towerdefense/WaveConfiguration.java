package io.github.stealingdapenta.idletd.towerdefense;

import lombok.Getter;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class WaveConfiguration {
    private final int amountOfMobs;
    private final Random random = new Random();
    private final int stageLevel;

    WaveConfiguration(int stageLevel) {
        this.stageLevel = stageLevel;
        this.amountOfMobs = calculateAmountOfMobs();
    }

    private int calculateAmountOfMobs() {
        return 2 * stageLevel;
    }

    public EntityType chooseMobType() {
        List<EntityType> mobTypes = new ArrayList<>();
        mobTypes.add(EntityType.ZOMBIE);
        mobTypes.add(EntityType.SKELETON);

        return mobTypes.get(random.nextInt(mobTypes.size()));
    }

    public int getRandomMobLevelBasedOnStageLevel() {
        return 2 * stageLevel + random.nextInt(5);
    }
}
