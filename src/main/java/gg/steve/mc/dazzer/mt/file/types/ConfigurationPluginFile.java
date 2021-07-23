package gg.steve.mc.dazzer.mt.file.types;

import gg.steve.mc.dazzer.mt.SPlugin;
import gg.steve.mc.dazzer.mt.file.AbstractPluginFile;
import gg.steve.mc.dazzer.mt.file.PluginFileClass;
import gg.steve.mc.dazzer.mt.file.PluginFileType;

import java.io.File;

@PluginFileClass
public class ConfigurationPluginFile extends AbstractPluginFile {

    public ConfigurationPluginFile(String name, File file, SPlugin sPlugin) {
        super(name, file, PluginFileType.CONFIGURATION, sPlugin);
    }
}
