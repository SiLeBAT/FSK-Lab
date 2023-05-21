package de.bund.bfr.knime.fsklab.preferences;

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
 *   Mar 4, 2022 (benjamin): created
 */

import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class FSKPreferenceInitializer extends AbstractPreferenceInitializer {

	/** Path to R v.3 */
	static final String R3_PATH_CFG = "r3.path";
	static final String FSK_PATH_CFG = "fsk.path";
	static final String IS_R_CONDA = "rconda";
	static final String CONDA_PATH_CFG = "conda.path";
	static final String R_ENV_CFG = "r.env";
	static final String RESTORE_RPROFILE = "restore_profile";

	private static RPreferenceProvider cachedRProvider = null;
	public static boolean refresh;

	@Override
	public void initializeDefaultPreferences() {

		String rHome = "";

		if (RPathUtil.getPackagedRHome() != null) {
			rHome = RPathUtil.getPackagedRHome().getAbsolutePath();
		} else if (RPathUtil.getSystemRHome() != null) {
			rHome = RPathUtil.getSystemRHome().getAbsolutePath();
		}

		IPreferenceStore store = Plugin.getDefault().getPreferenceStore();
		store.setDefault(R3_PATH_CFG, rHome);
		store.setDefault(FSK_PATH_CFG, System.getProperty("java.io.tmpdir"));
		store.setDefault(CONDA_PATH_CFG, "");
		
		store.setDefault(IS_R_CONDA, "TRUE");
		store.setDefault(RESTORE_RPROFILE, true);
	}

	/** @return provider to the path to the R3 executable. */
	public static final RPreferenceProvider getR3Provider() {
		String rHome = "";
		if(!isRConda()) {
			rHome =  Plugin.getDefault().getPreferenceStore().getString(R3_PATH_CFG);
		}else {
			rHome =  createExecutableString(Plugin.getDefault().getPreferenceStore().getString(R_ENV_CFG));
		}
		
		if (cachedRProvider == null || !cachedRProvider.getRHome().equals(rHome)) {
			if(isTychoTest())
				rHome = RPathUtil.getSystemRHome().getAbsolutePath();
			cachedRProvider = new DefaultRPreferenceProvider(rHome);
		}

		return cachedRProvider;
	}

	public static final String getRPath() {
		if(!isRConda()) {
			return Plugin.getDefault().getPreferenceStore().getString(R3_PATH_CFG);
		}else {
			return createExecutableString(Plugin.getDefault().getPreferenceStore().getString(R_ENV_CFG));
		}
		
	}
	public static final String getFSKWorkingDirectory() {
		return Plugin.getDefault().getPreferenceStore().getString(FSK_PATH_CFG);
	}
	
	

	public static final String getCondaPath() {
		return Plugin.getDefault().getPreferenceStore().getString(CONDA_PATH_CFG);
	}

	
	
	public static final String getREnv() {
		return Plugin.getDefault().getPreferenceStore().getString(R_ENV_CFG);
	}
	public static final boolean isRProfileToBeRestored() {
		return Plugin.getDefault().getPreferenceStore().getBoolean(RESTORE_RPROFILE);
	}

	
	public static final boolean isRConda() {
		if(isTychoTest()) 
			return false;
		return Plugin.getDefault().getPreferenceStore().getString(IS_R_CONDA).equals("TRUE") ? true : false;
	}
	
	private static boolean isTychoTest() {
		String runningMode = System.getProperty("eclipse.application");
		if(runningMode!=null && runningMode.equals("org.eclipse.tycho.surefire.osgibooter.uitest")){
			return true;
		}
		return false;
	}

	/**
	 * Invalidate the cached R3 preference provider returned by
	 * {@link #getR3Provider()}, to refetch R properties (which launches an external
	 * R command).
	 */
	public static final void invalidateProviderCache() {
		cachedRProvider = null;
	}
	
    static String createExecutableString(final String environmentDirectoryPath) {
        final Path executablePath = Paths.get(environmentDirectoryPath, "lib", "R"); // NOSONAR
        return executablePath.toString();
    }
}
