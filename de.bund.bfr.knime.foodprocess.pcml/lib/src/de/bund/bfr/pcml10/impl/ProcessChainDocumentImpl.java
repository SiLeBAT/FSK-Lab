/*
 * An XML document type.
 * Localname: ProcessChain
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.ProcessChainDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * A document containing one ProcessChain(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public class ProcessChainDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.ProcessChainDocument
{
    private static final long serialVersionUID = 1L;
    
    public ProcessChainDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PROCESSCHAIN$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "ProcessChain");
    
    
    /**
     * Gets the "ProcessChain" element
     */
    public de.bund.bfr.pcml10.ProcessChainDocument.ProcessChain getProcessChain()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.ProcessChainDocument.ProcessChain target = null;
            target = (de.bund.bfr.pcml10.ProcessChainDocument.ProcessChain)get_store().find_element_user(PROCESSCHAIN$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "ProcessChain" element
     */
    public void setProcessChain(de.bund.bfr.pcml10.ProcessChainDocument.ProcessChain processChain)
    {
        generatedSetterHelperImpl(processChain, PROCESSCHAIN$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "ProcessChain" element
     */
    public de.bund.bfr.pcml10.ProcessChainDocument.ProcessChain addNewProcessChain()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.ProcessChainDocument.ProcessChain target = null;
            target = (de.bund.bfr.pcml10.ProcessChainDocument.ProcessChain)get_store().add_element_user(PROCESSCHAIN$0);
            return target;
        }
    }
    /**
     * An XML ProcessChain(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public static class ProcessChainImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.ProcessChainDocument.ProcessChain
    {
        private static final long serialVersionUID = 1L;
        
        public ProcessChainImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName PROCESSNODE$0 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "ProcessNode");
        private static final javax.xml.namespace.QName EXTENSION$2 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Extension");
        
        
        /**
         * Gets array of all "ProcessNode" elements
         */
        public de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode[] getProcessNodeArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(PROCESSNODE$0, targetList);
                de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode[] result = new de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "ProcessNode" element
         */
        public de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode getProcessNodeArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode target = null;
                target = (de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode)get_store().find_element_user(PROCESSNODE$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "ProcessNode" element
         */
        public int sizeOfProcessNodeArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(PROCESSNODE$0);
            }
        }
        
        /**
         * Sets array of all "ProcessNode" element  WARNING: This method is not atomicaly synchronized.
         */
        public void setProcessNodeArray(de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode[] processNodeArray)
        {
            check_orphaned();
            arraySetterHelper(processNodeArray, PROCESSNODE$0);
        }
        
        /**
         * Sets ith "ProcessNode" element
         */
        public void setProcessNodeArray(int i, de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode processNode)
        {
            generatedSetterHelperImpl(processNode, PROCESSNODE$0, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "ProcessNode" element
         */
        public de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode insertNewProcessNode(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode target = null;
                target = (de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode)get_store().insert_element_user(PROCESSNODE$0, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "ProcessNode" element
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
         * Removes the ith "ProcessNode" element
         */
        public void removeProcessNode(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(PROCESSNODE$0, i);
            }
        }
        
        /**
         * Gets array of all "Extension" elements
         */
        public de.bund.bfr.pcml10.ExtensionDocument.Extension[] getExtensionArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(EXTENSION$2, targetList);
                de.bund.bfr.pcml10.ExtensionDocument.Extension[] result = new de.bund.bfr.pcml10.ExtensionDocument.Extension[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "Extension" element
         */
        public de.bund.bfr.pcml10.ExtensionDocument.Extension getExtensionArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.ExtensionDocument.Extension target = null;
                target = (de.bund.bfr.pcml10.ExtensionDocument.Extension)get_store().find_element_user(EXTENSION$2, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "Extension" element
         */
        public int sizeOfExtensionArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(EXTENSION$2);
            }
        }
        
        /**
         * Sets array of all "Extension" element  WARNING: This method is not atomicaly synchronized.
         */
        public void setExtensionArray(de.bund.bfr.pcml10.ExtensionDocument.Extension[] extensionArray)
        {
            check_orphaned();
            arraySetterHelper(extensionArray, EXTENSION$2);
        }
        
        /**
         * Sets ith "Extension" element
         */
        public void setExtensionArray(int i, de.bund.bfr.pcml10.ExtensionDocument.Extension extension)
        {
            generatedSetterHelperImpl(extension, EXTENSION$2, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "Extension" element
         */
        public de.bund.bfr.pcml10.ExtensionDocument.Extension insertNewExtension(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.ExtensionDocument.Extension target = null;
                target = (de.bund.bfr.pcml10.ExtensionDocument.Extension)get_store().insert_element_user(EXTENSION$2, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "Extension" element
         */
        public de.bund.bfr.pcml10.ExtensionDocument.Extension addNewExtension()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.ExtensionDocument.Extension target = null;
                target = (de.bund.bfr.pcml10.ExtensionDocument.Extension)get_store().add_element_user(EXTENSION$2);
                return target;
            }
        }
        
        /**
         * Removes the ith "Extension" element
         */
        public void removeExtension(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(EXTENSION$2, i);
            }
        }
    }
}
