package de.bund.bfr.knime.fsklab.nodes.metadataeditor;

import java.util.Random;

import org.knime.core.node.*;
import org.knime.js.core.JSONViewContent;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import de.bund.bfr.knime.fsklab.nodes.FskMetaData;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class FskMetadataEditorViewValue extends JSONViewContent {

	FskMetaData metadata;

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
