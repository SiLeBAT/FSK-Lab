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

import java.net.URI;
import java.util.Objects;

public class Study {

	/** Study tile. */
	public String title = "";

	/** Study description. */
	public String description = "";

	/** Study type. */
	public String designType = "";

	/** Observed measured in the assay. */
	public String measurementType = "";

	/** Employed technology to observe this measurement. */
	public String technologyType = "";

	/** Used technology platform. */
	public String technologyPlatform = "";

	/** Used accreditation procedure. */
	public String accreditationProcedure = "";
	public String protocolName = "";
	public String protocolType = "";

	/** Type of the protocol (e.g. Extraction protocol). */
	public String protocolDescription = "";
	public URI protocolUri;
	public String protocolVersion = "";

	/** Parameters used when executing this protocol. */
	public String parametersName = "";
	public String componentsName = "";
	public String componentsType = "";

	@Override
	public int hashCode() {
		return Objects.hash(accreditationProcedure, componentsName, componentsType, description, designType,
				measurementType, parametersName, protocolDescription, protocolName, protocolType, protocolUri,
				protocolVersion, technologyPlatform, technologyType, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Study other = (Study) obj;
		if (accreditationProcedure == null) {
			if (other.accreditationProcedure != null)
				return false;
		} else if (!accreditationProcedure.equals(other.accreditationProcedure))
			return false;
		if (componentsName == null) {
			if (other.componentsName != null)
				return false;
		} else if (!componentsName.equals(other.componentsName))
			return false;
		if (componentsType == null) {
			if (other.componentsType != null)
				return false;
		} else if (!componentsType.equals(other.componentsType))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (designType == null) {
			if (other.designType != null)
				return false;
		} else if (!designType.equals(other.designType))
			return false;
		if (measurementType == null) {
			if (other.measurementType != null)
				return false;
		} else if (!measurementType.equals(other.measurementType))
			return false;
		if (parametersName == null) {
			if (other.parametersName != null)
				return false;
		} else if (!parametersName.equals(other.parametersName))
			return false;
		if (protocolDescription == null) {
			if (other.protocolDescription != null)
				return false;
		} else if (!protocolDescription.equals(other.protocolDescription))
			return false;
		if (protocolName == null) {
			if (other.protocolName != null)
				return false;
		} else if (!protocolName.equals(other.protocolName))
			return false;
		if (protocolType == null) {
			if (other.protocolType != null)
				return false;
		} else if (!protocolType.equals(other.protocolType))
			return false;
		if (protocolUri == null) {
			if (other.protocolUri != null)
				return false;
		} else if (!protocolUri.equals(other.protocolUri))
			return false;
		if (protocolVersion == null) {
			if (other.protocolVersion != null)
				return false;
		} else if (!protocolVersion.equals(other.protocolVersion))
			return false;
		if (technologyPlatform == null) {
			if (other.technologyPlatform != null)
				return false;
		} else if (!technologyPlatform.equals(other.technologyPlatform))
			return false;
		if (technologyType == null) {
			if (other.technologyType != null)
				return false;
		} else if (!technologyType.equals(other.technologyType))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

}
