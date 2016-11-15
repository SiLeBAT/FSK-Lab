/*
 * ------------------------------------------------------------------
 *  Copyright by KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ---------------------------------------------------------------------
 *
 * History
 *   17.09.2007 (thiel): created
 */
package de.bund.bfr.knime.fsklab.nodes.rbin.preferences;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import com.sun.jna.Platform;

import de.bund.bfr.knime.fsklab.nodes.rbin.RBinUtil;


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
	public String getRBinPath(final String command) {
		Path binPath = Paths.get(getRHome(), "bin");
		if (Platform.isWindows()) {
			if (Platform.is64Bit()) {
				return binPath.resolve("x64").toString() + File.separator + command + ".exe";
			}
			return binPath.resolve("i386").toString() + File.separator + command + ".exe";
		}
		return binPath + File.separator + command;
	}

	@Override
	public String getRServeBinPath() {
		if (m_properties == null) {
			m_properties = RBinUtil.retrieveRProperties(this);
		}

		final Path rservePath = Paths.get(m_properties.getProperty("Rserve.path"));
		final Path rserveLibs = rservePath.resolve("libs");

		if (Platform.isWindows()) {
			if (Platform.is64Bit()) {
				return rserveLibs.resolve("x64/Rserve.exe").toString();
			}
			return rserveLibs.resolve("i386/Rserve.exe").toString();
		}
		return rserveLibs.resolve("Rserve.dbg").toString();
	}

	/**
	 * Get the properties for this provider. Use this method to avoid calling
	 * {@link RBinUtil#retrieveRProperties()}, which launches an external R
	 * process to retrieve R properties.
	 *
	 * @return The properties for this provider
	 */
	Properties getProperties() {
		if (m_properties == null) {
			m_properties = RBinUtil.retrieveRProperties(this);
		}

		return m_properties;
	}
}
