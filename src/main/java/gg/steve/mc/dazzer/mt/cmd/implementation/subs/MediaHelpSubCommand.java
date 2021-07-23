package gg.steve.mc.dazzer.mt.cmd.implementation.subs;

import gg.steve.mc.dazzer.mt.cmd.AbstractCommand;
import gg.steve.mc.dazzer.mt.cmd.AbstractSubCommand;
import org.bukkit.command.CommandSender;

public class MediaHelpSubCommand extends AbstractSubCommand {

    public MediaHelpSubCommand(AbstractCommand parent, String command, String permissionKey, int minArgLength, int maxArgLength) {
        super(parent, command, permissionKey, minArgLength, maxArgLength);
    }

    @Override
    public void run(CommandSender executor, String[] arguments) {

    }
}
