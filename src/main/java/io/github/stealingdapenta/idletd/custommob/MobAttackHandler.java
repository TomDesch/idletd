package io.github.stealingdapenta.idletd.custommob;

import static io.github.stealingdapenta.idletd.custommob.mobtypes.CustomMob.createFrom;

import io.github.stealingdapenta.idletd.custommob.mobtypes.CustomMob;
import java.util.HashMap;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MobAttackHandler {

    private static final int SECOND_IN_MS = 1000;

    // map<Living custom mob, last attack time
    private final HashMap<CustomMob, Long> livingCustomMobs = new HashMap<>();

    public void addCustomMob(CustomMob customMob) {
        livingCustomMobs.put(customMob, System.currentTimeMillis());
    }

    public void removeDeadMobs() {
        livingCustomMobs.keySet()
                        .removeIf(this::mobRemoved);
    }

    private boolean mobRemoved(CustomMob customMob) {
        return Objects.isNull(customMob) || Objects.isNull(customMob.getMob()) || !customMob.getMob()
                                                                                            .isValid() || customMob.getMob()
                                                                                                                   .isDead();
    }

    private void checkAllAttacks() {
        // todo have one task that loops over them, removes dead mobs, checks if remaining can attack

        removeDeadMobs();

        livingCustomMobs.keySet()
                        .forEach(customMob -> {
                            if (canAttack(customMob, livingCustomMobs.get(customMob))) {
                                doAttack(customMob);
                            }
                        });
    }


    private boolean canAttack(CustomMob customMob, long lastAttack) {
        long msSinceLastAttack = System.currentTimeMillis() - lastAttack;
        MobWrapper mobWrapper = createFrom(customMob.getMob());
        double attackSpeedPerSecond = mobWrapper.getAttackSpeed();

        return enoughTimePassed(attackSpeedPerSecond, msSinceLastAttack);
    }

    private boolean enoughTimePassed(double minimalDelay, long msPassed) {
        return minimalDelay * SECOND_IN_MS <= msPassed;
    }

    private void doAttack(CustomMob customMob) {
        switch (customMob.getAttackType()) {
            case MELEE -> doMeleeAttack(customMob);
            case RANGED -> doRangedAttack(customMob);
        }

        this.livingCustomMobs.put(customMob, System.currentTimeMillis());
    }

    private void doRangedAttack(CustomMob customMob) {
        // todo do atk animation with protocollib

    }

    private void doMeleeAttack(CustomMob customMob) {
        // todo do atk animation with protocollib

    }


}
