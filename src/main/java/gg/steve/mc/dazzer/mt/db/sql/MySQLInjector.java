package gg.steve.mc.dazzer.mt.db.sql;

import gg.steve.mc.dazzer.mt.SPlugin;
import gg.steve.mc.dazzer.mt.db.DatabaseImplementation;
import gg.steve.mc.dazzer.mt.utility.LogUtil;

import java.sql.DriverManager;
import java.sql.SQLException;

@DatabaseInjectorClass
public class MySQLInjector extends AbstractDatabaseInjector {

    public MySQLInjector(SPlugin sPlugin) {
        super(DatabaseImplementation.MYSQL, sPlugin);
    }

    @Override
    public void connect() {
        try {
            this.setConnection(DriverManager.getConnection("jdbc:mysql://" + this.getHost() + ":" + this.getPort() + "/" + this.getDatabase() + "?autoReconnect=true&useSSL=" + this.isSsl(), this.getUser(), this.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.info("There was an error connecting to the MediaTop SQL database: " + e.getMessage());
            return;
        }
        LogUtil.info("Successfully connected to the MediaTop SQL database.");
    }

    @Override
    public void disconnect() {
        try {
            if (this.getConnection() != null && !this.getConnection().isClosed()) this.getConnection().close();
            LogUtil.info("Successfully disconnected from the MediaTop SQL database.");
        } catch (SQLException e) {
            LogUtil.info("There was an error disconnecting from the MediaTop SQL database: " + e.getMessage());
        }
    }
}
