package io.github.stealingdapenta.idletd;

import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerManager;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerRepository;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerService;
import io.github.stealingdapenta.idletd.listener.CustomMobListener;
import io.github.stealingdapenta.idletd.listener.IdlePlayerListener;
import io.github.stealingdapenta.idletd.listener.SpawnListener;
import io.github.stealingdapenta.idletd.plot.PlotRepository;
import io.github.stealingdapenta.idletd.plot.PlotService;
import io.github.stealingdapenta.idletd.service.command.TowerDefenseCommand;
import io.github.stealingdapenta.idletd.service.command.plot.PlotCommand;
import io.github.stealingdapenta.idletd.service.custommob.CustomMobHandler;
import io.github.stealingdapenta.idletd.service.utils.SchematicHandler;
import io.github.stealingdapenta.idletd.towerdefense.TowerDefenseManager;
import io.github.stealingdapenta.idletd.towerdefense.TowerDefenseRepository;
import io.github.stealingdapenta.idletd.towerdefense.TowerDefenseService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.logging.Logger;

import static io.github.stealingdapenta.idletd.service.utils.Schematic.TOWER_DEFENSE_SCHEMATIC;

public class Idletd extends JavaPlugin {
    public static Logger logger;
    private static volatile boolean shuttingDown = false;
    private static Idletd instance = null;
    // Repositories
    private final PlotRepository plotRepository = new PlotRepository();
    private final IdlePlayerRepository idlePlayerRepository = new IdlePlayerRepository();
    private final TowerDefenseRepository towerDefenseRepository = new TowerDefenseRepository();

    // Handlers and services
    private final CustomMobHandler customMobHandler = new CustomMobHandler();
    private final SchematicHandler schematicHandler = new SchematicHandler();
    private final PlotService plotService = new PlotService(schematicHandler, plotRepository);
    private final PlotCommand plotCommand = new PlotCommand(plotService);
    private final IdlePlayerService idlePlayerService = new IdlePlayerService(idlePlayerRepository, plotService);
    private final IdlePlayerManager idlePlayerManager = new IdlePlayerManager(idlePlayerService);
    private final TowerDefenseService towerDefenseService = new TowerDefenseService(towerDefenseRepository, plotService, idlePlayerService, schematicHandler);
    private final TowerDefenseManager towerDefenseManager = new TowerDefenseManager(idlePlayerService, towerDefenseService);

    // Commands
    private final IdlePlayerListener idlePlayerListener = new IdlePlayerListener(idlePlayerManager, idlePlayerService, towerDefenseManager, towerDefenseService);
    private final TowerDefenseCommand towerDefenseCommand = new TowerDefenseCommand(plotService, towerDefenseService, idlePlayerService, towerDefenseManager);
    // Listeners
    private final SpawnListener spawnListener = new SpawnListener();
    private final CustomMobListener customMobListener = new CustomMobListener(customMobHandler);

    public static void shutDown() {
        shuttingDown = true;
    }

    public static Idletd getInstance() {
        return instance;
    }

    public static boolean isShuttingDown() {
        return shuttingDown;
    }

    @Override
    public void onEnable() {
        instance = this;
        logger = this.getLogger();

        this.copyResourcesToDataFolder();

        this.registerCommands();
        this.registerEvents();

        this.pluginEnabledLog();

        this.towerDefenseManager.initializeActiveGameManager();
    }

    private void registerCommands() {
        Objects.requireNonNull(this.getCommand("plot")).setExecutor(plotCommand);
        Objects.requireNonNull(this.getCommand("p")).setExecutor(plotCommand);

        Objects.requireNonNull(this.getCommand("td")).setExecutor(towerDefenseCommand);
        Objects.requireNonNull(this.getCommand("idletd")).setExecutor(towerDefenseCommand);
        Objects.requireNonNull(this.getCommand("tower")).setExecutor(towerDefenseCommand);
        Objects.requireNonNull(this.getCommand("towerdefense")).setExecutor(towerDefenseCommand);
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(customMobListener, getInstance());
        Bukkit.getPluginManager().registerEvents(spawnListener, getInstance());
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