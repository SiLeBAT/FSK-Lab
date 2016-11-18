package de.bund.bfr.knime.fsklab.nodes;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import de.bund.bfr.knime.fsklab.nodes.FskMetaData.DataType;

/**
 * Holds variable-related data.
 * 
 * value, min and max can hold different types of values: integers, real
 * numbers, strings or even arrays. So they are kept as strings in order to
 * support all these types.
 * 
 * @author de
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Variable implements Serializable {

	private static final long serialVersionUID = -331516812131854597L;

	/** Null or empty if not set. */
	public String name;
	
	/** Null or empty if not set. */
	public String unit;
	
	/** Null or empty if not set. */
	public String value;
	
	/** Null or empty if not set. */
	public String min;
	
	/** Null or empty if not set. */
	public String max;
	
	/** Null if not set. */
	public DataType type;

	@Override
	public String toString() {
		return "Variable [" + name + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, unit, type, value, min, max);
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Variable other = (Variable) obj;

		return Objects.equals(name, other.name) && Objects.equals(unit, other.unit)
				&& Objects.equals(value, other.value) && Objects.equals(min, other.min)
				&& Objects.equals(max, other.max) && Objects.equals(type, other.type);
	}
}