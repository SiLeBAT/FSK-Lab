/***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 * <p>
 * Contributors: Department Biological Safety - BfR
 **************************************************************************************************/
package de.bund.bfr.knime.fsklab.rakip;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gmail.gcolaianni5.jris.bean.Record;

import ezvcard.VCard;

public class RakipModule extends SimpleModule {

	private static final long serialVersionUID = 6106945494093650252L;

	public RakipModule() {
		super("RakipModule", Version.unknownVersion());

		addSerializer(Record.class, new RisReferenceSerializer());
		addDeserializer(Record.class, new RisReferenceDeserializer());

		addSerializer(VCard.class, new VCardSerializer());
		addDeserializer(VCard.class, new VCardDeserializer());
	}
}
