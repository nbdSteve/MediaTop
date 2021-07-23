package gg.steve.mc.dazzer.mt.cmd.implementation.subs;

import gg.steve.mc.dazzer.mt.cmd.AbstractCommand;
import gg.steve.mc.dazzer.mt.cmd.AbstractSubCommand;
import gg.steve.mc.dazzer.mt.gui.GuiManager;
import gg.steve.mc.dazzer.mt.gui.exception.AbstractGuiNotFoundException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MediaGuiSubCommand extends AbstractSubCommand {

    public MediaGuiSubCommand(AbstractCommand parent) {
        super(parent, "gui", "media", 0, 1);
    }

    @Override
    public void run(CommandSender executor, String[] arguments) {
        if (!(executor instanceof Player)) return;
        try {
            GuiManager.getInstance().openGui((Player) executor, "vote-gui");
        } catch (AbstractGuiNotFoundException e) {
            e.printStackTrace();
        }
    }
}
