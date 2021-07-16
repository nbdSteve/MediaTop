package gg.steve.mc.dazzer.mt.db;

import gg.steve.mc.dazzer.mt.manager.AbstractManager;
import gg.steve.mc.dazzer.mt.manager.ManagerClass;

import java.util.UUID;

@ManagerClass
public class MediaTokenDatabaseManager extends AbstractManager {
    private static MediaTokenDatabaseManager instance;

    public MediaTokenDatabaseManager() {
        instance = this;
        AbstractManager.registerManager(instance);
    }

    @Override
    protected String getManagerName() {
        return "Media Tokens Database";
    }

    @Override
    public void onLoad() {
        this.generateTables();
    }

    @Override
    public void onShutdown() {

    }

    private void generateTables() {
        SQLDatabaseHandler.getInstance().execute("CREATE TABLE IF NOT EXISTS media_tokens(player_id VARCHAR(36) NOT NULL, balance INT NOT NULL, PRIMARY KEY (player_id)");
    }

    public int getMediaTokenBalanceForPlayer(UUID playerId) {
        try {
            return Integer.parseInt(SQLDatabaseHandler.getInstance().query("SELECT * FROM media_tokens WHERE player_id='" + String.valueOf(playerId) + "'", "balance"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setMediaTokenBalanceForPlayer(UUID playerId, int balance) {
        if (this.hasExistingMediaTokenBalance(playerId)) {
            SQLDatabaseHandler.getInstance().update("UPDATE media_tokens SET balance='" + balance + "' WHERE player_id='" + String.valueOf(playerId) + "'");
        } else {
            SQLDatabaseHandler.getInstance().insert("INSERT INTO media_tokens (player_id, token_balances) VALUES (?, ?);", String.valueOf(playerId), String.valueOf(balance));
        }
    }

    public boolean hasExistingMediaTokenBalance(UUID playerId) {
        return this.getMediaTokenBalanceForPlayer(playerId) >= 0;
    }
}
