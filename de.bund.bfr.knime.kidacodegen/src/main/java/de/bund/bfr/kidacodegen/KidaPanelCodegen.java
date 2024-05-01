package de.bund.bfr.kidacodegen;

public class KidaPanelCodegen {
	public String fieldName;
	public String schemaName;
	public String metadataSource;
	public String schemaSource;
	public String label;
	public boolean array;
	public boolean hasParent;
	public String parent;

	public String getMetadatParent() {
		if(array && getParent().endsWith(metadataSource) ) 
			return getParent().substring(0,getParent().indexOf(metadataSource)-1);
		else if(getParent().equals(metadataSource))
			return "";

		else
			return getParent();
	}
	public boolean isMetadatParentbBlank() {
		System.out.println(getMetadatParent());
		if(getMetadatParent().equals("")) 
			return true;
		else
			return false;
	}

	public String fieldNameWithParent;
	
	public String getFieldNameWithParent() {
		return fieldNameWithParent;
	}

	public void setFieldNameWithParent(String fieldNameWithParent) {
		this.fieldNameWithParent = fieldNameWithParent;
	}

	public String getSchemaSource() {
		return schemaSource;
	}

	public void setSchemaSource(String schemaSource) {
		schemaSource = schemaSource;
	}

	public boolean isHasParent() {
		return hasParent;
	}

	public void setHasParent(boolean hasParent) {
		this.hasParent = hasParent;
	}

	public String getParent() {
			if (parent.endsWith("."))
				return parent.substring(0,parent.length()-1);
			return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getMetadataSource() {
		return metadataSource;
	}

	public void setMetadataSource(String metadataSource) {
		this.metadataSource = metadataSource;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isArray() {
		return array;
	}

	public void setArray(boolean array) {
		this.array = array;
	}

}
