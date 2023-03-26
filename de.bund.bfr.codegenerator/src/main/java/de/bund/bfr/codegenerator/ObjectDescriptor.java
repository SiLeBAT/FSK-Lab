package de.bund.bfr.codegenerator;

import java.util.ArrayList;
import java.util.List;

public class ObjectDescriptor {
	String className;
	List<FieldDescriptor> fields = new ArrayList<FieldDescriptor>();
	int requiredFieldNum;
	int requiredIndex;
	int index;

	boolean notLastElement() {
		boolean isLastElement = fields.size() != ++index;
		if (fields.size() == index)
			index = 0;
		return isLastElement;
	}

	boolean notLastRequiredElement() {
		boolean isLastElement = requiredFieldNum > ++requiredIndex && requiredFieldNum != 0;
		if (requiredFieldNum == requiredIndex)
			requiredIndex = 0;
		return isLastElement;
	}

	boolean hasRequiredFields() {
		return requiredFieldNum > 0;
	}

	boolean hasFields() {
		return fields.size() > 0;
	}
	boolean allAreRequired() {
		return fields.size() == requiredFieldNum;
	}

	String JSONRepresentation;
}
