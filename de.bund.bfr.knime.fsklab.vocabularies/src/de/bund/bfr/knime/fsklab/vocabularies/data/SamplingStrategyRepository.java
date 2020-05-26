package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.SamplingStrategy;

public class SamplingStrategyRepository implements BasicRepository<SamplingStrategy> {

	private final Connection connection;

	public SamplingStrategyRepository(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Optional<SamplingStrategy> getById(int id) {

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM sampling_strategy WHERE id = " + id);

			if (resultSet.next()) {
				String name = resultSet.getString("name");
				String comment = resultSet.getString("comment");
				return Optional.of(new SamplingStrategy(id, name, comment));
			}
			return Optional.empty();
		} catch (SQLException err) {
			return Optional.empty();
		}
	}

	@Override
	public SamplingStrategy[] getAll() {

		ArrayList<SamplingStrategy> strategies = new ArrayList<>();

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM sampling_strategy");

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String comment = resultSet.getString("comment");

				strategies.add(new SamplingStrategy(id, name, comment));
			}
		} catch (SQLException err) {
		}

		return strategies.toArray(new SamplingStrategy[0]);
	}
}
