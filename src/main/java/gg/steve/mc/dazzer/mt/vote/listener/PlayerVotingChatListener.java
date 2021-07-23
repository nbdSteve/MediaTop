package gg.steve.mc.dazzer.mt.vote.listener;

import gg.steve.mc.dazzer.mt.message.MessageManager;
import gg.steve.mc.dazzer.mt.utility.NumberFormatUtil;
import gg.steve.mc.dazzer.mt.vote.MediaVoteManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class PlayerVotingChatListener implements Listener {

    @EventHandler
    public void chat(AsyncPlayerChatEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        if (!MediaVoteManager.getInstance().isPlayerVoting(playerId)) return;
        event.setCancelled(true);
        if (event.getMessage().equalsIgnoreCase("exit")) {
            event.getPlayer().sendMessage("Exited vote mode.");
            MediaVoteManager.getInstance().unsetPlayerInVoteMode(playerId);
            MessageManager.getInstance().sendMessage("exit-vote-mode", event.getPlayer());
            return;
        }
        int amount = -1;
        try {
            amount = Integer.parseInt(event.getMessage());
        } catch (NumberFormatException e) {
            MessageManager.getInstance().sendMessage("invalid-amount", event.getPlayer());
            return;
        }
        if (MediaVoteManager.getInstance().doPlayerVote(playerId, amount)) {
            MessageManager.getInstance().sendMessage("successful-vote", event.getPlayer(), NumberFormatUtil.format(amount));
        } else {
            MessageManager.getInstance().sendMessage("insufficient", event.getPlayer());
        }
    }
}
