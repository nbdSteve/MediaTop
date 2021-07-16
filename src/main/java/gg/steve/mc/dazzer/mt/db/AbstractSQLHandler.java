package gg.steve.mc.dazzer.mt.db;

import gg.steve.mc.dazzer.mt.SPlugin;
import gg.steve.mc.dazzer.mt.db.sql.AbstractDatabaseInjector;
import gg.steve.mc.dazzer.mt.manager.AbstractManager;
import gg.steve.mc.dazzer.mt.utility.LogUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractSQLHandler extends AbstractManager implements DatabaseHandler {
    private final SPlugin sPlugin;
    private AbstractDatabaseInjector injector;
    private DatabaseImplementation implementation;

    public AbstractSQLHandler(SPlugin sPlugin) {
        this.sPlugin = sPlugin;
    }

    public void setDatabaseImplementation(DatabaseImplementation implementation) {
        this.implementation = implementation;
    }

    public void initialiseInjector() {
        this.injector = DatabaseImplementation.getInjectorInstanceForImplementation(this.implementation, this.sPlugin);
        this.injector.setDatabaseCredentials();
    }

    @Override
    public String query(String sql, String field) {
        Connection connection = injector.getConnection();
        String result = "";
        synchronized (this.sPlugin.getPlugin()) {
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rs = statement.executeQuery();
                result = rs.getString(field);
                statement.close();
            } catch (SQLException e) {
                LogUtil.warning("An error occurred while trying to execute an sql query.");
            }
        }
        return result;
    }

    @Override
    public void update(String sql) {
        Connection connection = injector.getConnection();
        Bukkit.getScheduler().runTaskAsynchronously(this.sPlugin.getPlugin(), () -> {
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                LogUtil.warning("An error occurred while trying to execute an sql update statement.");
            }
        });
    }

    @Override
    public void delete(String sql) {
        Connection connection = injector.getConnection();
        Bukkit.getScheduler().runTaskAsynchronously(this.sPlugin.getPlugin(), () -> {
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                LogUtil.warning("An error occurred while trying to execute an sql delete statement.");
            }
        });
    }

    @Override
    public void insert(String sql, String... values) {
        Connection connection = injector.getConnection();
        Bukkit.getScheduler().runTaskAsynchronously(this.sPlugin.getPlugin(), () -> {
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                if (values != null) {
                    List<String> replacements = Arrays.asList(values);
                    for (int i = 1; i <= replacements.size(); i++) {
                        statement.setString(i, replacements.get(i - 1));
                    }
                }
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                LogUtil.warning("An error occurred while trying to execute an sql insert statement.");
            }
        });
    }

    @Override
    public void execute(String sql) {
        Connection connection = injector.getConnection();
        Bukkit.getScheduler().runTaskAsynchronously(this.sPlugin.getPlugin(), () -> {
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.execute();
                statement.close();
            } catch (SQLException e) {
                LogUtil.warning("An error occurred while trying to execute an sql execute statement.");
            }
        });
    }
}
