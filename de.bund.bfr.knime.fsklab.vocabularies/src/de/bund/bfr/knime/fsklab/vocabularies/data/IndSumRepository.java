package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.IndSum;

public class IndSumRepository implements BasicRepository<IndSum> {

	private final Connection connection;

	public IndSumRepository(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Optional<IndSum> getById(int id) {

		try {

			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM ind_sum WHERE id = " + id);

			if (resultSet.next()) {
				String name = resultSet.getString("name");
				String ssd = resultSet.getString("ssd");

				return Optional.of(new IndSum(id, name, ssd));
			}
			return Optional.empty();

		} catch (SQLException err) {
			return Optional.empty();
		}
	}

	@Override
	public IndSum[] getAll() {

		ArrayList<IndSum> sums = new ArrayList<>();

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM ind_sum");

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String ssd = resultSet.getString("ssd");

				sums.add(new IndSum(id, name, ssd));
			}
		} catch (SQLException err) {
		}

		return sums.toArray(new IndSum[0]);
	}
}
