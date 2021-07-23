package gg.steve.mc.dazzer.mt.cmd.implementation;

import gg.steve.mc.dazzer.mt.cmd.AbstractCommand;
import org.bukkit.command.CommandSender;

public class MediaTopCommand extends AbstractCommand {

    public MediaTopCommand(String permissionKey) {
        super("MediaTop", permissionKey);
    }

    @Override
    public void registerAllSubCommands() {

    }

    @Override
    public void runOnNoArgsGiven(CommandSender sender) {

    }

    @Override
    public void registerAliases() {
        this.registerAlias("mt");
        this.registerAlias("media");
    }
}
