/*
 * An XML document type.
 * Localname: ProcessNode
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.ProcessNodeDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * A document containing one ProcessNode(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public class ProcessNodeDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.ProcessNodeDocument
{
    private static final long serialVersionUID = 1L;
    
    public ProcessNodeDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PROCESSNODE$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "ProcessNode");
    
    
    /**
     * Gets the "ProcessNode" element
     */
    public de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode getProcessNode()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode target = null;
            target = (de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode)get_store().find_element_user(PROCESSNODE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "ProcessNode" element
     */
    public void setProcessNode(de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode processNode)
    {
        generatedSetterHelperImpl(processNode, PROCESSNODE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "ProcessNode" element
     */
    public de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode addNewProcessNode()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode target = null;
            target = (de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode)get_store().add_element_user(PROCESSNODE$0);
            return target;
        }
    }
    /**
     * An XML ProcessNode(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public static class ProcessNodeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode
    {
        private static final long serialVersionUID = 1L;
        
        public ProcessNodeImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName PROCESS$0 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Process");
        private static final javax.xml.namespace.QName PARAMETERS$2 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Parameters");
        private static final javax.xml.namespace.QName INPORT$4 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Inport");
        private static final javax.xml.namespace.QName OUTPORT$6 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Outport");
        private static final javax.xml.namespace.QName ID$8 = 
            new javax.xml.namespace.QName("", "id");
        private static final javax.xml.namespace.QName TYPE$10 = 
            new javax.xml.namespace.QName("", "type");
        
        
        /**
         * Gets the "Process" element
         */
        public de.bund.bfr.pcml10.NameAndDatabaseId getProcess()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.NameAndDatabaseId target = null;
                target = (de.bund.bfr.pcml10.NameAndDatabaseId)get_store().find_element_user(PROCESS$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Sets the "Process" element
         */
        public void setProcess(de.bund.bfr.pcml10.NameAndDatabaseId process)
        {
            generatedSetterHelperImpl(process, PROCESS$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "Process" element
         */
        public de.bund.bfr.pcml10.NameAndDatabaseId addNewProcess()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.NameAndDatabaseId target = null;
                target = (de.bund.bfr.pcml10.NameAndDatabaseId)get_store().add_element_user(PROCESS$0);
                return target;
            }
        }
        
        /**
         * Gets the "Parameters" element
         */
        public de.bund.bfr.pcml10.ProcessParameters getParameters()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.ProcessParameters target = null;
                target = (de.bund.bfr.pcml10.ProcessParameters)get_store().find_element_user(PARAMETERS$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Sets the "Parameters" element
         */
        public void setParameters(de.bund.bfr.pcml10.ProcessParameters parameters)
        {
            generatedSetterHelperImpl(parameters, PARAMETERS$2, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "Parameters" element
         */
        public de.bund.bfr.pcml10.ProcessParameters addNewParameters()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.ProcessParameters target = null;
                target = (de.bund.bfr.pcml10.ProcessParameters)get_store().add_element_user(PARAMETERS$2);
                return target;
            }
        }
        
        /**
         * Gets array of all "Inport" elements
         */
        public de.bund.bfr.pcml10.InportDocument.Inport[] getInportArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(INPORT$4, targetList);
                de.bund.bfr.pcml10.InportDocument.Inport[] result = new de.bund.bfr.pcml10.InportDocument.Inport[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "Inport" element
         */
        public de.bund.bfr.pcml10.InportDocument.Inport getInportArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.InportDocument.Inport target = null;
                target = (de.bund.bfr.pcml10.InportDocument.Inport)get_store().find_element_user(INPORT$4, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "Inport" element
         */
        public int sizeOfInportArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(INPORT$4);
            }
        }
        
        /**
         * Sets array of all "Inport" element  WARNING: This method is not atomicaly synchronized.
         */
        public void setInportArray(de.bund.bfr.pcml10.InportDocument.Inport[] inportArray)
        {
            check_orphaned();
            arraySetterHelper(inportArray, INPORT$4);
        }
        
        /**
         * Sets ith "Inport" element
         */
        public void setInportArray(int i, de.bund.bfr.pcml10.InportDocument.Inport inport)
        {
            generatedSetterHelperImpl(inport, INPORT$4, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "Inport" element
         */
        public de.bund.bfr.pcml10.InportDocument.Inport insertNewInport(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.InportDocument.Inport target = null;
                target = (de.bund.bfr.pcml10.InportDocument.Inport)get_store().insert_element_user(INPORT$4, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "Inport" element
         */
        public de.bund.bfr.pcml10.InportDocument.Inport addNewInport()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.InportDocument.Inport target = null;
                target = (de.bund.bfr.pcml10.InportDocument.Inport)get_store().add_element_user(INPORT$4);
                return target;
            }
        }
        
        /**
         * Removes the ith "Inport" element
         */
        public void removeInport(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(INPORT$4, i);
            }
        }
        
        /**
         * Gets array of all "Outport" elements
         */
        public de.bund.bfr.pcml10.OutportDocument.Outport[] getOutportArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(OUTPORT$6, targetList);
                de.bund.bfr.pcml10.OutportDocument.Outport[] result = new de.bund.bfr.pcml10.OutportDocument.Outport[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "Outport" element
         */
        public de.bund.bfr.pcml10.OutportDocument.Outport getOutportArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.OutportDocument.Outport target = null;
                target = (de.bund.bfr.pcml10.OutportDocument.Outport)get_store().find_element_user(OUTPORT$6, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "Outport" element
         */
        public int sizeOfOutportArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(OUTPORT$6);
            }
        }
        
        /**
         * Sets array of all "Outport" element  WARNING: This method is not atomicaly synchronized.
         */
        public void setOutportArray(de.bund.bfr.pcml10.OutportDocument.Outport[] outportArray)
        {
            check_orphaned();
            arraySetterHelper(outportArray, OUTPORT$6);
        }
        
        /**
         * Sets ith "Outport" element
         */
        public void setOutportArray(int i, de.bund.bfr.pcml10.OutportDocument.Outport outport)
        {
            generatedSetterHelperImpl(outport, OUTPORT$6, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "Outport" element
         */
        public de.bund.bfr.pcml10.OutportDocument.Outport insertNewOutport(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.OutportDocument.Outport target = null;
                target = (de.bund.bfr.pcml10.OutportDocument.Outport)get_store().insert_element_user(OUTPORT$6, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "Outport" element
         */
        public de.bund.bfr.pcml10.OutportDocument.Outport addNewOutport()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.OutportDocument.Outport target = null;
                target = (de.bund.bfr.pcml10.OutportDocument.Outport)get_store().add_element_user(OUTPORT$6);
                return target;
            }
        }
        
        /**
         * Removes the ith "Outport" element
         */
        public void removeOutport(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(OUTPORT$6, i);
            }
        }
        
        /**
         * Gets the "id" attribute
         */
        public java.lang.String getId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ID$8);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "id" attribute
         */
        public org.apache.xmlbeans.XmlString xgetId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(ID$8);
                return target;
            }
        }
        
        /**
         * Sets the "id" attribute
         */
        public void setId(java.lang.String id)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ID$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ID$8);
                }
                target.setStringValue(id);
            }
        }
        
        /**
         * Sets (as xml) the "id" attribute
         */
        public void xsetId(org.apache.xmlbeans.XmlString id)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(ID$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(ID$8);
                }
                target.set(id);
            }
        }
        
        /**
         * Gets the "type" attribute
         */
        public de.bund.bfr.pcml10.ProcessNodeType.Enum getType()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TYPE$10);
                if (target == null)
                {
                    return null;
                }
                return (de.bund.bfr.pcml10.ProcessNodeType.Enum)target.getEnumValue();
            }
        }
        
        /**
         * Gets (as xml) the "type" attribute
         */
        public de.bund.bfr.pcml10.ProcessNodeType xgetType()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.ProcessNodeType target = null;
                target = (de.bund.bfr.pcml10.ProcessNodeType)get_store().find_attribute_user(TYPE$10);
                return target;
            }
        }
        
        /**
         * True if has "type" attribute
         */
        public boolean isSetType()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(TYPE$10) != null;
            }
        }
        
        /**
         * Sets the "type" attribute
         */
        public void setType(de.bund.bfr.pcml10.ProcessNodeType.Enum type)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TYPE$10);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(TYPE$10);
                }
                target.setEnumValue(type);
            }
        }
        
        /**
         * Sets (as xml) the "type" attribute
         */
        public void xsetType(de.bund.bfr.pcml10.ProcessNodeType type)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.ProcessNodeType target = null;
                target = (de.bund.bfr.pcml10.ProcessNodeType)get_store().find_attribute_user(TYPE$10);
                if (target == null)
                {
                    target = (de.bund.bfr.pcml10.ProcessNodeType)get_store().add_attribute_user(TYPE$10);
                }
                target.set(type);
            }
        }
        
        /**
         * Unsets the "type" attribute
         */
        public void unsetType()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(TYPE$10);
            }
        }
    }
}
