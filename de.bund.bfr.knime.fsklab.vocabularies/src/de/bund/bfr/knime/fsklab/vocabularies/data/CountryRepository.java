package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.bund.bfr.knime.fsklab.vocabularies.domain.Country;

public class CountryRepository implements BasicRepository<Country> {

    private final Connection connection;

    public CountryRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Country getById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM country WHERE id = " + id);

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String iso = resultSet.getString("iso");

            return new Country(id, name, iso);
        } else {
            return null;
        }
    }

    @Override
    public Country[] getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM country");

        ArrayList<Country> countryList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String iso = resultSet.getString("iso");

            countryList.add(new Country(id, name, iso));
        }

        return countryList.toArray(new Country[0]);
    }
}
