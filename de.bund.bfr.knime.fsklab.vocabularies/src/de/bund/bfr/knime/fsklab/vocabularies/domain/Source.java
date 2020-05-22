package de.bund.bfr.knime.fsklab.vocabularies.domain;

public class Source {

    private int id;
    private String name;
    private String comment;

    public Source(int id, String name, String comment) {
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