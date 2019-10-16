package de.bund.bfr.knime.pmm.js.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

@SuppressWarnings("static-method")
public class UnitListTest {

	static Unit unit = UnitTest.unit;

	@Test
	public void testUnits() {
		final UnitList list = new UnitList();
		assertNull(list.getUnits());

		list.setUnits(new Unit[] { unit });

		final Unit expected = unit; // expected Unit
		final Unit obtained = list.getUnits()[0]; // obtained Unit

		compare(obtained, expected);
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		final UnitList list = new UnitList();
		list.setUnits(new Unit[] { unit });

		final NodeSettings settings = new NodeSettings("irrelevantKey");
		list.saveToNodeSettings(settings);

		assertEquals(1, settings.getInt("numUnits"));

		final Unit expected = unit; // expected unit
		final Unit obtained = new Unit(); // obtained unit
		obtained.loadFromNodeSettings(settings.getNodeSettings("units0"));

		compare(obtained, expected);
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("numUnits", 1);
		unit.saveToNodeSettings(settings.addNodeSettings("units0"));

		final UnitList list = new UnitList();
		list.loadFromNodeSettings(settings);

		final Unit expected = unit; // expected Unit
		final Unit obtained = list.getUnits()[0]; // obtained Unit
		compare(obtained, expected);
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
