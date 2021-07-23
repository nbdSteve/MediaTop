package gg.steve.mc.dazzer.mt.listener;

import gg.steve.mc.dazzer.mt.file.FileManager;
import gg.steve.mc.dazzer.mt.message.MessageManager;
import gg.steve.mc.dazzer.mt.token.MediaTokenPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class ConnectionListener implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent event) {
        MediaTokenPlayerManager.getInstance().registerMediaTokenPlayer(event.getPlayer().getUniqueId());
        if (FileManager.CoreFiles.OFFLINE_WINNERS.get().getKeys(false).contains(String.valueOf(event.getPlayer().getUniqueId()))) {
            MessageManager.getInstance().sendMessage("offline-winner", event.getPlayer());
            List<String> commands = FileManager.CoreFiles.CONFIG.get().getStringList(String.valueOf(event.getPlayer().getUniqueId()));
            for (String command : commands) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", event.getPlayer().getName()));
            }
            FileManager.CoreFiles.OFFLINE_WINNERS.get().set(String.valueOf(event.getPlayer().getUniqueId()), null);
            FileManager.CoreFiles.OFFLINE_WINNERS.save();
        }
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        MediaTokenPlayerManager.getInstance().unregisterTokenPlayer(event.getPlayer().getUniqueId());
    }
}
