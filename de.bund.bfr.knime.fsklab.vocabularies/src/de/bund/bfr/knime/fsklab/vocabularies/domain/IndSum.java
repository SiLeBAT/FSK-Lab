package de.bund.bfr.knime.fsklab.vocabularies.domain;

/** Hazard ind sum. */
public class IndSum {

    private final int id;
    private final String name;
    private final String ssd;

    public IndSum(int id, String name, String ssd) {
        this.id = id;
        this.name = name;
        this.ssd = ssd;
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
}
