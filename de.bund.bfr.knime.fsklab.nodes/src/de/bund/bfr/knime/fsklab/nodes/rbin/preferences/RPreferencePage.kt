/***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
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
 * not, see <http:></http:>//www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 */
package de.bund.bfr.knime.fsklab.nodes.rbin.preferences

import com.sun.jna.Platform
import de.bund.bfr.knime.fsklab.nodes.rbin.RBinUtil
import de.bund.bfr.knime.fsklab.nodes.rbin.RBinUtil.InvalidRHomeException
import de.bund.bfr.knime.fsklab.FskPlugin
import org.eclipse.jface.dialogs.IMessageProvider
import org.eclipse.jface.preference.DirectoryFieldEditor
import org.eclipse.jface.preference.FieldEditorPreferencePage
import org.eclipse.jface.preference.StringFieldEditor
import org.eclipse.jface.resource.JFaceResources
import org.eclipse.swt.widgets.Composite
import org.eclipse.ui.IWorkbench
import org.eclipse.ui.IWorkbenchPreferencePage
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Preferences page.
 *
 * @author Miguel Alba
 */
class RPreferencePage : FieldEditorPreferencePage(FieldEditorPreferencePage.GRID), IWorkbenchPreferencePage {
    /** Creates a new preference page.  */
    init {
        preferenceStore = FskPlugin.default?.preferenceStore
        description = "BfR R nodes preferences"
    }

    override fun createFieldEditors() {
        val parent = fieldEditorParent
        addField(RHomeDirectoryFieldEditor(RPreferenceInitializer.R3_PATH, "R v3 environment location", parent))
    }

    override fun init(workbench: IWorkbench) {
        // nothing to do
    }

    /**
     * Modified DirectoryFieldEditor with an appropriate doCheckState() overridden and verification on
     * key stroke.
     *
     * @author Jonathan Hale
     */
    private inner class RHomeDirectoryFieldEditor(name: String, labelText: String,
                                                  parent: Composite) : DirectoryFieldEditor() {

        init {
            init(name, labelText)
            setChangeButtonText(JFaceResources.getString("openBrowse"))
            setValidateStrategy(StringFieldEditor.VALIDATE_ON_KEY_STROKE)
            createControl(parent)
        }

        override fun doCheckState(): Boolean {
            val rHome = stringValue

            val rHomePath = Paths.get(rHome)
            if (!Files.isDirectory(rHomePath)) {
                setMessage("The selected path is not a directory.", IMessageProvider.ERROR)
                return false
            }

            try {
                RBinUtil.checkRHome(rHome, true)

                val props = RPreferenceProvider(rHome).properties
                // the version numbers may contain spaces
                val version = "${props.getProperty("major")}.${props.getProperty("minor")}".replace(" ", "")

                if ("3.1.0" == version) {
                    setMessage("You have selected an R 3.1.0 installation. " +
                            "Please see http://tech.knime.org/faq#q26 for details.", IMessageProvider.WARNING)
                    return true
                }

                if (props.getProperty("Rserve.path").isNullOrEmpty()) {
                    setMessage(
                            "The package 'Rserve' needs to be installed in your R installation. " +
                                    "Please install it in R using \"install.packages('Rserve')\".",
                            IMessageProvider.ERROR)
                    return false
                }

                if (props.getProperty("miniCRAN.path").isNullOrEmpty()) {
                    setMessage("The package 'miniCRAN' needs to be installed in your R installation. " +
                            "Please install it in R using \"install.packages('miniCRAN')\".",
                            IMessageProvider.ERROR)
					return false
                }

                // under Mac we need the Cairo package to use png()/bmp() devices
                if (Platform.isMac()) {
                    if (props.getProperty("Cairo.path").isNullOrEmpty()) {
                        setMessage(
                                "The package 'Cairo' needs to be installed in your R installation for " +
                                        "bitmap graphics devices to work properly. Please install it in R using " +
                                        "\"install.packages('Cairo')\".",
                                IMessageProvider.WARNING)
                        return false
                    }
                }

                setMessage(null, IMessageProvider.NONE)
                return true
            } catch (e: InvalidRHomeException) {
                setMessage(e.message, IMessageProvider.ERROR)
                return false
            }
        }
    }
}
