package de.bund.bfr.knime.fsklab.python2.config;


import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.python2.ManualPythonCommand;
import org.knime.python2.PythonCommand;

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

import org.knime.python2.PythonVersion;

import de.bund.bfr.knime.fsklab.preferences.RPythonVersion;

/**
 * @author Marcel Wiedenmann, KNIME GmbH, Konstanz, Germany
 * @author Christian Dietz, KNIME GmbH, Konstanz, Germany
 */
public final class FSKEnvironmentConfig implements PythonConfig {
	/*static final String R3_PATH_CFG = "r3.path";
	static final String FSK_PATH_CFG = "fsk.path";
	static final String IS_PYTHON_CONDA = "pythonconda";
	static final String IS_R_CONDA = "rconda";
	static final String CONDA_PATH_CFG = "conda.path";
	static final String PYTHON2_PATH_CFG = "python2.path";
	static final String PYTHON3_PATH_CFG = "python3.path";
	static final String PYTHON2_ENV_CFG = "python2.env";
	static final String R_ENV_CFG = "r.env";
	static final String PYTHON3_ENV_CFG = "python3.env";
	static final String RESTORE_RPROFILE = "restore_profile";
	*/
	
	//static final String RESTORE_RPROFILE = "restore_profile";
	//static final String FSK_PATH_CFG = "fsk.path";
	/**
     * Configuration key for the path to the R  executable ("environment").
     */
    //public static final String R3_PATH_CFG = "rPath";
    

    
    /**
     * Use the command 'r' without a specified location by default.
     */
    //public static final String DEFAULT_FSK_PATH = System.getProperty("java.io.tmpdir");
    private final SettingsModelString m_FSKPath;


    public FSKEnvironmentConfig(final String configKey,final String defaultFSKPath) {
            
    	m_FSKPath = new SettingsModelString(configKey, defaultFSKPath );
        }

    

    /**
     * @return The path to the Python executable.
     */
    public SettingsModelString getFSKPath() {
        return m_FSKPath;
    }

    

    @Override
    public void saveConfigTo(final PythonConfigStorage storage) {
        storage.saveStringModel(m_FSKPath);
    }

    @Override
    public void loadConfigFrom(final PythonConfigStorage storage) {
        storage.loadStringModel(m_FSKPath);
    }

	
}
