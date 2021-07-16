package gg.steve.mc.dazzer.mt.gui.action;

import gg.steve.mc.dazzer.mt.gui.AbstractGui;
import gg.steve.mc.dazzer.mt.xseries.XItemStack;
import lombok.Data;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Data
public abstract class AbstractInventoryClickAction {
    private final String uniqueName;
    private final int args;

    public AbstractInventoryClickAction( String uniqueName, int args) {
        this.uniqueName = uniqueName;
        this.args = args;
    }

    public ItemStack getRenderedItem(Player player, ConfigurationSection section) {
        return XItemStack.deserialize(section);
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