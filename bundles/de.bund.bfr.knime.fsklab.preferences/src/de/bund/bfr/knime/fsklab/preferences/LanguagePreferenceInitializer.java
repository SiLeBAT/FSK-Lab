/*
 ***************************************************************************************************
 * Copyright (c) 2018 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *************************************************************************************************
 */
package de.bund.bfr.knime.fsklab.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class LanguagePreferenceInitializer extends AbstractPreferenceInitializer {

	static final String LANGUAGE = "language";

	private static LanguagePreferenceProvider cachedProvider = null;

	@Override
	public void initializeDefaultPreferences() {
		// Set language initially to English (en)
		IPreferenceStore store = Plugin.getDefault().getPreferenceStore();
		store.setDefault(LANGUAGE, "en");
	}

	/** @return language provider. */
	public static final LanguagePreferenceProvider getProvider() {

		IPreferenceStore store = Plugin.getDefault().getPreferenceStore();
		String language = store.getString(LANGUAGE);

		if (cachedProvider == null || !cachedProvider.getLanguage().equals(language)) {
			cachedProvider = new DefaultLanguagePreferenceProvider(language);
		}

		return cachedProvider;
	}

	/**
	 * Invalidate the cached language provider returned by {@link #getProvider()}.
	 */
	public static final void invalidateProviderCache() {
		cachedProvider = null;
	}
}
