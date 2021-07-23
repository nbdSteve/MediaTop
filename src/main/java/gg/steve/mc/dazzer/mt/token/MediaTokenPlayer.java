package gg.steve.mc.dazzer.mt.token;

import gg.steve.mc.dazzer.mt.db.MediaTokenDatabaseManager;
import lombok.Data;

import java.util.UUID;

@Data
public class MediaTokenPlayer {
    private UUID playerId;
    private int balance, secondsOnline;

    public MediaTokenPlayer(UUID playerId) {
        this.playerId = playerId;
        this.balance = MediaTokenDatabaseManager.getInstance().getMediaTokenBalanceForPlayer(this.playerId);
        if (this.balance == -1) this.balance = 0;
        this.secondsOnline = 0;
    }

    public enum BalanceUpdateType {
        INCREMENT,
        DECREMENT,
        SET,
        RESET;
    }

    public int update(BalanceUpdateType updateType, int change) {
        change = Math.abs(change);
        switch (updateType) {
            case INCREMENT:
                return this.increment(change);
            case DECREMENT:
                return this.decrement(change);
            case SET:
                return this.set(change);
            case RESET:
                return this.reset();
        }
        return this.balance;
    }

    public void save() {
        MediaTokenDatabaseManager.getInstance().setMediaTokenBalanceForPlayer(this.playerId, this.balance);
    }

    public void incrementOnlineSeconds() {
        this.secondsOnline++;
    }

    private int increment(int amount) {
        return this.balance += amount;
    }

    private int decrement(int amount) {
        return this.balance -= amount;
    }

    private int set(int amount) {
        return this.balance = amount;
    }

    private int reset() {
        return this.balance = 0;
    }
}