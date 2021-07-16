package gg.steve.mc.dazzer.mt.exception;

import gg.steve.mc.dazzer.mt.utility.LogUtil;
import lombok.Data;

@Data
public abstract class AbstractException extends Exception {

    protected AbstractException() {
        LogUtil.warning(getDebugMessage());
    }

    public abstract String getDebugMessage();
}
