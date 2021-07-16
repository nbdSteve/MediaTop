package gg.steve.mc.dazzer.mt.placeholder;

public enum PlaceholderProviderType {
    PAPI("PlaceholderAPI");

    private String pluginName;

    PlaceholderProviderType(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getProviderPlugin() {
        return this.pluginName;
    }
}
