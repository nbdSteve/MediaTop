package gg.steve.mc.dazzer.mt.db;

import gg.steve.mc.dazzer.mt.manager.AbstractManager;
import gg.steve.mc.dazzer.mt.manager.ManagerClass;
import gg.steve.mc.dazzer.mt.utility.LogUtil;
import gg.steve.mc.dazzer.mt.vote.MediaVote;

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

    public static MediaTokenDatabaseManager getInstance() {
        return instance;
    }

    private void generateTables() {
        SQLDatabaseHandler.getInstance().execute("CREATE TABLE IF NOT EXISTS media_tokens (player_id VARCHAR(36) NOT NULL, balance INT NOT NULL, PRIMARY KEY (player_id))");
        SQLDatabaseHandler.getInstance().execute("CREATE TABLE IF NOT EXISTS player_vote_statistics (player_id VARCHAR(36) NOT NULL, total_votes INT NOT NULL, prize_pools_entered INT NOT NULL, prize_pools_won INT NOT NULL, PRIMARY KEY (player_id))");
        SQLDatabaseHandler.getInstance().execute("CREATE TABLE IF NOT EXISTS media_votes (vote_id VARCHAR(36) NOT NULL, is_active INT(1) NOT NULL, CONSTRAINT ck_is_active_bool CHECK (is_active IN (1, 0)), PRIMARY KEY (vote_id))");
    }

    public int getMediaTokenBalanceForPlayer(UUID playerId) {
        try {
            return Integer.parseInt(SQLDatabaseHandler.getInstance().query("SELECT * FROM media_tokens WHERE player_id='" + String.valueOf(playerId) + "'", "balance"));
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return -1;
    }

    public void setMediaTokenBalanceForPlayer(UUID playerId, int balance) {
        if (this.hasExistingMediaTokenBalance(playerId)) {
            SQLDatabaseHandler.getInstance().synchronousUpdate("UPDATE media_tokens SET balance='" + balance + "' WHERE player_id='" + String.valueOf(playerId) + "'");
        } else {
            SQLDatabaseHandler.getInstance().synchronousInsert("INSERT INTO media_tokens (player_id, balance) VALUES (?, ?);", String.valueOf(playerId), String.valueOf(balance));
        }
    }

    public boolean hasExistingMediaTokenBalance(UUID playerId) {
        return this.getMediaTokenBalanceForPlayer(playerId) >= 0;
    }

    public boolean isActiveMediaVote() {
        try {
            UUID.fromString(SQLDatabaseHandler.getInstance().query("SELECT * FROM media_votes WHERE is_active=1", "vote_id"));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public UUID getActiveMediaVote() {
        try {
            return UUID.fromString(SQLDatabaseHandler.getInstance().query("SELECT * FROM media_votes WHERE is_active=1", "vote_id"));
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isMediaVotePresentInDb(UUID voteId) {
        try {
            UUID.fromString(SQLDatabaseHandler.getInstance().query("SELECT * FROM media_votes WHERE vote_id='" + String.valueOf(voteId) + "'", "vote_id"));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void saveMediaVote(MediaVote vote) {
        if (this.isMediaVotePresentInDb(vote.getVoteId())) {
            SQLDatabaseHandler.getInstance().synchronousUpdate("UPDATE media_votes SET is_active='" + vote.isActiveInt() + "' WHERE vote_id='" + String.valueOf(vote.getVoteId()) + "'");
        } else {
            SQLDatabaseHandler.getInstance().synchronousInsert("INSERT INTO media_votes (vote_id, is_active) VALUES (?, ?);", String.valueOf(vote.getVoteId()), String.valueOf(vote.isActiveInt()));
        }
    }
}
