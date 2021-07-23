package gg.steve.mc.dazzer.mt.db;

import gg.steve.mc.dazzer.mt.SPlugin;
import gg.steve.mc.dazzer.mt.file.FileManager;
import gg.steve.mc.dazzer.mt.manager.AbstractManager;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class SQLDatabaseHandler extends AbstractSQLHandler {
    private static SQLDatabaseHandler instance;

    public SQLDatabaseHandler(SPlugin sPlugin) {
        super(sPlugin);
        instance = this;
        AbstractManager.registerManager(instance);
    }

    @Override
    public void onLoad() {
        DatabaseImplementation implementation = DatabaseImplementation.getImplementationUsed(FileManager.CoreFiles.CONFIG.get().getString("database.implementation"));
        this.setDatabaseImplementation(implementation);
        this.initialiseInjector();
        this.getInjector().connect();
    }

    @Override
    public void onShutdown() { this.getInjector().disconnect();
    }

    public static SQLDatabaseHandler getInstance() {
        return instance;
    }

    @Override
    protected String getManagerName() {
        return "SQL";
    }
}