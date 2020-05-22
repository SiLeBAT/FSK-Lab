package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.bund.bfr.knime.fsklab.vocabularies.domain.Right;

public class RightRepository implements BasicRepository<Right> {

    private final Connection connection;

    public RightRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Right getById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM rights WHERE id = '" + id + "'");

        if (resultSet.next()) {
            String shortname = resultSet.getString("shortname");
            String fullname = resultSet.getString("fullname");
            String url = resultSet.getString("url");

            return new Right(id, shortname, fullname, url);
        } else {
            return null;
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
