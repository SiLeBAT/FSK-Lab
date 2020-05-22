package de.bund.bfr.knime.fsklab.vocabularies.domain;

public class Country {

    private final int id;
    private final String name;
    private final String iso;

    public Country(int id, String name, String iso) {
        this.id = id;
        this.name = name;
        this.iso = iso;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIso() {
        return iso;
    }
}
