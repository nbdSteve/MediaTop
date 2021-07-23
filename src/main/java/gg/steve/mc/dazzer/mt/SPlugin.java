package gg.steve.mc.dazzer.mt;

import gg.steve.mc.dazzer.mt.cmd.CommandManager;
import gg.steve.mc.dazzer.mt.cmd.listener.TabCompleteListener;
import gg.steve.mc.dazzer.mt.data.VoteDataYmlManager;
import gg.steve.mc.dazzer.mt.db.MediaTokenDatabaseManager;
import gg.steve.mc.dazzer.mt.db.SQLDatabaseHandler;
import gg.steve.mc.dazzer.mt.event.EventManager;
import gg.steve.mc.dazzer.mt.file.FileManager;
import gg.steve.mc.dazzer.mt.gui.GuiManager;
import gg.steve.mc.dazzer.mt.gui.action.InventoryClickActionManager;
import gg.steve.mc.dazzer.mt.manager.AbstractManager;
import gg.steve.mc.dazzer.mt.message.MessageManager;
import gg.steve.mc.dazzer.mt.permission.PermissionManager;
import gg.steve.mc.dazzer.mt.placeholder.PlaceholderManager;
import gg.steve.mc.dazzer.mt.token.MediaTokenPlayerManager;
import gg.steve.mc.dazzer.mt.utility.LogUtil;
import gg.steve.mc.dazzer.mt.vote.MediaVoteManager;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Data
public class SPlugin {
    private static SPlugin instance;
    // Store the main instance of the plugin
    private final JavaPlugin plugin;
    // Store the name of the plugin
    private final String pluginName;
    // Store manager instances to be accessed by addons
    private final MessageManager messageManager;
    private final CommandManager commandManager;
    private final PermissionManager permissionManager;
    private final EventManager eventManager;
    private final PlaceholderManager placeholderManager;
    private final InventoryClickActionManager inventoryClickActionManager;
    private final GuiManager guiManager;
    private final FileManager fileManager;
    private final SQLDatabaseHandler sqlDatabaseHandler;
    // Custom manager classes
    private final MediaTokenDatabaseManager mediaTokenDatabaseManager;
    private final MediaTokenPlayerManager mediaTokenPlayerManager;
    private final VoteDataYmlManager voteDataYmlManager;
    private final MediaVoteManager mediaVoteManager;

    public SPlugin(JavaPlugin plugin) {
        instance = this;
        this.plugin = plugin;
        // Name
        this.pluginName = this.plugin.getName();
        // register managers
        this.messageManager = new MessageManager();
        this.placeholderManager = new PlaceholderManager();
        this.eventManager = new EventManager(instance);
        this.inventoryClickActionManager = new InventoryClickActionManager();
        this.guiManager = new GuiManager();
        this.fileManager = new FileManager(instance);
        this.permissionManager = new PermissionManager();
        this.commandManager = new CommandManager(instance);
        this.sqlDatabaseHandler = new SQLDatabaseHandler(instance);
        // Custom managers
        this.mediaTokenDatabaseManager = new MediaTokenDatabaseManager();
        this.mediaTokenPlayerManager = new MediaTokenPlayerManager();
        this.voteDataYmlManager = new VoteDataYmlManager(instance);
        this.mediaVoteManager = new MediaVoteManager();
        // load manager classes
        this.enable();
        // set up remaining core
        this.doLoadDebug();
    }

    public void enable() {
        AbstractManager.loadManagers();
    }

    public void shutdown() {
        AbstractManager.shutdownManagers();
    }

//    public static void setupMetrics(JavaPlugin instance, int id) {
//        Metrics metrics = new Metrics(instance, id);
//        metrics.addCustomChart(new Metrics.MultiLineChart("players_and_servers", () -> {
//            Map<String, Integer> valueMap = new HashMap<>();
//            valueMap.put("servers", 1);
//            valueMap.put("players", Bukkit.getOnlinePlayers().size());
//            return valueMap;
//        }));
//    }

    public void reload() {
        shutdown();
    }

    public static void disable() {
        Bukkit.getPluginManager().disablePlugin(SPlugin.getSPluginInstance().getPlugin());
    }

    public static SPlugin getSPluginInstance() {
        return instance;
    }

    private void doLoadDebug() {
        this.eventManager.registerListener(new TabCompleteListener());
        LogUtil.severe("<-------------------------------=+=-------------------------------->");
        for (String line : FileManager.CoreFiles.CONFIG.get().getStringList("logo")) {
            LogUtil.severe(line);
        }
        LogUtil.severe(" ");
        LogUtil.severe("Messages loaded: " + this.messageManager.getMessages().size());
        LogUtil.severe("Files loaded: " + this.fileManager.getFiles().size());
        LogUtil.severe("SQL Connected: " + (this.sqlDatabaseHandler.getInjector().getConnection() != null));
        LogUtil.severe("<-------------------------------=+=-------------------------------->");
    }
}