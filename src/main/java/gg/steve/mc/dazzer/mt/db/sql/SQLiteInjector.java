package gg.steve.mc.dazzer.mt.db.sql;

import gg.steve.mc.dazzer.mt.SPlugin;
import gg.steve.mc.dazzer.mt.db.DatabaseImplementation;
import gg.steve.mc.dazzer.mt.utility.LogUtil;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

@DatabaseInjectorClass
public class SQLiteInjector extends AbstractDatabaseInjector {

    public SQLiteInjector(SPlugin sPlugin) {
        super(DatabaseImplementation.SQLITE, sPlugin);
    }

    @Override
    public void connect() {
        try {
            File dataFolder = new File(super.getSPlugin().getPlugin().getDataFolder(), "data");
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }
            File folder = new File(super.getSPlugin().getPlugin().getDataFolder() + File.separator + "data", super.getDatabase() + ".db");
            if (!folder.exists()) {
                try {
                    folder.createNewFile();
                } catch (IOException e) {
                    LogUtil.warning("Error creating the MeidaTop SQLite db file");
                }
            }
            Class.forName("org.sqlite.JDBC");
            this.setConnection(DriverManager.getConnection("jdbc:sqlite:" + folder));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.info("There was an error connecting to the MeidaTop SQLite database: " + e.getMessage());
            return;
        }
        LogUtil.info("Successfully connected to the MeidaTop SQLite database.");
    }

    @Override
    public void disconnect() {
        try {
            if (super.getConnection() != null && !super.getConnection().isClosed()) super.getConnection().close();
            LogUtil.info("Successfully disconnected from the MeidaTop SQLite database.");
        } catch (SQLException e) {
            LogUtil.info("There was an error disconnecting from the MeidaTop SQLite database: " + e.getMessage());
        }
    }
}
