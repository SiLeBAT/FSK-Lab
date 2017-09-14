/*
 ***************************************************************************************************
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
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *************************************************************************************************
 */
package de.bund.bfr.knime.fsklab.nodes.ui

import com.toedter.calendar.JDateChooser
import de.bund.bfr.knime.fsklab.nodes.rsnippet.RSnippetDocument
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.Theme
import org.fife.ui.rtextarea.RTextScrollPane
import org.knime.core.node.NodeLogger
import java.io.File
import java.awt.BorderLayout
import java.awt.Image
import java.io.IOException
import javax.swing.*


/** Creates a panel with the names of a number of libraries.  */
fun createLibrariesPanel(libs: Set<File>): JPanel {

    val panel = JPanel(BorderLayout())
    panel.name = "Libraries list"

    val libNames = libs.map(File::getName).toTypedArray()
    val list = JList(libNames)
    list.layoutOrientation = JList.VERTICAL
    list.selectionMode = ListSelectionModel.SINGLE_SELECTION
    panel.add(JScrollPane(list))

    return panel
}

/** Fixes JDateChooser and disables the text field.  */
class FixedDateChooser : JDateChooser() {

    init {
        // Fixes bug AP-5865
        popup.isFocusable = false

        /* Text field is disabled so that the dates are only chooseable through the calendar widget.
         Then there is no need to validate the dates. */
        dateEditor.setEnabled(true)
    }
}

/**
 * Displays the R result image.
 *
 * @author Thomas Gabriel, University of Konstanz
 */
class RPlotterViewPanel : JPanel() {

    private val m_label = JLabel("<No Plot>")

    /** Creates a new panel with an empty label.  */
    init {
        add(m_label)
    }

    /** @param image The new image or null to display. */
    fun update(image: Image?) {
        if (image == null) {
            m_label.icon = null
            m_label.text = "<No plot>"
        } else {
            m_label.text = null
            m_label.icon = ImageIcon(image)
        }

        repaint()
    }
}

/**
 * A text area for the R snippet expression.
 *
 * @author Heiko Hofer.
 */
class RSnippetTextArea : RSyntaxTextArea(RSnippetDocument(), null, 20, 60) {
    /** Create a new component.  */
    init {

        document = RSnippetDocument()
        try {
            applySyntaxColors()
        } catch (e: Exception) {
            LOGGER.debug(e.message, e)
        }

        syntaxEditingStyle = RSnippetDocument.SYNTAX_STYLE_R
    }// initial text != null causes a NullPointerException

    @Throws(IOException::class)
    private fun applySyntaxColors() {
        val pack = RSnippetTextArea::class.java.`package`
        val base = pack.name.replace(".", "/") + "/"
        val url = javaClass.classLoader.getResource(base + "r_syntax_style.xml")
        url?.openStream().use { inputStream ->
            val theme = Theme.load(inputStream)
            theme.apply(this)
        }
    }

    companion object {
        private val LOGGER = NodeLogger.getLogger(RSyntaxTextArea::class.java)
    }
}

/** JPanel with an R script  */
class ScriptPanel(title: String, script: String, editable: Boolean) : JPanel(BorderLayout()) {
    val textArea: RSnippetTextArea

    init {
        name = title

        textArea = RSnippetTextArea()
        textArea.lineWrap = true
        textArea.text = script
        textArea.isEditable = editable
        add(RTextScrollPane(textArea))
    }
}