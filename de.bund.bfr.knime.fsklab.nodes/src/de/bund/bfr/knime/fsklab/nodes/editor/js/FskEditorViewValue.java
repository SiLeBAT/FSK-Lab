package de.bund.bfr.knime.fsklab.nodes.editor.js;

import java.util.Random;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class FskEditorViewValue extends JSONViewContent {
	
	String modelScript = "";
	String paramScript = "";
	String vizScript = "";

	public final int pseudoIdentifier = (new Random()).nextInt();
	
	@Override
	public void saveToNodeSettings(NodeSettingsWO settings) {
	}
	
	@Override
	public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
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
	
	@Override
	public int hashCode() {
		return pseudoIdentifier;
	}
}
