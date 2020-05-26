package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.util.Optional;

public interface BasicRepository<T> {

    Optional<T> getById(int id);

    T[] getAll();
}
