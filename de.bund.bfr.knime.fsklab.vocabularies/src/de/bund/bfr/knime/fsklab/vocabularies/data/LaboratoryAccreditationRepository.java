package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.LaboratoryAccreditation;

public class LaboratoryAccreditationRepository implements BasicRepository<LaboratoryAccreditation> {

    private final Connection connection;

    public LaboratoryAccreditationRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<LaboratoryAccreditation> getById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM laboratory_accreditation WHERE id = " + id);

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String ssd = resultSet.getString("ssd");

            return Optional.of(new LaboratoryAccreditation(id, name, ssd));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public LaboratoryAccreditation[] getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM laboratory_accreditation");

        ArrayList<LaboratoryAccreditation> accreditations = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String ssd = resultSet.getString("ssd");

            accreditations.add(new LaboratoryAccreditation(id, name, ssd));
        }

        return accreditations.toArray(new LaboratoryAccreditation[0]);
    }
}
