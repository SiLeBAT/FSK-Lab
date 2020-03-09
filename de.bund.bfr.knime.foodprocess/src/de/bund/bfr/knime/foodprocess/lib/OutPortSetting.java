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

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.Config;

import de.bund.bfr.knime.util.Matrix;

public class OutPortSetting {
	
	private final String PARAM_PARAMETERS = "parameters";
	private final String PARAM_MATRIX = "matrix";
	private final String PARAM_OUTFLUX = "outFlux";
	private final String PARAM_FROMINPORT = "fromInPort";
	
	private Matrix matrix;
	private Double outFlux;
		
	public ParametersSetting getParametersSetting() {
		return parametersSetting;
	}
	public Double[] getFromInPort() {
		return fromInPort;
	}
	public void setParametersSetting(ParametersSetting parametersSetting) {
		this.parametersSetting = parametersSetting;
	}
	public void setFromInPort(Double[] fromInPort) {
		this.fromInPort = fromInPort;
	}
	public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}
	public Matrix getMatrix() {
		return matrix;
	}
	public void setOutFlux(Double outFlux) {
		this.outFlux = outFlux;
	}
	public Double getOutFlux() {
		return outFlux;
	}
	private Double[] fromInPort;
	
	private ParametersSetting parametersSetting;
	
	public OutPortSetting( final int n_inports ) {
		fromInPort = new Double[ n_inports ];
		parametersSetting = new ParametersSetting();
	}
	
	@Override
	public String toString() {
		
		String ret;
		int i;
		
		ret = "out flux : "+outFlux+" %\n";
		ret += "new matrix definition : "+matrix+"\n";
		for( i = 0; i < fromInPort.length; i++ ) {
			ret += "from in port "+i+" : "+fromInPort[ i ]+" %\n";
		}
		/*
		ret += "volume : "+volume+" "+"\n";
		ret += "volume_func : "+volume_func+" "+"\n";
		ret += "temperature : "+temperature+"\n";
		ret += "temperature_func : "+temperature_func+" "+"\n";
		ret += "pH : "+ph+"\n";
		ret += "ph_func : "+ph_func+" "+"\n";
		ret += "aw : "+aw+"\n";
		ret += "aw_func : "+aw_func+" "+"\n";
		ret += "pressure : "+pressure+"\n";
		ret += "pressure_func : "+pressure_func+" "+"\n";
		*/
		return ret;
	}
	
	public void saveSettings( final Config config ) {		
		int i;
		
		Config c = config.addConfig(PARAM_PARAMETERS);
		parametersSetting.saveSettings( c );
		config.addInt( PARAM_MATRIX, matrix == null ? 0 : matrix.getId() );
		if (outFlux != null) {
			config.addDouble( PARAM_OUTFLUX, outFlux );			
		}

		for( i = 0; i < fromInPort.length; i++ ) {
			if (fromInPort[ i ] != null) {
				config.addDouble( PARAM_FROMINPORT+"_"+i, fromInPort[ i ] );
			}
		}
		
	}
	
	public void loadSettings( final Config config ) throws InvalidSettingsException {		
		int i;
		Config c;
		
		if (config.containsKey(PARAM_MATRIX) && config.getInt(PARAM_MATRIX) > 0) matrix = new Matrix(config.getInt(PARAM_MATRIX));
		else matrix = null;
		outFlux = config.containsKey(PARAM_OUTFLUX) ? config.getDouble( PARAM_OUTFLUX ) : null;

		for( i = 0; i < fromInPort.length; i++ ) {
			fromInPort[ i ] = config.containsKey(PARAM_FROMINPORT+"_"+i) ? config.getDouble( PARAM_FROMINPORT+"_"+i ) : null;
		}
		
		parametersSetting = new ParametersSetting();
		c = config.getConfig(PARAM_PARAMETERS);
		parametersSetting.loadSettings( c );
	}
	
	public void loadSettingsForDialog( final Config config ) {		
		try {
			loadSettings( config );
		}
		catch( InvalidSettingsException e ) {
			e.printStackTrace( System.err );
			assert false;
		}		
	}
}
