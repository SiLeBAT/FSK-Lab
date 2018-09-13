package de.bund.bfr.knime.pmm.js.common;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AgentTest.class, AgentListTest.class, CatalogModelTest.class, DepTest.class, EstModelTest.class,
		IndepTest.class, IndepListTest.class, LiteratureListTest.class, LiteratureTest.class, MatrixListTest.class,
		MatrixTest.class, MdInfoTest.class, MiscListTest.class, MiscTest.class, ParamListTest.class, ParamTest.class,
		TimeSeriesListTest.class, TimeSeriesTest.class, UnitListTest.class, UnitTest.class })
public class ModelEditorTests {

}
