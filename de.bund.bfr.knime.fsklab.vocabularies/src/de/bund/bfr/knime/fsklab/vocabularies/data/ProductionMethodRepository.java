package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.bund.bfr.knime.fsklab.vocabularies.domain.ProductionMethod;

public class ProductionMethodRepository implements BasicRepository<ProductionMethod> {

    private final Connection connection;

    public ProductionMethodRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ProductionMethod getById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM prodmeth WHERE id = " + id);

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String ssd = resultSet.getString("ssd");
            String comment = resultSet.getString("comment");

            return new ProductionMethod(id, name, ssd, comment);
        } else {
            return null;
        }
    }

    @Override
    public ProductionMethod[] getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM prodmeth");

        ArrayList<ProductionMethod> methodList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String ssd = resultSet.getString("ssd");
            String comment = resultSet.getString("comment");

            methodList.add(new ProductionMethod(id, name, ssd, comment));
        }

        return methodList.toArray(new ProductionMethod[0]);
    }
}
