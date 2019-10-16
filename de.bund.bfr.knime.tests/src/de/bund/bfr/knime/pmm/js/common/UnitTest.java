package de.bund.bfr.knime.pmm.js.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

@SuppressWarnings("static-method")
public class UnitTest {

	static Unit unit;
	static {
		unit = new Unit();
		unit.id = 1;
		unit.unit = "log10(Anzahl pro g)";
		unit.description = "log10 Anzahl (Zellen, Partikel, ...) pro Gramm (log10 Anzahl/g)";
		unit.name = "log10(number of objects per g)";
		unit.kind_of_property_quantity = "Number Content (count/mass)";
		unit.notation_case_sensitive = "log10(1/g)";
		unit.convert_to = "1/g";
		unit.conversion_function_factor = "10^x";
		unit.inverse_conversion_function_factor = "log10(x)";
		unit.object_type = "not_CFU";
		unit.display_in_GUI_as = "log10(count/g)";
		unit.mathML_string = "<unitDefinition id=\"log10_item_g\"><annotation><transformation name=\"log10\" xmlns=\"http://sourceforge.net/projects/microbialmodelingexchange/files/Units\"/></annotation><listOfUnits><unit kind=\"item\"/><unit exponent=\"-1\" kind=\"gram\"/></listOfUnits></unitDefinition>";
		unit.priority_for_display_in_GUI = "false";
	}

	@Test
	public void testConstructor() {
		final Unit unit = new Unit();
		assertThat(unit.id, is(nullValue()));
		assertThat(unit.unit, is(nullValue()));
		assertThat(unit.description, is(nullValue()));
		assertThat(unit.name, is(nullValue()));
		assertThat(unit.kind_of_property_quantity, is(nullValue()));
		assertThat(unit.notation_case_sensitive, is(nullValue()));
		assertThat(unit.convert_to, is(nullValue()));
		assertThat(unit.conversion_function_factor, is(nullValue()));
		assertThat(unit.inverse_conversion_function_factor, is(nullValue()));
		assertThat(unit.object_type, is(nullValue()));
		assertThat(unit.display_in_GUI_as, is(nullValue()));
		assertThat(unit.mathML_string, is(nullValue()));
		assertThat(unit.priority_for_display_in_GUI, is(nullValue()));
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		unit.saveToNodeSettings(settings);

		assertThat(settings.getInt("id"), equalTo(unit.id));
		assertThat(settings.getString("unit"), equalTo(unit.unit));
		assertThat(settings.getString("description"), equalTo(unit.description));
		assertThat(settings.getString("kindOfPropertyQuantity"), equalTo(unit.kind_of_property_quantity));
		assertThat(settings.getString("notationCaseSensitive"), equalTo(unit.notation_case_sensitive));
		assertThat(settings.getString("convertTo"), equalTo(unit.convert_to));
		assertThat(settings.getString("conversionFunctionFactor"), equalTo(unit.conversion_function_factor));
		assertThat(settings.getString("inversionConversionFunctionFactor"), equalTo(unit.inverse_conversion_function_factor));
		assertThat(settings.getString("objectType"), equalTo(unit.object_type));
		assertThat(settings.getString("displayInGuiAs"), equalTo(unit.display_in_GUI_as));
		assertThat(settings.getString("mathmlString"), equalTo(unit.mathML_string));
		assertThat(settings.getString("priorityForDisplayInGui"), equalTo(unit.priority_for_display_in_GUI));
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("id", unit.id);
		settings.addString("unit", unit.unit);
		settings.addString("description", unit.description);
		settings.addString("name", unit.name);
		settings.addString("kindOfPropertyQuantity", unit.kind_of_property_quantity);
		settings.addString("notationCaseSensitive", unit.notation_case_sensitive);
		settings.addString("convertTo", unit.convert_to);
		settings.addString("conversionFunctionFactor", unit.conversion_function_factor);
		settings.addString("inversionConversionFunctionFactor", unit.inverse_conversion_function_factor);
		settings.addString("objectType", unit.object_type);
		settings.addString("displayInGuiAs", unit.display_in_GUI_as);
		settings.addString("mathmlString", unit.mathML_string);
		settings.addString("priorityForDisplayInGui", unit.priority_for_display_in_GUI);

		final Unit actual = new Unit();
		actual.loadFromNodeSettings(settings);
		compare(actual, unit);
	}

	private static void compare(Unit obtained, Unit expected) {
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
