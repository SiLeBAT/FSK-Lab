package de.bund.bfr.knime.fsklab.vocabularies.domain;

public class SamplingPoint {

    private final int id;
    private final String name;
    private final String sampnt;

    public SamplingPoint(int id, String name, String sampnt) {
        this.id = id;
        this.name = name;
        this.sampnt = sampnt;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSampnt() {
        return sampnt;
    }
}
