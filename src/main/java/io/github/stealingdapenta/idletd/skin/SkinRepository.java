package io.github.stealingdapenta.idletd.skin;

import static io.github.stealingdapenta.idletd.Idletd.LOGGER;
import static io.github.stealingdapenta.idletd.database.DatabaseManager.getDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SkinRepository {
    // todo add a save, so players can save their own skin and apply to their agent?

    public Skin getSkin(long id) {
        try (Connection connection = getDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM SKIN WHERE ID = ?")) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return convertResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error getting Skin by ID.");
            e.printStackTrace();
        }
        return null;
    }

    public List<Skin> getAllSkins() {
        List<Skin> skinList = new ArrayList<>();

        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM SKIN")) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    skinList.add(convertResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error getting all skins from the database.");
            e.printStackTrace();
        }

        return skinList;
    }

    private Skin convertResultSet(ResultSet resultSet) throws SQLException {
        return Skin.builder()
                   .id(resultSet.getLong("ID"))
                   .name(resultSet.getString("NAME"))
                   .description(resultSet.getString("DESCRIPTION"))
                   .dataToken(resultSet.getString("DATA_TOKEN"))
                   .signatureToken(resultSet.getString("SIGNATURE_TOKEN"))
                   .build();
    }
}
