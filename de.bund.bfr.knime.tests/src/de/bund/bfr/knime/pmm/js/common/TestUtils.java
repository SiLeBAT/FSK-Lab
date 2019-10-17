package de.bund.bfr.knime.pmm.js.common;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;

class TestUtils {

	private TestUtils() {
	}

	static void compare(final Agent obtained, final Agent expected) {
		assertThat(obtained.id, equalTo(expected.id));
		assertThat(obtained.name, equalTo(expected.name));
		assertThat(obtained.detail, equalTo(expected.detail));
		assertThat(obtained.dbuuid, equalTo(expected.dbuuid));
	}

	static void compare(final CatalogModel obtained, final CatalogModel expected) {
		assertThat(obtained.id, equalTo(expected.id));
		assertThat(obtained.name, equalTo(expected.name));
		assertThat(obtained.formula, equalTo(expected.formula));
		assertThat(obtained.modelClass, equalTo(expected.modelClass));
		assertThat(obtained.comment, equalTo(expected.comment));
		assertThat(obtained.dbuuid, equalTo(expected.dbuuid));
	}

	static void compare(final Dep obtained, final Dep expected) {
		assertThat(obtained.name, equalTo(expected.name));
		assertThat(obtained.origname, equalTo(expected.origname));
		assertThat(obtained.min, equalTo(expected.min));
		assertThat(obtained.max, equalTo(expected.max));
		assertThat(obtained.category, equalTo(expected.category));
		assertThat(obtained.unit, equalTo(expected.unit));
		assertThat(obtained.description, equalTo(expected.description));
	}

	static void compare(final EstModel obtained, final EstModel expected) {
		assertThat(obtained.id, equalTo(expected.id));
		assertThat(obtained.name, equalTo(expected.name));
		assertThat(obtained.sse, equalTo(expected.sse));
		assertThat(obtained.rms, equalTo(expected.rms));
		assertThat(obtained.r2, equalTo(expected.r2));
		assertThat(obtained.aic, equalTo(expected.aic));
		assertThat(obtained.bic, equalTo(expected.bic));
		assertThat(obtained.dof, equalTo(expected.dof));
		assertThat(obtained.qualityScore, equalTo(expected.qualityScore));
		assertThat(obtained.checked, equalTo(expected.checked));
		assertThat(obtained.comment, equalTo(expected.comment));
		assertThat(obtained.dbuuid, equalTo(expected.dbuuid));
	}

	static void compare(final Indep obtained, final Indep expected) {
		assertThat(obtained.name, equalTo(expected.name));
		assertThat(obtained.origname, equalTo(expected.origname));
		assertThat(obtained.min, equalTo(expected.min));
		assertThat(obtained.max, equalTo(expected.max));
		assertThat(obtained.category, equalTo(expected.category));
		assertThat(obtained.unit, equalTo(expected.unit));
		assertThat(obtained.description, equalTo(expected.description));
	}

	static void compare(final Literature obtained, final Literature expected) {
		assertThat(obtained.id, equalTo(expected.id));
		assertThat(obtained.author, equalTo(expected.author));
		assertThat(obtained.title, equalTo(expected.title));
		assertThat(obtained.abstractText, equalTo(expected.abstractText));
		assertThat(obtained.year, equalTo(expected.year));
		assertThat(obtained.journal, equalTo(expected.journal));
		assertThat(obtained.volume, equalTo(expected.volume));
		assertThat(obtained.issue, equalTo(expected.issue));
		assertThat(obtained.page, equalTo(expected.page));
		assertThat(obtained.approvalMode, equalTo(expected.approvalMode));
		assertThat(obtained.website, equalTo(expected.website));
		assertThat(obtained.type, equalTo(expected.type));
		assertThat(obtained.comment, equalTo(expected.comment));
		assertThat(obtained.dbuuid, equalTo(expected.dbuuid));
	}

	static void compare(Matrix obtained, Matrix expected) {
		assertThat(obtained.id, equalTo(expected.id));
		assertThat(obtained.name, equalTo(expected.name));
		assertThat(obtained.detail, equalTo(expected.detail));
		assertThat(obtained.dbuuid, equalTo(expected.dbuuid));
	}

	static void compare(MdInfo obtained, MdInfo expected) {
		assertThat(obtained.id, equalTo(expected.id));
		assertThat(obtained.name, equalTo(expected.name));
		assertThat(obtained.comment, equalTo(expected.comment));
		assertThat(obtained.qualityScore, equalTo(expected.qualityScore));
		assertThat(obtained.checked, equalTo(expected.checked));
	}

	static void compare(Misc obtained, Misc expected) {
		assertThat(obtained.getId(), equalTo(expected.getId()));
		assertThat(obtained.getName(), equalTo(expected.getName()));
		assertThat(obtained.getDescription(), equalTo(expected.getDescription()));
		assertThat(obtained.getValue(), equalTo(expected.getValue()));
		assertThat(obtained.getCategories(), arrayContaining(expected.getCategories()));
	}

	static void compare(Param obtained, Param expected) {
		assertThat(obtained.name, equalTo(expected.name));
		assertThat(obtained.origName, equalTo(expected.origName));
		assertThat(obtained.isStart, equalTo(expected.isStart));
		assertThat(obtained.value, equalTo(expected.value));
		assertThat(obtained.error, equalTo(expected.error));
		assertThat(obtained.min, equalTo(expected.min));
		assertThat(obtained.max, equalTo(expected.max));
		assertThat(obtained.p, equalTo(expected.p));
		assertThat(obtained.t, equalTo(expected.t));
		assertThat(obtained.minGuess, equalTo(expected.minGuess));
		assertThat(obtained.maxGuess, equalTo(expected.maxGuess));
		assertThat(obtained.category, equalTo(expected.category));
		assertThat(obtained.unit, equalTo(expected.unit));
		assertThat(obtained.description, equalTo(expected.description));
		assertThat(obtained.correlationNames, equalTo(expected.correlationNames));
		assertThat(obtained.correlationValues, equalTo(expected.correlationValues));
	}

	static void compare(TimeSeries obtained, TimeSeries expected) {
		assertThat(obtained.name, equalTo(expected.name));
		assertThat(obtained.timeUnit, equalTo(expected.timeUnit));
		assertThat(obtained.origTimeUnit, equalTo(expected.origTimeUnit));
		assertThat(obtained.concentration, equalTo(expected.concentration));
		assertThat(obtained.concentrationUnit, equalTo(expected.concentrationUnit));
		assertThat(obtained.concentrationUnitObjectType, equalTo(expected.concentrationUnitObjectType));
		assertThat(obtained.origConcentrationUnit, equalTo(expected.origConcentrationUnit));
		assertThat(obtained.concentrationStdDev, equalTo(expected.concentrationStdDev));
		assertThat(obtained.numberOfMeasurements, equalTo(expected.numberOfMeasurements));
	}

	static void compare(Unit obtained, Unit expected) {
		assertThat(obtained.id, equalTo(expected.id));
		assertThat(obtained.unit, equalTo(expected.unit));
		assertThat(obtained.description, equalTo(expected.description));
		assertThat(obtained.name, equalTo(expected.name));
		assertThat(obtained.kind_of_property_quantity, equalTo(expected.kind_of_property_quantity));
		assertThat(obtained.notation_case_sensitive, equalTo(expected.notation_case_sensitive));
		assertThat(obtained.convert_to, equalTo(expected.convert_to));
		assertThat(obtained.conversion_function_factor, equalTo(expected.conversion_function_factor));
		assertThat(obtained.inverse_conversion_function_factor, equalTo(expected.inverse_conversion_function_factor));
		assertThat(obtained.object_type, equalTo(expected.object_type));
		assertThat(obtained.display_in_GUI_as, equalTo(expected.display_in_GUI_as));
		assertThat(obtained.mathML_string, equalTo(expected.mathML_string));
		assertThat(obtained.priority_for_display_in_GUI, equalTo(expected.priority_for_display_in_GUI));
	}
}
