/***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http:></http:>//www.gnu.org/licenses/>.
 *
 *
 * Contributors: Department Biological Safety - BfR
 */
package de.bund.bfr.knime.fsklab.nodes.rbin.preferences

import de.bund.bfr.knime.fsklab.FskPlugin
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer

/**
 * Initializes preference page with default paths to R2 and R3 environments.
 *
 * @author Miguel Alba
 */
class RPreferenceInitializer : AbstractPreferenceInitializer() {

    override fun initializeDefaultPreferences() {
        FskPlugin.default?.preferenceStore?.setDefault(R3_PATH, "")
    }

    companion object {

        /** Path to R v.3  */
        internal val R3_PATH = "r3.path"

        private var m_cachedR3PreferenceProvider: RPreferenceProvider? = null

        /** @return provider to the path to the R3 executable. */
        val r3Provider: RPreferenceProvider
            get() {
                val r3Home = FskPlugin.default?.preferenceStore?.getString(R3_PATH) ?: ""
                if (m_cachedR3PreferenceProvider == null || m_cachedR3PreferenceProvider!!.rHome != r3Home) {
                    m_cachedR3PreferenceProvider = RPreferenceProvider(r3Home)
                }

                return m_cachedR3PreferenceProvider as RPreferenceProvider
            }

        /**
         * Invalidate the cached R3 preference provider returned by [.getR3Provider], to refetch R
         * properties (which launches an external R command).
         */
        fun invalidateR3PreferenceProviderCache() {
            m_cachedR3PreferenceProvider = null
        }
    }
}
