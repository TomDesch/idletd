package io.github.stealingdapenta.idletd.custommob;

import static io.github.stealingdapenta.idletd.service.utils.Time.ONE_TICK;

import io.github.stealingdapenta.idletd.Idletd;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class CustomMobAttackTask extends BukkitRunnable {

    public static void startTask() {
        CustomMobAttackTask customMobAttackTask = new CustomMobAttackTask();
        customMobAttackTask.runTaskTimer(Idletd.getInstance(), 0L, ONE_TICK);
    }

    @Override
    public void run() {
        CustomMobAttackHandler customMobAttackHandler = CustomMobAttackHandler.getInstance();

        customMobAttackHandler.removeDeadMobs();
        customMobAttackHandler.checkAllAttacks();
    }
}
