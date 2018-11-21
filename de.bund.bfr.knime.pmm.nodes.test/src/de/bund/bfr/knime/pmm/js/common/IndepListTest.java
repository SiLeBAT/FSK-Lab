package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

public class IndepListTest {

	static Indep indep;

	static {
		indep = new Indep();
		indep.name = IndepTest.name;
		indep.origname = IndepTest.origname;
		indep.min = IndepTest.min;
		indep.max = IndepTest.max;
		indep.category = IndepTest.category;
		indep.unit = IndepTest.unit;
		indep.description = IndepTest.description;
	}

	@Test
	public void testIndeps() {
		IndepList list = new IndepList();
		assertNull(list.getIndeps());

		Indep[] indeps = new Indep[] { indep };
		list.setIndeps(indeps);

		Indep expected = indeps[0]; // expected Indep
		Indep obtained = list.getIndeps()[0]; // obtained Indep

		assertEquals(expected.name, obtained.name);
		assertEquals(expected.origname, obtained.origname);
		assertEquals(expected.min, obtained.min);
		assertEquals(expected.max, obtained.max);
		assertEquals(expected.category, obtained.category);
		assertEquals(expected.unit, obtained.unit);
		assertEquals(expected.description, obtained.description);
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		Indep[] indeps = new Indep[] { indep };
		IndepList list = new IndepList();
		list.setIndeps(indeps);

		NodeSettings settings = new NodeSettings("irrelevantKey");
		list.saveToNodeSettings(settings);

		Indep[] obtainedIndeps = new Indep[1];
		obtainedIndeps[0] = new Indep();
		obtainedIndeps[0].loadFromNodeSettings(settings.getNodeSettings("indeps" + 0));
		
		Indep expected = indeps[0];  // expected Indep
		Indep obtained = obtainedIndeps[0];  // obtained Indep

		assertEquals(expected.name, obtained.name);
		assertEquals(expected.origname, obtained.origname);
		assertEquals(expected.min, obtained.min);
		assertEquals(expected.max, obtained.max);
		assertEquals(expected.category, obtained.category);
		assertEquals(expected.unit, obtained.unit);
		assertEquals(expected.description, obtained.description);
	}

	@Test
	public void testLoadFromNodeSettings() {

		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("numIndeps", 1);
		indep.saveToNodeSettings(settings.addNodeSettings("indeps" + 0));

		IndepList list = new IndepList();
		list.loadFromNodeSettings(settings);
		
		Indep expected = indep;  // expected Indep
		Indep obtained = list.getIndeps()[0];  // obtained Indep

		assertEquals(expected.name, obtained.name);
		assertEquals(expected.origname, obtained.origname);
		assertEquals(expected.min, obtained.min);
		assertEquals(expected.max, obtained.max);
		assertEquals(expected.category, obtained.category);
		assertEquals(expected.unit, obtained.unit);
		assertEquals(expected.description, obtained.description);
	}
}
