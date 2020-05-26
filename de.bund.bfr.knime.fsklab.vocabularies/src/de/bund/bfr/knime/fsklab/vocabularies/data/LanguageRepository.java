package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.Language;

public class LanguageRepository implements BasicRepository<Language> {

	private final Connection connection;

	public LanguageRepository(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Optional<Language> getById(int id) {

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM language WHERE id = " + id);

			if (resultSet.next()) {
				String code = resultSet.getString("code");
				String name = resultSet.getString("name");
				return Optional.of(new Language(id, code, name));
			}
			return Optional.empty();
		} catch (SQLException err) {
			return Optional.empty();
		}
	}

	@Override
	public Language[] getAll() {

		ArrayList<Language> languageList = new ArrayList<>();

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM language");

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String code = resultSet.getString("code");
				String name = resultSet.getString("name");

				languageList.add(new Language(id, code, name));
			}
		} catch (SQLException err) {
		}

		return languageList.toArray(new Language[0]);
	}
}
