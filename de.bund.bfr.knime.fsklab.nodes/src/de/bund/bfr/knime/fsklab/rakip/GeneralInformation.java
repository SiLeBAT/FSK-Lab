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

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gmail.gcolaianni5.jris.bean.Record;

import ezvcard.VCard;

public class GeneralInformation {

	/** Name given to the model or data. */
	public String name = "";

	/** Related resource from which the resource is derived. */
	public String source = "";

	/** Unambiguous ID given to the model or data. */
	public String identifier = "";

	public final List<VCard> creators = new ArrayList<>();

	/** Model creation date. */
	public Date creationDate = new Date();

	public final List<Date> modificationDate = new ArrayList<>();

	/** Rights held in over the resource. */
	public String rights = "";

	/** Availability of data or model. */
	public boolean isAvailable;

	/** Web address referencing the resource location. */
	public URL url;

	/** Form of data (file extension). */
	public String format = "";

	public final List<Record> reference = new ArrayList<>();

	/** Language of the resource. */
	public String language = "";

	/** Program in which the model has been implemented. */
	public String software = "";

	/** Language used to write the model. */
	public String languageWrittenIn = "";

	public ModelCategory modelCategory = new ModelCategory();

	/** Curation status of the model. */
	public String status = "";

	/** Objective of the model or data. */
	public String objective = "";

	/** General description of the study, data or model. */
	public String description = "";

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((creators == null) ? 0 : creators.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((format == null) ? 0 : format.hashCode());
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + (isAvailable ? 1231 : 1237);
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((languageWrittenIn == null) ? 0 : languageWrittenIn.hashCode());
		result = prime * result + ((modelCategory == null) ? 0 : modelCategory.hashCode());
		result = prime * result + ((modificationDate == null) ? 0 : modificationDate.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((objective == null) ? 0 : objective.hashCode());
		result = prime * result + ((reference == null) ? 0 : reference.hashCode());
		result = prime * result + ((rights == null) ? 0 : rights.hashCode());
		result = prime * result + ((software == null) ? 0 : software.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeneralInformation other = (GeneralInformation) obj;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (creators == null) {
			if (other.creators != null)
				return false;
		} else if (!creators.equals(other.creators))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (format == null) {
			if (other.format != null)
				return false;
		} else if (!format.equals(other.format))
			return false;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (isAvailable != other.isAvailable)
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (languageWrittenIn == null) {
			if (other.languageWrittenIn != null)
				return false;
		} else if (!languageWrittenIn.equals(other.languageWrittenIn))
			return false;
		if (modelCategory == null) {
			if (other.modelCategory != null)
				return false;
		} else if (!modelCategory.equals(other.modelCategory))
			return false;
		if (modificationDate == null) {
			if (other.modificationDate != null)
				return false;
		} else if (!modificationDate.equals(other.modificationDate))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (objective == null) {
			if (other.objective != null)
				return false;
		} else if (!objective.equals(other.objective))
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		if (rights == null) {
			if (other.rights != null)
				return false;
		} else if (!rights.equals(other.rights))
			return false;
		if (software == null) {
			if (other.software != null)
				return false;
		} else if (!software.equals(other.software))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
}
