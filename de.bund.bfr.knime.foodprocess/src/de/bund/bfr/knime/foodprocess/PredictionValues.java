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

import de.bund.bfr.knime.pmm.common.chart.ChartAllPanel;
import de.bund.bfr.knime.pmm.predictorview.PredictorViewNodeDialog;
import de.bund.bfr.knime.pmm.predictorview.SettingsHelper;

public class PredictionValues {

	public PredictorViewNodeDialog getPvnd() {
		return pvnd;
	}

	public void setPvnd(PredictorViewNodeDialog pvnd) {
		this.pvnd = pvnd;
	}

	public ChartAllPanel getChartPanel() {
		return chartPanel;
	}

	public void setChartPanel(ChartAllPanel chartPanel) {
		this.chartPanel = chartPanel;
	}

	public String getLog10N0() {
		return log10N0;
	}

	public void setLog10N0(String log10n0) {
		log10N0 = log10n0;
	}

	public String getLag() {
		return lag;
	}

	public void setLag(String lag) {
		this.lag = lag;
	}

	public String getUnitLog10N0() {
		return unitLog10N0;
	}

	public void setUnitLog10N0(String unitLog10N0) {
		this.unitLog10N0 = unitLog10N0;
	}

	public String getUnitTime() {
		return unitTime;
	}

	public void setUnitTime(String unitTime) {
		this.unitTime = unitTime;
	}

	public String getUnitTemp() {
		return unitTemp;
	}

	public void setUnitTemp(String unitTemp) {
		this.unitTemp = unitTemp;
	}

	public String getUnitLog10N() {
		return unitLog10N;
	}

	public void setUnitLog10N(String unitLog10N) {
		this.unitLog10N = unitLog10N;
	}

	public String getUnitPres() {
		return unitPres;
	}

	public void setUnitPres(String unitPres) {
		this.unitPres = unitPres;
	}

	private SettingsHelper set;
	public SettingsHelper getSet() {
		return set;
	}

	private PredictorViewNodeDialog pvnd = null;
	private ChartAllPanel chartPanel = null;
	private String log10N0;
	private String lag;
	private String unitLog10N0;
	private String unitTime;
	private String unitTemp;
	private String unitLog10N;
	private String unitPres;
	//private String unitLag = "h"
	
	public PredictionValues(SettingsHelper set) {
		this.set = set;
		log10N0 = "log10N0";
		lag = "lag";
		unitLog10N0 = FoodProcessNodeModel.defaultBacterialUnit;//"log10(count/g)";
		unitTime = "h";
		unitTemp = "°C";
		unitLog10N = FoodProcessNodeModel.defaultBacterialUnit;//"log10(count/g)";
		unitPres = "bar";
		// , unitLag = "h"
	}
}
