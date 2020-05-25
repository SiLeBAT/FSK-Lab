package de.bund.bfr.knime.fsklab.vocabularies.domain;

public class Language {

    private final int id;

    /** ISO 639-1 two-letter code. */
    private final String code;

    private final String name;

    public Language(int id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
