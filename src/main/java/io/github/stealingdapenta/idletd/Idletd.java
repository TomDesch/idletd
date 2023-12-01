package io.github.stealingdapenta.idletd;

import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerManager;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerRepository;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerService;
import io.github.stealingdapenta.idletd.listener.CustomMobListener;
import io.github.stealingdapenta.idletd.listener.DamageIndicatorListener;
import io.github.stealingdapenta.idletd.listener.IdlePlayerListener;
import io.github.stealingdapenta.idletd.listener.SpawnListener;
import io.github.stealingdapenta.idletd.listener.TrackerListener;
import io.github.stealingdapenta.idletd.plot.PlotRepository;
import io.github.stealingdapenta.idletd.plot.PlotService;
import io.github.stealingdapenta.idletd.service.command.SpawnZombieCommand;
import io.github.stealingdapenta.idletd.service.command.TrackerCommand;
import io.github.stealingdapenta.idletd.service.command.plot.PlotCommand;
import io.github.stealingdapenta.idletd.service.customitem.InventoryHandler;
import io.github.stealingdapenta.idletd.service.customitem.TrackerItem;
import io.github.stealingdapenta.idletd.service.custommob.CustomMobHandler;
import io.github.stealingdapenta.idletd.service.custommob.CustomMobSpawner;
import io.github.stealingdapenta.idletd.service.utils.SchematicHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import static io.github.stealingdapenta.idletd.service.utils.Schematic.TOWER_DEFENSE_SCHEMATIC;

public class Idletd extends JavaPlugin {
    private static volatile boolean shuttingDown = false;
    private static Idletd instance = null;

    // Repositories
    private final PlotRepository plotRepository = new PlotRepository();
    private final IdlePlayerRepository idlePlayerRepository = new IdlePlayerRepository();

    // Handlers and services
    private final InventoryHandler inventoryHandler = new InventoryHandler();
    private final TrackerItem trackerItem = new TrackerItem();
    private final CustomMobHandler customMobHandler = new CustomMobHandler();
    private final SchematicHandler schematicHandler = new SchematicHandler();
    private final PlotService plotService = new PlotService(schematicHandler, plotRepository);
    private final IdlePlayerService idlePlayerService = new IdlePlayerService(idlePlayerRepository);
    private final IdlePlayerManager idlePlayerManager = new IdlePlayerManager(idlePlayerService);

    // Commands
    private final TrackerCommand trackerCommand = new TrackerCommand(inventoryHandler, trackerItem);
    private final CustomMobSpawner customMobSpawner = new CustomMobSpawner(customMobHandler);
    private final SpawnZombieCommand spawnZombieCommand = new SpawnZombieCommand(customMobSpawner);
    private final IdlePlayerListener idlePlayerListener = new IdlePlayerListener(idlePlayerManager, idlePlayerService);

    // Listeners
    private final TrackerListener trackerListener = new TrackerListener(trackerItem, customMobHandler);
    private final SpawnListener spawnListener = new SpawnListener();
    private final CustomMobListener customMobListener = new CustomMobListener();
    private final DamageIndicatorListener damageIndicatorListener = new DamageIndicatorListener();
    private final PlotCommand plotCommand = new PlotCommand(plotService);

    public static void shutDown() {
        shuttingDown = true;
    }

    public static Idletd getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        this.copyResourcesToDataFolder();

        this.registerCommands();
        this.registerEvents();

        this.pluginEnabledLog();
    }

    private void registerCommands() {
        Objects.requireNonNull(this.getCommand("zombie")).setExecutor(spawnZombieCommand);
        Objects.requireNonNull(this.getCommand("tracker")).setExecutor(trackerCommand);
        Objects.requireNonNull(this.getCommand("plot")).setExecutor(plotCommand);
        Objects.requireNonNull(this.getCommand("p")).setExecutor(plotCommand);
    }

    public static boolean isShuttingDown() {
        return shuttingDown;
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(trackerListener, getInstance());
        Bukkit.getPluginManager().registerEvents(customMobListener, getInstance());
        Bukkit.getPluginManager().registerEvents(spawnListener, getInstance());
        Bukkit.getPluginManager().registerEvents(damageIndicatorListener, getInstance());
        Bukkit.getPluginManager().registerEvents(idlePlayerListener, getInstance());
    }

    private void pluginEnabledLog() {
        getLogger().info("IdleMCTD enabled.");
    }

    private void pluginDisabledLog() {
        getLogger().info("IdleMCTD is now disabled.");
    }

    public File getIdleTdFolder() {
        File pluginFolder = this.getDataFolder();
        if (!pluginFolder.exists() && (!pluginFolder.mkdirs())) {
            getLogger().warning("Failed to generate idletd data folder!");
        }
        return pluginFolder;
    }

    private void copyResourcesToDataFolder() {
        File dataFolder = getDataFolder();
        File schematicsFolder = new File(dataFolder, "schematics");

        if (!schematicsFolder.exists() && schematicsFolder.mkdirs()) {
            getLogger().info("Schematics folder created.");
        }

        this.copyResource("schematics" + File.separator + TOWER_DEFENSE_SCHEMATIC.getFileName(),
                          new File(schematicsFolder, TOWER_DEFENSE_SCHEMATIC.getFileName()));
    }

    private void copyResource(String resourcePath, File targetFile) {
        try (InputStream inputStream = getClass().getResourceAsStream("/" + resourcePath);
             OutputStream outputStream = new FileOutputStream(targetFile)) {

            byte[] buffer = new byte[4096];
            int length;
            while (true) {
                assert inputStream != null;
                if ((length = inputStream.read(buffer)) <= 0) break;
                outputStream.write(buffer, 0, length);
            }

            getLogger().info("Copied resource: " + resourcePath);
        } catch (IOException e) {
            getLogger().warning("Failed to copy resource: " + resourcePath);
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        shutDown();
        instance = null;

        this.pluginDisabledLog();
    }
}