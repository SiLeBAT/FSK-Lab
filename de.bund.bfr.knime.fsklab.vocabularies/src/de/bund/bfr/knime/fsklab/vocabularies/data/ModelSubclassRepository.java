package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.bund.bfr.knime.fsklab.vocabularies.domain.ModelClass;
import de.bund.bfr.knime.fsklab.vocabularies.domain.ModelSubclass;

public class ModelSubclassRepository implements BasicRepository<ModelSubclass> {

    private final Connection connection;
    private final ModelClassRepository modelClassRepository;

    public ModelSubclassRepository(Connection connection) {
        this.connection = connection;
        this.modelClassRepository = new ModelClassRepository(connection);
    }

    @Override
    public ModelSubclass getById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM model_subclass WHERE id = " + id);

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            int classId = resultSet.getInt("class_id");
            ModelClass classCategory = modelClassRepository.getById(classId);

            return new ModelSubclass(id, name, classCategory);
        } else {
            return null;
        }
    }

    @Override
    public ModelSubclass[] getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM model_subclass");

        ArrayList<ModelSubclass> subclasses = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int classId = resultSet.getInt("class_id");
            ModelClass classCategory = modelClassRepository.getById(classId);

            subclasses.add(new ModelSubclass(id, name, classCategory));
        }

        return subclasses.toArray(new ModelSubclass[0]);
    }
}
