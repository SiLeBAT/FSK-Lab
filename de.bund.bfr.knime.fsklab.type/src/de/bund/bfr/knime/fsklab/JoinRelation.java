/*
 ***************************************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *************************************************************************************************
 */
package de.bund.bfr.knime.fsklab;

import org.emfjson.jackson.module.EMFModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.bund.bfr.metadata.swagger.Parameter;

/**
 * An object that describe the relation between two FSK Objects.
 * 
 * @author Ahmad Swaid, BfR, Berlin.
 */
public class JoinRelation {

	private Parameter sourceParam;
	private Parameter targetParam;
	private String command;
	private String language_written_in;
	
	public JoinRelation(Parameter sourceParam, Parameter targetParam, String command, String languageWrittenIn) {
		this.sourceParam = sourceParam;
		this.targetParam = targetParam;
		this.command = command;
		this.language_written_in = languageWrittenIn;
	}

	public String getLanguage_written_in() {
		return language_written_in;
	}

	public String getCommand() {
		return command;
	}

	public Parameter getSourceParam() {
		return sourceParam;
	}

	public Parameter getTargetParam() {
		return targetParam;
	}

	public String getJsonReresentaion() {
		ObjectMapper mapper = EMFModule.setupDefaultMapper();
		String out = "";
		try {
			String sourceParamAsJSONString = mapper.writeValueAsString(sourceParam);
			String targetParamAsJSONString = mapper.writeValueAsString(targetParam);
			out = "{\"sourceParam\" :" + sourceParamAsJSONString + ",\"targetParam\" :" + targetParamAsJSONString
					+ ",\"language_written_in\" :\"" + language_written_in + "\",\"command\" :\"" + command + "\"}";
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return out;
	}
}