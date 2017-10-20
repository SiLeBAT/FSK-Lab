package de.bund.bfr.knime.fsklab.nodes;

import java.net.URI;

import org.knime.core.node.InvalidSettingsException;

import com.sun.jna.Platform;

import de.bund.bfr.fskml.URIS;

public class NodeUtils {

	/**
	 * @return the libraries URI for the running platform.
	 * @throws InvalidSettingsException if the running platform is not supported.
	 */
	public static URI getLibURI() throws InvalidSettingsException {
		if (Platform.isWindows())
			return URIS.zip;
		if (Platform.isMac())
			return URIS.tgz;
		if (Platform.isLinux())
			return URIS.tar_gz;
		throw new InvalidSettingsException("Unsupported platform");
	}
}
