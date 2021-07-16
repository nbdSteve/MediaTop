package gg.steve.mc.dazzer.mt.cmd.exception;

import gg.steve.mc.dazzer.mt.cmd.AbstractSubCommand;
import gg.steve.mc.dazzer.mt.exception.AbstractException;
import gg.steve.mc.dazzer.mt.exception.ExceptionClass;

@ExceptionClass
public class AliasAlreadyRegisteredException extends AbstractException {
    private final String alias;
    private final AbstractSubCommand subCommand;

    public AliasAlreadyRegisteredException(String alias, AbstractSubCommand subCommand) {
        this.alias = alias;
        this.subCommand = subCommand;
    }

    @Override
    public String getDebugMessage() {
        return "Tried to register alias, " + this.alias + ", but it was already registered for sub command: " + this.subCommand.getCommand();
    }
}
