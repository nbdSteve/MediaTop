package gg.steve.mc.dazzer.mt.token;

import gg.steve.mc.dazzer.mt.SPlugin;
import gg.steve.mc.dazzer.mt.file.FileManager;
import gg.steve.mc.dazzer.mt.manager.AbstractManager;
import gg.steve.mc.dazzer.mt.manager.ManagerClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ManagerClass
public class MediaTokenPlayerManager extends AbstractManager {
    private static MediaTokenPlayerManager instance;
    private Map<UUID, MediaTokenPlayer> mediaTokenPlayers;
    private BukkitTask distributionTask;

    public MediaTokenPlayerManager() {
        instance = this;
        AbstractManager.registerManager(instance);
    }

    @Override
    protected String getManagerName() {
        return "Media Token Player";
    }

    @Override
    public void onLoad() {
        this.registerOnlineTokenPlayers();
        this.registerDistributionTask();
    }

    @Override
    public void onShutdown() {
        this.unregisterDistributionTask();
        this.unregisterOnlineTokenPlayers();
    }

    public static MediaTokenPlayerManager getInstance() {
        return instance;
    }

    public boolean registerMediaTokenPlayer(UUID playerId) {
        if (this.mediaTokenPlayers == null) this.mediaTokenPlayers = new HashMap<>();
        if (this.mediaTokenPlayers.containsKey(playerId)) return false;
        return this.mediaTokenPlayers.put(playerId, new MediaTokenPlayer(playerId)) != null;
    }

    public boolean unregisterTokenPlayer(UUID playerId) {
        if (this.mediaTokenPlayers == null) return true;
        if (!this.mediaTokenPlayers.containsKey(playerId)) return false;
        this.mediaTokenPlayers.get(playerId).save();
        return this.mediaTokenPlayers.remove(playerId) != null;
    }

    public void registerOnlineTokenPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.registerMediaTokenPlayer(player.getUniqueId());
        }
    }

    private void registerDistributionTask() {
        if (this.distributionTask != null) this.distributionTask.cancel();
        this.distributionTask = Bukkit.getScheduler().runTaskTimerAsynchronously(SPlugin.getSPluginInstance().getPlugin(), () -> {
            if (this.mediaTokenPlayers == null || this.mediaTokenPlayers.isEmpty()) return;
            for (MediaTokenPlayer player : this.mediaTokenPlayers.values()) {
                player.incrementOnlineSeconds();
                if (player.getSecondsOnline() % (FileManager.CoreFiles.CONFIG.get().getInt("media-token-interval")) == 0) {
                    this.give(player.getPlayerId(), 1);
                }
            }
        }, 0L, 20L);
    }

    private void unregisterDistributionTask() {
        if (this.distributionTask != null) {
            this.distributionTask.cancel();
            this.distributionTask = null;
        }
    }

    public void unregisterOnlineTokenPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.unregisterTokenPlayer(player.getUniqueId());
        }
    }

    public MediaTokenPlayer getMediaTokenPlayer(UUID playerId) {
        if (this.mediaTokenPlayers == null) this.mediaTokenPlayers = new HashMap<>();
        if (!this.mediaTokenPlayers.containsKey(playerId)) {
            this.mediaTokenPlayers.put(playerId, new MediaTokenPlayer(playerId));
        }
        return this.mediaTokenPlayers.get(playerId);
    }

    public int getMediaTokenBalanceForPlayer(UUID playerId) {
        return this.getMediaTokenPlayer(playerId).getBalance();
    }

    public void pay(UUID from, UUID to, int amount) {
        remove(from, amount);
        give(to, amount);
    }

    public int give(UUID playerId, int amount) {
        return this.getMediaTokenPlayer(playerId).update(MediaTokenPlayer.BalanceUpdateType.INCREMENT, amount);
    }

    public int remove(UUID playerId, int amount) {
        return this.getMediaTokenPlayer(playerId).update(MediaTokenPlayer.BalanceUpdateType.DECREMENT, amount);
    }

    public int set(UUID playerId, int amount) {
        return this.getMediaTokenPlayer(playerId).update(MediaTokenPlayer.BalanceUpdateType.SET, amount);
    }

    public int reset(UUID playerId) {
        return this.getMediaTokenPlayer(playerId).update(MediaTokenPlayer.BalanceUpdateType.RESET, 0);
    }
}
