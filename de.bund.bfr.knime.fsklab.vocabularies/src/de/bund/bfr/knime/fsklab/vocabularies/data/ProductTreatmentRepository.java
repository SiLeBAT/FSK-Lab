package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.bund.bfr.knime.fsklab.vocabularies.domain.ProductTreatment;

public class ProductTreatmentRepository implements BasicRepository<ProductTreatment> {

    private final Connection connection;

    public ProductTreatmentRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ProductTreatment getById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM prodTreat WHERE id = " + id);

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String ssd = resultSet.getString("ssd");
            String comment = resultSet.getString("comment");

            return new ProductTreatment(id, name, ssd, comment);
        } else {
            return null;
        }
    }

    @Override
    public ProductTreatment[] getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM prodTreat");

        ArrayList<ProductTreatment> treatmentList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String ssd = resultSet.getString("ssd");
            String comment = resultSet.getString("comment");

            treatmentList.add(new ProductTreatment(id, name, ssd, comment));
        }

        return treatmentList.toArray(new ProductTreatment[0]);
    }
}
