package io.github.stealingdapenta.idletd.agent;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.plot.Plot;
import lombok.RequiredArgsConstructor;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class MainAgent {
    // skin
    // position -> 100% static
    // knockback resistant
    // Regeneration /s
    // "Overheal" (when full health, generate a shield at a lower rate)
    // anti projectile armor
    // anti sword armor
    // anti axe armor
    // attack power
    // Attack range
    // attack speed
    // crit hit chance
    // crit hit multiplier
    // splash dmg
    // Summon allies ability?
    // wololo ability?
    // debuff resistance
    // lifesteal?
    // debuff infliction?
    // gold drop boost
    // exp gain boost <- exp system?
    // temporary invincible ability?
    // AoE atk ability?
    // Slow ability?
    // Loot system on mobs <= more than just gold?

    private final Player player;
    private final Plot plot;
    //    private final Location position;
    private final List<Integer> skins = new ArrayList<>();
    private NPC npc;
    // Stats
    private int levelHealth; // stuff like this should be stored as a level; and when initialized, calculated with a formula; e.g. lv * 2 for hp; and then possibly add multipliers
    private int currentSkin;

//    private final PlotService plotService; todo add agent service


    public MainAgent(Player player, Plot plot, Location position) {
        this.player = player;
        this.plot = plot;
//        this.position = calculatePosition();
        // todo link the object to the plot; and only spawn one <= CRUCIAL
        // if there already is one, dont spawn a new one
    }

    private boolean isCitizensEnabled() {
        Plugin citizens = Idletd.getInstance().getServer().getPluginManager().getPlugin("Citizens");
        return Objects.nonNull(citizens) && citizens.isEnabled();
    }

//    private Location calculatePosition() {
//        Location spawnLocation = plotService.getPlayerSpawnPoint(plot);
//        spawnLocation.add(0d, -10d, -8d);
//        spawnLocation.setYaw(180); // Facing north
//        spawnLocation.setPitch(0);
//        return spawnLocation;
//    }

    public void setCurrentSkin(int currentSkin) {
        this.currentSkin = currentSkin;

//        Skin skinProfile = skinManager.getSkins().get(currentSkin);
//        SkinnableEntity skinnableEntity = (SkinnableEntity) npc.getEntity();
//
//        SkinTrait skinTrait = npc.getOrAddTrait(SkinTrait.class);
//
//        skinTrait.setSkinPersistent("idc", skinProfile.getData(), skinProfile.getSignature());
//        npc.addTrait(skinTrait);
//        skinnableEntity.getSkinTracker().notifySkinChange(true);
    }

    public void spawn(Location location) {
        if (!isCitizensEnabled()) {
            Idletd.getInstance().getLogger().severe("Citizens not found or not enabled");
            return;
        }

        if (Objects.isNull(npc)) {
            this.npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Wizard of Oz");
            this.setCurrentSkin(this.currentSkin);
        }

        this.npc.spawn(location);
    }
}
