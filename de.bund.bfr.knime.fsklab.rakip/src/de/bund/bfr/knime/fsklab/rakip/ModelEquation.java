package de.bund.bfr.knime.fsklab.rakip;

import java.util.ArrayList;
import java.util.List;

import com.gmail.gcolaianni5.jris.bean.Record;

public class ModelEquation {

  /** Model equation name. */
  public String equationName;

  /** Model equation class. */
  public String equationClass;

  /** Model equation references (RIS). */
  public final List<Record> equationReference = new ArrayList<>();

  /** Model equation or script. */
  public String equation;
}
