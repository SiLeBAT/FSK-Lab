package de.bund.bfr.knime.fsklab.vocabularies.domain;

public class Unit {

    private final int id;
    private final String name;
    private final String ssd;
    private final String comment;
    private final UnitCategory category;

    public Unit(int id, String name, String ssd, String comment, UnitCategory category) {
        this.id = id;
        this.name = name;
        this.ssd = ssd;
        this.comment = comment;
        this.category = category;
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

    public UnitCategory getCategory() {
        return category;
    }
}
