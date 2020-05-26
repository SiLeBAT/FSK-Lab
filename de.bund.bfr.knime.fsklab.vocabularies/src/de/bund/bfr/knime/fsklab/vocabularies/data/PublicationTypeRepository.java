package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.PublicationType;

public class PublicationTypeRepository implements BasicRepository<PublicationType> {

	private final Connection connection;

	public PublicationTypeRepository(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Optional<PublicationType> getById(int id) {

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM publication_type WHERE id = '" + id + "'");

			if (resultSet.next()) {
				String shortName = resultSet.getString("shortName");
				String fullName = resultSet.getString("fullName");

				return Optional.of(new PublicationType(id, shortName, fullName));
			}
			return Optional.empty();
		} catch (SQLException err) {
			return Optional.empty();
		}
	}

	@Override
	public PublicationType[] getAll() {

		ArrayList<PublicationType> typeList = new ArrayList<>();

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM publication_type");

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String shortName = resultSet.getString("shortName");
				String fullName = resultSet.getString("fullName");

				typeList.add(new PublicationType(id, shortName, fullName));
			}
		} catch (SQLException err) {
		}

		return typeList.toArray(new PublicationType[0]);
	}
}
