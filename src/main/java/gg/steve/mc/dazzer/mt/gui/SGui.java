package gg.steve.mc.dazzer.mt.gui;

import gg.steve.mc.dazzer.mt.SPlugin;
import gg.steve.mc.dazzer.mt.file.AbstractPluginFile;
import gg.steve.mc.dazzer.mt.gui.exception.InvalidConfigurationFileTypeException;

public class SGui extends AbstractGui {

    public SGui(String guiUniqueName, AbstractPluginFile configuration, SPlugin sPlugin) throws InvalidConfigurationFileTypeException {
        super(guiUniqueName, configuration, sPlugin);
    }

    @Override
    public void refreshInventoryContents() {
        super.applyComponentsToInventory(super.getOwner());
    }

    @Override
    public AbstractGui createDuplicateGui() {
        try {
            return new SGui(super.getGuiUniqueName(), super.getConfiguration(), super.getSPlugin());
        } catch (InvalidConfigurationFileTypeException e) {
            e.printStackTrace();
        }
        return null;
    }
}
