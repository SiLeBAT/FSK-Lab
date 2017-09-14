/*
 ***************************************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
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
package de.bund.bfr.knime.fsklab

import org.eclipse.ui.plugin.AbstractUIPlugin
import org.osgi.framework.BundleContext
import java.util.*

class FskPlugin : AbstractUIPlugin() {
    init {
        default = this
    }

    @Throws(Exception::class)
    override fun start(context: BundleContext) {
        super.start(context)
    }

    @Throws(Exception::class)
    override fun stop(context: BundleContext?) {
        super.stop(context)
        default = null
    }

    companion object {
        var default: FskPlugin? = null
            private set

        val MESSAGES_BUNDLE: ResourceBundle by lazy { ResourceBundle.getBundle("MessagesBundle") }
    }
}
