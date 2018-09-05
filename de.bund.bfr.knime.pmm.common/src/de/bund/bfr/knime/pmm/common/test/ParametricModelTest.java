/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.common.test;
/*
import static org.junit.Assert.*;

import org.jdom2.Element;
import org.junit.Test;

import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.PmmException;
*/
public class ParametricModelTest {
	/*
	@Test
	public void errorMrsTest() throws PmmException {
		
		ParametricModel model;
		boolean allowed;
		
		model = new ParametricModel( "testModel", "y=x", "y", 1 );
		
		// initially everything is NaN
		assertTrue( Double.isNaN( model.getAic() ) );
		assertTrue( Double.isNaN( model.getRss() ) );
		assertTrue( Double.isNaN( model.getRms() ) );
		assertTrue( Double.isNaN( model.getRsquared() ) );
		
		// negative squared errors are not allowed.
		allowed = true;
		try {
			model.setRss( -1 );
		}
		catch( PmmException ex ) {
			allowed = false;
		}
		assertFalse( allowed );
		
		allowed = true;
		try {
			model.setRms( -1 );
		}
		catch( PmmException ex ) {
			allowed = false;
		}
		assertFalse( allowed );
		
		// an R^2 value of more than one is not allowed.
		allowed = true;
		try {
			model.setRsquared( 2 );
		}
		catch( PmmException ex ) {
			allowed = false;
		}
		assertFalse( allowed );
		
		// the instance stores the values 
		model.setRsquared( .5 );
		model.setRss( 10 );
		model.setRms( 5 );
		assertEquals( .5, model.getRsquared(), .001 );
		assertEquals( 10, model.getRss(), .001 );
		assertEquals( 5, model.getRms(), .001 );
		
	}

	@Test
	public void rangeTest() {
		
		ParametricModel model;
		String modelName, depVar, formula;
		int level;
		Element element;
		
		
		formula = "y = a*x + b";
		depVar = "y";
		modelName = "Some linear model";
		level = 1;
		
		// create a new parametric model
		model = new ParametricModel( modelName, formula, depVar, level );
		
		
		// add the single independent variable
		assertTrue( model.getIndepVarSet().isEmpty() );
		assertTrue( model.getParamNameSet().isEmpty() );
		model.addIndepVar( "x" );
		assertTrue( Double.isNaN( model.getMin( "x" ) ) );
		assertTrue( Double.isNaN( model.getMax( "x" ) ) );
		
		
		// set some minima and maxima
		model.setMin( "x", 0. );
		model.setMax( "x", 10000. );
		model.addParam( "a", 0., -1., 2. );
		
		// make sure all information was stored
		assertEquals( modelName, model.getModelName() );
		assertEquals( formula.replaceAll( "\\s", "" ), model.getFormula() );
		assertEquals( depVar, model.getDepVar() );
		assertEquals( level, model.getLevel() );
		assertEquals( 1, model.getIndepVarSet().size() );
		assertEquals( "x", model.getIndepVarSet().getFirst() );
		assertEquals( 1, model.getParamNameSet().size() );
		assertEquals( 0, model.getMin( "x" ), .001 );
		assertEquals( 10000, model.getMax( "x" ), .001 );
		assertEquals( 0, model.getParamValue( "a" ), .001 );
		assertEquals( -1, model.getMin( "a" ), .001 );
		assertEquals( 2, model.getMax( "a" ), .001 );
		
		
		// check whether everything is ok also after exporting and reimporting from XML
		element = model.toXmlElement();
		model = new ParametricModel( element );
		
		assertEquals( modelName, model.getModelName() );
		assertEquals( formula.replaceAll( "\\s", "" ), model.getFormula() );
		assertEquals( depVar, model.getDepVar() );
		assertEquals( level, model.getLevel() );
		assertEquals( 1, model.getIndepVarSet().size() );
		assertEquals( "x", model.getIndepVarSet().getFirst() );
		assertEquals( 1, model.getParamNameSet().size() );
		assertEquals( 0, model.getMin( "x" ), .001 );
		assertEquals( 10000, model.getMax( "x" ), .001 );
		assertEquals( 0, model.getParamValue( "a" ), .001 );
		assertEquals( -1, model.getMin( "a" ), .001 );
		assertEquals( 2, model.getMax( "a" ), .001 );
	}
	*/
}
