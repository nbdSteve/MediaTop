package gg.steve.mc.dazzer.mt.file.types;

import gg.steve.mc.dazzer.mt.SPlugin;
import gg.steve.mc.dazzer.mt.file.AbstractPluginFile;
import gg.steve.mc.dazzer.mt.file.PluginFileClass;
import gg.steve.mc.dazzer.mt.file.PluginFileType;
import gg.steve.mc.dazzer.mt.message.MessageManager;

import java.io.File;

@PluginFileClass
public class MessagePluginFile extends AbstractPluginFile {

    public MessagePluginFile(String name, File file, SPlugin sPlugin) {
        super(name, file, PluginFileType.MESSAGE, sPlugin);
        // Register all messages from the file with the plugin
        MessageManager.getInstance().registerMessagesFromFile(this);
    }
}
