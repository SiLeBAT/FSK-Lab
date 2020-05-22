package de.bund.bfr.knime.fsklab.vocabularies.domain;

public class ModelSubclass {

    private final int id;
    private final String name;
    private final ModelClass classCategory;

    public ModelSubclass(int id, String name, ModelClass classCategory) {
        this.id = id;
        this.name = name;
        this.classCategory = classCategory;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ModelClass getClassCategory() {
        return classCategory;
    }
}
