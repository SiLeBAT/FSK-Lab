package de.bund.bfr.knime.fsklab.rakip;

import java.net.URI;

public class Study {

  /** Study tile. */
  public String title;

  /** Study description. */
  public String description;

  /** Study type. */
  public String designType;

  /** Observed measured in the assay. */
  public String measurementType;

  /** Employed technology to observe this measurement. */
  public String technologyType;

  /** Used technology platform. */
  public String technologyPlatform;

  /** Used accreditation procedure. */
  public String accreditationProcedure;
  public String protocolName;
  public String protocolType;

  /** Type of the protocol (e.g. Extraction protocol). */
  public String protocolDescription;
  public URI protocolUri;
  public String protocolVersion;

  /** Parameters used when executing this protocol. */
  public String parametersName;
  public String componentsName;
  public String componentsType;
}
