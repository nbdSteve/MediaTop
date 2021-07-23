package gg.steve.mc.dazzer.mt.data.exception;

import gg.steve.mc.dazzer.mt.exception.AbstractException;
import gg.steve.mc.dazzer.mt.exception.ExceptionClass;

import java.util.UUID;

@ExceptionClass
public class VoteDataYmlFileNotFoundException extends AbstractException {
    private final UUID ymlFileId;

    public VoteDataYmlFileNotFoundException(UUID ymlFileId) {
        this.ymlFileId = ymlFileId;
    }

    @Override
    public String getDebugMessage() {
        return "Unable to locate the desired yml file with id: " + String.valueOf(this.ymlFileId) + ", please check the data folder.";
    }
}
