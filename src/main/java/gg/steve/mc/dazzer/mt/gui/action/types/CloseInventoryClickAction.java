package gg.steve.mc.dazzer.mt.gui.action.types;

import gg.steve.mc.dazzer.mt.gui.AbstractGui;
import gg.steve.mc.dazzer.mt.gui.action.AbstractInventoryClickAction;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class CloseInventoryClickAction extends AbstractInventoryClickAction {

    public CloseInventoryClickAction() {
        super("close", 1);
    }

    @Override
    public void onClick(AbstractGui gui, Player player, ConfigurationSection section, int slot) {
        player.closeInventory();
    }
}
