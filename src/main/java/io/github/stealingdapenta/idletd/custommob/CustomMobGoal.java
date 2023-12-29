package io.github.stealingdapenta.idletd.custommob;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.agent.Agent;
import io.github.stealingdapenta.idletd.custommob.mobtypes.CustomMob;
import java.util.EnumSet;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class CustomMobGoal implements Goal<Mob>, Listener {

    private final GoalKey<Mob> goalKey = GoalKey.of(Mob.class, new NamespacedKey("idletd", "mob"));
    private final Mob mob;
    private final Agent target;
    private final MobWrapper mobWrapper;
    private LivingEntity livingTarget;
    private long lastAttack;

    public CustomMobGoal(Mob mob, Agent target) {
        this.mob = mob;
        this.target = target;
        this.mobWrapper = CustomMob.createFrom(mob);
        this.lastAttack = System.currentTimeMillis();
    }

    @Override
    public boolean shouldActivate() {
        return true;
    }

    @Override
    public @NotNull GoalKey<Mob> getKey() {
        return goalKey;
    }

    @Override
    public @NotNull EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.TARGET);
    }

    @Override
    public boolean shouldStayActive() {
        return shouldActivate();
    }

    @Override
    public void start() {
        Bukkit.getPluginManager()
              .registerEvents(this, Idletd.getInstance());
    }

    @Override
    public void stop() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void tick() {
        if (Objects.isNull(livingTarget) && Objects.nonNull(target.getAgentNPC()
                                                                  .getNpc())) {
            setLivingTarget();
        }

        if (Objects.isNull(mob.getTarget())) {
            mob.setTarget(livingTarget);
        }

        if (canAttackAgain()) {
            lastAttack = System.currentTimeMillis();
            if (mob instanceof Skeleton skeleton) {
                skeleton.setArrowCooldown(0);
                // trigger the entityShootBow event
                // todo this does NOT trigger the entityShootBow event
                skeleton.launchProjectile(Arrow.class, new Vector(1, 1, 1));
            } else if (mob instanceof Zombie zombie) {
                if (!targetIsOutOfRange()) {
                    // todo this does NOT flick the arms as quickly as it should
                    zombie.setArmsRaised(true);
                    zombie.attack(livingTarget);
                    zombie.setArmsRaised(false);
                }
            }
        }
    }

    private boolean targetIsOutOfRange() {
        return mobWrapper.getSummonedEntity()
                         .getLocation()
                         .distanceSquared(livingTarget.getEyeLocation()) > mobWrapper.getAttackRangeSquared();
    }

    private boolean canAttackAgain() {
        double attackSpeed = this.mobWrapper.getAttackSpeed();
        long waitTime = (long) (1000.0 / attackSpeed);
        return System.currentTimeMillis() > (lastAttack + waitTime);
    }

    public void setLivingTarget() {
        this.livingTarget = (LivingEntity) target.getAgentNPC()
                                                 .getNpc()
                                                 .getEntity();
    }

    @EventHandler
    private void entityDamageByEntityEvent(EntityRemoveFromWorldEvent event) {
        if (event.getEntity()
                 .equals(mob)) {
            HandlerList.unregisterAll(this);
        }
    }
}
