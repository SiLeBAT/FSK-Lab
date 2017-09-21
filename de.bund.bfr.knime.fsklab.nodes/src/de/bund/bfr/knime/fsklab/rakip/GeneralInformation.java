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
}
