package de.bund.bfr.knime.fsklab;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.property.filter.FilterHandler;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettings;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortType;
import org.knime.core.node.wizard.WizardNode;
import org.knime.core.node.wizard.WizardViewCreator;
import org.knime.js.core.JavaScriptViewCreator;
import org.knime.js.core.node.AbstractWizardNodeModel;

public abstract class EMFAbstractWizardNodeModel <REP extends EMFJSONViewContent, VAL extends EMFJSONViewContent>
extends NodeModel implements WizardNode<REP, VAL> {

private static final NodeLogger LOGGER = NodeLogger.getLogger(AbstractWizardNodeModel.class);

private final Object m_lock = new Object();

private String m_viewPath;
private REP m_representation;
private VAL m_value;
private final String m_viewName;
private final JavaScriptViewCreator<REP, VAL> m_viewCreator;

/**
* Creates a new {@link WizardNode} model with the given number (and types!) of input and
* output types.
* @param inPortTypes an array of non-null in-port types
* @param outPortTypes an array of non-null out-port types
* @param viewName The view name
*/
protected EMFAbstractWizardNodeModel(final PortType[] inPortTypes, final PortType[] outPortTypes, final String viewName) {
super(inPortTypes, outPortTypes);
m_viewName = viewName;
m_viewCreator = new JavaScriptViewCreator<>(getJavascriptObjectID());
}

/**
* A lock object, which should be used when modifying view representation or value.
* @return the lock
*/
public Object getLock() {
return m_lock;
}

/**
* {@inheritDoc}
*/
@Override
protected final PortObject[] execute(final PortObject[] inObjects, final ExecutionContext exec) throws Exception {
synchronized (m_lock) {
    if (m_representation == null) {
        m_representation = createEmptyViewRepresentation();
    }
    if (m_value == null) {
        m_value = createEmptyViewValue();
    }
}
PortObject[] portObjects = performExecute(inObjects, exec);
return portObjects;
}

/**
* {@inheritDoc}
*/
@Override
protected final BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
    throws Exception {
return super.execute(inData, exec);
}

/**
* Invoked on execute. See {@link NodeModel#execute(PortObject[], ExecutionContext) NodeModel.execute}.
* @param inObjects The input objects.
* @param exec For {@link BufferedDataTable} creation and progress.
* @return The output objects.
* @throws Exception If the node execution fails for any reason.
*/
protected abstract PortObject[] performExecute(final PortObject[] inObjects, final ExecutionContext exec)
throws Exception;

/**
* {@inheritDoc}
*/
@Override
protected final void reset() {
synchronized (m_lock) {
    m_representation = createEmptyViewRepresentation();
    m_value = createEmptyViewValue();
    resetViewHTML();
}
performReset();
}

/**
* Invoked on reset.
*/
protected abstract void performReset();

/**
* @param portIndex the port index to retrieve the table id from
* @return a unique table id to separate interactivity events
*/
protected final String getTableId(final int portIndex) {
return getInHiLiteHandler(portIndex).getHiliteHandlerID().toString();
}

/**
* @param spec the table spec
* @return a string array containing all available filter ids
*/
protected final String[] getSubscriptionFilterIds(final DataTableSpec spec) {
List<String> idList = new ArrayList<String>();
for (int i = 0; i < spec.getNumColumns(); i++) {
    Optional<FilterHandler> filterHandler = spec.getColumnSpec(i).getFilterHandler();
    if (filterHandler.isPresent()) {
        idList.add(filterHandler.get().getModel().getFilterUUID().toString());
    }
}
return idList.toArray(new String[0]);
}

/**
* {@inheritDoc}
*/
@Override
public final String getViewHTMLPath() {
if (m_viewPath == null || m_viewPath.isEmpty()) {
    // view is not created
    m_viewPath = createViewPath();
} else {
    // check if file still exists, create otherwise
    File viewFile = new File(m_viewPath);
    if (!viewFile.exists()) {
        m_viewPath = createViewPath();
    }
}
return m_viewPath;
}

private String createViewPath() {
JavaScriptViewCreator<REP, VAL> viewCreator = new JavaScriptViewCreator<REP, VAL>(getJavascriptObjectID());
try {
    return viewCreator.createWebResources(getInteractiveViewName(), getViewRepresentation(), getViewValue());
} catch (IOException e) {
    LOGGER.error("Creating view HTML failed: " + e.getMessage(), e);
    return null;
}
}

/**
* Gets the name of the interactive view.
* This can usually be achieved by returning the concrete {@link NodeFactory#getInteractiveViewName()}.
* @return the view name
*/
protected final String getInteractiveViewName() {
return m_viewName;
}

/**
* Resets the view HTML, attempts to delete file.
*/
void resetViewHTML() {
if (m_viewPath != null) {
    try {
        File viewFile = new File(m_viewPath);
        if (viewFile.exists()) {
            viewFile.delete();
        }
    } catch (Exception e) { /* do nothing */ }
}
m_viewPath = null;
}

/**
* {@inheritDoc}
*/
@Override
public WizardViewCreator<REP, VAL> getViewCreator() {
return m_viewCreator;
}

/**
* {@inheritDoc}
*/
@Override
public REP getViewRepresentation() {
synchronized (m_lock) {
    return m_representation;
}
}

/**
* Sets the view representation.
* @param representation the representation to set.
*/
protected void setViewRepresentation(final REP representation) {
synchronized (m_lock) {
    m_representation = representation;
}
}

/**
* This method can be used to determine if a view representation was previously set. If the view representation
* is unchanged from the empty created state, this method returns true.
*
* @return true if the view representation is null or equals to the empty view representation, false otherwise
*/
protected final boolean isViewRepresentationEmpty() {
synchronized (m_lock) {
    REP emptyRepresentation = createEmptyViewRepresentation();
    REP curRepresentation = getViewRepresentation();
    return curRepresentation == null || emptyRepresentation.equals(curRepresentation);
}
}

/**
* {@inheritDoc}
*/
@Override
public VAL getViewValue() {
synchronized (m_lock) {
    return m_value;
}
}

/**
* Sets the view value.
* @param value the value to set.
*/
protected void setViewValue(final VAL value) {
synchronized (m_lock) {
    m_value = value;
}
}

/**
* This method can be used to determine if a view value was previously set. If the view value
* is unchanged from the empty created state, this method returns true.
*
* @return true if the view value is null or equals to the empty view value, false otherwise
*/
protected final boolean isViewValueEmpty() {
synchronized (m_lock) {
    VAL emptyValue = createEmptyViewValue();
    VAL curValue = getViewValue();
    return curValue == null || emptyValue.equals(curValue);
}
}

/**
* {@inheritDoc}
*/
@Override
public void loadViewValue(final VAL viewValue, final boolean useAsDefault) {
synchronized (m_lock) {
    m_value = viewValue;
    if (useAsDefault) {
        useCurrentValueAsDefault();
    }
}
}

/**
* Uses current view value as new default.
* Usually achieved by overwriting node config with view values.
*/
protected abstract void useCurrentValueAsDefault();

/**
* {@inheritDoc}
*/
@Override
protected void loadInternals(final File nodeInternDir, final ExecutionMonitor exec) throws IOException,
CanceledExecutionException {
File repFile = new File(nodeInternDir, "representation.xml");
File valFile = new File(nodeInternDir, "value.xml");
NodeSettingsRO repSettings = NodeSettings.loadFromXML(new FileInputStream(repFile));
NodeSettingsRO valSettings = NodeSettings.loadFromXML(new FileInputStream(valFile));
m_representation = createEmptyViewRepresentation();
m_value = createEmptyViewValue();
try {
    m_representation.loadFromNodeSettings(repSettings);
    m_value.loadFromNodeSettings(valSettings);
} catch (InvalidSettingsException e) {
    // what to do?
    LOGGER.error("Error loading internals: " + e.getMessage(), e);
}
}

/**
* {@inheritDoc}
*/
@Override
protected void saveInternals(final File nodeInternDir, final ExecutionMonitor exec) throws IOException,
CanceledExecutionException {
NodeSettings repSettings = new NodeSettings("viewRepresentation");
NodeSettings valSettings = new NodeSettings("viewValue");
if (m_representation != null) {
    m_representation.saveToNodeSettings(repSettings);
}
if (m_value != null) {
    m_value.saveToNodeSettings(valSettings);
}
File repFile = new File(nodeInternDir, "representation.xml");
File valFile = new File(nodeInternDir, "value.xml");
repSettings.saveToXML(new FileOutputStream(repFile));
valSettings.saveToXML(new FileOutputStream(valFile));
}
}
