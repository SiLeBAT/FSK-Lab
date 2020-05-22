package de.bund.bfr.knime.fsklab.vocabularies.domain;

public class ProductTreatment {

    private final int id;
    private final String name;
    private final String ssd;
    private final String comment;

    public ProductTreatment(int id, String name, String ssd, String comment) {
        this.id = id;
        this.name = name;
        this.ssd = ssd;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSsd() {
        return ssd;
    }

    public String getComment() {
        return comment;
    }
}
