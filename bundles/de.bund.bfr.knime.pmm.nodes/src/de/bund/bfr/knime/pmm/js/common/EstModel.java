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
package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Strings;

import de.bund.bfr.knime.pmm.common.EstModelXml;

/**
 * PmmLab estimated model. Holds:
 * <ul>
 * <li>id
 * <li>name
 * <li>sse
 * <li>rms
 * <li>r2
 * <li>aic
 * <li>bic
 * <li>dof
 * <li>quality score
 * <li>checked
 * <li>comment
 * <li>dbuuid
 * </ul>
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class EstModel implements ViewValue {

	// Configuration keys
	static final String ID = "id";
	static final String NAME = "name";
	static final String SSE = "sse";
	static final String RMS = "rms";
	static final String R2 = "r2";
	static final String AIC = "aic";
	static final String BIC = "bic";
	static final String DOF = "dof";
	static final String QUALITY_SCORE = "qualityScore";
	static final String CHECKED = "checked";
	static final String COMMENT = "comment";
	static final String DBUUID = "dbuuid";

	private Integer id;
	private String name;
	private Double sse;
	private Double rms;
	private Double r2;
	private Double aic;
	private Double bic;
	private Integer dof;
	private Integer qualityScore;
	private Boolean checked;
	private String comment;
	private String dbuuid;

	/**
	 * Returns the id of this {@link EstModel}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the id of this {@link EstModel}
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Returns the name of this {@link EstModel}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the name of this {@link EstModel}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the SSE of this {@link EstModel}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the SSE of this {@link EstModel}
	 */
	public Double getSse() {
		return sse;
	}

	/**
	 * Returns the RMS of this {@link EstModel}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the RMS of this {@link EstModel}
	 */
	public Double getRms() {
		return rms;
	}

	/**
	 * Returns the R2 of this {@link EstModel}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the R2 of this {@link EstModel}
	 */
	public Double getR2() {
		return r2;
	}

	/**
	 * Returns the AIC of this {@link EstModel}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the AIC of this {@link EstModel}
	 */
	public Double getAIC() {
		return aic;
	}

	/**
	 * Returns the BIC of this {@link EstModel}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the BIC of this {@link EstModel}
	 */
	public Double getBIC() {
		return bic;
	}

	/**
	 * Returns the DOF of this {@link EstModel}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the DOF of this {@link EstModel}
	 */
	public Integer getDof() {
		return dof;
	}

	/**
	 * Returns the quality score of this {@link EstModel}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the quality score of this {@link EstModel}
	 */
	public Integer getQualityScore() {
		return qualityScore;
	}

	/**
	 * Returns the checked status of this {@link EstModel}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the checked status of this {@link EstModel}
	 */
	public Boolean getChecked() {
		return checked;
	}

	/**
	 * Returns the comment of this {@link EstModel}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the comment of this {@link EstModel}
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Returns the DBUUID of this {@link EstModel}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the DBUUID of this {@link EstModel}
	 */
	public String getDbuuid() {
		return dbuuid;
	}

	/**
	 * Sets the id of this {@link EstModel}.
	 * 
	 * @param id the id to be set
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * Sets the name of this {@link EstModel}.
	 * 
	 * @param name the name to be set
	 */
	public void setName(final String name) {
		this.name = Strings.emptyToNull(name);
	}

	/**
	 * Sets the SSE of this {@link EstModel}.
	 * 
	 * @param sse the SSE to be set
	 */
	public void setSse(final Double sse) {
		this.sse = sse;
	}

	/**
	 * Sets the RMS of this {@link EstModel}.
	 * 
	 * @param rms the RMS to be set
	 */
	public void setRms(final Double rms) {
		this.rms = rms;
	}

	/**
	 * Sets the R2 of this {@link EstModel}.
	 * 
	 * @param r2 the R2 to be set
	 */
	public void setR2(final Double r2) {
		this.r2 = r2;
	}

	/**
	 * Sets the AIC of this {@link EstModel}.
	 * 
	 * @param aic the AIC to be set
	 */
	public void setAIC(final Double aic) {
		this.aic = aic;
	}

	/**
	 * Sets the BIC of this {@link EstModel}.
	 * 
	 * @param bic the BIC to be set
	 */
	public void setBIC(final Double bic) {
		this.bic = bic;
	}

	/**
	 * Sets the degrees of freedom of this {@link EstModel}.
	 * 
	 * @param dof the degrees of freedom to be set
	 */
	public void setDof(final Integer dof) {
		this.dof = dof;
	}

	/**
	 * Sets the quality score of this {@link EstModel}.
	 * 
	 * @param qualityScore the quality score to be set
	 */
	public void setQualityScore(final Integer qualityScore) {
		this.qualityScore = qualityScore;
	}

	/**
	 * Sets the checked status of this {@link EstModel}.
	 * 
	 * @param checked the checked status to be set
	 */
	public void setChecked(final Boolean checked) {
		this.checked = checked;
	}

	/**
	 * Sets the comment of this {@link EstModel}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param comment the comment to be set
	 */
	public void setComment(final String comment) {
		this.comment = Strings.emptyToNull(comment);
	}

	/**
	 * Sets the DBUUID of this {@link EstModel}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param dbuuid the DBUUID to be set
	 */
	public void setDbuuid(final String dbuuid) {
		this.dbuuid = Strings.emptyToNull(dbuuid);
	}

	/** Saves estimated model properties into a {@link NodeSettingsWO}. */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		SettingsHelper.addInt(ID, id, settings);
		SettingsHelper.addString(NAME, name, settings);
		SettingsHelper.addDouble(SSE, sse, settings);
		SettingsHelper.addDouble(RMS, rms, settings);
		SettingsHelper.addDouble(R2, r2, settings);
		SettingsHelper.addDouble(AIC, aic, settings);
		SettingsHelper.addDouble(BIC, bic, settings);
		SettingsHelper.addInt(DOF, dof, settings);
		SettingsHelper.addInt(QUALITY_SCORE, qualityScore, settings);
		SettingsHelper.addBoolean(CHECKED, checked, settings);
		SettingsHelper.addString(COMMENT, comment, settings);
		SettingsHelper.addString(DBUUID, dbuuid, settings);
	}

	/**
	 * Loads estimated model properties from a {@link NodeSettingsRO}.
	 */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		id = SettingsHelper.getInteger(ID, settings);
		name = SettingsHelper.getString(NAME, settings);
		sse = SettingsHelper.getDouble(SSE, settings);
		rms = SettingsHelper.getDouble(RMS, settings);
		r2 = SettingsHelper.getDouble(R2, settings);
		aic = SettingsHelper.getDouble(AIC, settings);
		bic = SettingsHelper.getDouble(BIC, settings);
		dof = SettingsHelper.getInteger(DOF, settings);
		qualityScore = SettingsHelper.getInteger(QUALITY_SCORE, settings);
		checked = SettingsHelper.getBoolean(CHECKED, settings);
		comment = SettingsHelper.getString(COMMENT, settings);
		dbuuid = SettingsHelper.getString(DBUUID, settings);
	}

	/** Creates an {@link EstModel} from an {@link EstModelXml}. */
	public static EstModel toEstModel(EstModelXml estModelXml) {
		EstModel estModel = new EstModel();
		estModel.setId(estModelXml.getId());
		estModel.setName(estModelXml.getName());
		estModel.setSse(estModelXml.getSse());
		estModel.setRms(estModelXml.getRms());
		estModel.setR2(estModelXml.getR2());
		estModel.setAIC(estModelXml.getAic());
		estModel.setBIC(estModelXml.getBic());
		estModel.setDof(estModelXml.getDof());
		estModel.setQualityScore(estModelXml.getQualityScore());
		estModel.setChecked(estModelXml.getChecked());
		estModel.setComment(estModelXml.getComment());
		estModel.setDbuuid(estModelXml.getDbuuid());

		return estModel;
	}
	
	public EstModelXml toEstModelXml() {
		EstModelXml estModelXml = new EstModelXml(id, name, sse, rms, r2, aic, bic, dof, checked, qualityScore, dbuuid);
		estModelXml.setComment(comment);
		
		return estModelXml;
	}
}
