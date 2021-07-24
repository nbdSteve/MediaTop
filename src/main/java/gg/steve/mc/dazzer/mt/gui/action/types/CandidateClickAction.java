package gg.steve.mc.dazzer.mt.gui.action.types;

import gg.steve.mc.dazzer.mt.file.FileManager;
import gg.steve.mc.dazzer.mt.gui.AbstractGui;
import gg.steve.mc.dazzer.mt.gui.action.AbstractInventoryClickAction;
import gg.steve.mc.dazzer.mt.message.MessageManager;
import gg.steve.mc.dazzer.mt.token.MediaTokenPlayer;
import gg.steve.mc.dazzer.mt.token.MediaTokenPlayerManager;
import gg.steve.mc.dazzer.mt.utility.ItemBuilderUtil;
import gg.steve.mc.dazzer.mt.utility.NumberFormatUtil;
import gg.steve.mc.dazzer.mt.vote.Candidate;
import gg.steve.mc.dazzer.mt.vote.MediaVoteManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CandidateClickAction extends AbstractInventoryClickAction {

    public CandidateClickAction() {
        super("candidate", 2);
    }

    @Override
    public void onClick(AbstractGui gui, Player player, ConfigurationSection section, int slot) {
        if (!MediaVoteManager.getInstance().isMediaVoteActive()) return;
        int candidateId = this.getCandidateIdFromConfig(section.getString("action"));
        gui.close(player);
//        player.sendMessage(ChatColor.RED + "Player, " + player.getName() + ", has clicked candidate with id: " + candidateId);
        MediaVoteManager.getInstance().setPlayerInVoteMode(player.getUniqueId(), candidateId);
//        player.sendMessage(ChatColor.RED + "In vote mode: " + MediaVoteManager.getInstance().isPlayerVoting(player.getUniqueId()));
        MessageManager.getInstance().sendMessage("enter-vote-mode", player, NumberFormatUtil.format(MediaTokenPlayerManager.getInstance().getMediaTokenBalanceForPlayer(player.getUniqueId())));
    }

    @Override
    public ItemStack getRenderedItem(Player player, ConfigurationSection section) {
        if (!MediaVoteManager.getInstance().isMediaVoteActive()) {
            MediaTokenPlayer tokenPlayer = MediaTokenPlayerManager.getInstance().getMediaTokenPlayer(player.getUniqueId());
            section = FileManager.CoreFiles.CONFIG.get().getConfigurationSection("no-vote-active-candidate-item");
            ItemBuilderUtil builder = ItemBuilderUtil.getBuilderForMaterial(section.getString("material"), section.getString("data"));
            builder.addName(section.getString("name"));
            builder.setLorePlaceholders("{tokens}");
            builder.addLore(section.getStringList("lore"), NumberFormatUtil.format(tokenPlayer.getBalance()));
            builder.addEnchantments(section.getStringList("enchantments"));
            builder.addItemFlags(section.getStringList("item-flags"));
            builder.addNBT(section.getBoolean("unbreakable"));
            return builder.getItem();
        }
        Candidate candidate = MediaVoteManager.getInstance().getActiveMediaVote().getCandidates().get(this.getCandidateIdFromConfig(section.getString("action")));
        if (candidate == null) return new ItemStack(Material.BARRIER);
        ItemBuilderUtil builder = ItemBuilderUtil.getBuilderForMaterial(section.getString("material"), section.getString("data"));
        builder.addName(section.getString("name"));
        builder.setLorePlaceholders("{total votes}", "{player votes}");
        builder.addLore(section.getStringList("lore"), NumberFormatUtil.format(candidate.getTotalVotes()), NumberFormatUtil.format(candidate.getPlayerVotes(player.getUniqueId())));
        builder.addEnchantments(section.getStringList("enchantments"));
        builder.addItemFlags(section.getStringList("item-flags"));
        builder.addNBT(section.getBoolean("unbreakable"));
        return builder.getItem();
    }

    private int getCandidateIdFromConfig(String actionFromConfig) {
        String[] parts = actionFromConfig.split(":");
        return Integer.parseInt(parts[1]);
    }
}
