package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.LanguageWrittenIn;

public class LanguageWrittenInRepository implements BasicRepository<LanguageWrittenIn> {

	private final Connection connection;

	public LanguageWrittenInRepository(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Optional<LanguageWrittenIn> getById(int id) {

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM language_written_in WHERE id = " + id);

			if (resultSet.next()) {
				String name = resultSet.getString("name");
				return Optional.of(new LanguageWrittenIn(id, name));
			}
			return Optional.empty();
		} catch (SQLException err) {
			return Optional.empty();
		}
	}

	@Override
	public LanguageWrittenIn[] getAll() {
		ArrayList<LanguageWrittenIn> languageList = new ArrayList<>();

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM language_written_in");

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				languageList.add(new LanguageWrittenIn(id, name));
			}
		} catch (SQLException err) {
		}

		return languageList.toArray(new LanguageWrittenIn[0]);
	}
}
