package de.bund.bfr.knime.fsklab.nodes;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;

import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class FskMetaData implements Serializable {

	private static final long serialVersionUID = -625136501840140815L;

	/**
	 * Date format used for date fields.
	 */
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");

	/** Null or empty string if not set. */
	public String modelName;

	/** Null or empty string if not set. */
	public String modelId;

	/** Null if not set. */
	public String modelLink;

	/** Null or empty string if not set. */
	public String organism;

	/** Null or empty string if not set. */
	public String organismDetails;

	/** Null or empty string if not set. */
	public String matrix;

	/** Null or empty string if not set. */
	public String matrixDetails;

	/** Null or empty string if not set. */
	public String creator;

	/** Null or empty string if not set. */
	public String familyName;

	/** Null or empty string if not set. */
	public String contact;

	/** Null if not set. */
	public Software software;

	/** Null or empty string if not set. */
	public String referenceDescription;

	/** Null if not set. */
	public String referenceDescriptionLink;

	/** Creation date. Null if not set. */
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM.dd.yyyy")
	public Date createdDate;

	/** Last modification date. Null if not set. */
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM.dd.yyyy")
	public Date modifiedDate;

	/** Null or empty string if not set. */
	public String rights;

	/** Null or empty string if not set. */
	public String notes;

	/** <code>false</code> if not set. */
	public boolean curated;

	/** Null if not set. */
	public ModelType type;

	/** {@link ModelClass#UNKNOWN} if not set. */
	public ModelClass subject = ModelClass.UNKNOWN;

	/** Null or empty string if not set. */
	public String foodProcess;

	/** Null if not set. */
	public Variable dependentVariable = new Variable();

	/** Null if not set. */
	public List<Variable> independentVariables = new ArrayList<>();

	/** <code>false</code> if not set */
	public boolean hasData;

	public enum Software {
		R, Matlab
	}

	public enum DataType {
		/** String values. */
		character,
		/** Integers. */
		integer,
		/** Real numbers. */
		numeric,
		/** Arrays. */
		array
	}

	@Override
	public int hashCode() {
		return Objects.hash(modelName, modelId, modelLink, organism, organismDetails, matrix, matrixDetails, creator,
				familyName, contact, software, referenceDescription, referenceDescriptionLink, createdDate,
				modifiedDate, rights, notes, curated, type, subject, foodProcess, dependentVariable,
				independentVariables, hasData);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FskMetaData other = (FskMetaData) obj;

		return Objects.equals(modelName, other.modelName) && Objects.equals(modelId, other.modelId)
				&& Objects.equals(modelLink, other.modelLink) && Objects.equals(organism, other.organism)
				&& Objects.equals(organismDetails, other.organismDetails) && Objects.equals(matrix, other.matrix)
				&& Objects.equals(matrixDetails, other.matrixDetails) && Objects.equals(creator, other.creator)
				&& Objects.equals(familyName, other.familyName) && Objects.equals(contact, other.contact)
				&& Objects.equals(software, other.software)
				&& Objects.equals(referenceDescription, other.referenceDescription)
				&& Objects.equals(referenceDescriptionLink, other.referenceDescriptionLink)
				&& Objects.equals(createdDate, other.createdDate) && Objects.equals(modifiedDate, other.modifiedDate)
				&& Objects.equals(rights, other.rights) && Objects.equals(notes, other.notes)
				&& Objects.equals(curated, other.curated) && Objects.equals(type, other.type)
				&& Objects.equals(subject, other.subject) && Objects.equals(foodProcess, other.foodProcess)
				&& Objects.equals(dependentVariable, other.dependentVariable)
				&& Objects.equals(independentVariables, other.independentVariables) && hasData == other.hasData;
	}
}