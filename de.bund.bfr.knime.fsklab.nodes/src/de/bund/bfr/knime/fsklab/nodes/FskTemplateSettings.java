package de.bund.bfr.knime.fsklab.nodes;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.fsklab.nodes.FskMetaData.DataType;
import de.bund.bfr.knime.fsklab.nodes.FskMetaData.Software;
import de.bund.bfr.knime.fsklab.nodes.FskMetaDataTuple.Key;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;

public class FskTemplateSettings {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");

	public FskMetaData template = new FskMetaData();

	/**
	 * Loads {@link FSMRTemplate} from a {@link NodeSettingsRO}.
	 * 
	 * @param settings
	 * @throws InvalidSettingsException
	 */
	public void loadFromNodeSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

		try {
			template.modelName = settings.getString(Key.name.name(), null);
			template.modelId = settings.getString(Key.id.name(), null);
			template.modelLink = settings.getString(Key.model_link.name(), null);

			template.organism = settings.getString(Key.species.name(), null);
			template.organismDetails = settings.getString(Key.species_details.name(), null);

			template.matrix = settings.getString(Key.matrix.name(), null);
			template.matrixDetails = settings.getString(Key.matrix_details.name(), null);

			template.creator = settings.getString(Key.creator.name(), null);
			template.familyName = settings.getString(Key.family_name.name(), null);
			template.contact = settings.getString(Key.contact.name(), null);

			String softwareAsString = settings.getString(Key.software.name(), null);
			template.software = softwareAsString == null ? null : Software.valueOf(softwareAsString);

			template.referenceDescription = settings.getString(Key.reference_description.name(), null);
			template.referenceDescriptionLink = settings.getString(Key.reference_description_link.name(), null);

			String createdDate = settings.getString(Key.created_date.name(), null);
			if (createdDate != null)
				template.createdDate = dateFormat.parse(createdDate);

			String modifiedDate = settings.getString(Key.modified_date.name(), null);
			if (modifiedDate != null)
				template.modifiedDate = dateFormat.parse(modifiedDate);

			template.rights = settings.getString(Key.rights.name(), null);
			template.notes = settings.getString(Key.notes.name(), null);
			template.curated = settings.getBoolean(Key.curation_status.name(), false);

			String modelType = settings.getString(Key.model_type.name(), null);
			if (modelType != null) {
				template.type = ModelType.valueOf(modelType);
			}

			String modelSubject = settings.getString(Key.subject.name(), null);
			if (modelSubject != null)
				template.subject = ModelClass.fromName(modelSubject);

			template.foodProcess = settings.getString(Key.food_process.name(), null);

			// Dependent variable
			template.dependentVariable.name = settings.getString(Key.depvar.name(), null);
			template.dependentVariable.unit = settings.getString(Key.depvar_unit.name(), null);
			String typeAsString = settings.getString(Key.depvar_type.name(), null);
			template.dependentVariable.type = typeAsString == null ? null : DataType.valueOf(typeAsString);
			template.dependentVariable.min = settings.getString(Key.depvar_min.name(), null);
			template.dependentVariable.max = settings.getString(Key.depvar_max.name(), null);

			// Independent variables
			{
				template.independentVariables.clear();
				
				String[] names = settings.getStringArray(Key.indepvars.name());
				String[] units = settings.getStringArray(Key.indepvars_units.name());
				String[] types = settings.getStringArray(Key.indepvars_types.name());
				String[] mins = settings.getStringArray(Key.indepvars_mins.name());
				String[] maxs = settings.getStringArray(Key.indepvars_maxs.name());
				String[] values = settings.getStringArray(Key.indepvars_values.name());

				if (names != null && units != null && types != null && mins != null && maxs != null
						&& names.length == units.length && names.length == types.length && names.length == mins.length
						&& names.length == maxs.length) {

					for (int i = 0; i < names.length; i++) {
						Variable v = new Variable();
						v.name = names[i];
						v.unit = units[i];
						v.type = DataType.valueOf(types[i]);
						v.min = mins[i];
						v.max = maxs[i];
						v.value = values[i];

						template.independentVariables.add(v);
					}
				}
			}

			template.hasData = settings.getBoolean(Key.has_data.name(), false);
		} catch (ParseException e) {
			// does not happen -> internal dates are checked before
			// being saved
			throw new RuntimeException(e);
		}
	}

	/**
	 * Saves {@link FSMRTemplate} into a {@link NodeSettingsWO}.
	 * <p>
	 * Missing string values are replace with <code>null</code>
	 * </p>
	 * <p>
	 * Missing single doubles are replaced with {@link Double#NaN}.
	 * </p>
	 * 
	 * @param settings
	 */
	public void saveToNodeSettings(final NodeSettingsWO settings) {

		settings.addString(Key.name.name(), template.modelName);
		settings.addString(Key.id.name(), template.modelId);
		settings.addString(Key.model_link.name(), template.modelLink == null ? null : template.modelLink.toString());

		settings.addString(Key.species.name(), template.organism);
		settings.addString(Key.species_details.name(), template.organismDetails);

		settings.addString(Key.matrix.name(), template.matrix);
		settings.addString(Key.matrix_details.name(), template.matrixDetails);

		settings.addString(Key.creator.name(), template.creator);
		settings.addString(Key.family_name.name(), template.familyName);
		settings.addString(Key.contact.name(), template.contact);
		settings.addString(Key.software.name(), template.software == null ? null : template.software.name());
		settings.addString(Key.reference_description.name(), template.referenceDescription);
		settings.addString(Key.reference_description_link.name(),
				template.referenceDescriptionLink == null ? null : template.referenceDescriptionLink.toString());

		settings.addString(Key.created_date.name(),
				template.createdDate == null ? null : FskMetaData.dateFormat.format(template.createdDate));
		settings.addString(Key.modified_date.name(),
				template.modifiedDate == null ? null : FskMetaData.dateFormat.format(template.modifiedDate));

		settings.addString(Key.rights.name(), template.rights);
		settings.addString(Key.notes.name(), template.notes);
		settings.addBoolean(Key.curation_status.name(), template.curated);
		settings.addString(Key.model_type.name(), template.type == null ? null : template.type.name());
		settings.addString(Key.subject.name(), template.subject == null ? "" : template.subject.fullName());
		settings.addString(Key.food_process.name(), template.foodProcess);

		// Dependent variable
		settings.addString(Key.depvar.name(), template.dependentVariable.name);
		settings.addString(Key.depvar_unit.name(), template.dependentVariable.unit);
		settings.addString(Key.depvar_type.name(),
				template.dependentVariable.type == null ? null : template.dependentVariable.type.name());
		settings.addString(Key.depvar_min.name(), template.dependentVariable.min);
		settings.addString(Key.depvar_max.name(), template.dependentVariable.max);

		// Independent variables
		{
			String[] names = template.independentVariables.stream().map(v -> v.name).toArray(String[]::new);
			String[] units = template.independentVariables.stream().map(v -> v.unit).toArray(String[]::new);
			String[] types = template.independentVariables.stream().map(v -> v.type.name()).toArray(String[]::new);
			String[] mins = template.independentVariables.stream().map(v -> v.min).toArray(String[]::new);
			String[] maxs = template.independentVariables.stream().map(v -> v.max).toArray(String[]::new);
			String[] values = template.independentVariables.stream().map(v -> v.value).toArray(String[]::new);

			settings.addStringArray(Key.indepvars.name(), names);
			settings.addStringArray(Key.indepvars_units.name(), units);
			settings.addStringArray(Key.indepvars_types.name(), types);
			settings.addStringArray(Key.indepvars_mins.name(), mins);
			settings.addStringArray(Key.indepvars_maxs.name(), maxs);
			settings.addStringArray(Key.indepvars_values.name(), values);
		}

		settings.addBoolean(Key.has_data.name(), template.hasData);
	}
}
