package de.bund.bfr.kidacodegen;

import java.util.List;
import java.util.Set;

import io.swagger.codegen.v3.CodegenModel;

public class KidaCodegenModel extends CodegenModel {
	public List<KidaPanelCodegen> panels;
	public String jsonSchema;
	public String getJsonSchema() {
		return jsonSchema;
	}

	public void setJsonSchema(String jsonSchema) {
		this.jsonSchema = jsonSchema;
	}

	public List<KidaPanelCodegen> getPanels() {
		return panels;
	}

	public void setPanels(List<KidaPanelCodegen> panels) {
		this.panels = panels;
	}

	public String menus;

	public String getMenus() {
		return menus;
	}

	public void setMenus(String menus) {
		this.menus = menus;
	}

	public boolean isParentOf() {
		return (children != null && children.size() > 0);
	}

	public boolean isParentModelNotNUll() {
		return parentModel != null;
	}
}
