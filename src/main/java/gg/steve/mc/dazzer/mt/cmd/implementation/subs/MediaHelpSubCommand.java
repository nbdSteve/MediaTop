package gg.steve.mc.dazzer.mt.cmd.implementation.subs;

import gg.steve.mc.dazzer.mt.cmd.AbstractCommand;
import gg.steve.mc.dazzer.mt.cmd.AbstractSubCommand;
import gg.steve.mc.dazzer.mt.message.MessageManager;
import org.bukkit.command.CommandSender;

public class MediaHelpSubCommand extends AbstractSubCommand {

    public MediaHelpSubCommand(AbstractCommand parent) {
        super(parent, "help", "help", 1, 1);
        this.registerAlias("h");
    }

    @Override
    public void run(CommandSender executor, String[] arguments) {
        MessageManager.getInstance().sendMessage("help", executor);
    }
}
