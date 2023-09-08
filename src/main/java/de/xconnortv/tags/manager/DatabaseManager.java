package de.xconnortv.tags.manager;

import de.xconnortv.tags.Tags;
import de.xconnortv.tags.manager.database.DatabaseInfo;
import de.xconnortv.tags.manager.database.DatabaseType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.stream.Collectors;


public class DatabaseManager {

    private Tags instance;
    private DatabaseType type;
    private Connection connection;

    public DatabaseManager(Tags instance, DatabaseType type) {
        this.instance = instance;
        this.type = type;

    }
    public DatabaseManager(Tags instance) {
        this(instance, DatabaseType.MYSQL);
    }

    public void connect(){
        try {
            DatabaseInfo info = check();
            if(this.connection == null) {
                String jdbc = info.getJDBC();
                this.connection = DriverManager.getConnection(jdbc, info.getUser(), info.getPassword());
                if(!connection.isValid(1)) throw new SQLException("Cannot reach Database");
                setupTables();
            }
        } catch (Exception e) {
            System.out.println("Checks failed:\n" + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(instance);
        }

    }

    public void disconnect(){
        try {
            this.connection.close();
            this.connection = null;
        } catch (SQLException e) {
            System.out.println("Failed to close:\n" + e.getMessage());
        }
    }

    public void setupTables() throws Exception {
        String setup = null;
        try (InputStream in = instance.getResource("setup.sql")) {
            setup = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            instance.getLogger().log(Level.SEVERE, "Could not read db setup file.", e);
        }
        String[] queries = setup.split(";");
        for (String query : queries) {
            if (query.isBlank()) continue;
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.execute();
        }
    }

    private DatabaseInfo check() throws Exception{
        FileConfiguration config = instance.getDefaultConfig().get();
        DatabaseInfo info = DatabaseInfo.parseFromConfig(config);
        if(info.isNotProvided()) throw new Exception("Some database configs are missing");
        return info;
    }

    public Connection getConnection(boolean init) {
        if(init) connect();
        return connection;
    }
    public Connection getConnection() {
        return getConnection(false);
    }
}
