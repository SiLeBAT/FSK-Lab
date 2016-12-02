package de.bund.bfr.knime.fsklab.nodes;

import java.util.List;

import org.jdom2.DefaultJDOMFactory;
import org.jdom2.Element;
import org.jdom2.Namespace;

import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;
import de.unirostock.sems.cbarchive.meta.MetaDataObject;

public class FskOmexMetaData {

	public MetaDataObject modelObj;
	public MetaDataObject paramObj;
	public MetaDataObject vizObj;
	public MetaDataObject workspaceObj;

	private static final Namespace dcNamespace = Namespace.getNamespace("dc", "http://purl.org/dc/elements/1.1/");
	private static DefaultJDOMFactory factory = new DefaultJDOMFactory();
	
	public enum ResourceType { modelScript, parametersScript, visualizationScript, workspace };

	public FskOmexMetaData() {
	}

	public FskOmexMetaData(final List<MetaDataObject> objs) {
		for (final MetaDataObject mdo : objs) {
			Element node = mdo.getXmlDescription();
			ResourceType type = ResourceType.valueOf(node.getChildText("type", dcNamespace));

			if (type.equals(ResourceType.modelScript)) {
				modelObj = mdo;
			} else if (type.equals(ResourceType.parametersScript)) {
				paramObj = mdo;
			} else if (type.equals(ResourceType.visualizationScript)) {
				vizObj = mdo;
			} else if (type.equals(ResourceType.workspace)) {
				workspaceObj = mdo;
			}
		}
	}

	public String getRes(final ResourceType type) {
		MetaDataObject targetObj;
		if (type.equals(ResourceType.modelScript)) {
			targetObj = modelObj;
		} else if (type.equals(ResourceType.parametersScript)) {
			targetObj = paramObj;
		} else if (type.equals(ResourceType.visualizationScript)) {
			targetObj = vizObj;
		} else if (type.equals(ResourceType.workspace)) {
			targetObj = workspaceObj;
		} else {
			return null;
		}
		
		return targetObj == null ? null : targetObj.getXmlDescription().getChildText("source", dcNamespace);
	}

	public void setRes(final ResourceType type, final String script) {

		MetaDataObject mdo = null;
		if (type.equals(ResourceType.modelScript)) {
			mdo = modelObj;
		} else if (type.equals(ResourceType.parametersScript)) {
			mdo = paramObj;
		} else if (type.equals(ResourceType.visualizationScript)) {
			mdo = vizObj;
		} else if (type.equals(ResourceType.workspace)) {
			mdo = workspaceObj;
		}

		if (mdo == null) {
			Element typeNode = factory.element("type", dcNamespace);
			typeNode.setText(type.name());

			Element srcNode = factory.element("source", dcNamespace);
			srcNode.setText(script);

			Element node = factory.element("element");
			node.addContent(typeNode);
			node.addContent(srcNode);

			mdo = new DefaultMetaDataObject(node);
			
			if (type.equals(ResourceType.modelScript)) {
				modelObj = mdo;
			} else if (type.equals(ResourceType.parametersScript)) {
				paramObj = mdo;
			} else if (type.equals(ResourceType.visualizationScript)) {
				vizObj = mdo;
			} else if (type.equals(ResourceType.workspace)) {
				workspaceObj = mdo;
			}
		} else {
			mdo.getXmlDescription().getChild("source", dcNamespace).setText(script);
		}
	}
}
