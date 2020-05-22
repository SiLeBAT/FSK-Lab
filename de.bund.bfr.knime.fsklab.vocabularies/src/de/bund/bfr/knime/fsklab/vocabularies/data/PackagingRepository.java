package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.bund.bfr.knime.fsklab.vocabularies.domain.Packaging;

public class PackagingRepository implements BasicRepository<Packaging> {

    private final Connection connection;

    public PackagingRepository(Connection connection) {
        this.connection = connection;
    }


    @Override
    public Packaging getById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM packaging WHERE id = " + id);

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String ssd = resultSet.getString("ssd");
            String comment = resultSet.getString("comment");

            return new Packaging(id, name, ssd, comment);
        } else {
            return null;
        }
    }

    @Override
    public Packaging[] getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM packaging");

        ArrayList<Packaging> packagingList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String ssd = resultSet.getString("ssd");
            String comment = resultSet.getString("comment");

            packagingList.add(new Packaging(id, name, ssd, comment));
        }

        return packagingList.toArray(new Packaging[0]);
    }
}
