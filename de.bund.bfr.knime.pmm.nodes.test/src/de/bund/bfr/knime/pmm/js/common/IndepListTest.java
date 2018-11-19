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
		indep.setName(IndepTest.name);
		indep.setOrigname(IndepTest.origname);
		indep.setMin(IndepTest.min);
		indep.setMax(IndepTest.max);
		indep.setCategory(IndepTest.category);
		indep.setUnit(IndepTest.unit);
		indep.setDescription(IndepTest.description);
	}

	@Test
	public void testIndeps() {
		IndepList list = new IndepList();
		assertNull(list.getIndeps());

		Indep[] indeps = new Indep[] { indep };
		list.setIndeps(indeps);

		Indep expected = indeps[0]; // expected Indep
		Indep obtained = list.getIndeps()[0]; // obtained Indep

		assertEquals(expected.getName(), obtained.getName());
		assertEquals(expected.getOrigname(), obtained.getOrigname());
		assertEquals(expected.getMin(), obtained.getMin());
		assertEquals(expected.getMax(), obtained.getMax());
		assertEquals(expected.getCategory(), obtained.getCategory());
		assertEquals(expected.getUnit(), obtained.getUnit());
		assertEquals(expected.getDescription(), obtained.getDescription());
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

		assertEquals(expected.getName(), obtained.getName());
		assertEquals(expected.getOrigname(), obtained.getOrigname());
		assertEquals(expected.getMin(), obtained.getMin());
		assertEquals(expected.getMax(), obtained.getMax());
		assertEquals(expected.getCategory(), obtained.getCategory());
		assertEquals(expected.getUnit(), obtained.getUnit());
		assertEquals(expected.getDescription(), obtained.getDescription());
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

		assertEquals(expected.getName(), obtained.getName());
		assertEquals(expected.getOrigname(), obtained.getOrigname());
		assertEquals(expected.getMin(), obtained.getMin());
		assertEquals(expected.getMax(), obtained.getMax());
		assertEquals(expected.getCategory(), obtained.getCategory());
		assertEquals(expected.getUnit(), obtained.getUnit());
		assertEquals(expected.getDescription(), obtained.getDescription());
	}
}
