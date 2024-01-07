package io.github.stealingdapenta.idletd;

import static io.github.stealingdapenta.idletd.service.utils.Schematic.TOWER_DEFENSE_SCHEMATIC;

import io.github.stealingdapenta.idletd.agent.AgentManager;
import io.github.stealingdapenta.idletd.agent.AgentRepository;
import io.github.stealingdapenta.idletd.agent.AgentService;
import io.github.stealingdapenta.idletd.agent.AgentStatsService;
import io.github.stealingdapenta.idletd.agent.mainagent.MainAgentStatsRepository;
import io.github.stealingdapenta.idletd.agent.mainagent.MainAgentStatsService;
import io.github.stealingdapenta.idletd.command.AgentCommand;
import io.github.stealingdapenta.idletd.command.BalanceCommand;
import io.github.stealingdapenta.idletd.command.CustomMobCommand;
import io.github.stealingdapenta.idletd.command.PayCommand;
import io.github.stealingdapenta.idletd.command.TowerDefenseCommand;
import io.github.stealingdapenta.idletd.command.plot.PlotCommand;
import io.github.stealingdapenta.idletd.custommob.CustomMobHandler;
import io.github.stealingdapenta.idletd.idlelocation.IdleLocationRepository;
import io.github.stealingdapenta.idletd.idlelocation.IdleLocationService;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerManager;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerRepository;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerService;
import io.github.stealingdapenta.idletd.idleplayer.battlestats.BattleStatsRepository;
import io.github.stealingdapenta.idletd.idleplayer.battlestats.BattleStatsService;
import io.github.stealingdapenta.idletd.idleplayer.stats.BalanceHandler;
import io.github.stealingdapenta.idletd.listener.CustomMobListener;
import io.github.stealingdapenta.idletd.listener.DamageListener;
import io.github.stealingdapenta.idletd.listener.IdlePlayerListener;
import io.github.stealingdapenta.idletd.listener.IncomeListener;
import io.github.stealingdapenta.idletd.listener.SpawnListener;
import io.github.stealingdapenta.idletd.plot.PlotRepository;
import io.github.stealingdapenta.idletd.plot.PlotService;
import io.github.stealingdapenta.idletd.service.utils.Coloring;
import io.github.stealingdapenta.idletd.service.utils.EntityTracker;
import io.github.stealingdapenta.idletd.service.utils.SchematicHandler;
import io.github.stealingdapenta.idletd.skin.SkinManager;
import io.github.stealingdapenta.idletd.skin.SkinRepository;
import io.github.stealingdapenta.idletd.skin.SkinService;
import io.github.stealingdapenta.idletd.towerdefense.TowerDefenseManager;
import io.github.stealingdapenta.idletd.towerdefense.TowerDefenseRepository;
import io.github.stealingdapenta.idletd.towerdefense.TowerDefenseService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.logging.Logger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Idletd extends JavaPlugin {

    public static Logger logger;

    private static volatile boolean shuttingDown = false;
    private static Idletd instance = null;

    private final PlotRepository plotRepository = new PlotRepository();
    private final IdlePlayerRepository idlePlayerRepository = new IdlePlayerRepository();
    private final TowerDefenseRepository towerDefenseRepository = new TowerDefenseRepository();
    private final SkinRepository skinRepository = new SkinRepository();
    private final IdleLocationRepository idleLocationRepository = new IdleLocationRepository();
    private final AgentRepository agentRepository = new AgentRepository();
    private final Coloring coloring = new Coloring();
    private final CustomMobHandler customMobHandler = new CustomMobHandler();
    private final SchematicHandler schematicHandler = new SchematicHandler();
    private final PlotService plotService = new PlotService(schematicHandler, plotRepository);
    private final IdlePlayerService idlePlayerService = new IdlePlayerService(idlePlayerRepository, plotService);
    private final BalanceHandler balanceHandler = new BalanceHandler(idlePlayerService);
    private final PlotCommand plotCommand = new PlotCommand(plotService);
    private final IdleLocationService idleLocationService = new IdleLocationService(idleLocationRepository);
    private final SkinService skinService = new SkinService(skinRepository, coloring);
    private final SkinManager skinManager = new SkinManager(coloring, skinService);
    private final EntityTracker entityTracker = new EntityTracker(customMobHandler);
    private final BattleStatsRepository battleStatsRepository = new BattleStatsRepository();
    private final BattleStatsService battleStatsService = new BattleStatsService(battleStatsRepository);
    private final MainAgentStatsRepository mainAgentStatsRepository = new MainAgentStatsRepository();
    private final MainAgentStatsService mainAgentStatsService = new MainAgentStatsService(mainAgentStatsRepository);
    private final AgentStatsService agentStatsService = new AgentStatsService(mainAgentStatsService);
    private final AgentService agentService = new AgentService(agentRepository, idlePlayerService, idleLocationService, skinService, entityTracker,
            agentStatsService);
    private final AgentManager agentManager = new AgentManager(agentService, agentStatsService);
    private final TowerDefenseService towerDefenseService = new TowerDefenseService(towerDefenseRepository, plotService, idlePlayerService, schematicHandler,
            agentManager);
    private final TowerDefenseManager towerDefenseManager = new TowerDefenseManager(idlePlayerService, plotService, towerDefenseService);
    private final TowerDefenseCommand towerDefenseCommand = new TowerDefenseCommand(plotService, towerDefenseService, idlePlayerService, towerDefenseManager,
            agentManager);
    private final IdlePlayerManager idlePlayerManager = new IdlePlayerManager(idlePlayerService, battleStatsService, agentManager, towerDefenseManager,
            towerDefenseService);
    private final PayCommand payCommand = new PayCommand(idlePlayerService, idlePlayerManager, balanceHandler);
    private final CustomMobCommand customMobCommand = new CustomMobCommand(idlePlayerManager, agentManager);
    private final IdlePlayerListener idlePlayerListener = new IdlePlayerListener(idlePlayerManager, idlePlayerService, battleStatsService);
    private final BalanceCommand balanceCommand = new BalanceCommand(idlePlayerManager);
    private final IncomeListener incomeListener = new IncomeListener(customMobHandler, idlePlayerService, idlePlayerManager, balanceHandler);
    private final CustomMobListener customMobListener = new CustomMobListener(customMobHandler, idlePlayerService, towerDefenseManager);
    private final AgentCommand agentCommand = new AgentCommand(plotService, idlePlayerService, agentService, agentManager, idleLocationService,
            mainAgentStatsService);
    private final DamageListener damageListener = new DamageListener(customMobHandler, agentManager, idlePlayerManager);
    private final SpawnListener spawnListener = new SpawnListener();

    public static void shutDown() {
        shuttingDown = true;
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
        Objects.requireNonNull(this.getCommand("plot"))
               .setExecutor(plotCommand);
        Objects.requireNonNull(this.getCommand("towerdefense"))
               .setExecutor(towerDefenseCommand);
        Objects.requireNonNull(this.getCommand("agent"))
               .setExecutor(agentCommand);
        Objects.requireNonNull(this.getCommand("bal"))
               .setExecutor(balanceCommand);
        Objects.requireNonNull(this.getCommand("pay"))
               .setExecutor(payCommand);

        Objects.requireNonNull(this.getCommand("custommob"))
               .setExecutor(customMobCommand);
    }

    private void registerEvents() {
        Bukkit.getPluginManager()
              .registerEvents(customMobListener, getInstance());
        Bukkit.getPluginManager()
              .registerEvents(spawnListener, getInstance());
        Bukkit.getPluginManager()
              .registerEvents(idlePlayerListener, getInstance());
        Bukkit.getPluginManager()
              .registerEvents(incomeListener, getInstance());
        Bukkit.getPluginManager()
              .registerEvents(damageListener, getInstance());
    }

    private void pluginEnabledLog() {
        logger.info("IdleMCTD enabled.");
    }

    private void pluginDisabledLog() {
        logger.info("IdleMCTD is now disabled.");
    }

    public File getIdleTdFolder() {
        File pluginFolder = this.getDataFolder();
        if (!pluginFolder.exists() && (!pluginFolder.mkdirs())) {
            logger.warning("Failed to generate idletd data folder!");
        }
        return pluginFolder;
    }

    private void copyResourcesToDataFolder() {
        File dataFolder = getDataFolder();
        File schematicsFolder = new File(dataFolder, "schematics");

        if (!schematicsFolder.exists() && schematicsFolder.mkdirs()) {
            logger.info("Schematics folder created.");
        }

        this.copyResource("/schematics/" + TOWER_DEFENSE_SCHEMATIC.getFileName(), new File(schematicsFolder, TOWER_DEFENSE_SCHEMATIC.getFileName()));
    }

    private void copyResource(String resourcePath, File targetFile) {
        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath); OutputStream outputStream = new FileOutputStream(targetFile)) {
            byte[] buffer = new byte[4096];
            int length;
            while (true) {
                assert inputStream != null;
                if ((length = inputStream.read(buffer)) <= 0) {
                    break;
                }
                outputStream.write(buffer, 0, length);
            }

            logger.info("Copied resource: " + resourcePath);
        } catch (IOException e) {
            logger.warning("Failed to copy resource: " + resourcePath);
            logger.warning(e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        shutDown();
        kickAllPlayers(); // Used to make sure all log-outs are handled correctly
        instance = null;

        this.pluginDisabledLog();
    }

    private void kickAllPlayers() {
        Bukkit.getOnlinePlayers()
              .forEach(player -> player.kick(Component.text("Idle TD is reloading.")));
    }

    public static boolean isShuttingDown() {
        // Project fails if a lombok getter is used instead
        return shuttingDown;
    }

    public static Idletd getInstance() {
        // Project fails if a lombok getter is used instead
        return instance;
    }
}