package de.bund.bfr.knime.fsklab.vocabularies.domain;

public class ProductMatrix {

    private final int id;
    private final String ssd;
    private final String name;
    private final String comment;

    public ProductMatrix(int id, String ssd, String name, String comment) {
        this.id = id;
        this.ssd = ssd;
        this.name = name;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public String getSsd() {
        return ssd;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }
}
