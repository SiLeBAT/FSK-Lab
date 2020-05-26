package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.PublicationStatus;

public class PublicationStatusRepository implements BasicRepository<PublicationStatus> {

	private final Connection connection;

	public PublicationStatusRepository(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Optional<PublicationStatus> getById(int id) {

		try {

			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM publication_status WHERE id = " + id);

			if (resultSet.next()) {
				String name = resultSet.getString("name");
				String comment = resultSet.getString("comment");

				return Optional.of(new PublicationStatus(id, name, comment));
			}
			return Optional.empty();
		} catch (SQLException err) {
			return Optional.empty();
		}
	}

	@Override
	public PublicationStatus[] getAll() {

		ArrayList<PublicationStatus> statusList = new ArrayList<>();

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM publication_status");

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String comment = resultSet.getString("comment");

				statusList.add(new PublicationStatus(id, name, comment));
			}
		} catch (SQLException err) {
		}

		return statusList.toArray(new PublicationStatus[0]);
	}
}
