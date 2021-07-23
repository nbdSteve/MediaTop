package gg.steve.mc.dazzer.mt.cmd.implementation.subs;

import gg.steve.mc.dazzer.mt.cmd.AbstractCommand;
import gg.steve.mc.dazzer.mt.cmd.AbstractSubCommand;
import org.bukkit.command.CommandSender;

public class MediaGuiSubCommand extends AbstractSubCommand {

    public MediaGuiSubCommand(AbstractCommand parent) {
        super(parent, "gui", "media", 1, 1);
    }

    @Override
    public void run(CommandSender executor, String[] arguments) {

    }
}
