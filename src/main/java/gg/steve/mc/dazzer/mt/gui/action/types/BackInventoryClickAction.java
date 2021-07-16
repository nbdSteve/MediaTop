package gg.steve.mc.dazzer.mt.gui.action.types;

import gg.steve.mc.dazzer.mt.gui.AbstractGui;
import gg.steve.mc.dazzer.mt.gui.GuiManager;
import gg.steve.mc.dazzer.mt.gui.action.AbstractInventoryClickAction;
import gg.steve.mc.dazzer.mt.gui.exception.AbstractGuiNotFoundException;
import gg.steve.mc.dazzer.mt.utility.LogUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class BackInventoryClickAction extends AbstractInventoryClickAction {

    public BackInventoryClickAction() {
        super("back", 1);
    }

    @Override
    public void onClick(AbstractGui gui, Player player, ConfigurationSection section, int slot) {
        if (!gui.isHasParentGui()) return;
        gui.close(player);
        try {
            GuiManager.getInstance().openGui(player, gui.getParentGuiUniqueName());
        } catch (AbstractGuiNotFoundException e) {
            LogUtil.warning(e.getDebugMessage());
            e.printStackTrace();
        }
    }
}