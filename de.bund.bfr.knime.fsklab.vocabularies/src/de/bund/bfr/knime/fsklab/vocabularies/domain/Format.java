package de.bund.bfr.knime.fsklab.vocabularies.domain;

public class Format {

    private final int id;
    private final String name;
    private final String comment;

    public Format(int id, String name, String comment) {
        this.id = id;
        this.name = name;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }
}
