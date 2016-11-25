/***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
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
 **************************************************************************************************/
package de.bund.bfr.knime.fsklab.nodes;

import de.bund.bfr.pmfml.ModelType;

import java.text.ParseException;
import java.util.Date;

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import com.google.common.base.Strings;

public class MetadataAnnotation {

	private static final String METADATA_TAG = "metadata"; // Metadata tag
	private static final String METADATA_NS = "pmf"; // Metadata namespace

	// Namespace and tag for the creator: <dc:creator>
	private final static String CREATOR_NS = "dc";
	private final static String CREATOR_TAG = "creator";

	// Namespace and tag for the created date: <dcterms:created>
	private final static String CREATED_NS = "dcterms";
	private final static String CREATED_TAG = "created";

	// Namespace and tag for the modified date: <dcterms:modified>
	private final static String MODIFIED_NS = "dcterms";
	private final static String MODIFIED_TAG = "modified";

	// Namespace and tag for the type: <dc:type>
	private final static String TYPE_NS = "dc";
	private final static String TYPE_TAG = "type";

	// Namespace and tag for the rights: <dc:rights>
	private final static String RIGHTS_NS = "dc";
	private final static String RIGHTS_TAG = "rights";

	// Namespace and tag for the reference description: <dc:description>
	private final static String REFDESC_NS = "dc";
	private final static String REFDESC_TAG = "description";
	
	// Namespace and tag for the reference description link: <dc:source>
	private final static String REFDESCLINK_NS = "dc";
	private final static String REFDESCLINK_TAG = "source";

	public Annotation annotation;

	// Fields with metadata
	public String givenName;
	public String familyName;
	public String contact;
	public Date createdDate;
	public Date modifiedDate;
	public ModelType type;
	public String rights;
	public String referenceDescription;
	public String referenceDescriptionLink;

	public MetadataAnnotation(final FskMetaData metadata) {

		XMLTriple pmfTriple = new XMLTriple(METADATA_TAG, "", METADATA_NS);
		XMLNode pmfNode = new XMLNode(pmfTriple);

		// Builds creator node
		if (!Strings.isNullOrEmpty(metadata.creator) || !Strings.isNullOrEmpty(metadata.familyName)
				|| !Strings.isNullOrEmpty(metadata.contact)) {
			givenName = Strings.nullToEmpty(metadata.creator);
			familyName = Strings.nullToEmpty(metadata.familyName);
			contact = Strings.nullToEmpty(metadata.contact);

			String creator = givenName + "." + familyName + "." + contact;
			XMLNode creatorNode = new XMLNode(new XMLTriple(CREATOR_TAG, null, CREATOR_NS));
			creatorNode.addChild(new XMLNode(creator));
			pmfNode.addChild(creatorNode);
		}
		
		// Builds created date node
		if (metadata.createdDate != null) {
			XMLNode createdNode = new XMLNode(new XMLTriple(CREATED_TAG, "", CREATED_NS));
			createdNode.addChild(new XMLNode(FskMetaData.dateFormat.format(metadata.createdDate)));
			pmfNode.addChild(createdNode);
		}
		
		// Builds modified date node
		if (metadata.modifiedDate != null) {
			XMLNode modifiedNode = new XMLNode(new XMLTriple(MODIFIED_TAG, "", MODIFIED_NS));
			modifiedNode.addChild(new XMLNode(FskMetaData.dateFormat.format(metadata.modifiedDate)));
			pmfNode.addChild(modifiedNode);
		}
		
		// Builds type node
		if (metadata.type != null) {
			XMLNode typeNode = new XMLNode(new XMLTriple(TYPE_TAG, "", TYPE_NS));
			typeNode.addChild(new XMLNode(metadata.type.name()));
			pmfNode.addChild(typeNode);
		}
		
		// Builds rights node
		if (!Strings.isNullOrEmpty(metadata.rights)) {
			XMLNode rightsNode = new XMLNode(new XMLTriple(RIGHTS_TAG, "", RIGHTS_NS));
			rightsNode.addChild(new XMLNode(metadata.rights));
			pmfNode.addChild(rightsNode);
		}
		
		// Builds reference description node
		if (!Strings.isNullOrEmpty(metadata.referenceDescription)) {
			XMLNode refdescNode = new XMLNode(new XMLTriple(REFDESC_TAG, "", REFDESC_NS));
			refdescNode.addChild(new XMLNode(metadata.referenceDescription));
			pmfNode.addChild(refdescNode);
		}
		
		// Builds reference description link node
		if (!Strings.isNullOrEmpty(metadata.referenceDescriptionLink)) {
			XMLNode refdescLinkNode = new XMLNode(new XMLTriple(REFDESCLINK_TAG, "", REFDESCLINK_NS));
			refdescLinkNode.addChild(new XMLNode(metadata.referenceDescriptionLink));
			pmfNode.addChild(refdescLinkNode);
		}
		
		// Create annotation
		annotation = new Annotation();
		annotation.setNonRDFAnnotation(pmfNode);
	}

	public MetadataAnnotation(final Annotation annotation) {
		XMLNode pmfNode = annotation.getNonRDFannotation().getChildElement(METADATA_TAG, "");
		
		// Reads creatorNode
		XMLNode creatorNode = pmfNode.getChildElement(CREATOR_TAG,"");
		if (creatorNode != null) {
			String[] tempStrings = creatorNode.getChild(0).getCharacters().split("\\.", 3);
			givenName = Strings.nullToEmpty(tempStrings[0]);
			familyName = Strings.nullToEmpty(tempStrings[1]);
			contact = Strings.nullToEmpty(tempStrings[2]);
		}
		
		// Reads created date
		XMLNode createdNode = pmfNode.getChildElement(CREATED_TAG, "");
		if (createdNode != null) {
			try {
				createdDate = FskMetaData.dateFormat.parse(createdNode.getChild(0).getCharacters());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Reads modified date
		XMLNode modifiedNode = pmfNode.getChildElement(MODIFIED_TAG, "");
		if (modifiedNode != null) {
			try {
				modifiedDate = FskMetaData.dateFormat.parse(modifiedNode.getChild(0).getCharacters());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Reads model type
		XMLNode typeNode = pmfNode.getChildElement(TYPE_TAG, "");
		if (typeNode != null) {
			type = ModelType.valueOf(typeNode.getChild(0).getCharacters());
		}
		
		// Reads rights
		XMLNode rightsNode = pmfNode.getChildElement(RIGHTS_TAG, "");
		if (rightsNode != null) {
			rights = rightsNode.getChild(0).getCharacters();
		}
		
		// Reads reference description
		XMLNode refdescNode = pmfNode.getChildElement(REFDESC_TAG, "");
		if (refdescNode != null) {
			referenceDescription = refdescNode.getChild(0).getCharacters();
		}
		
		// Reads reference description link
		XMLNode refdescLinkNode = pmfNode.getChildElement(REFDESCLINK_TAG, "");
		if (refdescLinkNode != null) {
			referenceDescriptionLink = refdescLinkNode.getChild(0).getCharacters();
		}
		
		// Copies annotation
		this.annotation = annotation;
	}
}
