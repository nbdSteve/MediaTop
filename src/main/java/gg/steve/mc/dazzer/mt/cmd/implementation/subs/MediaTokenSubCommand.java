package gg.steve.mc.dazzer.mt.cmd.implementation.subs;

import gg.steve.mc.dazzer.mt.cmd.AbstractCommand;
import gg.steve.mc.dazzer.mt.cmd.AbstractSubCommand;
import gg.steve.mc.dazzer.mt.message.MessageManager;
import gg.steve.mc.dazzer.mt.token.MediaTokenPlayerManager;
import gg.steve.mc.dazzer.mt.utility.NumberFormatUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * Command syntax: /mt token give, remove, set, reset, get
 */
public class MediaTokenSubCommand extends AbstractSubCommand {

    public MediaTokenSubCommand(AbstractCommand parent) {
        super(parent, "token", "token", 3, 4);
    }

    protected enum Argument {
        GIVE(new String[]{"give", "add"}),
        REMOVE(new String[]{"remove", "take"}),
        SET(new String[]{"set"}),
        GET(new String[]{"get", "g"}),
        RESET(new String[]{"reset"});

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
        int amount = 0;
        if (arguments.length == 4) {
            try {
                amount = Integer.parseInt(arguments[3]);
                if (amount < 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                MessageManager.getInstance().sendMessage("invalid-amount", executor);
                return;
            }
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(arguments[2]);
        int balance;
        switch (argument) {
            case GIVE:
                balance = MediaTokenPlayerManager.getInstance().give(offlinePlayer.getUniqueId(), amount);
                break;
            case REMOVE:
                balance = MediaTokenPlayerManager.getInstance().remove(offlinePlayer.getUniqueId(), amount);
                break;
            case SET:
                balance = MediaTokenPlayerManager.getInstance().set(offlinePlayer.getUniqueId(), amount);
                break;
            case GET:
                balance = MediaTokenPlayerManager.getInstance().getMediaTokenBalanceForPlayer(offlinePlayer.getUniqueId());
                MessageManager.getInstance().sendMessage("player-token-balance", executor, offlinePlayer.getName(), NumberFormatUtil.format(balance));
                return;
            case RESET:
                balance = MediaTokenPlayerManager.getInstance().reset(offlinePlayer.getUniqueId());
                break;
            default:
                this.getParent().doInvalidCommandMessage(executor);
                return;
        }
        MessageManager.getInstance().sendMessage("update-player-token-balance", executor, offlinePlayer.getName(), NumberFormatUtil.format(balance));
    }
}
