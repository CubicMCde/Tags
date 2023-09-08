package de.xconnortv.tags.manager.database;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public class DatabaseInfo {
    private DatabaseType type;
    private String host;
    private int port;
    private String database;
    private String user;
    private String password;

    public DatabaseInfo(DatabaseType type, String host, int port, String database, String user, String password) {
        this.type = type;
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public static DatabaseInfo parseFromConfig(FileConfiguration configuration) {
        DatabaseType type = DatabaseType.MYSQL;
        String host = configuration.getString("database.host");
        int port = configuration.getInt("database.port");
        String database = configuration.getString("database.database");
        String user = configuration.getString("database.user");
        String password = configuration.getString("database.password");
        return new DatabaseInfo(type, host, port, database, user, password);

    }

    public boolean isNotProvided(){
        return (type == null || host.isEmpty() || port == 0 || database.isEmpty() || user.isEmpty() || password.isEmpty());
    }

    public String getJDBC(){
        if(isNotProvided()) return null;

        return String.format("jdbc:mysql://@%s:%d/%s", host, port, database);
    }

}
