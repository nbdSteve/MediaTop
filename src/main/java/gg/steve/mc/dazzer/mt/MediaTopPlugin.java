package gg.steve.mc.dazzer.mt;

import gg.steve.mc.dazzer.mt.utility.LogUtil;
import org.bukkit.plugin.java.JavaPlugin;

public final class MediaTopPlugin extends JavaPlugin {
    private static MediaTopPlugin instance;
    private SPlugin SPlugin;

    @Override
    public void onLoad() {
        instance = this;
        LogUtil.setPluginInstance(instance);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.SPlugin = new SPlugin(instance);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (this.SPlugin != null) this.SPlugin.shutdown();
    }

    public static MediaTopPlugin getInstance() {
        return instance;
    }
}
