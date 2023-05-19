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
 *   Jan 25, 2019 (marcel): created
 */

import static de.bund.bfr.knime.fsklab.preferences.PythonPreferenceUtils.performActionOnWidgetInUiThread;
import static de.bund.bfr.knime.fsklab.preferences.PythonPreferenceUtils.setLabelTextAndResize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import de.bund.bfr.knime.fsklab.python2.config.AbstractSerializerPanel;
import de.bund.bfr.knime.fsklab.python2.config.SerializerConfig;
import org.knime.python2.extensions.serializationlibrary.SerializationLibraryExtension;
import org.knime.python2.extensions.serializationlibrary.SerializationLibraryExtensions;

/**
 * @author Marcel Wiedenmann, KNIME GmbH, Konstanz, Germany
 * @author Christian Dietz, KNIME GmbH, Konstanz, Germany
 */
public final class SerializerPreferencePanel extends AbstractSerializerPanel<Composite> {

    public SerializerPreferencePanel(final SerializerConfig config, final Composite parent) {
        super(config, parent);
    }

    @Override
    protected Composite createPanel(final Composite parent) {
        final Composite panel = new Composite(parent, SWT.NONE);
        panel.setLayout(new GridLayout());
        return panel;
    }

    @Override
    protected void createSerializerWidget(final SettingsModelString serializerConfig,
        final SettingsModelString errorMessageModel, final Composite panel) {
        final SerializerSelectionBox serializerSelection =
            new SerializerSelectionBox(serializerConfig, errorMessageModel, panel);
        final GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = SWT.FILL;
        serializerSelection.setLayoutData(gridData);
    }

    private static final class SerializerSelectionBox extends Composite {

        private final Combo m_serializerSelection;

        private List<String> m_serializerIdsSortedByName;

        /**
         * @param serializerConfig The settings model for the serializer selection.
         * @param errorMessageModel The settings model for the error label. May be updated asynchronously, that is, in a
         *            non-UI thread.
         * @param parent The parent widget.
         */
        public SerializerSelectionBox(final SettingsModelString serializerConfig,
            final SettingsModelString errorMessageModel, final Composite parent) {
            super(parent, SWT.NONE);
            final GridLayout gridLayout = new GridLayout();
            gridLayout.numColumns = 2;
            setLayout(gridLayout);

            // Label:
            final Label serializerLabel = new Label(this, SWT.NONE);
            serializerLabel.setText("Serialization library");
            serializerLabel.setLayoutData(new GridData());

            // Serializer selection:
            m_serializerSelection = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
            final String[] serializerNamesSorted = setupSerializers();
            m_serializerSelection.setItems(serializerNamesSorted);
            setSelectedSerializer(serializerConfig.getStringValue());
            serializerConfig.addChangeListener(e -> setSelectedSerializer(serializerConfig.getStringValue()));
            m_serializerSelection.addSelectionListener(new SelectionListener() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    serializerConfig.setStringValue(getSelectedSerializer());
                }

                @Override
                public void widgetDefaultSelected(final SelectionEvent e) {
                    widgetSelected(e);
                }
            });
            m_serializerSelection.setLayoutData(new GridData());

            // Error label:
            final Label error = new Label(this, SWT.NONE);
            final Color red = new Color(parent.getDisplay(), 255, 0, 0);
            error.setForeground(red);
            error.addDisposeListener(e -> red.dispose());
            setLabelText(error, errorMessageModel.getStringValue());
            errorMessageModel.addChangeListener(e -> setLabelText(error, errorMessageModel.getStringValue()));
            final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1);
            gridData.verticalIndent = 10;
            error.setLayoutData(gridData);
        }

        private String[] setupSerializers() {
            final TreeMap<String, String> serializersSortedByName = new TreeMap<>();
            for (final SerializationLibraryExtension serializer : SerializationLibraryExtensions.getExtensions()) {
                if (!serializer.isHidden()) {
                    serializersSortedByName.put(serializer.getJavaSerializationLibraryFactory().getName(),
                        serializer.getId());
                }
            }
            String[] serializerNamesSorted = new String[serializersSortedByName.size()];
            m_serializerIdsSortedByName = new ArrayList<>(serializersSortedByName.size());
            int i = 0;
            for (final Entry<String, String> serializer : serializersSortedByName.entrySet()) {
                serializerNamesSorted[i] = serializer.getKey();
                m_serializerIdsSortedByName.add(serializer.getValue());
                i++;
            }
            return serializerNamesSorted;
        }

        private String getSelectedSerializer() {
            return m_serializerIdsSortedByName.get(m_serializerSelection.getSelectionIndex());
        }

        private void setSelectedSerializer(final String serializerId) {
            final int selectionIndex = m_serializerIdsSortedByName.indexOf(serializerId);
            if (selectionIndex == -1) {
                throw new IllegalStateException("Python serialization library '" + serializerId
                    + "' is not available. Are you missing a KNIME extension?");
            }
            m_serializerSelection.select(selectionIndex);
        }

        private static void setLabelText(final Label label, final String text) {
            performActionOnWidgetInUiThread(label, () -> {
                setLabelTextAndResize(label, text);
                return null;
            }, false);
        }
    }
}