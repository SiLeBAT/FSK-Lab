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
import static org.junit.Assert.assertNull;
import java.util.Date;
import org.junit.Test;
import de.bund.bfr.knime.fsklab.nodes.MetadataDocument.MetadataAnnotation;
import de.bund.bfr.knime.fsklab.nodes.MetadataDocument.RuleAnnotation;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;

public class MetadataDocumentTest {

  @Test
  public void testMetadataAnnotation() {

    // With empty metadata
    FskMetaData expected = new FskMetaData();
    MetadataAnnotation ma1 = new MetadataAnnotation(expected);
    MetadataAnnotation ma2 = new MetadataAnnotation(ma1.annotation);
    assertNull(ma2.givenName);
    assertNull(ma2.familyName);
    assertNull(ma2.contact);
    assertNull(ma2.createdDate);
    assertNull(ma2.modifiedDate);
    assertNull(ma2.type);
    assertNull(ma2.rights);
    assertNull(ma2.referenceDescription);
    assertNull(ma2.referenceDescriptionLink);

    // With filled metadata
    expected.creator = "Creator";
    expected.familyName = "Family name";
    expected.contact = "Contact";
    expected.createdDate = new Date(2016, 2, 21); // my birthday
    expected.modifiedDate = new Date(2016, 8, 29); // my name day
    expected.type = ModelType.EXPERIMENTAL_DATA;
    expected.rights = "Rights";
    expected.referenceDescription = "Reference description";
    expected.referenceDescriptionLink = "https://myresearch.com";

    ma1 = new MetadataAnnotation(expected);
    ma2 = new MetadataAnnotation(ma1.annotation);

    assertEquals(expected.creator, ma2.givenName);
    assertEquals(expected.familyName, ma2.familyName);
    assertEquals(expected.contact, ma2.contact);
    assertEquals(expected.createdDate, ma2.createdDate);
    assertEquals(expected.modifiedDate, ma2.modifiedDate);
    assertEquals(expected.type, ma2.type);
    assertEquals(expected.rights, ma2.rights);
    assertEquals(expected.referenceDescription, ma2.referenceDescription);
    assertEquals(expected.referenceDescriptionLink, ma2.referenceDescriptionLink);
  }

  @Test
  public void testRuleAnnotation() {
    RuleAnnotation annot1 = new RuleAnnotation(ModelClass.UNKNOWN);
    RuleAnnotation annot2 = new RuleAnnotation(annot1.annotation);
    assertEquals(ModelClass.UNKNOWN, annot2.modelClass);
  }
}
