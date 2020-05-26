package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.ProductTreatment;

public class ProductTreatmentRepository implements BasicRepository<ProductTreatment> {

	private final Connection connection;

	public ProductTreatmentRepository(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Optional<ProductTreatment> getById(int id) {

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM prodTreat WHERE id = " + id);

			if (resultSet.next()) {
				String name = resultSet.getString("name");
				String ssd = resultSet.getString("ssd");
				String comment = resultSet.getString("comment");

				return Optional.of(new ProductTreatment(id, name, ssd, comment));
			}
			return Optional.empty();
		} catch (SQLException err) {
			return Optional.empty();
		}
	}

	@Override
	public ProductTreatment[] getAll() {

		ArrayList<ProductTreatment> treatmentList = new ArrayList<>();

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM prodTreat");

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String ssd = resultSet.getString("ssd");
				String comment = resultSet.getString("comment");

				treatmentList.add(new ProductTreatment(id, name, ssd, comment));
			}
		} catch (SQLException err) {
		}

		return treatmentList.toArray(new ProductTreatment[0]);
	}
}
