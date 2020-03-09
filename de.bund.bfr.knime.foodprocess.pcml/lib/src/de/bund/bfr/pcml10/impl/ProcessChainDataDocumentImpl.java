/*
 * An XML document type.
 * Localname: ProcessChainData
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.ProcessChainDataDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * A document containing one ProcessChainData(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public class ProcessChainDataDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.ProcessChainDataDocument
{
    private static final long serialVersionUID = 1L;
    
    public ProcessChainDataDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PROCESSCHAINDATA$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "ProcessChainData");
    
    
    /**
     * Gets the "ProcessChainData" element
     */
    public de.bund.bfr.pcml10.ProcessChainDataDocument.ProcessChainData getProcessChainData()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.ProcessChainDataDocument.ProcessChainData target = null;
            target = (de.bund.bfr.pcml10.ProcessChainDataDocument.ProcessChainData)get_store().find_element_user(PROCESSCHAINDATA$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "ProcessChainData" element
     */
    public void setProcessChainData(de.bund.bfr.pcml10.ProcessChainDataDocument.ProcessChainData processChainData)
    {
        generatedSetterHelperImpl(processChainData, PROCESSCHAINDATA$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
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
            target = (de.bund.bfr.pcml10.ProcessChainDataDocument.ProcessChainData)get_store().add_element_user(PROCESSCHAINDATA$0);
            return target;
        }
    }
    /**
     * An XML ProcessChainData(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public static class ProcessChainDataImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.ProcessChainDataDocument.ProcessChainData
    {
        private static final long serialVersionUID = 1L;
        
        public ProcessChainDataImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName PROCESSDATA$0 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "ProcessData");
        private static final javax.xml.namespace.QName EXTENSION$2 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Extension");
        
        
        /**
         * Gets array of all "ProcessData" elements
         */
        public de.bund.bfr.pcml10.ProcessDataDocument.ProcessData[] getProcessDataArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(PROCESSDATA$0, targetList);
                de.bund.bfr.pcml10.ProcessDataDocument.ProcessData[] result = new de.bund.bfr.pcml10.ProcessDataDocument.ProcessData[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "ProcessData" element
         */
        public de.bund.bfr.pcml10.ProcessDataDocument.ProcessData getProcessDataArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.ProcessDataDocument.ProcessData target = null;
                target = (de.bund.bfr.pcml10.ProcessDataDocument.ProcessData)get_store().find_element_user(PROCESSDATA$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "ProcessData" element
         */
        public int sizeOfProcessDataArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(PROCESSDATA$0);
            }
        }
        
        /**
         * Sets array of all "ProcessData" element  WARNING: This method is not atomicaly synchronized.
         */
        public void setProcessDataArray(de.bund.bfr.pcml10.ProcessDataDocument.ProcessData[] processDataArray)
        {
            check_orphaned();
            arraySetterHelper(processDataArray, PROCESSDATA$0);
        }
        
        /**
         * Sets ith "ProcessData" element
         */
        public void setProcessDataArray(int i, de.bund.bfr.pcml10.ProcessDataDocument.ProcessData processData)
        {
            generatedSetterHelperImpl(processData, PROCESSDATA$0, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "ProcessData" element
         */
        public de.bund.bfr.pcml10.ProcessDataDocument.ProcessData insertNewProcessData(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.ProcessDataDocument.ProcessData target = null;
                target = (de.bund.bfr.pcml10.ProcessDataDocument.ProcessData)get_store().insert_element_user(PROCESSDATA$0, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "ProcessData" element
         */
        public de.bund.bfr.pcml10.ProcessDataDocument.ProcessData addNewProcessData()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.ProcessDataDocument.ProcessData target = null;
                target = (de.bund.bfr.pcml10.ProcessDataDocument.ProcessData)get_store().add_element_user(PROCESSDATA$0);
                return target;
            }
        }
        
        /**
         * Removes the ith "ProcessData" element
         */
        public void removeProcessData(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(PROCESSDATA$0, i);
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
