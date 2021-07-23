package gg.steve.mc.dazzer.mt.cmd.implementation.subs;

import gg.steve.mc.dazzer.mt.cmd.AbstractCommand;
import gg.steve.mc.dazzer.mt.cmd.AbstractSubCommand;
import gg.steve.mc.dazzer.mt.message.MessageManager;
import gg.steve.mc.dazzer.mt.utility.LogUtil;
import gg.steve.mc.dazzer.mt.vote.MediaVote;
import gg.steve.mc.dazzer.mt.vote.MediaVoteManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * Command syntax: /mt vote {start, end}
 */
public class MediaVoteSubCommand extends AbstractSubCommand {

    public MediaVoteSubCommand(AbstractCommand parent) {
        super(parent, "vote", "vote", 2, 2);
    }

    protected enum Argument {
        START(new String[]{"start", "s", "begin"}),
        END(new String[]{"end", "e", "stop"});

        private String[] aliases;

        Argument(String[] aliases) {
            this.aliases = aliases;
        }

        protected static Argument getArgumentFromString(String query) {
            for (Argument argument : Argument.values()) {
                if (Arrays.stream(argument.getAliases()).anyMatch(s -> s.equalsIgnoreCase(query))) return argument;
            }
            return null;
        }

        protected String[] getAliases() {
            return aliases;
        }
    }

    @Override
    public void run(CommandSender executor, String[] arguments) {
        Argument argument = Argument.getArgumentFromString(arguments[1]);
        if (argument == null) {
            this.getParent().doInvalidCommandMessage(executor);
            return;
        }
        switch (argument) {
            case START:
                this.start(executor);
                break;
            case END:
                this.end(executor);
                break;
            default:
                this.getParent().doInvalidCommandMessage(executor);
        }
    }

    private void start(CommandSender executor) {
        if (MediaVoteManager.getInstance().startMediaVote()) {
            MediaVote vote = MediaVoteManager.getInstance().getActiveMediaVote();
            MessageManager.getInstance().sendMessage("vote-start", executor, String.valueOf(vote.getVoteId()));
        } else {
            MessageManager.getInstance().sendMessage("vote-active", executor);
        }
    }

    private void end(CommandSender executor) {
        if (MediaVoteManager.getInstance().endActiveMediaVote()) {
            MessageManager.getInstance().sendMessage("vote-end", executor);
        } else {
            MessageManager.getInstance().sendMessage("no-vote-active", executor);
        }
    }
}
