/*
 * An XML document type.
 * Localname: ProcessData
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.ProcessDataDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * A document containing one ProcessData(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public class ProcessDataDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.ProcessDataDocument
{
    private static final long serialVersionUID = 1L;
    
    public ProcessDataDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PROCESSDATA$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "ProcessData");
    
    
    /**
     * Gets the "ProcessData" element
     */
    public de.bund.bfr.pcml10.ProcessDataDocument.ProcessData getProcessData()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.ProcessDataDocument.ProcessData target = null;
            target = (de.bund.bfr.pcml10.ProcessDataDocument.ProcessData)get_store().find_element_user(PROCESSDATA$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "ProcessData" element
     */
    public void setProcessData(de.bund.bfr.pcml10.ProcessDataDocument.ProcessData processData)
    {
        generatedSetterHelperImpl(processData, PROCESSDATA$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "ProcessData" element
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
     * An XML ProcessData(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public static class ProcessDataImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.ProcessDataDocument.ProcessData
    {
        private static final long serialVersionUID = 1L;
        
        public ProcessDataImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName DATATABLE$0 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "DataTable");
        private static final javax.xml.namespace.QName REF$2 = 
            new javax.xml.namespace.QName("", "ref");
        private static final javax.xml.namespace.QName TIME$4 = 
            new javax.xml.namespace.QName("", "time");
        
        
        /**
         * Gets the "DataTable" element
         */
        public de.bund.bfr.pcml10.DataTableDocument.DataTable getDataTable()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.DataTableDocument.DataTable target = null;
                target = (de.bund.bfr.pcml10.DataTableDocument.DataTable)get_store().find_element_user(DATATABLE$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Sets the "DataTable" element
         */
        public void setDataTable(de.bund.bfr.pcml10.DataTableDocument.DataTable dataTable)
        {
            generatedSetterHelperImpl(dataTable, DATATABLE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "DataTable" element
         */
        public de.bund.bfr.pcml10.DataTableDocument.DataTable addNewDataTable()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.DataTableDocument.DataTable target = null;
                target = (de.bund.bfr.pcml10.DataTableDocument.DataTable)get_store().add_element_user(DATATABLE$0);
                return target;
            }
        }
        
        /**
         * Gets the "ref" attribute
         */
        public java.lang.String getRef()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(REF$2);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "ref" attribute
         */
        public org.apache.xmlbeans.XmlString xgetRef()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(REF$2);
                return target;
            }
        }
        
        /**
         * Sets the "ref" attribute
         */
        public void setRef(java.lang.String ref)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(REF$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(REF$2);
                }
                target.setStringValue(ref);
            }
        }
        
        /**
         * Sets (as xml) the "ref" attribute
         */
        public void xsetRef(org.apache.xmlbeans.XmlString ref)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(REF$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(REF$2);
                }
                target.set(ref);
            }
        }
        
        /**
         * Gets the "time" attribute
         */
        public double getTime()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TIME$4);
                if (target == null)
                {
                    return 0.0;
                }
                return target.getDoubleValue();
            }
        }
        
        /**
         * Gets (as xml) the "time" attribute
         */
        public org.apache.xmlbeans.XmlDouble xgetTime()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlDouble target = null;
                target = (org.apache.xmlbeans.XmlDouble)get_store().find_attribute_user(TIME$4);
                return target;
            }
        }
        
        /**
         * True if has "time" attribute
         */
        public boolean isSetTime()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(TIME$4) != null;
            }
        }
        
        /**
         * Sets the "time" attribute
         */
        public void setTime(double time)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TIME$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(TIME$4);
                }
                target.setDoubleValue(time);
            }
        }
        
        /**
         * Sets (as xml) the "time" attribute
         */
        public void xsetTime(org.apache.xmlbeans.XmlDouble time)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlDouble target = null;
                target = (org.apache.xmlbeans.XmlDouble)get_store().find_attribute_user(TIME$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlDouble)get_store().add_attribute_user(TIME$4);
                }
                target.set(time);
            }
        }
        
        /**
         * Unsets the "time" attribute
         */
        public void unsetTime()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(TIME$4);
            }
        }
    }
}
