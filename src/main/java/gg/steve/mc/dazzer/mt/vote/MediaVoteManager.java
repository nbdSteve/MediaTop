package gg.steve.mc.dazzer.mt.vote;

import gg.steve.mc.dazzer.mt.data.VoteDataYmlManager;
import gg.steve.mc.dazzer.mt.data.exception.VoteDataYmlFileNotFoundException;
import gg.steve.mc.dazzer.mt.db.MediaTokenDatabaseManager;
import gg.steve.mc.dazzer.mt.file.types.DataPluginFile;
import gg.steve.mc.dazzer.mt.manager.AbstractManager;
import gg.steve.mc.dazzer.mt.manager.ManagerClass;

import java.util.Map;
import java.util.UUID;

@ManagerClass
public class MediaVoteManager extends AbstractManager {
    private static MediaVoteManager instance;
    private MediaVote activeMediaVote;
    private Map<UUID, MediaVote> archivedMediaVotes;

    public MediaVoteManager() {
        instance = this;
        AbstractManager.registerManager(instance);
    }

    @Override
    protected String getManagerName() {
        return "Media Vote";
    }

    @Override
    public void onLoad() {
        this.loadMediaVoteIfActive();
    }

    @Override
    public void onShutdown() {
        if (this.isMediaVoteActive()) this.activeMediaVote.save();
        if (this.archivedMediaVotes != null && !this.archivedMediaVotes.isEmpty()) this.archivedMediaVotes.clear();
    }

    public boolean isMediaVoteActive() {
        return this.activeMediaVote == null;
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

    public boolean endActiveMediaVote() {
        if (!this.isMediaVoteActive()) return false;
        this.activeMediaVote.end();
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
}
