/*
 * An XML document type.
 * Localname: InlineTable
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.InlineTableDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * A document containing one InlineTable(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public class InlineTableDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.InlineTableDocument
{
    private static final long serialVersionUID = 1L;
    
    public InlineTableDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName INLINETABLE$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "InlineTable");
    
    
    /**
     * Gets the "InlineTable" element
     */
    public de.bund.bfr.pcml10.InlineTableDocument.InlineTable getInlineTable()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.InlineTableDocument.InlineTable target = null;
            target = (de.bund.bfr.pcml10.InlineTableDocument.InlineTable)get_store().find_element_user(INLINETABLE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "InlineTable" element
     */
    public void setInlineTable(de.bund.bfr.pcml10.InlineTableDocument.InlineTable inlineTable)
    {
        generatedSetterHelperImpl(inlineTable, INLINETABLE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "InlineTable" element
     */
    public de.bund.bfr.pcml10.InlineTableDocument.InlineTable addNewInlineTable()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.InlineTableDocument.InlineTable target = null;
            target = (de.bund.bfr.pcml10.InlineTableDocument.InlineTable)get_store().add_element_user(INLINETABLE$0);
            return target;
        }
    }
    /**
     * An XML InlineTable(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public static class InlineTableImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.InlineTableDocument.InlineTable
    {
        private static final long serialVersionUID = 1L;
        
        public InlineTableImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName EXTENSION$0 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Extension");
        private static final javax.xml.namespace.QName ROW$2 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "row");
        
        
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
        
        /**
         * Gets array of all "row" elements
         */
        public de.bund.bfr.pcml10.RowDocument.Row[] getRowArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(ROW$2, targetList);
                de.bund.bfr.pcml10.RowDocument.Row[] result = new de.bund.bfr.pcml10.RowDocument.Row[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "row" element
         */
        public de.bund.bfr.pcml10.RowDocument.Row getRowArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.RowDocument.Row target = null;
                target = (de.bund.bfr.pcml10.RowDocument.Row)get_store().find_element_user(ROW$2, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "row" element
         */
        public int sizeOfRowArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(ROW$2);
            }
        }
        
        /**
         * Sets array of all "row" element  WARNING: This method is not atomicaly synchronized.
         */
        public void setRowArray(de.bund.bfr.pcml10.RowDocument.Row[] rowArray)
        {
            check_orphaned();
            arraySetterHelper(rowArray, ROW$2);
        }
        
        /**
         * Sets ith "row" element
         */
        public void setRowArray(int i, de.bund.bfr.pcml10.RowDocument.Row row)
        {
            generatedSetterHelperImpl(row, ROW$2, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "row" element
         */
        public de.bund.bfr.pcml10.RowDocument.Row insertNewRow(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.RowDocument.Row target = null;
                target = (de.bund.bfr.pcml10.RowDocument.Row)get_store().insert_element_user(ROW$2, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "row" element
         */
        public de.bund.bfr.pcml10.RowDocument.Row addNewRow()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.RowDocument.Row target = null;
                target = (de.bund.bfr.pcml10.RowDocument.Row)get_store().add_element_user(ROW$2);
                return target;
            }
        }
        
        /**
         * Removes the ith "row" element
         */
        public void removeRow(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(ROW$2, i);
            }
        }
    }
}
