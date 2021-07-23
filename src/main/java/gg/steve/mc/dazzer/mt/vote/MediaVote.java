package gg.steve.mc.dazzer.mt.vote;

import gg.steve.mc.dazzer.mt.SPlugin;
import gg.steve.mc.dazzer.mt.data.VoteDataYmlManager;
import gg.steve.mc.dazzer.mt.db.MediaTokenDatabaseManager;
import gg.steve.mc.dazzer.mt.file.FileManager;
import gg.steve.mc.dazzer.mt.file.types.DataPluginFile;
import gg.steve.mc.dazzer.mt.prize.PrizePool;
import gg.steve.mc.dazzer.mt.utility.LogUtil;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

@Data
public class MediaVote {
    private UUID voteId;
    private int secondsActive;
    private Map<Integer, Candidate> candidates;
    private final BukkitTask lifeTimer;
    private boolean active;
    // need to store the gui here probably

    public MediaVote() {
        this.voteId = UUID.randomUUID();
        this.candidates = new HashMap<>(FileManager.CoreFiles.CONFIG.get().getInt("candidates"));
        for (int i = 1; i <= FileManager.CoreFiles.CONFIG.get().getInt("candidates"); i++) {
            this.candidates.put(i, new Candidate(1));
        }
        this.active = true;
        this.lifeTimer = this.registerLifeTimer();
    }

    public MediaVote(DataPluginFile activeData, boolean active) {
        YamlConfiguration data = activeData.get();
        this.voteId = UUID.fromString(data.getString("vote.id"));
        this.secondsActive = data.getInt("vote.seconds-active");
        this.candidates = new HashMap<>();
        for (String key : data.getConfigurationSection("candidates").getKeys(false)) {
            int entry = Integer.parseInt(key);
            this.candidates.put(entry, new Candidate(entry, data.getConfigurationSection("candidates." + entry)));
        }
        this.active = active;
        if (this.active) {
            this.lifeTimer = this.registerLifeTimer();
        } else {
            this.lifeTimer = null;
        }
    }

    public void end() {
        this.active = false;
        this.save();
        // do the draw here
        new PrizePool(this);
    }

    public void save() {
        if (this.lifeTimer != null) this.lifeTimer.cancel();
        VoteDataYmlManager.getInstance().saveVoteDataAsYml(this);
        MediaTokenDatabaseManager.getInstance().saveMediaVote(this);
    }

    private BukkitTask registerLifeTimer() {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(SPlugin.getSPluginInstance().getPlugin(), () -> this.secondsActive++, 0L, 20L);
    }

    public List<Candidate> getMostVotedAsList() {
        Deque<Candidate> mostVoted = new ArrayDeque<>();
        for (Candidate candidate : this.candidates.values()) {
            if (mostVoted.isEmpty()) {
                mostVoted.push(candidate);
            } else {
                if (candidate.getTotalVotes() > mostVoted.peek().getTotalVotes()) {
                    mostVoted.clear();
                    mostVoted.push(candidate);
                } else if (candidate.getTotalVotes() == mostVoted.peek().getTotalVotes()) {
                    mostVoted.push(candidate);
                }
            }
        }
        return new ArrayList<>(mostVoted);
    }

    public Candidate getMostVotedCandidate() {
        return this.getMostVotedAsList().get(new Random().nextInt(this.getMostVotedAsList().size()));
    }

    public int getCandidateVotes(int entry) {
        if (!this.candidates.containsKey(entry)) return -1;
        return this.candidates.get(entry).getTotalVotes();
    }

    public int getPlayerVotesForCandidate(UUID playerId, int entry) {
        if (!this.candidates.containsKey(entry)) return -2;
        return this.candidates.get(entry).getPlayerVotes(playerId);
    }

    public int isActiveInt() {
        return isActive() ? 1 : 0;
    }
}
