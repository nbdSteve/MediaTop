package gg.steve.mc.dazzer.mt.file.types;

import gg.steve.mc.dazzer.mt.SPlugin;
import gg.steve.mc.dazzer.mt.file.AbstractPluginFile;
import gg.steve.mc.dazzer.mt.file.PluginFileClass;
import gg.steve.mc.dazzer.mt.file.PluginFileType;
import gg.steve.mc.dazzer.mt.gui.GuiManager;

import java.io.File;

@PluginFileClass
public class GuiPluginFile extends AbstractPluginFile {

    public GuiPluginFile(String name, File file, SPlugin sPlugin) {
        super(name, file, PluginFileType.GUI, sPlugin);
        // need to register the gui with the server
        GuiManager.getInstance().registerGuiFromFile(this);
    }
}
