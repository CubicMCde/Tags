package de.xconnortv.tags.manager;

import de.xconnortv.tags.Tags;
import de.xconnortv.tags.manager.tags.Tag;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TagManager {

    private final Tags instance;
    public TagManager(Tags instance) {
        this.instance = instance;
    }

    public Optional<Tag> getTag(UUID uuid) {
        try {
            Connection connection = this.instance.getDatabaseManager().getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM Tags where uuid = ?"
            );
            stmt.setString(1, uuid.toString());

            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()) {
                return Optional.of(
                        new Tag(
                                UUID.fromString(resultSet.getString("uuid")),
                                resultSet.getString("name"),
                                resultSet.getString("symbol")));
            }
            return Optional.empty();

        }catch (Exception e) {
            System.out.println("Could not get Data:\n" + e.getMessage());
            return Optional.empty();
        }
    }

    public boolean addTag(Tag tag) {
        try {
            Connection connection = this.instance.getDatabaseManager().getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO Tags (uuid, name ,symbol) VALUES (?, ?, ?);"
            );
            stmt.setString(1, tag.getUuid().toString());
            stmt.setString(2, tag.getName());
            stmt.setString(3, tag.getSymbol());
            return stmt.execute();
        }catch (Exception e) {
            System.out.println("Could not set Data:\n" + e.getMessage());
            return false;
        }
    }

    @NotNull
    private Optional<List<Tag>> fetchTags(PreparedStatement stmt) throws SQLException {
        ResultSet resultSet = stmt.executeQuery();
        List<Tag> tags = new LinkedList<>();
        while (resultSet.next()) {
            tags.add(
                    new Tag(
                            UUID.fromString(resultSet.getString("uuid")),
                            resultSet.getString("name"),
                            resultSet.getString("symbol"))
            );
        }
        return Optional.of(tags);
    }

    public Optional<List<Tag>> getTags() {
        try {
            Connection connection = this.instance.getDatabaseManager().getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM Tags"
            );

            return fetchTags(stmt);

        }catch (Exception e) {
            System.out.println("Could not get Data:\n" + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<List<Tag>> getTags(int offset, int limit) {
        try {
            Connection connection = this.instance.getDatabaseManager().getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM Tags LIMIT ?, ?"
             );
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);

            return fetchTags(stmt);

        }catch (Exception e) {
            System.out.println("Could not get Data:\n" + e.getMessage());
            return Optional.empty();
        }
    }


    public boolean setPlayerTag(Player player, Tag tag) {
        try {
            Connection connection = this.instance.getDatabaseManager().getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO PlayerTags (uuid, tag) VALUES (?, ?) ON DUPLICATE KEY UPDATE tag = ?"
            );
            stmt.setString(1, player.getUniqueId().toString());
            stmt.setString(2, tag.getUuid().toString());
            stmt.setString(3, tag.getUuid().toString());
            return stmt.execute();
        }catch (Exception e) {
            System.out.println("Could not set Data:\n" + e.getMessage());
            return false;
        }
    }

    public boolean hasPlayerTag(Player player){
        try {
            Connection connection = this.instance.getDatabaseManager().getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM PlayerTags WHERE uuid = ?"
            );
            stmt.setString(1, player.getUniqueId().toString());
            ResultSet set = stmt.executeQuery();
            return set.next();
        }catch (Exception e) {
            System.out.println("Could not set Data:\n" + e.getMessage());
            return false;
        }
    }


    public boolean removePlayerTag(Player player){
        try {
            Connection connection = this.instance.getDatabaseManager().getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM PlayerTags WHERE uuid = ?"
            );
            stmt.setString(1, player.getUniqueId().toString());
            return stmt.execute();
        }catch (Exception e) {
            System.out.println("Could not set Data:\n" + e.getMessage());
            return false;
        }
    }



    public Optional<Tag> getPlayerTag(Player player) {
        try {
            Connection connection = this.instance.getDatabaseManager().getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT PlayerTags.uuid, Tags.symbol, Tags.name FROM PlayerTags JOIN Tags ON PlayerTags.tag = Tags.uuid WHERE PlayerTags.uuid = ?;"
            );
            stmt.setString(1, player.getUniqueId().toString());

            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()) {
                return Optional.of( new Tag(
                        UUID.fromString(resultSet.getString("uuid")),
                        resultSet.getString("name"),
                        resultSet.getString("symbol"))
                    );
            }

        }catch (Exception e) {
            System.out.println("Could not get Data:\n" + e.getMessage());
            return Optional.empty();
        }
        return Optional.empty();
    }
}
