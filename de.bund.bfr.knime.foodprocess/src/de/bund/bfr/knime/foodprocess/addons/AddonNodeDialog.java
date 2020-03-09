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
package de.bund.bfr.knime.foodprocess.addons;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.port.PortObjectSpec;

/**
 * <code>NodeDialog</code> for the "Ingredients" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author BfR
 */
public class AddonNodeDialog extends NodeDialogPane {

	private AddonC mIc;
	public AddonC getSettingsPanel() {
		return mIc;
	}

	private boolean forMatrices;

    /**
     * New pane for configuring Ingredients node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected AddonNodeDialog(boolean forMatrices) {
        super();
        this.forMatrices = forMatrices;
        mIc = new AddonC(forMatrices);
        this.addTab(forMatrices ? "Ingredients" : "Agents", mIc);
    }

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		double[] volumeDef = mIc.getVolumeDef();
		if (volumeDef != null) {
	    	settings.addDoubleArray(AddonNodeModel.PARAM_VOLUME, volumeDef);
	    	settings.addStringArray(AddonNodeModel.PARAM_VOLUME_UNIT, mIc.getVolumeUnitDef());
	    	mIc.saveSettings(settings.addConfig(forMatrices ? AddonNodeModel.PARAM_MATRICES : AddonNodeModel.PARAM_AGENTS));
    	}
	}

    @Override
    protected void loadSettingsFrom(final NodeSettingsRO s,
    		final PortObjectSpec[] specs) throws NotConfigurableException {
    	try {
			double[] volumeDef = s.containsKey(AddonNodeModel.PARAM_VOLUME) ? s.getDoubleArray(AddonNodeModel.PARAM_VOLUME) : null;
	    	String[] volumeUnitDef = s.containsKey(AddonNodeModel.PARAM_VOLUME_UNIT) ? s.getStringArray(AddonNodeModel.PARAM_VOLUME_UNIT) : null;
	    	String key = forMatrices ? AddonNodeModel.PARAM_MATRICES : AddonNodeModel.PARAM_AGENTS;
	    	mIc.setSettings(s.containsKey(key) ? s.getConfig(key) : null, volumeDef, volumeUnitDef);
		}
    	catch (InvalidSettingsException e) {
			e.printStackTrace();
		}
    }
}