package gg.steve.mc.dazzer.mt.db;

public interface DatabaseHandler {

    String query(String sql, String field);

    void update(String sql);

    void delete(String sql);

    void insert(String sql, String... values);

    void execute(String sql);
}
