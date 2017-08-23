package de.bund.bfr.knime.fsklab.rakip;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gmail.gcolaianni5.jris.bean.Record;

import ezvcard.VCard;

public class GeneralInformation {

  /** Name given to the model or data. */
  public String name;

  /** Related resource from which the resource is derived. */
  public String source;

  /** Unambiguous ID given to the model or data. */
  public String identifier;

  public final List<VCard> creators = new ArrayList<>();

  /** Model creation date. */
  public Date creationDate = new Date();

  public final List<Date> modificationDate = new ArrayList<>();

  /** Rights held in over the resource. */
  public String rights;

  /** Availability of data or model. */
  public boolean isAvailable;

  /** Web address referencing the resource location. */
  public URL url;

  /** Form of data (file extension). */
  public String format;

  public final List<Record> reference = new ArrayList<>();

  /** Language of the resource. */
  public String language;

  /** Program in which the model has been implemented. */
  public String software;

  /** Language used to write the model. */
  public String languageWrittenIn;

  public ModelCategory modelCategory;

  /** Curation status of the model. */
  public String status;

  /** Objective of the model or data. */
  public String objective;

  /** General description of the study, data or model. */
  public String description;
}
