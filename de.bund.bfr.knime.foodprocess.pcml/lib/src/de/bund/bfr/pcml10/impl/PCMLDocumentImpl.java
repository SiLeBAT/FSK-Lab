/*
 * An XML document type.
 * Localname: PCML
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.PCMLDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * A document containing one PCML(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public class PCMLDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.PCMLDocument
{
    private static final long serialVersionUID = 1L;
    
    public PCMLDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PCML$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "PCML");
    
    
    /**
     * Gets the "PCML" element
     */
    public de.bund.bfr.pcml10.PCMLDocument.PCML getPCML()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.PCMLDocument.PCML target = null;
            target = (de.bund.bfr.pcml10.PCMLDocument.PCML)get_store().find_element_user(PCML$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "PCML" element
     */
    public void setPCML(de.bund.bfr.pcml10.PCMLDocument.PCML pcml)
    {
        generatedSetterHelperImpl(pcml, PCML$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "PCML" element
     */
    public de.bund.bfr.pcml10.PCMLDocument.PCML addNewPCML()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.PCMLDocument.PCML target = null;
            target = (de.bund.bfr.pcml10.PCMLDocument.PCML)get_store().add_element_user(PCML$0);
            return target;
        }
    }
    /**
     * An XML PCML(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public static class PCMLImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.PCMLDocument.PCML
    {
        private static final long serialVersionUID = 1L;
        
        public PCMLImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName HEADER$0 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Header");
        private static final javax.xml.namespace.QName PROCESSCHAIN$2 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "ProcessChain");
        private static final javax.xml.namespace.QName PROCESSCHAINDATA$4 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "ProcessChainData");
        private static final javax.xml.namespace.QName EXTENSION$6 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Extension");
        private static final javax.xml.namespace.QName VERSION$8 = 
            new javax.xml.namespace.QName("", "version");
        
        
        /**
         * Gets the "Header" element
         */
        public de.bund.bfr.pcml10.HeaderDocument.Header getHeader()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.HeaderDocument.Header target = null;
                target = (de.bund.bfr.pcml10.HeaderDocument.Header)get_store().find_element_user(HEADER$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Sets the "Header" element
         */
        public void setHeader(de.bund.bfr.pcml10.HeaderDocument.Header header)
        {
            generatedSetterHelperImpl(header, HEADER$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "Header" element
         */
        public de.bund.bfr.pcml10.HeaderDocument.Header addNewHeader()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.HeaderDocument.Header target = null;
                target = (de.bund.bfr.pcml10.HeaderDocument.Header)get_store().add_element_user(HEADER$0);
                return target;
            }
        }
        
        /**
         * Gets the "ProcessChain" element
         */
        public de.bund.bfr.pcml10.ProcessChainDocument.ProcessChain getProcessChain()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.ProcessChainDocument.ProcessChain target = null;
                target = (de.bund.bfr.pcml10.ProcessChainDocument.ProcessChain)get_store().find_element_user(PROCESSCHAIN$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "ProcessChain" element
         */
        public boolean isSetProcessChain()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(PROCESSCHAIN$2) != 0;
            }
        }
        
        /**
         * Sets the "ProcessChain" element
         */
        public void setProcessChain(de.bund.bfr.pcml10.ProcessChainDocument.ProcessChain processChain)
        {
            generatedSetterHelperImpl(processChain, PROCESSCHAIN$2, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
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
                target = (de.bund.bfr.pcml10.ProcessChainDocument.ProcessChain)get_store().add_element_user(PROCESSCHAIN$2);
                return target;
            }
        }
        
        /**
         * Unsets the "ProcessChain" element
         */
        public void unsetProcessChain()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(PROCESSCHAIN$2, 0);
            }
        }
        
        /**
         * Gets the "ProcessChainData" element
         */
        public de.bund.bfr.pcml10.ProcessChainDataDocument.ProcessChainData getProcessChainData()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.ProcessChainDataDocument.ProcessChainData target = null;
                target = (de.bund.bfr.pcml10.ProcessChainDataDocument.ProcessChainData)get_store().find_element_user(PROCESSCHAINDATA$4, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "ProcessChainData" element
         */
        public boolean isSetProcessChainData()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(PROCESSCHAINDATA$4) != 0;
            }
        }
        
        /**
         * Sets the "ProcessChainData" element
         */
        public void setProcessChainData(de.bund.bfr.pcml10.ProcessChainDataDocument.ProcessChainData processChainData)
        {
            generatedSetterHelperImpl(processChainData, PROCESSCHAINDATA$4, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "ProcessChainData" element
         */
        public de.bund.bfr.pcml10.ProcessChainDataDocument.ProcessChainData addNewProcessChainData()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.ProcessChainDataDocument.ProcessChainData target = null;
                target = (de.bund.bfr.pcml10.ProcessChainDataDocument.ProcessChainData)get_store().add_element_user(PROCESSCHAINDATA$4);
                return target;
            }
        }
        
        /**
         * Unsets the "ProcessChainData" element
         */
        public void unsetProcessChainData()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(PROCESSCHAINDATA$4, 0);
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
                get_store().find_all_element_users(EXTENSION$6, targetList);
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
                target = (de.bund.bfr.pcml10.ExtensionDocument.Extension)get_store().find_element_user(EXTENSION$6, i);
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
                return get_store().count_elements(EXTENSION$6);
            }
        }
        
        /**
         * Sets array of all "Extension" element  WARNING: This method is not atomicaly synchronized.
         */
        public void setExtensionArray(de.bund.bfr.pcml10.ExtensionDocument.Extension[] extensionArray)
        {
            check_orphaned();
            arraySetterHelper(extensionArray, EXTENSION$6);
        }
        
        /**
         * Sets ith "Extension" element
         */
        public void setExtensionArray(int i, de.bund.bfr.pcml10.ExtensionDocument.Extension extension)
        {
            generatedSetterHelperImpl(extension, EXTENSION$6, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
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
                target = (de.bund.bfr.pcml10.ExtensionDocument.Extension)get_store().insert_element_user(EXTENSION$6, i);
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
                target = (de.bund.bfr.pcml10.ExtensionDocument.Extension)get_store().add_element_user(EXTENSION$6);
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
                get_store().remove_element(EXTENSION$6, i);
            }
        }
        
        /**
         * Gets the "version" attribute
         */
        public java.lang.String getVersion()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VERSION$8);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "version" attribute
         */
        public org.apache.xmlbeans.XmlString xgetVersion()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(VERSION$8);
                return target;
            }
        }
        
        /**
         * Sets the "version" attribute
         */
        public void setVersion(java.lang.String version)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VERSION$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(VERSION$8);
                }
                target.setStringValue(version);
            }
        }
        
        /**
         * Sets (as xml) the "version" attribute
         */
        public void xsetVersion(org.apache.xmlbeans.XmlString version)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(VERSION$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(VERSION$8);
                }
                target.set(version);
            }
        }
    }
}
