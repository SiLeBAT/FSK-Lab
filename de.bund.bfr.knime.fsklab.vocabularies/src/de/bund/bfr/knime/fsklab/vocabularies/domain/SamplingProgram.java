package de.bund.bfr.knime.fsklab.vocabularies.domain;

/** Type of sampling program. */
public class SamplingProgram {

    private final int id;
    private final String name;
    private final String progType;

    public SamplingProgram(int id, String name, String progType) {
        this.id = id;
        this.name = name;
        this.progType = progType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProgType() {
        return progType;
    }
}
