package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.Country;

public class CountryRepository implements BasicRepository<Country> {

	private final Connection connection;

	public CountryRepository(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Optional<Country> getById(int id) {
		
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM country WHERE id = " + id);

			if (resultSet.next()) {
				String name = resultSet.getString("name");
				String iso = resultSet.getString("iso");

				return Optional.of(new Country(id, name, iso));
			}
			
			return Optional.empty();
		} catch (SQLException e) {
			return Optional.empty();
		}
	}

	@Override
	public Country[] getAll() {

		ArrayList<Country> countryList = new ArrayList<>();

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM country");

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String iso = resultSet.getString("iso");

				countryList.add(new Country(id, name, iso));
			}

		} catch (SQLException e) {
		}

		return countryList.toArray(new Country[0]);
	}
}