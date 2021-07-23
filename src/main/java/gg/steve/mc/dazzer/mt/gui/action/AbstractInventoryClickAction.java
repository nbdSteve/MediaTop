package gg.steve.mc.dazzer.mt.gui.action;

import gg.steve.mc.dazzer.mt.gui.AbstractGui;
import gg.steve.mc.dazzer.mt.token.MediaTokenPlayer;
import gg.steve.mc.dazzer.mt.token.MediaTokenPlayerManager;
import gg.steve.mc.dazzer.mt.utility.ItemBuilderUtil;
import gg.steve.mc.dazzer.mt.utility.NumberFormatUtil;
import lombok.Data;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Data
public abstract class AbstractInventoryClickAction {
    private final String uniqueName;
    private final int args;

    public AbstractInventoryClickAction(String uniqueName, int args) {
        this.uniqueName = uniqueName;
        this.args = args;
    }

    public ItemStack getRenderedItem(Player player, ConfigurationSection section) {
        MediaTokenPlayer tokenPlayer = MediaTokenPlayerManager.getInstance().getMediaTokenPlayer(player.getUniqueId());
        ItemBuilderUtil builder = ItemBuilderUtil.getBuilderForMaterial(section.getString("material"), section.getString("data"));
        builder.addName(section.getString("name"));
        builder.setLorePlaceholders("{tokens}");
        builder.addLore(section.getStringList("lore"), NumberFormatUtil.format(tokenPlayer.getBalance()));
        builder.addEnchantments(section.getStringList("enchantments"));
        builder.addItemFlags(section.getStringList("item-flags"));
        builder.addNBT(section.getBoolean("unbreakable"));
        return builder.getItem();
    }

    public abstract void onClick(AbstractGui gui, Player player, ConfigurationSection section, int slot);

    public boolean isUniqueNameMatch(String query) {
        return this.uniqueName.equalsIgnoreCase(query);
    }

    public boolean isValidFormat(String actionFromConfig) {
        String[] parts = actionFromConfig.split(":");
        if (!isUniqueNameMatch(parts[0])) return false;
        return this.args == parts.length;
    }
}
