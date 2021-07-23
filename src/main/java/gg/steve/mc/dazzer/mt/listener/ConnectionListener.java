package gg.steve.mc.dazzer.mt.listener;

import gg.steve.mc.dazzer.mt.token.MediaTokenPlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent event) {
        MediaTokenPlayerManager.getInstance().registerMediaTokenPlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        MediaTokenPlayerManager.getInstance().unregisterTokenPlayer(event.getPlayer().getUniqueId());
    }
}
