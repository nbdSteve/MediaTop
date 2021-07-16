package gg.steve.mc.dazzer.mt.token;

import gg.steve.mc.dazzer.mt.manager.AbstractManager;
import gg.steve.mc.dazzer.mt.manager.ManagerClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ManagerClass
public class MediaTokenPlayerManager extends AbstractManager {
    private static MediaTokenPlayerManager instance;
    private Map<UUID, MediaTokenPlayer> mediaTokenPlayers;

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
    }

    @Override
    public void onShutdown() {
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

    public void give(UUID playerId, int amount) {
        this.getMediaTokenPlayer(playerId).update(MediaTokenPlayer.BalanceUpdateType.INCREMENT, amount);
    }

    public void remove(UUID playerId, int amount) {
        this.getMediaTokenPlayer(playerId).update(MediaTokenPlayer.BalanceUpdateType.DECREMENT, amount);
    }

    public void set(UUID playerId, int amount) {
        this.getMediaTokenPlayer(playerId).update(MediaTokenPlayer.BalanceUpdateType.SET, amount);
    }

    public void reset(UUID playerId) {
        this.getMediaTokenPlayer(playerId).update(MediaTokenPlayer.BalanceUpdateType.RESET, 0);
    }
}
