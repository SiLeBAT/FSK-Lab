/*******************************************************************************
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
 *******************************************************************************/
package de.bund.bfr.knime.pmm.js.modelplotter.modern;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 * Node configuration data, such as chart title and Y0.
 * 
 * @author Kilian Thiel, KNIME.com AG, Berlin, Germany
 *
 */
final class ModelPlotterViewConfig {

  static final String CHART_TITLE = "chartTitle";

  static final String FUNCTION = "function";
  static final String FUNCTION_FULL = "function.full";

  static final String MODEL_NAME = "modelName";
  static final String DBUUID = "dbuuid";

  static final String CONSTANTS_KEYS = "constants.keys";
  static final String CONSTANTS_VALUES = "constants.values";
  static final String VARIABLES = "variables";

  static final String X_UNIT = "x unit";
  static final String Y_UNIT = "y unit";

  static final String Y0 = "Y0";

  static final String MIN_X_AXIS = "Min X Axis";
  static final String MAX_X_AXIS = "Max X Axis";

  static final String MIN_Y_AXIS = "Min Y Axis";
  static final String MAX_Y_AXIS = "Max Y Axis";

  static final String HIDE_IN_WIZARD = "hideInWizard";

  static final double DEF_Y0 = 4.169;
  static final double MIN_Y0 = 0;
  static final double MAX_Y0 = 10;

  static final int DEF_MIN_X_AXIS = -10;
  static final int MAX_MIN_X_AXIS = 0;
  static final int MIN_MIN_X_AXIS = -1000;

  static final int DEF_MAX_X_AXIS = 100;
  static final int MAX_MAX_X_AXIS = 1000;
  static final int MIN_MAX_X_AXIS = 0;

  static final int DEF_MIN_Y_AXIS = -10;
  static final int MAX_MIN_Y_AXIS = -10;
  static final int MIN_MIN_Y_AXIS = -1000;

  static final int DEF_MAX_Y_AXIS = 20;
  static final int MAX_MAX_Y_AXIS = 1000;
  static final int MIN_MAX_Y_AXIS = 0;

  private static final String DEF_CHART_TITLE = "PMM Model Plot";
  private String m_chartTitle = DEF_CHART_TITLE;
  static final String modelSelection = "modelSelectionStatus";
  static final String secondarySelection = "secondarySelectionStatus";
  static final String HEADLESS = "HEADLESS";
  private boolean allModelAreSelected = true;
  private boolean secondaryModel;
  private boolean headLess;



  private boolean m_isHideInWizard = false;
  private double m_y0 = DEF_Y0;

  private int m_minXAxis = DEF_MIN_X_AXIS;
  private int m_maxXAxis = DEF_MAX_X_AXIS;
  private int m_minYAxis = DEF_MIN_Y_AXIS;
  private int m_maxYAxis = DEF_MAX_Y_AXIS;


  /**
   * @return
   */
  public int getMinYAxis() {
    return m_minYAxis;
  }

  /**
   * @param minXAxis
   */
  public void setMinYAxis(final int minYAxis) {
    m_minYAxis = minYAxis;
  }

  /**
   * @return
   */
  public int getMaxYAxis() {
    return m_maxYAxis;
  }

  /**
   * @param minXAxis
   */
  public void setMaxYAxis(final int maxYAxis) {
    m_maxYAxis = maxYAxis;
  }

  /**
   * @return
   */
  public int getMinXAxis() {
    return m_minXAxis;
  }

  /**
   * @param minXAxis
   */
  public void setMinXAxis(final int minXAxis) {
    m_minXAxis = minXAxis;
  }

  /**
   * @return
   */
  public int getMaxXAxis() {
    return m_maxXAxis;
  }

  /**
   * @param minXAxis
   */
  public void setMaxXAxis(final int maxXAxis) {
    m_maxXAxis = maxXAxis;
  }

  /**
   * @return
   */
  public double getY0() {
    return m_y0;
  }

  /**
   * @param y0
   */
  public void setY0(final double y0) {
    this.m_y0 = y0;
  }

  /**
   * @return
   */
  public boolean getHideInwizard() {
    return m_isHideInWizard;
  }

  /**
   * @param hideInWizard
   */
  public void setHideInWizard(final boolean hideInWizard) {
    this.m_isHideInWizard = hideInWizard;
  }

  /**
   * @return the chartTitle
   */
  public String getChartTitle() {
    return m_chartTitle;
  }

  /**
   * @param chartTitle the chartTitle to set
   */
  public void setChartTitle(final String chartTitle) {
    m_chartTitle = chartTitle;
  }

  /**
   * Loads parameters in NodeModel.
   * 
   * @param settings To load from.
   * @throws InvalidSettingsException If incomplete or wrong.
   */
  public void loadSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
    m_chartTitle = settings.getString(CHART_TITLE);
    allModelAreSelected = settings.getBoolean(modelSelection);
    secondaryModel = settings.getBoolean(secondarySelection);
    headLess = settings.getBoolean(HEADLESS);
    m_isHideInWizard = settings.getBoolean(HIDE_IN_WIZARD);
    m_y0 = settings.getDouble(Y0);
    m_minXAxis = settings.getInt(MIN_X_AXIS);
    m_maxXAxis = settings.getInt(MAX_X_AXIS);
    m_minYAxis = settings.getInt(MIN_Y_AXIS);
    m_maxYAxis = settings.getInt(MAX_Y_AXIS);
  }

  /**
   * Loads parameters in Dialog.
   * 
   * @param settings To load from.
   */
  public void loadSettingsForDialog(final NodeSettingsRO settings) {
    m_chartTitle = settings.getString(CHART_TITLE, DEF_CHART_TITLE);
    allModelAreSelected = settings.getBoolean(modelSelection, allModelAreSelected);
    secondaryModel = settings.getBoolean(secondarySelection, secondaryModel);
    headLess = settings.getBoolean(HEADLESS, headLess);

    m_isHideInWizard = settings.getBoolean(HIDE_IN_WIZARD, false);
    m_y0 = settings.getDouble(Y0, DEF_Y0);
    m_minXAxis = settings.getInt(MIN_X_AXIS, DEF_MIN_X_AXIS);
    m_maxXAxis = settings.getInt(MAX_X_AXIS, DEF_MAX_X_AXIS);
    m_minYAxis = settings.getInt(MIN_Y_AXIS, DEF_MIN_Y_AXIS);
    m_maxYAxis = settings.getInt(MAX_Y_AXIS, DEF_MAX_Y_AXIS);
  }

  /**
   * Saves current parameters to settings object.
   * 
   * @param settings To save to.
   */
  public void saveSettings(final NodeSettingsWO settings) {
    settings.addString(CHART_TITLE, m_chartTitle);
    settings.addBoolean(HIDE_IN_WIZARD, m_isHideInWizard);
    settings.addBoolean(modelSelection, allModelAreSelected);
    settings.addBoolean(secondarySelection, secondaryModel);
    settings.addBoolean(HEADLESS, headLess);
    settings.addDouble(Y0, m_y0);
    settings.addInt(MIN_X_AXIS, m_minXAxis);
    settings.addInt(MAX_X_AXIS, m_maxXAxis);
    settings.addInt(MIN_Y_AXIS, m_minYAxis);
    settings.addInt(MAX_Y_AXIS, m_maxYAxis);
  }

  public boolean isSecondaryModel() {
    return secondaryModel;
  }

  public void setSecondaryModel(boolean secondaryModel) {
    this.secondaryModel = secondaryModel;
  }

  public boolean isHeadLess() {
    return headLess;
  }

  public void setHeadLess(boolean headLess) {
    this.headLess = headLess;
  }

  public boolean isAllModelAreSelected() {
    return allModelAreSelected;
  }

  public void setAllModelAreSelected(boolean allModelAreSelected) {
    this.allModelAreSelected = allModelAreSelected;
  }
}
