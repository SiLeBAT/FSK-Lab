package de.bund.bfr.knime.pmm.common;

import static org.junit.Assert.assertFalse;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

@SuppressWarnings("static-method")
public class XmlConverterTest {

	@Test
	public void testColorMapToXml() throws Exception {
		Map<String, Color> colorMap = new HashMap<>();
		colorMap.put("RED", Color.RED);
		colorMap.put("ORANGE", Color.ORANGE);
		colorMap.put("GREEN", Color.GREEN);
		colorMap.put("BLUE", Color.BLUE);
		
		assertFalse(XmlConverter.colorMapToXml(colorMap).isEmpty());
	}
}
