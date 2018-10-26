package de.bund.bfr.knime.pmm.openfsmr;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.openfsmr.FSMRTemplate;
import de.bund.bfr.openfsmr.FSMRTemplateImpl;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;

public class FSMRTemplateSettings {

  // configuration keys
  private static final String MODEL_NAME = "Model name";
  private static final String MODEL_ID = "Model id";
  private static final String MODEL_LINK = "Model link";
  private static final String ORGANISM_NAME = "Organism name";
  private static final String ORGANISM_DETAILS = "Organism details";
  private static final String MATRIX_NAME = "Matrix name";
  private static final String MATRIX_DETAILS = "Matrix details";
  private static final String CREATOR = "Creator";
  private static final String FAMILY_NAME = "Family name";
  private static final String CONTACT = "Contact";
  private static final String REFERENCE_DESCRIPTION = "Reference description";
  private static final String REFERENCE_DESCRIPTION_LINK = "Reference description link";
  private static final String CREATED_DATE = "Created date";
  private static final String MODIFIED_DATE = "Modified date";
  private static final String RIGHTS = "Rights";
  private static final String NOTES = "Notes";
  private static final String CURATION_STATUS = "Curation status";
  private static final String MODEL_TYPE = "Model type";
  private static final String MODEL_SUBJECT = "Model subject";
  private static final String FOOD_PROCESS = "Food process";
  private static final String DEPENDENT_VARIABLE = "Dependent variable";
  private static final String DEPENDENT_VARIABLE_UNIT = "Dependent variable unit";
  private static final String DEPENDENT_VARIABLE_MIN = "Dependent variable minimum value";
  private static final String DEPENDENT_VARIABLE_MAX = "Dependent variable maximum value";
  private static final String INDEPENDENT_VARIABLES = "Independent variables";
  private static final String INDEPENDENT_VARIABLES_UNITS = "Independent variables units";
  private static final String INDEPENDENT_VARIABLES_MINS = "Independent variables minimum values";
  private static final String INDEPENDENT_VARIABLES_MAXS = "Independent variables maximum values";
  private static final String HAS_DATA = "Has data?";

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");

  private FSMRTemplate template = new FSMRTemplateImpl();

  public FSMRTemplate getTemplate() {
    return template;
  }

  public void setTemplate(FSMRTemplate template) {
    this.template = template;
  }

  /**
   * Loads {@link FSMRTemplate} from a {@link NodeSettingsRO}.
   * 
   * @param settings
   */
  public void loadFromNodeSettings(final NodeSettingsRO settings) {
    try {
      String modelName = settings.getString(MODEL_NAME, null);
      if (modelName != null) {
        template.setModelName(modelName);
      }

      String modelId = settings.getString(MODEL_ID, null);
      if (modelId != null) {
        template.setModelId(modelId);
      }

      String modelLink = settings.getString(MODEL_LINK, null);
      if (modelLink != null) {
        template.setModelLink(new URL(modelLink));
      }

      String organismName = settings.getString(ORGANISM_NAME, null);
      if (organismName != null) {
        template.setOrganismName(organismName);
      }

      String organismDetails = settings.getString(ORGANISM_DETAILS, null);
      if (organismDetails != null) {
        template.setOrganismDetails(organismDetails);
      }

      String matrixName = settings.getString(MATRIX_NAME, null);
      if (matrixName != null) {
        template.setMatrixName(matrixName);
      }

      String matrixDetails = settings.getString(MATRIX_DETAILS, null);
      if (matrixDetails != null) {
        template.setMatrixDetails(matrixDetails);
      }

      String creator = settings.getString(CREATOR, null);
      if (creator != null) {
        template.setCreator(creator);
      }

      String familyName = settings.getString(FAMILY_NAME, null);
      if (familyName != null) {
        template.setFamilyName(familyName);
      }

      String contact = settings.getString(CONTACT, null);
      if (contact != null) {
        template.setContact(contact);
      }

      String referenceDescription = settings.getString(REFERENCE_DESCRIPTION, null);
      if (referenceDescription != null) {
        template.setReferenceDescription(referenceDescription);
      }

      String referenceDescriptionLink = settings.getString(REFERENCE_DESCRIPTION_LINK, null);
      if (referenceDescriptionLink != null) {
        template.setReferenceDescriptionLink(new URL(referenceDescriptionLink));
      }

      String createdDate = settings.getString(CREATED_DATE, null);
      if (createdDate != null) {
        template.setCreatedDate(dateFormat.parse(createdDate));
      }

      String modifiedDate = settings.getString(MODIFIED_DATE, null);
      if (modifiedDate != null) {
        template.setModifiedDate(dateFormat.parse(modifiedDate));
      }

      String rights = settings.getString(RIGHTS, null);
      if (rights != null) {
        template.setRights(rights);
      }

      String notes = settings.getString(NOTES, null);
      if (notes != null) {
        template.setNotes(notes);
      }

      String curationStatus = settings.getString(CURATION_STATUS, null);
      if (curationStatus != null) {
        template.setCurationStatus(curationStatus);
      }

      String modelType = settings.getString(MODEL_TYPE, null);
      if (modelType != null) {
        template.setModelType(ModelType.valueOf(modelType));
      }

      String modelSubject = settings.getString(MODEL_SUBJECT, null);
      if (modelSubject != null) {
        template.setModelSubject(ModelClass.fromName(modelSubject));
      }

      String foodProcess = settings.getString(FOOD_PROCESS, null);
      if (foodProcess != null) {
        template.setFoodProcess(foodProcess);
      }

      String dependentVariable = settings.getString(DEPENDENT_VARIABLE, null);
      if (dependentVariable != null) {
        template.setDependentVariable(dependentVariable);
      }

      String dependentVariableUnit = settings.getString(DEPENDENT_VARIABLE_UNIT, null);
      if (dependentVariableUnit != null) {
        template.setDependentVariableUnit(dependentVariableUnit);
      }

      double dependentVariableMin = settings.getDouble(DEPENDENT_VARIABLE_MIN, Double.NaN);
      if (!Double.isNaN(dependentVariableMin)) {
        template.setDependentVariableMin(dependentVariableMin);
      }

      double dependentVariableMax = settings.getDouble(DEPENDENT_VARIABLE_MAX, Double.NaN);
      if (!Double.isNaN(dependentVariableMax)) {
        template.setDependentVariableMax(dependentVariableMax);
      }

      String[] independentVariables =
          settings.getStringArray(INDEPENDENT_VARIABLES, (String[]) null);
      if (independentVariables != null) {
        template.setIndependentVariables(independentVariables);
      }

      String[] independentVariablesUnits =
          settings.getStringArray(INDEPENDENT_VARIABLES_UNITS, (String[]) null);
      if (independentVariablesUnits != null) {
        template.setIndependentVariablesUnits(independentVariablesUnits);
      }

      double[] independentVariablesMins =
          settings.getDoubleArray(INDEPENDENT_VARIABLES_MINS, (double[]) null);
      if (independentVariablesMins != null) {
        template.setIndependentVariablesMins(independentVariablesMins);
      }

      double[] independentVariablesMaxs =
          settings.getDoubleArray(INDEPENDENT_VARIABLES_MAXS, (double[]) null);
      if (independentVariablesMaxs != null) {
        template.setIndependentVariablesMaxs(independentVariablesMaxs);
      }

      Boolean hasData = settings.getBoolean(HAS_DATA, Boolean.FALSE);
      template.setHasData(hasData);
    } catch (MalformedURLException e) {
      // does not happen -> internal links are checked before
      // being saved
      throw new RuntimeException(e);
    } catch (ParseException e) {
      // does not happen -> internal dates are checked before
      // being saved
      throw new RuntimeException(e);
    }
  }

  /**
   * Saves {@link FSMRTemplate} into a {@link NodeSettingsWO}.
   * <p>
   * Missing single doubles are replaced with {@link Double#NaN}.
   * 
   * @param settings
   */
  public void saveToNodeSettings(final NodeSettingsWO settings) {
    settings.addString(MODEL_NAME, template.isSetModelName() ? template.getModelName() : null);
    settings.addString(MODEL_ID, template.isSetModelId() ? template.getModelId() : null);
    settings.addString(MODEL_LINK,
        template.isSetModelLink() ? template.getModelLink().toString() : null);
    settings.addString(ORGANISM_NAME,
        template.isSetOrganismName() ? template.getOrganismName() : null);
    settings.addString(ORGANISM_DETAILS,
        template.isSetOrganismDetails() ? template.getOrganismDetails() : null);
    settings.addString(MATRIX_NAME, template.isSetMatrixName() ? template.getMatrixName() : null);
    settings.addString(MATRIX_DETAILS,
        template.isSetMatrixDetails() ? template.getMatrixDetails() : null);
    settings.addString(CREATOR, template.isSetCreator() ? template.getCreator() : null);
    settings.addString(FAMILY_NAME, template.isSetFamilyName() ? template.getFamilyName() : null);
    settings.addString(CONTACT, template.isSetContact() ? template.getContact() : null);
    settings.addString(REFERENCE_DESCRIPTION,
        template.isSetReferenceDescription() ? template.getReferenceDescription() : null);
    settings.addString(REFERENCE_DESCRIPTION_LINK, template.isSetReferenceDescriptionLink()
        ? template.getReferenceDescriptionLink().toString() : null);
    settings.addString(CREATED_DATE,
        template.isSetCreatedDate() ? dateFormat.format(template.getCreatedDate()) : null);
    settings.addString(MODIFIED_DATE,
        template.isSetModifiedDate() ? dateFormat.format(template.getModifiedDate()) : null);
    settings.addString(RIGHTS, template.isSetRights() ? template.getRights() : null);
    settings.addString(NOTES, template.isSetNotes() ? template.getNotes() : null);
    settings.addString(CURATION_STATUS,
        template.isSetCurationStatus() ? template.getCurationStatus() : null);
    settings.addString(MODEL_TYPE,
        template.isSetModelType() ? template.getModelType().name() : null);
    settings.addString(MODEL_SUBJECT,
        template.isSetModelSubject() ? template.getModelSubject().fullName() : null);
    settings.addString(FOOD_PROCESS,
        template.isSetFoodProcess() ? template.getFoodProcess() : null);
    settings.addString(DEPENDENT_VARIABLE,
        template.isSetDependentVariable() ? template.getDependentVariable() : null);
    settings.addString(DEPENDENT_VARIABLE_UNIT,
        template.isSetDependentVariableUnit() ? template.getDependentVariableUnit() : null);
    settings.addDouble(DEPENDENT_VARIABLE_MIN,
        template.isSetDependentVariableMin() ? template.getDependentVariableMin() : Double.NaN);
    settings.addDouble(DEPENDENT_VARIABLE_MAX,
        template.isSetDependentVariableMax() ? template.getDependentVariableMax() : Double.NaN);
    settings.addStringArray(INDEPENDENT_VARIABLES,
        template.isSetIndependentVariables() ? template.getIndependentVariables() : null);
    settings.addStringArray(INDEPENDENT_VARIABLES_UNITS,
        template.isSetIndependentVariablesUnits() ? template.getIndependentVariablesUnits() : null);
    settings.addDoubleArray(INDEPENDENT_VARIABLES_MINS,
        template.isSetIndependentVariablesMins() ? template.getIndependentVariablesMins() : null);
    settings.addDoubleArray(INDEPENDENT_VARIABLES_MAXS,
        template.isSetIndependentVariablesMaxs() ? template.getIndependentVariablesMaxs() : null);
    settings.addBoolean(HAS_DATA, template.isSetHasData() ? template.getHasData() : false);
  }
}
