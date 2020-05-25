package de.bund.bfr.knime.fsklab.vocabularies.domain;

public class Population {

    private final int id;
    private final String name;
    private final String foodon;

    public Population(int id, String name, String foodon) {
        this.id = id;
        this.name = name;
        this.foodon = foodon;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFoodon() {
        return foodon;
    }
}
