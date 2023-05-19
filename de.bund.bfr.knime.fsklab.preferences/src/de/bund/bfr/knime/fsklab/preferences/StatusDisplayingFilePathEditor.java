package de.bund.bfr.knime.fsklab.preferences;

/*
 * ------------------------------------------------------------------------
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
 * ------------------------------------------------------------------------
 */


import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.StringButtonFieldEditor;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import de.bund.bfr.knime.fsklab.envconfigs.Activator;

/**
 * Dialog component that allows to select the path to a file or directry. Allows to display installation info and error
 * messages. Is able to display selection as "default".
 *
 * @author Marcel Wiedenmann, KNIME GmbH, Konstanz, Germany
 * @author Christian Dietz, KNIME GmbH, Konstanz, Germany
 * @author Clemens von Schwerin, KNIME.com, Konstanz, Germany
 */
public final class StatusDisplayingFilePathEditor extends Composite {

    private final Label m_header;

    /**
     * @param pathModel The settings model for the file or directory path.
     * @param isFilePathEditor {@code true} if the editor is for file selection, {@code false} if it is for directory
     *            selection.
     * @param headerLabel The text of the header for the path editor's enclosing group box.
     * @param editorLabel The description text for the path editor.
     * @param infoMessageModel The settings model for the info label. May be updated asynchronously, that is, in a
     *            non-UI thread.
     * @param errorMessageModel The settings model for the error label. May be updated asynchronously, that is, in a
     *            non-UI thread.
     * @param parent The parent widget.
     */
    public StatusDisplayingFilePathEditor(final SettingsModelString pathModel, final boolean isFilePathEditor,
        final String headerLabel, final String editorLabel, final SettingsModelString infoMessageModel,
        final SettingsModelString errorMessageModel, final Composite parent) {
        this(pathModel, isFilePathEditor, headerLabel, editorLabel, infoMessageModel, null, errorMessageModel, parent);
    }

    /**
     * @param pathModel The settings model for the file or directory path.
     * @param isFilePathEditor {@code true} if the editor is for file selection, {@code false} if it is for directory
     *            selection.
     * @param headerLabel The text of the header for the path editor's enclosing group box.
     * @param editorLabel The description text for the path editor.
     * @param infoMessageModel The settings model for the info label. May be updated asynchronously, that is, in a
     *            non-UI thread.
     * @param warningMessageModel The settings model for the warning label. May be updated asynchronously, that is, in a
     *            non-UI thread. May be <code>null</code> if no warning should be displayed.
     * @param errorMessageModel The settings model for the error label. May be updated asynchronously, that is, in a
     *            non-UI thread.
     * @param parent The parent widget.
     */
    public StatusDisplayingFilePathEditor(final SettingsModelString pathModel, final boolean isFilePathEditor,
        final String headerLabel, final String editorLabel, final SettingsModelString infoMessageModel,
        final SettingsModelString warningMessageModel, final SettingsModelString errorMessageModel,
        final Composite parent) {
        super(parent, SWT.NONE);

        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        setLayout(gridLayout);

        // Header:
        m_header = new Label(this, SWT.NONE);
        FontDescriptor descriptor = FontDescriptor.createFrom(m_header.getFont());
        descriptor = descriptor.setStyle(SWT.BOLD);
        m_header.setFont(descriptor.createFont(m_header.getDisplay()));
        m_header.setText(headerLabel);
        GridData gridData = new GridData();
        gridData.horizontalSpan = 3;
        m_header.setLayoutData(gridData);

        // Path editor:
        final StringButtonFieldEditor pathEditor = isFilePathEditor //
            ? new FileFieldEditor(Activator.PLUGIN_ID, editorLabel, this) //
            : new DirectoryFieldEditor(Activator.PLUGIN_ID, editorLabel, this);

        pathEditor.setStringValue(pathModel.getStringValue());
        pathModel.addChangeListener(e -> pathEditor.setStringValue(pathModel.getStringValue()));
        pathEditor.getTextControl(this).addListener(SWT.Traverse, event -> {
            pathModel.setStringValue(pathEditor.getStringValue());
            if (event.detail == SWT.TRAVERSE_RETURN) {
                event.doit = false;
            }
        });
        pathEditor.setPropertyChangeListener(event -> pathModel.setStringValue(pathEditor.getStringValue()));

        // Info, warning and error labels:
        gridData = new GridData();
        gridData.verticalIndent = 10;
        gridData.horizontalSpan = 3;
        if (warningMessageModel == null) {
            final InstallationStatusDisplayPanel statusDisplay =
                new InstallationStatusDisplayPanel(infoMessageModel, errorMessageModel, this);
            statusDisplay.setLayoutData(gridData);
        } else {
            final InstallationStatusDisplayPanelWithWarning statusDisplay =
                new InstallationStatusDisplayPanelWithWarning(infoMessageModel, warningMessageModel, errorMessageModel,
                    this);
            statusDisplay.setLayoutData(gridData);
        }
    }

    public void setDisplayAsDefault(final boolean setAsDefault) {
        final String defaultSuffix = " (Default)";
        final String oldHeaderText = m_header.getText();
        if (setAsDefault) {
            if (!oldHeaderText.endsWith(defaultSuffix)) {
                m_header.setText(oldHeaderText + defaultSuffix);
                layout();
            }
        } else {
            final int suffixStart = oldHeaderText.indexOf(defaultSuffix);
            if (suffixStart != -1) {
                m_header.setText(oldHeaderText.substring(0, suffixStart));
                layout();
            }
        }
    }
}