package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.SQLException;
import java.util.Optional;

public interface BasicRepository<T> {

    Optional<T> getById(int id) throws SQLException;

    T[] getAll() throws SQLException;
}
