package de.bund.bfr.knime.fsklab.nodes.metadataeditor;

import java.util.Random;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public final class FskMetadataEditorViewRepresentation extends JSONViewContent {

	// no members to hash on
	public final int pseudoIdentifier = (new Random()).nextInt();
	
	@Override
	public void saveToNodeSettings(NodeSettingsWO settings) {
		// Nothing to do.
	}

	@Override
	public void loadFromNodeSettings(NodeSettingsRO settings)
			throws InvalidSettingsException {
		// Nothing to do.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return pseudoIdentifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		return false; // maybe add other criteria here
	}
}