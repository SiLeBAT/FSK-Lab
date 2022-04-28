/***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Contributors: Department Biological Safety - BfR
 **************************************************************************************************/
package de.bund.bfr.knime.fsklab.preferences;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Initializes preference page with default paths to R2 and R3 environments.
 *
 * @author Miguel Alba
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/** Path to R v.3 */
	static final String R3_PATH_CFG = "r3.path";
	static final String IS_PYTHON_CONDA = "pythonconda";
	static final String IS_R_CONDA = "rconda";
	static final String CONDA_PATH_CFG = "conda.path";
	static final String PYTHON2_PATH_CFG = "python2.path";
	static final String PYTHON3_PATH_CFG = "python3.path";
	static final String PYTHON2_ENV_CFG = "python2.env";
	static final String R_ENV_CFG = "r.env";
	static final String PYTHON3_ENV_CFG = "python3.env";
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
		store.setDefault(CONDA_PATH_CFG, "");
		store.setDefault(PYTHON2_PATH_CFG, "python");
		store.setDefault(PYTHON3_PATH_CFG, "python3");
		store.setDefault(IS_PYTHON_CONDA, "TRUE");
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

	public static final String getPython2Path() {
		return Plugin.getDefault().getPreferenceStore().getString(PYTHON2_PATH_CFG);
	}

	public static final String getPython3Path() {
		return Plugin.getDefault().getPreferenceStore().getString(PYTHON3_PATH_CFG);
	}

	public static final String getCondaPath() {
		return Plugin.getDefault().getPreferenceStore().getString(CONDA_PATH_CFG);
	}

	public static final String getPython2Env() {
		return Plugin.getDefault().getPreferenceStore().getString(PYTHON2_ENV_CFG);
	}

	public static final String getPython3Env() {
		return Plugin.getDefault().getPreferenceStore().getString(PYTHON3_ENV_CFG);
	}
	
	public static final String getREnv() {
		return Plugin.getDefault().getPreferenceStore().getString(R_ENV_CFG);
	}
	public static final boolean isRProfileToBeRestored() {
		return Plugin.getDefault().getPreferenceStore().getBoolean(RESTORE_RPROFILE);
	}

	public static final boolean isPythonConda() {
		return Plugin.getDefault().getPreferenceStore().getString(IS_PYTHON_CONDA).equals("TRUE") ? true : false;
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
