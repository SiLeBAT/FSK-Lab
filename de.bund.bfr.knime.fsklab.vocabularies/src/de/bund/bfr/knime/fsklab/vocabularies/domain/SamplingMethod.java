package de.bund.bfr.knime.fsklab.vocabularies.domain;

public class SamplingMethod {

    private final int id;
    private final String name;
    private final String sampmd;
    private final String comment;

    public SamplingMethod(int id, String name, String sampmd, String comment) {
        this.id = id;
        this.name = name;
        this.sampmd = sampmd;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSampmd() {
        return sampmd;
    }

    public String getComment() {
        return comment;
    }
}
