package de.bund.bfr.knime.fsklab.vocabularies.domain;

public class Right {

    private int id;
    private String shortname;
    private String fullname;
    private String url;

    public Right(int id, String shortname, String fullname, String url) {
        this.id = id;
        this.shortname = shortname;
        this.fullname = fullname;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getShortname() {
        return shortname;
    }

    public String getFullname() {
        return fullname;
    }

    public String getUrl() {
        return url;
    }
}