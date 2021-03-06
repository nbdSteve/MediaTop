package gg.steve.mc.dazzer.mt.cmd;

import gg.steve.mc.dazzer.mt.SPlugin;
import gg.steve.mc.dazzer.mt.cmd.implementation.MediaCommand;
import gg.steve.mc.dazzer.mt.cmd.implementation.MediaTopCommand;
import gg.steve.mc.dazzer.mt.manager.AbstractManager;
import gg.steve.mc.dazzer.mt.manager.ManagerClass;
import gg.steve.mc.dazzer.mt.utility.LogUtil;
import lombok.Data;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@ManagerClass
public class CommandManager extends AbstractManager {
    private static CommandManager instance;
    private SPlugin sPlugin;
    private SimpleCommandMap commandMap;
    private Map<String, AbstractCommand> commands;

    public CommandManager(SPlugin sPlugin) {
        instance = this;
        this.sPlugin = sPlugin;
        AbstractManager.registerManager(instance);
    }

    @Override
    public void onLoad() {
        SimplePluginManager pluginManager = (SimplePluginManager) this.sPlugin.getPlugin().getServer().getPluginManager();
        Field map;
        try {
            map = pluginManager.getClass().getDeclaredField("commandMap");
        } catch (NoSuchFieldException e) {
            LogUtil.warning("Unable to locate command map class, unable to register custom plugin commands.");
            e.printStackTrace();
            return;
        }
        map.setAccessible(true);
        try {
            this.commandMap = (SimpleCommandMap) map.get(pluginManager);
        } catch (IllegalAccessException e) {
            LogUtil.warning("Exception thrown whilst trying to assign command map, unable to register custom plugin commands.");
            e.printStackTrace();
        }
        // Register plugin commands
        this.registerCommand(new MediaTopCommand());
        this.registerCommand(new MediaCommand());
    }

    @Override
    public void onShutdown() {
        if (this.commandMap != null && this.commands != null && !this.commands.isEmpty()) {
            this.commands.forEach((s, command) ->  {
                if (this.commandMap.getCommand(s) != null) Objects.requireNonNull(this.commandMap.getCommand(s)).unregister(this.commandMap);
            });
        }
        if (this.commands != null && !this.commands.isEmpty()) this.commands.clear();
    }

    @Override
    protected String getManagerName() {
        return "Command";
    }

    public boolean registerCommand(AbstractCommand command) {
        if (this.commandMap == null) return false;
        if (this.commands == null) this.commands = new HashMap<>();
        if (this.commands.containsKey(command.getName())) return false;
        this.commands.put(command.getName(), command);
        this.commandMap.register(this.sPlugin.getPluginName(), command);
//        this.sPlugin.getPlugin().getCommand(command.getName()).setTabCompleter(command);
        return true;
    }

    public boolean unregisterCommand(String command) {
        if (this.commandMap == null || this.commands == null || this.commands.isEmpty()) return true;
        if (!this.commands.containsKey(command)) return false;
        this.commands.remove(command);
        if (this.commandMap.getCommand(command) == null) return true;
        return Objects.requireNonNull(this.commandMap.getCommand(command)).unregister(this.commandMap);
    }

    public static CommandManager getInstance() {
        return instance;
    }
}