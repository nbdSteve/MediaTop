package gg.steve.mc.dazzer.mt.vote;

import lombok.Data;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class Candidate {
    private int entry, totalVotes;
    private Map<UUID, Integer> votes;
    private ItemStack guiItem;

    public Candidate(int entry) {
        this.entry = entry;
        this.totalVotes = 0;
        this.votes = new HashMap<>();
    }

    public Candidate(int entry, ConfigurationSection section) {
        this.entry = entry;
        this.totalVotes = 0;
        this.votes = new HashMap<>();
        if (section != null) for (String uuid : section.getKeys(false)) {
            UUID playerId = UUID.fromString(uuid);
            this.update(playerId, VoteUpdateType.INCREMENT, Integer.parseInt(section.getString(uuid)));
        }
    }

    public enum VoteUpdateType {
        INCREMENT,
        DECREMENT,
        RESET;
    }

    public int update(UUID voter, VoteUpdateType updateType, int change) {
        change = Math.abs(change);
        switch (updateType) {
            case INCREMENT:
                return this.increment(voter, change);
            case DECREMENT:
                return this.decrement(voter, change);
            case RESET:
                return this.reset();
        }
        return this.totalVotes;
    }

    public void save() {

    }

    private int increment(UUID voter, int amount) {
        int current = this.votes.containsKey(voter) ? this.votes.get(voter) : 0;
        this.votes.put(voter, current + amount);
        return this.totalVotes += amount;
    }

    private int decrement(UUID voter, int amount) {
        int current = this.votes.containsKey(voter) ? this.votes.get(voter) : 0;
        this.votes.put(voter, current - amount);
        return this.totalVotes -= amount;
    }

    private int reset() {
        if (this.votes != null && !this.votes.isEmpty()) this.votes.clear();
        return this.totalVotes = 0;
    }

    public int getPlayerVotes(UUID playerId) {
        if (!this.votes.containsKey(playerId)) return 0;
        return this.votes.get(playerId);
    }
}
