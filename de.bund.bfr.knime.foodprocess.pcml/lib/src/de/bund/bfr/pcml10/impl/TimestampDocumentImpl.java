/*
 * An XML document type.
 * Localname: Timestamp
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.TimestampDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * A document containing one Timestamp(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public class TimestampDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.TimestampDocument
{
    private static final long serialVersionUID = 1L;
    
    public TimestampDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TIMESTAMP$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Timestamp");
    
    
    /**
     * Gets the "Timestamp" element
     */
    public de.bund.bfr.pcml10.TimestampDocument.Timestamp getTimestamp()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.TimestampDocument.Timestamp target = null;
            target = (de.bund.bfr.pcml10.TimestampDocument.Timestamp)get_store().find_element_user(TIMESTAMP$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Timestamp" element
     */
    public void setTimestamp(de.bund.bfr.pcml10.TimestampDocument.Timestamp timestamp)
    {
        generatedSetterHelperImpl(timestamp, TIMESTAMP$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "Timestamp" element
     */
    public de.bund.bfr.pcml10.TimestampDocument.Timestamp addNewTimestamp()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.TimestampDocument.Timestamp target = null;
            target = (de.bund.bfr.pcml10.TimestampDocument.Timestamp)get_store().add_element_user(TIMESTAMP$0);
            return target;
        }
    }
    /**
     * An XML Timestamp(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public static class TimestampImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.TimestampDocument.Timestamp
    {
        private static final long serialVersionUID = 1L;
        
        public TimestampImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName EXTENSION$0 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Extension");
        
        
        /**
         * Gets array of all "Extension" elements
         */
        public de.bund.bfr.pcml10.ExtensionDocument.Extension[] getExtensionArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(EXTENSION$0, targetList);
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
                target = (de.bund.bfr.pcml10.ExtensionDocument.Extension)get_store().find_element_user(EXTENSION$0, i);
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
                return get_store().count_elements(EXTENSION$0);
            }
        }
        
        /**
         * Sets array of all "Extension" element  WARNING: This method is not atomicaly synchronized.
         */
        public void setExtensionArray(de.bund.bfr.pcml10.ExtensionDocument.Extension[] extensionArray)
        {
            check_orphaned();
            arraySetterHelper(extensionArray, EXTENSION$0);
        }
        
        /**
         * Sets ith "Extension" element
         */
        public void setExtensionArray(int i, de.bund.bfr.pcml10.ExtensionDocument.Extension extension)
        {
            generatedSetterHelperImpl(extension, EXTENSION$0, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
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
                target = (de.bund.bfr.pcml10.ExtensionDocument.Extension)get_store().insert_element_user(EXTENSION$0, i);
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
                target = (de.bund.bfr.pcml10.ExtensionDocument.Extension)get_store().add_element_user(EXTENSION$0);
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
                get_store().remove_element(EXTENSION$0, i);
            }
        }
    }
}
