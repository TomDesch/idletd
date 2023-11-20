package io.github.stealingdapenta.idletd.towerdefense;

import io.github.stealingdapenta.idletd.service.custommob.mobtypes.CustomMob;
import io.github.stealingdapenta.idletd.service.custommob.mobtypes.ZombieMob;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TowerDefense {
    private final Random random = new Random();
    private final int stageLevel;
    private final List<CustomMob> livingMobs;
    private final WaveConfiguration wave;


    public TowerDefense() {
        this.stageLevel = 1;
        this.livingMobs = new ArrayList<>();
        this.wave = WaveConfiguration.getByLevel(stageLevel);
    }

    public void startWave() {
        for (int i = 0; i < wave.getNumMobs(); i++) {
            CustomMob mob = generateRandomMob();
            livingMobs.add(mob);
        }

        // Other wave-related logic
    }

    private CustomMob generateRandomMob() {
        int mobLevel = stageLevel + random.nextInt(3); // Adjust as needed

        // todo add stats logic in the mob class
        // todo create different mob type subclasses
        // todo summon mobs telkens met seconde tussen : echt "wave" gevoel
        // todo track if they're alive or dead
        // todo if all dead: countdown to next wave.

        return switch (wave.chooseMobType()) {
            case ZOMBIE -> new ZombieMob();
            default -> new ZombieMob();
        };
    }


    // Method to check if the wave has ended
    public boolean isWaveEnd() {
        return livingMobs.isEmpty();
    }

}
