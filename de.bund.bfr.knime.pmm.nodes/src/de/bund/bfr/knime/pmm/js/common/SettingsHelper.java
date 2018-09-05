package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

public class SettingsHelper {
	
	private SettingsHelper() { }
	
	/** Adds a String to a node settings. Ignores null. */
	public static void addString(final String key, final String val, final NodeSettingsWO settings) {
		if (val != null) {
			settings.addString(key, val);
		}
	}
	
	/** Adds an Integer to a node settings. Ignores null. */
	public static void addInt(final String key, final Integer val, final NodeSettingsWO settings) {
		if (val != null) {
			settings.addInt(key, val);
		}
	}

	/** Adds a Double to a node settings. Ignores null. */
	public static void addDouble(final String key, final Double val, final NodeSettingsWO settings) {
		if (val != null) {
			settings.addDouble(key, val);
		}
	}
	
	/** Adds a Boolean to a node settings. Ignores null. */
	public static void addBoolean(final String key, final Boolean val, final NodeSettingsWO settings) {
		if (val != null) {
			settings.addBoolean(key, val);
		}
	}
	
	/** Gets a String from a node settings. Returns null if missing. */
	public static String getString(final String key, final NodeSettingsRO settings) {
		try {
			return settings.getString(key);
		} catch(InvalidSettingsException e) {
			return null;
		}
	}

	/** Gets a Integer from a node settings. Returns null if missing. */
	public static Integer getInteger(final String key, final NodeSettingsRO settings) {
		try {
			return settings.getInt(key);
		} catch(InvalidSettingsException e) {
			return null;
		}
	}

	/** Gets a Double from a node settings. Returns null if missing. */
	public static Double getDouble(final String key, final NodeSettingsRO settings) {
		try {
			return settings.getDouble(key);
		} catch(InvalidSettingsException e) {
			return null;
		}
	}
	
	/** Gets a Boolean from a node settings. Returns null if missing. */
	public static Boolean getBoolean(final String key, final NodeSettingsRO settings) {
		try {
			return settings.getBoolean(key);
		} catch(InvalidSettingsException e) {
			return null;
		}
	}
}
