package gg.steve.mc.dazzer.mt.event;

import gg.steve.mc.dazzer.mt.SPlugin;
import gg.steve.mc.dazzer.mt.gui.listener.GuiEventListener;
import gg.steve.mc.dazzer.mt.listener.ConnectionListener;
import gg.steve.mc.dazzer.mt.manager.AbstractManager;
import gg.steve.mc.dazzer.mt.manager.ManagerClass;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

@ManagerClass
public class EventManager extends AbstractManager {
    private static EventManager instance;
    private SPlugin sPlugin;

    public EventManager(SPlugin sPlugin) {
        instance = this;
        this.sPlugin = sPlugin;
        AbstractManager.registerManager(instance);
    }

    @Override
    public void onLoad() {
        this.registerListener(new ConnectionListener());
    }

    @Override
    public void onShutdown() {

    }

    @Override
    protected String getManagerName() {
        return "Event";
    }

    public void registerListener(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, this.sPlugin.getPlugin());
    }

    public static EventManager getInstance() {
        return instance;
    }
}
