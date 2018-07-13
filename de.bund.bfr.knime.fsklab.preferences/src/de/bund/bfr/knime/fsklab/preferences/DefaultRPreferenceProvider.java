/*
 * ------------------------------------------------------------------ Copyright by KNIME GmbH,
 * Konstanz, Germany Website: http://www.knime.org; Email: contact@knime.org
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License, Version 3, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7:
 *
 * KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs. Hence, KNIME and ECLIPSE are
 * both independent programs and are not derived from each other. Should, however, the
 * interpretation of the GNU GPL Version 3 ("License") under any applicable laws result in KNIME and
 * ECLIPSE being a combined program, KNIME GMBH herewith grants you the additional permission to use
 * and propagate KNIME together with ECLIPSE with only the license terms in place for ECLIPSE
 * applying to ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the license terms of
 * ECLIPSE themselves allow for the respective use and propagation of ECLIPSE together with KNIME.
 *
 * Additional permission relating to nodes for KNIME that extend the Node Extension (and in
 * particular that are based on subclasses of NodeModel, NodeDialog, and NodeView) and that only
 * interoperate with KNIME through standard APIs ("Nodes"): Nodes are deemed to be separate and
 * independent programs and to not be covered works. Notwithstanding anything to the contrary in the
 * License, the License does not apply to Nodes, you are not required to license Nodes under the
 * License, and you are granted a license to prepare and propagate Nodes, in each case even if such
 * Nodes are propagated with or for interoperation with KNIME. The owner of a Node may freely choose
 * the license terms applicable to such Node, including when such Node is propagated with or for
 * interoperation with KNIME. ---------------------------------------------------------------------
 *
 * History 17.09.2007 (thiel): created
 */
package de.bund.bfr.knime.fsklab.preferences;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.eclipse.core.runtime.Platform;
import org.knime.core.node.NodeLogger;

/**
 * Default provider for R preferences. It determines the R binary path based on
 * the R home given in the constructor.
 *
 * @author Thorsten Meinl, KNIME.com, Zurich, Switzerland
 * @author Jonathan Hale
 */
public class DefaultRPreferenceProvider implements RPreferenceProvider {

	private final String m_rHome;

	private Properties m_properties = null;

	private static final NodeLogger LOGGER = NodeLogger.getLogger("DefaultRPreferenceProvider");

	/**
	 * Creates a new preference provider based on the given R home directory.
	 *
	 * @param rHome
	 *            R's home directory
	 */
	public DefaultRPreferenceProvider(final String rHome) {
		m_rHome = rHome;
	}

	@Override
	public String getRHome() {
		return m_rHome;
	}

	@Override
	public Path getRBinPath(final String command) {
		Path binPath = Paths.get(getRHome(), "bin");

		if (Platform.getOS().equals(Platform.OS_WIN32)) {

			final Path x64Path = binPath.resolve("x64"); // 64 bit binaries
			final Path i386Path = binPath.resolve("i386"); // 32 bit binaries

			if (Platform.getOSArch().equals(Platform.ARCH_X86_64)) {
				if (Files.exists(x64Path)) {
					return x64Path.resolve(command + ".exe");
				}

				// No 64 bits binaries were found. Then 32 bit binaries will be used.
				LOGGER.warn("Using 32 bit R on 64 bit. Please consider using 64 bit R");
				return i386Path.resolve(command + ".exe");
			}

			return i386Path.resolve(command + ".exe");
		}

		return binPath.resolve(command);
	}

	@Override
	public Path getRServeBinPath() {
		if (m_properties == null) {
			m_properties = RBinUtil.retrieveRProperties(this);
		}

		final Path rservePath = Paths.get(m_properties.getProperty("Rserve.path"));
		final Path rserveLibs = rservePath.resolve("libs");

		if (Platform.getOS().equals(Platform.OS_WIN32)) {
			// archProperty is "i386" for 32 bit or "x86_64" for 64 bit
			final String archProperty = m_properties.getProperty("arch");

			// "x64" for 64 bit and "i386" for 32 bit.
			final String arch = archProperty.equals("x86_64") ? "x64" : "i386";

			return rserveLibs.resolve(arch + "/Rserve.exe");
		}
		return rserveLibs.resolve("Rserve.dbg");
	}

	/**
	 * Get the properties for this provider. Use this method to avoid calling
	 * {@link RBinUtil#retrieveRProperties()}, which launches an external R process
	 * to retrieve R properties.
	 *
	 * @return The properties for this provider
	 */
	public Properties getProperties() {
		if (m_properties == null) {
			m_properties = RBinUtil.retrieveRProperties(this);
		}

		return m_properties;
	}
}
