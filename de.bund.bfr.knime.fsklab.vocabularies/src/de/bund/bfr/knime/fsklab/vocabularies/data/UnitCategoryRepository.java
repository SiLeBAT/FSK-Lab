package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.UnitCategory;

public class UnitCategoryRepository implements BasicRepository<UnitCategory> {

    private final Connection connection;

    public UnitCategoryRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<UnitCategory> getById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM unit_category WHERE id = " + id);

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            return Optional.of(new UnitCategory(id, name));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public UnitCategory[] getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM unit_category");

        ArrayList<UnitCategory> categoryList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");

            categoryList.add(new UnitCategory(id, name));
        }

        return categoryList.toArray(new UnitCategory[0]);
    }
}
