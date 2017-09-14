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
package de.bund.bfr.knime.fsklab.nodes.rbin.preferences

import com.sun.jna.Platform
import de.bund.bfr.knime.fsklab.nodes.rbin.RBinUtil
import java.io.File
import java.nio.file.Paths
import java.util.*

/**
 * Provider for R preferences. It determines the R binary path based on the R home given in the constructor.
 *
 * @author Thorsten Meinl, KNIME.com, Zurich, Switzerland (original Java implementation)
 * @author Jonathan Hale (original Java implementation)
 * @author Miguel de Alba (Kotlin port)
 */
class RPreferenceProvider(val rHome: String) {

    val properties: Properties by lazy { RBinUtil.retrieveRProperties(this) }

    fun getRBinPath(command: String): String {
        val binPath = Paths.get(rHome, "bin")
        return if (Platform.isWindows()) {
            val arch = if (Platform.is64Bit()) "x64" else "i386"
            "${binPath.resolve(arch)}${File.separator}$command.exe"
        } else "$binPath${File.separator}$command"
    }

    fun getRServeBinPath(): String {
        val rservePath = Paths.get(properties.getProperty("Rserve.path"))
        val rserveLibs = rservePath.resolve("libs")

        return if (Platform.isWindows()) {
            if (Platform.is64Bit()) rserveLibs.resolve("x64/Rserve.exe").toString()
            else rserveLibs.resolve("i386/Rserve.exe").toString()
        } else rserveLibs.resolve("Rserve.dbg").toString()
    }
}