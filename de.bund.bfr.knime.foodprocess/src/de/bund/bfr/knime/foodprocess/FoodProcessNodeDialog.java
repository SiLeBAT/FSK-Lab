/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.foodprocess;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.eclipse.swt.widgets.Display;
import org.hsh.bfr.db.DBKernel;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.config.Config;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.workflow.AnnotationData;
import org.knime.core.node.workflow.NodeAnnotation;
import org.knime.core.node.workflow.NodeAnnotationData;
import org.knime.core.node.workflow.NodeContainer;
import org.knime.core.node.workflow.NodeUIInformation;
import org.knime.core.node.workflow.WorkflowManager;

import de.bund.bfr.knime.foodprocess.lib.FoodProcessSetting;
import de.bund.bfr.knime.foodprocess.ui.FoodProcessUi;
import de.bund.bfr.knime.pcml.port.PCMLPortObjectSpec;
import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.estimatedmodelreader.EmReaderUi;
import de.bund.bfr.knime.util.Agent;
import de.bund.bfr.knime.util.Matrix;
import de.bund.bfr.knime.util.ValueAndUnit;

/**
 * <code>NodeDialog</code> for the "FoodProcess" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Jorgen Brandt
 */
public class FoodProcessNodeDialog extends NodeDialogPane implements ActionListener {
	private final FoodProcessNodeSettings settings;
	public FoodProcessNodeSettings getSettings() {
		return settings;
	}

	private final FoodProcessUi fpui;
	//private final DbConfigurationUi dbui;
	private Bfrdb db = null;
	private JComboBox<EmReaderUi_Agent> cb = null;
	private JPanel agentPanel = null;
	private JPanel modelPanel = null;
	public final static String defaultAgentname = "<agent>";
	private String initProcessName = null;
	private PortObjectSpec[] specs = null;

	//private PredictorViewNodeDialog pvnd = null;

	/**
	 * New pane for configuring the FoodProcess node.
	 */
	protected FoodProcessNodeDialog() {
		this.settings = new FoodProcessNodeSettings();
		//addTab( "FoodProcess settings", new JnFoodProcessUi() );
		fpui = new FoodProcessUi();
		addTab("FoodProcess settings", fpui);
		/*
		 * dbui = new DbConfigurationUi(true);
		 * dbui.getApplyButton().addActionListener(this); try { // fetch
		 * database connection if(dbui.getOverride()) { db = new
		 * Bfrdb(dbui.getFilename(), dbui.getLogin(), dbui.getPasswd()); } else
		 * { db = new Bfrdb(DBKernel.getLocalConn(true)); } } catch (Exception
		 * e) {}
		 */

		try {
			db = new Bfrdb(DBKernel.getLocalConn(true));
		} catch (Exception e1) {
		}

		refreshCB();

		EmReaderUi estmodelui = new EmReaderUi(db, null, true, true, true);
		EmReaderUi_Agent erua = new EmReaderUi_Agent(defaultAgentname, estmodelui);
		cb.addItem(erua);

		agentPanel = new JPanel();
		agentPanel.setBorder(new TitledBorder("Agent"));
		agentPanel.setLayout(new BorderLayout());
		agentPanel.add(cb, BorderLayout.CENTER);

		modelPanel = new JPanel();
		modelPanel.setLayout(new BorderLayout());
		modelPanel.add(agentPanel, BorderLayout.NORTH);
		modelPanel.add(estmodelui, BorderLayout.CENTER);

		addTab("Filter models", modelPanel);

		updateModelName();
		//addTab("Database connection", dbui);
	}

	private void refreshCB() {
		if (cb == null) {
			cb = new JComboBox<EmReaderUi_Agent>();
			cb.setEditable(false);
			cb.setFont(new Font("Serif", Font.BOLD, 16));
			cb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (modelPanel != null) {
						modelPanel.removeAll();
						modelPanel.add(agentPanel, BorderLayout.NORTH);
						EmReaderUi_Agent emrua = ((EmReaderUi_Agent) cb.getSelectedItem());
						if (emrua != null) {
							EmReaderUi estmodelui = emrua.getEstModelUi();
							modelPanel.add(estmodelui, BorderLayout.CENTER);
							modelPanel.repaint();
						}
					}
				}
			});
			cb.setRenderer(new DefaultListCellRenderer() {
				private static final long serialVersionUID = 7061846108043516406L;

				public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
					Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					JLabel label = (JLabel) c;
					label.setHorizontalAlignment(JLabel.CENTER);
					return label;
				}
			});
		} else {
			cb.removeAllItems();
		}
	}

	@Override
	public void saveSettingsTo(final NodeSettingsWO s) {
		try {
			settings.setFoodProcessSetting(fpui.getSettings());

			//System.err.println("save : " + fpui.getSettings().getProcessName());
			settings.saveSettings(s);
			//dbui.saveSettingsTo(s.addConfig("DbConfigurationUi"));

			if (cb != null) {
				List<String> l = new ArrayList<String>();
				for (int i = 0; i < cb.getItemCount(); i++) {
					EmReaderUi estmodelui = cb.getItemAt(i).getEstModelUi();
					if (estmodelui != null) {
						Config c = s.addConfig("Agent_" + cb.getItemAt(i).getAgentName());
						c = c.addConfig("EstModelReaderUi");
						estmodelui.saveSettingsTo(c);
						l.add(cb.getItemAt(i).getAgentName());
					}
				}
				s.addStringArray("Agents", l.toArray(new String[] {}));

				if (initProcessName != null && !initProcessName.equals(fpui.getSettings().getProcessName())) {
					setNodeAnnotation(fpui.getSettings().getProcessName(), s);
				}
			}
		} catch (InvalidSettingsException e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	private void setNodeAnnotation(String newAnnotation, NodeSettingsWO s) {
		long ttt = System.currentTimeMillis();
		Collection<NodeContainer> ncs = WorkflowManager.ROOT.getNodeContainers();
		for (NodeContainer nc : ncs) {
			if (nc instanceof WorkflowManager) {
				WorkflowManager wfm = (WorkflowManager) nc;
				Collection<NodeContainer> wnc = wfm.getNodeContainers();
				for (final NodeContainer wnci : wnc) {
					try {
						if (wnci.hasDialog()) {
							
							NodeDialogPane ndp = null;
							try {
								ndp = wnci.getDialogPaneWithSettings();
							}
							catch (Exception e) {}
							if (ndp != null && ndp instanceof FoodProcessNodeDialog && ndp.equals(this)) {
								FoodProcessNodeDialog fpnd = (FoodProcessNodeDialog) ndp;
								fpnd.loadSettingsFrom((NodeSettingsRO) s, specs);
								String an = wnci.getNodeAnnotation().getData().getText();
								if (!an.equals(newAnnotation)) {
									AnnotationData p2AnnoData = NodeAnnotationData.createFromObsoleteCustomName(newAnnotation);
									NodeAnnotation nodeAnno = wnci.getNodeAnnotation();
									nodeAnno.getData().copyFrom(p2AnnoData, true);

									Display.getDefault().asyncExec(new Runnable() {
										@Override
										public void run() {
											try {
												int[] b = wnci.getUIInformation().getBounds();
												NodeUIInformation uiInformation = NodeUIInformation.builder()
														.setNodeLocation(b[0], b[1], -1, -1)
														.setHasAbsoluteCoordinates(true).build();
												wnci.setUIInformation(uiInformation);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									});
								}
								System.err.println(System.currentTimeMillis() - ttt);
								return;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	protected void loadSettingsFrom(final NodeSettingsRO s, final PortObjectSpec[] specs) throws NotConfigurableException {
		this.specs = specs;
		settings.loadSettingsForDialog(s);

		FoodProcessSetting fps = settings.getFoodProcessSetting();
		fpui.setSettings(fps);
		initProcessName = fps.getProcessName();
		//System.err.println("load : " + fps.getProcessName());

		try {
			//dbui.setSettings(s.getConfig("DbConfigurationUi"));

			refreshCB();
			EmReaderUi estmodelui = new EmReaderUi(db, null, true, true, true);

			PCMLPortObjectSpec instantMix = FoodProcessNodeModel.calculateInstantMixture(specs, fps, null);
			if (instantMix != null && instantMix.getAgents() != null && !instantMix.getAgents().isEmpty()) {
				// take only matrix with biggest volume into account
				Integer matrixID = null;
				Double max = 0.0;
				if (instantMix.getMatrices() != null) {
					for (Matrix m : instantMix.getMatrices().keySet()) {
						if (instantMix.getMatrices().get(m) > max) {
							matrixID = m.getId();
							max = instantMix.getMatrices().get(m);
						}
					}
				}

				if (instantMix.getAgents() != null) {
					for (Agent a : instantMix.getAgents().keySet()) {
						estmodelui = new EmReaderUi(db, null, true, true, true);
						Config c = null;
						if (s.containsKey("Agent_" + a.getName())) {
							c = s.getConfig("Agent_" + a.getName());
							c = c.getConfig("EstModelReaderUi");
						} else if (s.containsKey("EstModelReaderUi")) c = s.getConfig("EstModelReaderUi");
						else if (s.containsKey("Agent_" + defaultAgentname)) {
							c = s.getConfig("Agent_" + defaultAgentname);
							c = c.getConfig("EstModelReaderUi");
						}
						updateModelName(estmodelui);
						ValueAndUnit vau = instantMix.getAgents().get(a);
						estmodelui.setSettings(c, a.getId(), matrixID, instantMix.getTemperature(), instantMix.getPH_value(), instantMix.getAw_value(),
								vau == null ? null : vau.getValue(), new MySettingsRefreshCaller(fpui, specs, estmodelui));
						EmReaderUi_Agent erua = new EmReaderUi_Agent(a.getName(), estmodelui);
						cb.addItem(erua);
					}
				}
			} else {
				Config c = null;
				if (s.containsKey("Agent_" + defaultAgentname)) {
					c = s.getConfig("Agent_" + defaultAgentname);
					c = c.getConfig("EstModelReaderUi");
				} else if (s.containsKey("EstModelReaderUi")) c = s.getConfig("EstModelReaderUi");
				updateModelName(estmodelui);
				estmodelui.setSettings(c);
				EmReaderUi_Agent erua = new EmReaderUi_Agent(defaultAgentname, estmodelui);
				cb.addItem(erua);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateModelName(Bfrdb db) throws SQLException, PmmException {
		if (cb != null) {
			for (int i = 0; i < cb.getItemCount(); i++) {
				updateModelName(cb.getItemAt(i).getEstModelUi());
			}
		}
	}

	private void updateModelName(EmReaderUi estmodelui) throws PmmException, SQLException {
		if (estmodelui != null) {
			estmodelui.clearModelSet();
			estmodelui.setMiscItems(DBKernel.getItemListMisc(db.getConnection()));
			ResultSet result = db.selectModel(1);
			while (result.next()) {
				int modelID = result.getInt(Bfrdb.ATT_MODELID);
				Object visible = DBKernel.getValue(db.getConnection(), "Modellkatalog", "ID", "" + modelID, "visible");
				estmodelui.addModelPrim(modelID, result.getString(Bfrdb.ATT_NAME), DBKernel.myDBi.getHashMap("ModelType").get(result.getInt("Klasse")), visible == null
						|| (visible instanceof Boolean && (Boolean) visible));
			}
			result = db.selectModel(2);
			while (result.next()) {
				int modelID = result.getInt(Bfrdb.ATT_MODELID);
				Object visible = DBKernel.getValue(db.getConnection(), "Modellkatalog", "ID", "" + modelID, "visible");
				estmodelui.addModelSec(modelID, result.getString(Bfrdb.ATT_NAME), DBKernel.myDBi.getHashMap("ModelType").get(result.getInt("Klasse")),
						visible == null || (visible instanceof Boolean && (Boolean) visible));
			}
		}
	}

	private void updateModelName() {
		try {
			updateModelName(db);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			updateModelName(db);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
