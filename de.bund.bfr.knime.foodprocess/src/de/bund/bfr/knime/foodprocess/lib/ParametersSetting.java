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

public class ParametersSetting {
	private final String PARAM_TEMPERATURE = "temperature";
	private final String PARAM_TEMPERATUREALT = "temperatureAlt";
	private final String PARAM_TEMPERATURE_UNIT = "temperatureUnit";
	private final String PARAM_VOLUME = "volume";
	private final String PARAM_VOLUME_UNIT = "volumeUnit";
	private final String PARAM_AW = "aw";
	private final String PARAM_AWALT = "awAlt";
	private final String PARAM_PH = "ph";
	private final String PARAM_PHALT = "phAlt";
	private final String PARAM_PRESSURE = "pressure";
	private final String PARAM_PRESSUREALT = "pressureAlt";
	private final String PARAM_PRESSURE_UNIT = "pressureUnit";
	
	private String temperature;
	private String tempAlt;
	public String getTempAlt() {
		return tempAlt;
	}
	public void setTempAlt(String tempAlt) {
		this.tempAlt = tempAlt;
	}
	public String getAwAlt() {
		return awAlt;
	}
	public void setAwAlt(String awAlt) {
		this.awAlt = awAlt;
	}
	public String getPhAlt() {
		return phAlt;
	}
	public void setPhAlt(String phAlt) {
		this.phAlt = phAlt;
	}
	public String getPresAlt() {
		return presAlt;
	}
	public void setPresAlt(String presAlt) {
		this.presAlt = presAlt;
	}

	private String temperatureUnit;
	
	private String volume;
	private String volumeUnit;
	
	private String aw;
	private String awAlt;
	private String ph;
	private String phAlt;
	
	private String pressure;
	private String presAlt;
	private String pressureUnit;

	public String getPressureUnit() {
		return pressureUnit;
	}
	public String getTemperatureUnit() {
		return temperatureUnit;
	}
	public String getVolumeUnit() {
		return volumeUnit;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public void setPressure(String pressure) {
		this.pressure = pressure;
	}
	public void setPh(String ph) {
		this.ph = ph;
	}
	public void setVolumeUnit(String volumeUnit) {
		this.volumeUnit = volumeUnit;
	}
	public void setPressureUnit(String pressureUnit) {
		this.pressureUnit = pressureUnit;
	}
	public void setTemperatureUnit(String temperatureUnit) {
		this.temperatureUnit = temperatureUnit;
	}
	public String getPh() {
		return ph;
	}
	public String getPressure() {
		return pressure;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getAw() {
		return aw;
	}
	public void setAw(String aw) {
		this.aw = aw;
	}

	public ParametersSetting() {
		
	}
	public void saveSettings( final Config config ) {
		if (volume != null) {
			config.addString( PARAM_VOLUME, volume );
		}
		config.addString(PARAM_VOLUME_UNIT, volumeUnit);
		if (temperature != null) {
			config.addString( PARAM_TEMPERATURE, temperature );
		}
		if (tempAlt != null) {
			config.addString( PARAM_TEMPERATUREALT, tempAlt );
		}
		config.addString(PARAM_TEMPERATURE_UNIT, temperatureUnit);
		if (ph != null) {
			config.addString( PARAM_PH, ph );
		}
		if (phAlt != null) {
			config.addString( PARAM_PHALT, phAlt );
		}
		if (aw != null) {
			config.addString( PARAM_AW, aw );
		}
		if (awAlt != null) {
			config.addString( PARAM_AWALT, awAlt );
		}
		if (pressure != null) {
			config.addString( PARAM_PRESSURE, pressure );
		}
		if (presAlt != null) {
			config.addString( PARAM_PRESSUREALT, presAlt );
		}
		config.addString(PARAM_PRESSURE_UNIT, pressureUnit);
	}
	public void loadSettings( final Config config ) throws InvalidSettingsException {		
		volume = config.containsKey(PARAM_VOLUME) ? config.getString( PARAM_VOLUME ) : null;
		volumeUnit = config.getString(PARAM_VOLUME_UNIT);
		temperature = config.containsKey(PARAM_TEMPERATURE) ? config.getString( PARAM_TEMPERATURE ) : null;
		tempAlt = config.containsKey(PARAM_TEMPERATUREALT) ? config.getString( PARAM_TEMPERATUREALT ) : null;
		temperatureUnit = config.getString(PARAM_TEMPERATURE_UNIT);
		ph = config.containsKey(PARAM_PH) ? config.getString( PARAM_PH ) : null;
		phAlt = config.containsKey(PARAM_PHALT) ? config.getString( PARAM_PHALT ) : null;
		aw = config.containsKey(PARAM_AW) ? config.getString( PARAM_AW ) : null;
		awAlt = config.containsKey(PARAM_AWALT) ? config.getString( PARAM_AWALT ) : null;
		pressure = config.containsKey(PARAM_PRESSURE) ? config.getString( PARAM_PRESSURE ) : null;
		presAlt = config.containsKey(PARAM_PRESSUREALT) ? config.getString( PARAM_PRESSUREALT ) : null;
		pressureUnit = config.getString(PARAM_PRESSURE_UNIT);
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
