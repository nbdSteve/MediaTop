package gg.steve.mc.dazzer.mt.cmd.implementation;

import gg.steve.mc.dazzer.mt.cmd.AbstractCommand;
import gg.steve.mc.dazzer.mt.cmd.AbstractSubCommand;
import gg.steve.mc.dazzer.mt.cmd.implementation.subs.MediaGuiSubCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class MediaCommand extends AbstractCommand {

    public MediaCommand() {
        super("media", "media");
    }

    @Override
    public void registerAllSubCommands() {
        this.registerSubCommand(new MediaGuiSubCommand(this));
    }

    @Override
    public void runOnNoArgsGiven(CommandSender sender) {
        if (this.getSubCommands() != null && this.getSubCommands().containsKey("gui"))
            this.getSubCommands().get("gui").execute(sender, new String[0]);
    }

    @Override
    public void registerAliases() {

    }

    @Override
    public List<String> onTabComplete(CommandSender executor, String[] arguments) {
        List<String> options = new ArrayList<>();
        if (arguments.length == 1 || arguments.length == 2) {
            for (AbstractSubCommand subCommand : this.getSubCommands().values()) {
                options.add(subCommand.getCommand());
            }
        }
        return options;
    }
}
