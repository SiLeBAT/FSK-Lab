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
package de.bund.bfr.knime.fsklab.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import de.bund.bfr.knime.fsklab.nodes.Variable.DataType;

public class VariableTest {

  @Test
  public void test() {
    // Test default values
    Variable v = new Variable();
    assertNull(v.name);
    assertNull(v.unit);
    assertNull(v.min);
    assertNull(v.max);
    assertNull(v.type);
  }

  @Test
  public void testToString() {
    Variable v = new Variable();
    v.name = "Prevalence";
    assertEquals("Variable [Prevalence]", v.toString());
  }

  @Test
  public void testEquals() {
    Variable p = new Variable();
    p.name = "Prevalence";
    p.unit = "percent";
    p.value = "50";
    p.min = "10";
    p.max = "100";
    p.type = DataType.numeric;

    // equals with same object should return true
    assertTrue(p.equals(p));

    // equals with null should return false
    assertFalse(p.equals(null));

    // equals with a different class object should return false
    assertFalse(p.equals(new Integer(0)));

    // equals with matching object
    Variable other = new Variable();
    other.name = p.name;
    other.unit = p.unit;
    other.value = p.value;
    other.min = p.min;
    other.max = p.max;
    other.type = p.type;
    assertEquals(p, other);

    // equals with different name
    other.name = "n.iter";
    assertFalse(p.equals(other));
    other.name = p.name;

    // equals with different unit
    other.unit = "no_name";
    assertFalse(p.equals(other));
    other.unit = p.unit;

    // equals with different value
    other.value = "200";
    assertFalse(p.equals(other));
    other.value = p.value;

    // equals with different min
    other.min = "1";
    assertFalse(p.equals(other));
    other.min = p.min;

    // equals with different max
    other.max = "1e8";
    assertFalse(p.equals(other));
    other.max = p.max;

    // equals with different type
    other.type = DataType.integer;
    assertFalse(p.equals(other));
    other.type = p.type;
  }

  @Test
  public void testHashCode() throws Exception {
    Variable v1 = new Variable();
    v1.name = "Prevalence";
    v1.unit = "percent";
    v1.value = "50";
    v1.min = "10";
    v1.max = "100";
    v1.type = DataType.numeric;

    Variable v2 = new Variable();
    v2.name = v1.name;
    v2.unit = v1.unit;
    v2.value = v1.value;
    v2.min = v1.min;
    v2.max = v1.max;
    v2.type = v1.type;

    assertTrue(v1.hashCode() == v2.hashCode());
  }
}
