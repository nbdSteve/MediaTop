package gg.steve.mc.dazzer.mt.prize;

import gg.steve.mc.dazzer.mt.SPlugin;
import gg.steve.mc.dazzer.mt.file.FileManager;
import gg.steve.mc.dazzer.mt.utility.ColorUtil;
import gg.steve.mc.dazzer.mt.utility.LogUtil;
import gg.steve.mc.dazzer.mt.vote.Candidate;
import gg.steve.mc.dazzer.mt.vote.MediaVote;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class PrizePool {
    private MediaVote vote;
    private Candidate candidate;
    private OfflinePlayer winner;
    private int countdown;
    private BukkitTask countdownTimerTask;
    private List<String> commands;

    public PrizePool(MediaVote vote) {
        this.vote = vote;
        this.candidate = this.vote.getMostVotedCandidate();
        this.countdown = FileManager.CoreFiles.CONFIG.get().getInt("prize-pool.countdown");
        this.commands = FileManager.CoreFiles.CONFIG.get().getStringList("prize-pool.commands");
        this.registerCountdownTimer();
        this.registerWinnerSelection();
    }

    private void registerCountdownTimer() {
        this.countdownTimerTask = Bukkit.getScheduler().runTaskTimer(SPlugin.getSPluginInstance().getPlugin(), () -> {
            if (this.countdown > 1) {
                this.countdown--;
                Bukkit.broadcastMessage(ColorUtil.colorize(FileManager.CoreFiles.CONFIG.get().getString("prize-pool.broadcast.countdown").replace("{seconds}", String.valueOf(this.countdown))));
            } else {
                if (this.winner != null) {
                    Bukkit.broadcastMessage(ColorUtil.colorize(FileManager.CoreFiles.CONFIG.get().getString("prize-pool.broadcast.winner").replace("{player}", this.winner.getName())));
                    if (this.winner.isOnline()) {
                        for (String command : commands) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", this.winner.getName()));
                        }
                    } else {
                        FileManager.CoreFiles.OFFLINE_WINNERS.get().set(String.valueOf(this.winner.getUniqueId()), commands);
                        FileManager.CoreFiles.OFFLINE_WINNERS.save();
                    }
                    this.countdownTimerTask.cancel();
                } else {
                    LogUtil.warning("Tried to do winner but the winner was still be calculated, contact nbdsteve if you see this message.");
                }
            }
        }, 0L, 20L);
    }

    private void registerWinnerSelection() {
        Bukkit.getScheduler().runTaskAsynchronously(SPlugin.getSPluginInstance().getPlugin(), () -> {
            List<UUID> players = new ArrayList<>();
            for (UUID playerId : this.candidate.getVotes().keySet()) {
                for (int i = 0; i < this.candidate.getPlayerVotes(playerId); i++) {
                    players.add(playerId);
                }
            }
            this.winner = Bukkit.getOfflinePlayer(players.get(new Random().nextInt(players.size())));
        });
    }
}
