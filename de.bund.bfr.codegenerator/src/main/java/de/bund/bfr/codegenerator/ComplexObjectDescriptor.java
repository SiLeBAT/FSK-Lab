package de.bund.bfr.codegenerator;

import java.util.ArrayList;
import java.util.List;

public class ComplexObjectDescriptor extends ObjectDescriptor {
	String parentClassName;
	List<ComplexObject> classes = new ArrayList<ComplexObject>();

	boolean hasFields() {
		return fields.size() > 0;
	}

	boolean hasClassesAndFields() {
		return classes.size() > 0 && fields.size() > 0;
	}

	int classIndex;

	boolean notLastClass() {
		boolean isLastClass = classes.size() != ++classIndex;
		if (classes.size() == classIndex)
			classIndex = 0;
		return isLastClass;
	}
}
