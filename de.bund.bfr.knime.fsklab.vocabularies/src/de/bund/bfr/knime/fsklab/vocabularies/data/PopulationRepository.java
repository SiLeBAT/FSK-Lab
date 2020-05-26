package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.Population;

public class PopulationRepository implements BasicRepository<Population> {

	private final Connection connection;

	public PopulationRepository(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Optional<Population> getById(int id) {

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM population WHERE id = " + id);

			if (resultSet.next()) {
				String name = resultSet.getString("name");
				String foodon = resultSet.getString("foodon");
				return Optional.of(new Population(id, name, foodon));
			}
			return Optional.empty();
		} catch (SQLException err) {
			return Optional.empty();
		}
	}

	@Override
	public Population[] getAll() {
		ArrayList<Population> populationList = new ArrayList<>();

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM population");

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String foodon = resultSet.getString("foodon");

				populationList.add(new Population(id, name, foodon));
			}			
		} catch (SQLException err) {
		}

		return populationList.toArray(new Population[0]);
	}
}
