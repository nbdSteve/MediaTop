package gg.steve.mc.dazzer.mt.file;

import java.util.Locale;

public enum PluginFileType {
    CONFIGURATION,
    MESSAGE,
    DATA,
    PERMISSION,
    GUI;

    public static PluginFileType getFileTypeFromString(String type) {
        try {
            return PluginFileType.valueOf(type.toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            return PluginFileType.CONFIGURATION;
        }
    }
}
