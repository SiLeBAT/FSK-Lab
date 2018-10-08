package de.bund.bfr.knime.node.editableTable;

import java.util.Random;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author Miguel de Alba, BfR, Berlin, Germany
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class EditableTableViewValue extends JSONViewContent {
	
	JSONDataTable table;

	// no members to hash on
	public final int pseudoIdentifier = (new Random()).nextInt();
	
	@Override
	public void saveToNodeSettings(NodeSettingsWO settings) {
		// table.saveJSONToNodeSettings(settings);	
	}
	
	@Override
	public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		// table = JSONDataTable.loadFromNodeSettings(settings);
	}

	@Override
	public int hashCode() {
		// return table.hashCode();
		return pseudoIdentifier;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return false;
	}
}
