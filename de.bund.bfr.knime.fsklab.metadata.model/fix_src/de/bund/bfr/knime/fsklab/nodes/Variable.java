package de.bund.bfr.knime.fsklab.nodes;
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


import java.io.Serializable;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Holds variable-related data.
 * 
 * value, min and max can hold different types of values: integers, real numbers, strings or even
 * arrays. So they are kept as strings in order to support all these types.
 * 
 * @author Miguel Alba
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Variable implements Serializable {

  private static final long serialVersionUID = -331516812131854597L;

  /** Null or empty if not set. */
  public String name;

  /** Null or empty if not set. */
  public String unit;

  /** Null or empty if not set. */
  public String value;

  /** Null or empty if not set. */
  public String min;

  /** Null or empty if not set. */
  public String max;

  /** Null if not set. */
  public DataType type;

  @Override
  public String toString() {
    return "Variable [" + name + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, unit, type, value, min, max);
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Variable other = (Variable) obj;

    return Objects.equals(name, other.name) && Objects.equals(unit, other.unit)
        && Objects.equals(value, other.value) && Objects.equals(min, other.min)
        && Objects.equals(max, other.max) && Objects.equals(type, other.type);
  }

  public enum DataType {
    /** String values. */
    character,
    /** Integers. */
    integer,
    /** Real numbers. */
    numeric,
    /** Arrays. */
    array
  }
}
