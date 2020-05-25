package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.Right;

public class RightRepository implements BasicRepository<Right> {

    private final Connection connection;

    public RightRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Right> getById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM rights WHERE id = '" + id + "'");

        if (resultSet.next()) {
            String shortname = resultSet.getString("shortname");
            String fullname = resultSet.getString("fullname");
            String url = resultSet.getString("url");

            return Optional.of(new Right(id, shortname, fullname, url));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Right[] getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM rights");

        ArrayList<Right> rightList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String shortname = resultSet.getString("shortname");
            String fullname = resultSet.getString("fullname");
            String url = resultSet.getString("url");

            rightList.add(new Right(id, shortname, fullname, url));
        }

        return rightList.toArray(new Right[0]);
    }
}
