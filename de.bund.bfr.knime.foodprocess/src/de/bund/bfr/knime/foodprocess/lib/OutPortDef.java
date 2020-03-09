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
package de.bund.bfr.knime.foodprocess.lib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyTable;

import de.bund.bfr.knime.util.Matrix;

public class OutPortDef {
		
	private int n_inports;//, n_outports;

	private JFormattedTextField outFlux;
	private JButton newMatrixDefinition;
	private JFormattedTextField[] fromInPort;
	private ParametersDef parametersDef;

	public JFormattedTextField getOutFlux() {
		return outFlux;
	}
	public JButton getNewMatrixDefinition() {
		return newMatrixDefinition;
	}
	public JFormattedTextField[] getFromInPort() {
		return fromInPort;
	}
	public ParametersDef getParametersDef() {
		return parametersDef;
	}
	//private OutPortDef[] outPortDef;
	private Matrix matrix = null;
	
	public OutPortDef( final int n_inports, final int n_outports, final OutPortDef[] outPortDef ) {
		
		int i;
		this.n_inports = n_inports;
		//this.n_outports = n_outports;
		//this.outPortDef = outPortDef;

		NumberFormat nf;
		nf = NumberFormat.getNumberInstance( java.util.Locale.US );
		outFlux = new JFormattedTextField( nf ) {
		    /**
			 * 
			 */
			private static final long serialVersionUID = -5745718848688964119L;

			@Override  
		    protected void processFocusEvent(final FocusEvent e) {  
		        if (e.getID() == FocusEvent.FOCUS_LOST) {  
		            if (getText() == null || getText().isEmpty()) {  
		                setValue(null);  
		            }  
		        }  
		        super.processFocusEvent(e);  
		    }  
		};
		outFlux.setColumns(5);
		
		newMatrixDefinition = new JButton("(select matrix)");
		//newMatrixDefinition.setSelectedIndex( 0 );
		//newMatrixDefinition.setEditable( true );
		newMatrixDefinition.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newMatrixDefinitionActionPerformed(e);
			}
		});
		
		fromInPort = new JFormattedTextField[ n_inports ];
		for( i = 0; i < n_inports; i++ ) {
			
			fromInPort[ i ] = new JFormattedTextField( nf );
			fromInPort[ i ].setColumns(10);
		}
				
		parametersDef = new ParametersDef();

	}
	
	public OutPortSetting getSetting() {
		
		OutPortSetting ops;
		int i;
		Double val;
		
		ops = new OutPortSetting( n_inports );
		
		////ops.setMatrix( ( String )newMatrixDefinition.getSelectedItem() );
		//ops.setMatrix((String) newMatrixDefinition.getEditor().getItem());
		ops.setMatrix(matrix);
		
		val = outFlux.getValue() == null ? null : ((Number)outFlux.getValue()).doubleValue();
		ops.setOutFlux( val );
		
		Double[] fromInVal = ops.getFromInPort();
		for( i = 0; i < n_inports; i++ ) {
			
			val = fromInPort[ i ].getValue() == null ? null : ((Number)fromInPort[ i ].getValue()).doubleValue();
			
			fromInVal[i] = val;
		}
		ops.setFromInPort(fromInVal);
				
		ops.setParametersSetting(parametersDef.getSetting());
		
		return ops;
	}
	
	public void setSetting( final OutPortSetting ops ) {		
		int i;
		Double val;
		
		val = ops.getOutFlux();
		outFlux.setValue( val );
		
		//newMatrixDefinition.setSelectedItem( ops.getMatrix() );
		matrix = ops.getMatrix();
		if (matrix != null) newMatrixDefinition.setText(matrix.getName());
		else newMatrixDefinition.setText("(select matrix)");
		
		for( i = 0; i < n_inports; i++ ) {
			val = ops.getFromInPort()[i];
			fromInPort[ i ].setValue( val );
		}
		
		parametersDef.setSetting(ops.getParametersSetting());
	}
	private void newMatrixDefinitionActionPerformed(ActionEvent e) {
		//System.err.println(e);
		JButton b = ((JButton) e.getSource());
		Integer id = (matrix == null ? null : matrix.getId());
		MyTable myT = DBKernel.myDBi.getTable("Matrices");
		Object newVal = DBKernel.mainFrame.openNewWindow(
				myT,
				id,
				(Object) ("Matrix"),
				null,
				null,
				null,
				null,
				true);
		if (newVal != null && newVal instanceof Integer) {
			String mname = ""+DBKernel.getValue("Matrices", "ID", newVal+"", "Matrixname");
			b.setText(mname);
			matrix = new Matrix(mname, (Integer) newVal);
		}
		else {
			b.setText("(select matrix)");
			matrix = null;
		}
	}
}
