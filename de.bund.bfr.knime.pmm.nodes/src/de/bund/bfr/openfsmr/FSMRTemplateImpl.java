/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Contributors: Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.openfsmr;

import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Implementation of the Open Food Safety Model Repository template ( {@link FSMRTemplate}) based on
 * instance variables.
 *
 * @author Miguel Alba
 */
public class FSMRTemplateImpl implements FSMRTemplate {

    private static final long serialVersionUID = -3414978919227860002L;

    private String modelName;
    private String modelId;
    private URL modelLink;
    private String organismName;
    private String organismDetails;
    private String matrixName;
    private String matrixDetails;
    private String creator;
    private String familyName;
    private String contact;
    private String referenceDescription;
    private URL referenceDescriptionLink;
    private Date createdDate;
    private Date modifiedDate;
    private String rights;
    private String notes;
    private String curationStatus;
    private ModelType modelType;
    private ModelClass modelSubject;
    private String foodProcess;
    private String dependentVariable;
    private String dependentVariableUnit;
    private Double dependentVariableMin;
    private Double dependentVariableMax;
    private String[] independentVariables;
    private String[] independentVariableUnits;
    private double[] independentVariableMins;
    private double[] independentVariableMaxs;
    private Boolean hasData;

    // --- model name ---
    public String getModelName() {
        if (modelName == null)
            throw new RuntimeException("Model name is not set");
        return modelName;
    }

    public boolean isSetModelName() {
        return modelName != null;
    }

    public void setModelName(final String name) {
        if (name != null && !name.isEmpty())
            this.modelName = name;
    }

    public void unsetModelName() {
        modelName = null;
    }

    // -- model id ---
    public String getModelId() {
        if (modelId == null)
            throw new RuntimeException("Model id is not set");
        return modelId;
    }

    public boolean isSetModelId() {
        return modelId != null;
    }

    public void setModelId(final String id) {
        if (id != null && !id.isEmpty())
            modelId = id;
    }

    public void unsetModelId() {
        modelId = null;
    }

    // --- model link ---
    public URL getModelLink() {
        if (modelLink == null)
            throw new RuntimeException("Model link is not set");
        return modelLink;
    }

    public boolean isSetModelLink() {
        return modelLink != null;
    }

    public void setModelLink(final URL link) {
        if (link != null)
            modelLink = link;
    }

    public void unsetModelLink() {
        modelLink = null;
    }

    // --- organism name ---
    public String getOrganismName() {
        if (organismName == null)
            throw new RuntimeException("Organism name is not set");
        return organismName;
    }

    public boolean isSetOrganismName() {
        return organismName != null;
    }

    public void setOrganismName(final String name) {
        if (name != null && !name.isEmpty())
            organismName = name;
    }

    public void unsetOrganismName() {
        organismName = null;
    }

    // --- organism details ---
    public String getOrganismDetails() {
        if (organismDetails == null)
            throw new RuntimeException("Organism details are not set");
        return organismDetails;
    }

    public boolean isSetOrganismDetails() {
        return organismDetails != null;
    }

    public void setOrganismDetails(final String details) {
        if (details != null && !details.isEmpty())
            organismDetails = details;
    }

    public void unsetOrganismDetail() {
        organismDetails = null;
    }

    // --- matrix name ---
    public String getMatrixName() {
        if (matrixName == null)
            throw new RuntimeException("Matrix name is not set");
        return matrixName;
    }

    public boolean isSetMatrixName() {
        return matrixName != null;
    }

    public void setMatrixName(final String name) {
        if (name != null && !name.isEmpty())
            matrixName = name;
    }

    public void unsetMatrixName() {
        matrixName = null;
    }


    // --- matrix details ---
    public String getMatrixDetails() {
        if (matrixDetails == null)
            throw new RuntimeException("Matrix details are not set");
        return matrixDetails;
    }

    public boolean isSetMatrixDetails() {
        return matrixDetails != null;
    }

    public void setMatrixDetails(final String details) {
        if (details != null && !details.isEmpty())
            matrixDetails = details;
    }

    public void unsetMatrixDetails() {
        matrixDetails = null;
    }

    // --- creator ---
    public String getCreator() {
        if (creator == null)
            throw new RuntimeException("Creator is not set");
        return creator;
    }

    public boolean isSetCreator() {
        return creator != null;
    }

    public void setCreator(final String creator) {
        if (creator != null && !creator.isEmpty())
            this.creator = creator;
    }

    public void unsetCreator() {
        creator = null;
    }

    // --- family name ---
    public String getFamilyName() {
        if (familyName == null)
            throw new RuntimeException("Family name is not set");
        return familyName;
    }

    public boolean isSetFamilyName() {
        return familyName != null;
    }

    public void setFamilyName(final String name) {
        if (name != null && !name.isEmpty())
            familyName = name;
    }

    public void unsetFamilyName() {
        familyName = null;
    }

    // --- contact ---
    public String getContact() {
        if (contact == null)
            throw new RuntimeException("Contact is not set");
        return contact;
    }

    public boolean isSetContact() {
        return contact != null;
    }

    public void setContact(final String contact) {
        if (contact != null && !contact.isEmpty())
            this.contact = contact;
    }

    public void unsetContact() {
        contact = null;
    }

    // --- reference description ---

    public String getReferenceDescription() {
        if (referenceDescription == null)
            throw new RuntimeException("Reference description is not set");
        return referenceDescription;
    }

    public boolean isSetReferenceDescription() {
        return referenceDescription != null;
    }

    public void setReferenceDescription(final String description) {
        if (description != null && !description.isEmpty())
            referenceDescription = description;
    }

    public void unsetReferenceDescription() {
        referenceDescription = null;
    }

    // --- reference description link ---
    public URL getReferenceDescriptionLink() {
        if (referenceDescriptionLink == null)
            throw new RuntimeException("Reference description link is not set");
        return referenceDescriptionLink;
    }

    public boolean isSetReferenceDescriptionLink() {
        return referenceDescriptionLink != null;
    }

    public void setReferenceDescriptionLink(final URL link) {
        if (link != null)
            referenceDescriptionLink = link;
    }

    public void unsetReferenceDescriptionLink() {
        referenceDescriptionLink = null;
    }

    // --- created date ---
    public Date getCreatedDate() {
        if (createdDate == null)
            throw new RuntimeException("Created date is not set");
        return createdDate;
    }

    public boolean isSetCreatedDate() {
        return createdDate != null;
    }

    public void setCreatedDate(final Date date) {
        if (date != null)
            createdDate = date;
    }

    public void unsetCreatedDate() {
        createdDate = null;
    }

    // --- modified date ---
    public Date getModifiedDate() {
        if (modifiedDate == null)
            throw new RuntimeException("Modified date is not set");
        return modifiedDate;
    }

    public boolean isSetModifiedDate() {
        return modifiedDate != null;
    }

    public void setModifiedDate(final Date date) {
        if (date != null)
            modifiedDate = date;
    }

    public void unsetModifiedDate() {
        modifiedDate = null;
    }

    // --- rights ---
    public String getRights() {
        if (rights == null)
            throw new RuntimeException("Rights are not set");
        return rights;
    }

    public boolean isSetRights() {
        return rights != null;
    }

    public void setRights(final String rights) {
        if (rights != null && !rights.isEmpty())
            this.rights = rights;
    }

    public void unsetRights() {
        rights = null;
    }

    // --- notes ---

    public String getNotes() {
        if (notes == null)
            throw new RuntimeException("Notes are not set");
        return notes;
    }

    public boolean isSetNotes() {
        return notes != null;
    }

    public void setNotes(final String notes) {
        if (notes != null && !notes.isEmpty())
            this.notes = notes;
    }

    public void unsetNotes() {
        notes = null;
    }

    // --- curation status ---
    public String getCurationStatus() {
        if (curationStatus == null)
            throw new RuntimeException("Curation status is not set");
        return curationStatus;
    }

    public boolean isSetCurationStatus() {
        return curationStatus != null;
    }

    public void setCurationStatus(final String status) {
        if (status != null && !status.isEmpty())
            curationStatus = status;
    }

    public void unsetCurationStatus() {
        curationStatus = null;
    }

    // --- model type ---

    public ModelType getModelType() {
        if (modelType == null)
            throw new RuntimeException("Model type is not set");
        return modelType;
    }

    public boolean isSetModelType() {
        return modelType != null;
    }

    public void setModelType(final ModelType type) {
        if (type != null)
            modelType = type;
    }

    public void unsetModelType() {
        modelType = null;
    }

    // --- model subject ---
    public ModelClass getModelSubject() {
        if (modelSubject == null)
            throw new RuntimeException("Model subject is not set");
        return modelSubject;
    }

    public boolean isSetModelSubject() {
        return modelSubject != null;
    }

    public void setModelSubject(final ModelClass subject) {
        if (subject != null)
            modelSubject = subject;
    }

    public void unsetModelSubject() {
        modelSubject = null;
    }

    // --- food process ---
    public String getFoodProcess() {
        if (foodProcess == null)
            throw new RuntimeException("Food process is not set");
        return foodProcess;
    }

    public boolean isSetFoodProcess() {
        return foodProcess != null;
    }

    public void setFoodProcess(final String process) {
        if (process != null && !process.isEmpty())
            foodProcess = process;
    }

    public void unsetFoodProcess() {
        foodProcess = null;
    }

    // --- dependent variable ---

    public String getDependentVariable() {
        if (dependentVariable == null)
            throw new RuntimeException("Dependent variable is not set");
        return dependentVariable;
    }

    public boolean isSetDependentVariable() {
        return dependentVariable != null;
    }

    public void setDependentVariable(final String var) {
        if (var != null && !var.isEmpty())
            dependentVariable = var;
    }

    public void unsetDependentVariable() {
        dependentVariable = null;
    }

    // --- dependent variable unit ---
    public String getDependentVariableUnit() {
        if (dependentVariableUnit == null)
            throw new RuntimeException("Unit of dependent variable is not set");
        return dependentVariableUnit;
    }

    public boolean isSetDependentVariableUnit() {
        return dependentVariableUnit != null;
    }

    public void setDependentVariableUnit(final String unit) {
        if (unit != null && !unit.isEmpty())
            dependentVariableUnit = unit;
    }

    public void unsetDependentVariableUnit() {
        dependentVariableUnit = null;
    }

    // --- dependent variable min ---

    public double getDependentVariableMin() {
        if (dependentVariableMin == null)
            throw new RuntimeException("Minimum value of dependent variable is not set");
        return dependentVariableMin;
    }

    public boolean isSetDependentVariableMin() {
        return dependentVariableMin != null;
    }

    public void setDependentVariableMin(double min) {
        dependentVariableMin = min;
    }

    public void unsetDependentVariableMin() {
        dependentVariableMin = null;
    }

    // --- dependent variable max ---

    public double getDependentVariableMax() {
        if (dependentVariableMax == null)
            throw new RuntimeException("Maximum value of dependent variable is not set");
        return dependentVariableMax;
    }

    public boolean isSetDependentVariableMax() {
        return dependentVariableMax != null;
    }

    public void setDependentVariableMax(double max) {
        dependentVariableMax = max;
    }

    public void unsetDependentVariableMax() {
        dependentVariableMax = null;
    }

    // --- independent variables ---

    public String[] getIndependentVariables() {
        if (independentVariables == null)
            throw new RuntimeException("Independent variables are not set");
        return independentVariables;
    }

    public boolean isSetIndependentVariables() {
        return independentVariables != null;
    }

    public void setIndependentVariables(final String[] vars) {
        if (vars != null && vars.length > 0)
            independentVariables = vars;
    }

    public void unsetIndependentVariables() {
        independentVariables = null;
    }


    // --- independent variables units ---

    public String[] getIndependentVariablesUnits() {
        if (independentVariableUnits == null)
            throw new RuntimeException("Units of independent variables are not set");
        return independentVariableUnits;
    }

    public boolean isSetIndependentVariablesUnits() {
        return independentVariableUnits != null;
    }

    public void setIndependentVariablesUnits(final String[] units) {
        if (units != null && units.length > 0)
            independentVariableUnits = units;
    }

    public void unsetIndependentVariableUnits() {
        independentVariableUnits = null;
    }

    // --- independent variables mins ---

    public double[] getIndependentVariablesMins() {
        if (independentVariableMins == null)
            throw new RuntimeException("Minimum values of independent variables are not set");
        return independentVariableMins;
    }

    public boolean isSetIndependentVariablesMins() {
        return independentVariableMins != null;
    }

    public void setIndependentVariablesMins(final double[] mins) {
        if (mins != null && mins.length > 0)
            independentVariableMins = mins;
    }

    public void unsetIndependentVariableMins() {
        independentVariableMins = null;
    }

    // --- independent variables maxs ---
    public double[] getIndependentVariablesMaxs() {
        if (independentVariableMaxs == null)
            throw new RuntimeException("Maximum values of independent variables are not set");
        return independentVariableMaxs;
    }

    public boolean isSetIndependentVariablesMaxs() {
        return independentVariableMaxs != null;
    }

    public void setIndependentVariablesMaxs(final double[] maxs) {
        if (maxs != null && maxs.length > 0)
            independentVariableMaxs = maxs;
    }

    public void unsetIndependentVariableMaxs() {
        independentVariableMaxs = null;
    }

    // --- has data ---
    public boolean getHasData() {
        if (hasData == null)
            throw new RuntimeException("Has data is not set");
        return hasData;
    }

    public boolean isSetHasData() {
        return hasData != null;
    }

    public void setHasData(boolean hasData) {
        this.hasData = hasData;
    }

    public void unsetHasData() {
        hasData = null;
    }

    public FSMRTemplateImpl() {
    }

    public FSMRTemplateImpl(final FSMRTemplate template) {
        try {
            if (template.isSetModelName()) {
                setModelName(template.getModelName());
            }
            if (template.isSetModelId()) {
                setModelId(template.getModelId());
            }
            if (template.isSetModelLink()) {
                setModelLink(new URL(template.getModelLink().toString()));
            }
            if (template.isSetOrganismName()) {
                setOrganismName(template.getOrganismName());
            }
            if (template.isSetOrganismDetails()) {
                setOrganismDetails(template.getOrganismDetails());
            }
            if (template.isSetMatrixName()) {
                setMatrixName(template.getMatrixName());
            }
            if (template.isSetMatrixDetails()) {
                setMatrixDetails(template.getMatrixDetails());
            }
            if (template.isSetCreator()) {
                setCreator(template.getCreator());
            }
            if (template.isSetFamilyName()) {
                setFamilyName(template.getFamilyName());
            }
            if (template.isSetContact()) {
                setContact(template.getContact());
            }
            if (template.isSetReferenceDescription()) {
                setReferenceDescription(template.getReferenceDescription());
            }
            if (template.isSetReferenceDescriptionLink()) {
                setReferenceDescriptionLink(new URL(template.getReferenceDescriptionLink().toString()));
            }
            if (template.isSetCreatedDate()) {
                setCreatedDate(new Date(template.getCreatedDate().getTime()));
            }
            if (template.isSetModifiedDate()) {
                setModifiedDate(new Date(template.getModifiedDate().getTime()));
            }
            if (template.isSetRights()) {
                setRights(template.getRights());
            }
            if (template.isSetNotes()) {
                setNotes(template.getNotes());
            }
            if (template.isSetCurationStatus()) {
                setCurationStatus(template.getCurationStatus());
            }
            if (template.isSetModelType()) {
                setModelType(template.getModelType());
            }
            if (template.isSetModelSubject()) {
                setModelSubject(template.getModelSubject());
            } else {
                setModelSubject(ModelClass.UNKNOWN);
            }
            if (template.isSetFoodProcess()) {
                setFoodProcess(template.getFoodProcess());
            }
            if (template.isSetDependentVariable()) {
                setDependentVariable(template.getDependentVariable());
            }
            if (template.isSetDependentVariableUnit()) {
                setDependentVariableUnit(template.getDependentVariableUnit());
            }
            if (template.isSetDependentVariableMin()) {
                setDependentVariableMin(template.getDependentVariableMin());
            }
            if (template.isSetDependentVariableMax()) {
                setDependentVariableMax(template.getDependentVariableMax());
            }
            if (template.isSetIndependentVariables()) {
                setIndependentVariables(template.getIndependentVariables().clone());
            }
            if (template.isSetIndependentVariablesUnits()) {
                setIndependentVariablesUnits(template.getIndependentVariablesUnits().clone());
            }
            if (template.isSetIndependentVariablesMins()) {
                setIndependentVariablesMins(template.getIndependentVariablesMins());
            }
            if (template.isSetIndependentVariablesMaxs()) {
                setIndependentVariablesMaxs(template.getIndependentVariablesMaxs());
            }
            if (template.isSetHasData()) {
                setHasData(template.getHasData());
            }
        } catch (MalformedURLException e) {
            // passed template has valid settings so these exceptions are never
            // thrown
            throw new RuntimeException(e);
        }
    }
}
