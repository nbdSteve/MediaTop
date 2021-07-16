package gg.steve.mc.dazzer.mt.file.types;

import gg.steve.mc.dazzer.mt.SPlugin;
import gg.steve.mc.dazzer.mt.file.AbstractPluginFile;
import gg.steve.mc.dazzer.mt.file.PluginFileClass;
import gg.steve.mc.dazzer.mt.file.PluginFileType;

import java.io.File;

@PluginFileClass
public class PermissionPluginFile extends AbstractPluginFile {

    public PermissionPluginFile(String name, File file, SPlugin sPlugin) {
        super(name, file, PluginFileType.PERMISSION, sPlugin);
        // Register all of the permissions from the file with the plugin
        sPlugin.getPermissionManager().registerPermissionsFromFile(this);
    }
}
