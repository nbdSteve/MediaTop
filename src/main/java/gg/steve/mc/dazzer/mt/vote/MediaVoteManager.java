package gg.steve.mc.dazzer.mt.vote;

import gg.steve.mc.dazzer.mt.data.VoteDataYmlManager;
import gg.steve.mc.dazzer.mt.data.exception.VoteDataYmlFileNotFoundException;
import gg.steve.mc.dazzer.mt.db.MediaTokenDatabaseManager;
import gg.steve.mc.dazzer.mt.event.EventManager;
import gg.steve.mc.dazzer.mt.file.types.DataPluginFile;
import gg.steve.mc.dazzer.mt.manager.AbstractManager;
import gg.steve.mc.dazzer.mt.manager.ManagerClass;
import gg.steve.mc.dazzer.mt.token.MediaTokenPlayerManager;
import gg.steve.mc.dazzer.mt.vote.listener.PlayerVotingChatListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.*;

@ManagerClass
public class MediaVoteManager extends AbstractManager {
    private static MediaVoteManager instance;
    private MediaVote activeMediaVote;
    private Map<UUID, MediaVote> archivedMediaVotes;
    private Map<UUID, Integer> votingPlayers;
    private PlayerVotingChatListener listener;

    public MediaVoteManager() {
        instance = this;
        this.archivedMediaVotes = new HashMap<>();
        this.votingPlayers = new Hashtable<>();
        AbstractManager.registerManager(instance);
    }

    @Override
    protected String getManagerName() {
        return "Media Vote";
    }

    @Override
    public void onLoad() {
        this.loadMediaVoteIfActive();
        this.listener = new PlayerVotingChatListener();
        EventManager.getInstance().registerListener(this.listener);
    }

    @Override
    public void onShutdown() {
        if (this.isMediaVoteActive()) this.activeMediaVote.save();
        if (this.archivedMediaVotes != null && !this.archivedMediaVotes.isEmpty()) this.archivedMediaVotes.clear();
    }

    public static MediaVoteManager getInstance() {
        return instance;
    }

    public boolean isMediaVoteActive() {
        return this.activeMediaVote != null;
    }

    public void loadMediaVoteIfActive() {
        if (!MediaTokenDatabaseManager.getInstance().isActiveMediaVote()) return;
        UUID voteId = MediaTokenDatabaseManager.getInstance().getActiveMediaVote();
        DataPluginFile file;
        try {
            file = VoteDataYmlManager.getInstance().loadYmlFileById(voteId);
        } catch (VoteDataYmlFileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        this.activeMediaVote = new MediaVote(file, true);
    }

    public boolean startMediaVote() {
        if (this.isMediaVoteActive()) return false;
        this.activeMediaVote = new MediaVote();
        return true;
    }

    public MediaVote getActiveMediaVote() {
        return activeMediaVote;
    }

    public boolean endActiveMediaVote() {
        if (!this.isMediaVoteActive()) return false;
        this.activeMediaVote.end();
        if (this.archivedMediaVotes == null) this.archivedMediaVotes = new HashMap<>();
        this.archivedMediaVotes.put(this.activeMediaVote.getVoteId(), this.activeMediaVote);
        this.activeMediaVote = null;
        return true;
    }

    public MediaVote getMediaVoteById(UUID voteId) {
        if (this.isMediaVoteActive() && this.activeMediaVote.getVoteId().equals(voteId)) return this.activeMediaVote;
        if (this.archivedMediaVotes.containsKey(voteId)) return this.archivedMediaVotes.get(voteId);
        DataPluginFile file;
        try {
            file = VoteDataYmlManager.getInstance().loadYmlFileById(voteId);
        } catch (VoteDataYmlFileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return this.archivedMediaVotes.put(voteId, new MediaVote(file, false));
    }

    public boolean isPlayerVoting(UUID playerId) {
        return this.votingPlayers.containsKey(playerId);
    }

    public void setPlayerInVoteMode(UUID playerId, int candidateId) {
        this.votingPlayers.put(playerId, candidateId);
    }

    public int getVotingPlayerCandidateId(UUID playerId) {
        if (!this.isPlayerVoting(playerId)) return -1;
        return this.votingPlayers.get(playerId);
    }

    public boolean doPlayerVote(UUID playerId, int amount) {
        if (!this.isMediaVoteActive()) {
//            Bukkit.getPlayer(playerId).sendMessage(ChatColor.RED + "Tried to cast media vote but there is no vote active.");
            return false;
        }
        if (MediaTokenPlayerManager.getInstance().getMediaTokenBalanceForPlayer(playerId) < amount) {
//            Bukkit.getPlayer(playerId).sendMessage(ChatColor.RED + "Tried to cast media vote but insufficient funds.");
            return false;
        }
        MediaTokenPlayerManager.getInstance().remove(playerId, amount);
        this.activeMediaVote.getCandidates().get(this.getVotingPlayerCandidateId(playerId)).update(playerId, Candidate.VoteUpdateType.INCREMENT, amount);
        this.unsetPlayerInVoteMode(playerId);
        return true;
    }

    public void unsetPlayerInVoteMode(UUID playerId) {
        if (!this.isPlayerVoting(playerId)) {

            return;
        }
        this.votingPlayers.remove(playerId);
    }
}
