package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.Format;

public class FormatRepository implements BasicRepository<Format> {

	private final Connection connection;

	public FormatRepository(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Optional<Format> getById(int id) {

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM format WHERE id = " + id);
			if (resultSet.next()) {
				String name = resultSet.getString("name");
				String comment = resultSet.getString("comment");

				return Optional.of(new Format(id, name, comment));
			}

			return Optional.empty();
		} catch (SQLException e) {
			return Optional.empty();
		}
	}

	@Override
	public Format[] getAll() {

		ArrayList<Format> formatList = new ArrayList<>();

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM format");

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String comment = resultSet.getString("comment");

				formatList.add(new Format(id, name, comment));
			}
		} catch (SQLException e) {
		}

		return formatList.toArray(new Format[0]);
	}
}
