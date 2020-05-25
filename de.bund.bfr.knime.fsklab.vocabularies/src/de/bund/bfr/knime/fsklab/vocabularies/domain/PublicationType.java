package de.bund.bfr.knime.fsklab.vocabularies.domain;

public class PublicationType {

    private final int id;
    private final String shortName;
    private final String fullName;
 
    public PublicationType(int id, String shortName, String fullName) {
        this.id = id;
        this.shortName = shortName;
        this.fullName = fullName;
    }

    public int getId() {
        return id;
    }

    public String getShortName() {
        return shortName;
    }

    public String getFullName() {
        return fullName;
    }
}