/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME AG, Zurich, Switzerland
 *  Website: http://www.knime.com; Email: contact@knime.com
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
 *  KNIME and ECLIPSE being a combined program, KNIME AG herewith grants
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
 *   Feb 21, 2019 (marcel): created
 */
package de.bund.bfr.knime.fsklab.python2.config;

import org.knime.python2.PythonVersion;

import de.bund.bfr.knime.fsklab.preferences.RPythonVersion;

/**
 * @author Marcel Wiedenmann, KNIME GmbH, Konstanz, Germany
 * @author Christian Dietz, KNIME GmbH, Konstanz, Germany
 */
public final class ManualEnvironmentsConfig implements PythonEnvironmentsConfig {

	/**
     * Configuration key for the path to the R  executable ("environment").
     */
    public static final String CFG_KEY_R_PATH = "rPath";
    /**
     * Configuration key for the path to the Python 2 executable ("environment").
     */
    public static final String CFG_KEY_PYTHON2_PATH = "python2Path";

    /**
     * Configuration key for the path to the Python 3 executable ("environment").
     */
    public static final String CFG_KEY_PYTHON3_PATH = "python3Path";

    
    /**
     * Use the command 'r' without a specified location by default.
     */
    public static final String DEFAULT_R_PATH = "R";
    /**
     * Use the command 'python' without a specified location by default.
     */
    public static final String DEFAULT_PYTHON2_PATH = "python";

    /**
     * Use the command 'python3' without a specified location by default.
     */
    public static final String DEFAULT_PYTHON3_PATH = "python3";

    private final ManualEnvironmentConfig m_python2EnvironmentConfig =
        new ManualEnvironmentConfig(RPythonVersion.PYTHON2, CFG_KEY_PYTHON2_PATH, DEFAULT_PYTHON2_PATH);

    private final ManualEnvironmentConfig m_python3EnvironmentConfig =
        new ManualEnvironmentConfig(RPythonVersion.PYTHON3, CFG_KEY_PYTHON3_PATH, DEFAULT_PYTHON3_PATH);
    private final ManualEnvironmentConfig m_rEnvironmentConfig =
            new ManualEnvironmentConfig(RPythonVersion.R, CFG_KEY_R_PATH, DEFAULT_R_PATH);

    @Override
    public ManualEnvironmentConfig getPython2Config() {
        return m_python2EnvironmentConfig;
    }

    @Override
    public ManualEnvironmentConfig getPython3Config() {
        return m_python3EnvironmentConfig;
    }
    @Override
	public ManualEnvironmentConfig getRConfig() {
    	return m_rEnvironmentConfig;
	}
    @Override
    public void saveDefaultsTo(final PythonConfigStorage storage) {
        m_python2EnvironmentConfig.saveDefaultsTo(storage);
        m_python3EnvironmentConfig.saveDefaultsTo(storage);
        m_rEnvironmentConfig.saveDefaultsTo(storage);
    }

    @Override
    public void saveConfigTo(final PythonConfigStorage storage) {
        m_python2EnvironmentConfig.saveConfigTo(storage);
        m_python3EnvironmentConfig.saveConfigTo(storage);
        m_rEnvironmentConfig.saveConfigTo(storage);
    }

    @Override
    public void loadConfigFrom(final PythonConfigStorage storage) {
        m_python2EnvironmentConfig.loadConfigFrom(storage);
        m_python3EnvironmentConfig.loadConfigFrom(storage);
        m_rEnvironmentConfig.loadConfigFrom(storage);
    }

	
}
