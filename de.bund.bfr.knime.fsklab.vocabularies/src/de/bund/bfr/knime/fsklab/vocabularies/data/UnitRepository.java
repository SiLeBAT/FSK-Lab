package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.Unit;
import de.bund.bfr.knime.fsklab.vocabularies.domain.UnitCategory;

public class UnitRepository implements BasicRepository<Unit> {

	private final Connection connection;
	private final UnitCategoryRepository categoryRepository;

	public UnitRepository(Connection connection) {
		this.connection = connection;
		this.categoryRepository = new UnitCategoryRepository(connection);
	}

	@Override
	public Optional<Unit> getById(int id) {

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM unit WHERE id = " + id);

			if (resultSet.next()) {
				String name = resultSet.getString("name");
				String ssd = resultSet.getString("ssd");
				String comment = resultSet.getString("comment");
				int categoryId = resultSet.getInt("category_id");

				Optional<UnitCategory> category = categoryRepository.getById(categoryId);
				if (category.isPresent()) {
					return Optional.of(new Unit(id, name, ssd, comment, category.get()));
				}
			}
			return Optional.empty();

		} catch (SQLException err) {
			return Optional.empty();
		}
	}

	@Override
	public Unit[] getAll() {
		ArrayList<Unit> units = new ArrayList<>();

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM unit");

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String ssd = resultSet.getString("ssd");
				String comment = resultSet.getString("comment");
				int categoryId = resultSet.getInt("category_id");
				Optional<UnitCategory> category = categoryRepository.getById(categoryId);
				if (!category.isPresent())
					continue;

				units.add(new Unit(id, name, ssd, comment, category.get()));
			}
		} catch (SQLException err) {
		}

		return units.toArray(new Unit[0]);
	}
}
