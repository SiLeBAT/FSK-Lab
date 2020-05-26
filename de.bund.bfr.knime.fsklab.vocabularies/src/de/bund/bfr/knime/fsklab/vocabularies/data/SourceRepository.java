package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.Source;

public class SourceRepository implements BasicRepository<Source> {

	private final Connection connection;

	public SourceRepository(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Optional<Source> getById(int id) {

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM sources WHERE id = " + id);

			if (resultSet.next()) {
				String name = resultSet.getString("name");
				String comment = resultSet.getString("comment");

				return Optional.of(new Source(id, name, comment));
			}
			return Optional.empty();
		} catch (SQLException err) {
			return Optional.empty();
		}
	}

	@Override
	public Source[] getAll() {

		ArrayList<Source> sourceList = new ArrayList<>();

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM sources");

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String comment = resultSet.getString("comment");

				sourceList.add(new Source(id, name, comment));
			}
		} catch (SQLException err) {
		}

		return sourceList.toArray(new Source[0]);
	}
}
