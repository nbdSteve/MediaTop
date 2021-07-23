package gg.steve.mc.dazzer.mt.message.exception;

import gg.steve.mc.dazzer.mt.exception.AbstractException;
import gg.steve.mc.dazzer.mt.exception.ExceptionClass;

@ExceptionClass
public class PluginMessageNotFoundException extends AbstractException {
    private final String key;

    public PluginMessageNotFoundException(String key) {
        this.key = key;
    }

    @Override
    public String getDebugMessage() {
        return "Unable to find the plugin message with the key: " + this.key + ".";
    }
}
