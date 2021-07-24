package gg.steve.mc.dazzer.mt.cmd.implementation.subs;

import gg.steve.mc.dazzer.mt.SPlugin;
import gg.steve.mc.dazzer.mt.cmd.AbstractCommand;
import gg.steve.mc.dazzer.mt.cmd.AbstractSubCommand;
import gg.steve.mc.dazzer.mt.message.MessageManager;
import gg.steve.mc.dazzer.mt.utility.NumberFormatUtil;
import org.bukkit.command.CommandSender;

public class MediaReloadSubCommand extends AbstractSubCommand {

    public MediaReloadSubCommand(AbstractCommand parent) {
        super(parent, "reload", "reload", 1, 1);
        this.registerAlias("r");
    }

    @Override
    public void run(CommandSender executor, String[] arguments) {
        SPlugin.getSPluginInstance().getPlugin().onDisable();
        SPlugin.getSPluginInstance().getPlugin().onLoad();
        SPlugin.getSPluginInstance().getPlugin().onEnable();
        MessageManager.getInstance().sendMessage("reload", executor, "SUCCESS");
    }
}
