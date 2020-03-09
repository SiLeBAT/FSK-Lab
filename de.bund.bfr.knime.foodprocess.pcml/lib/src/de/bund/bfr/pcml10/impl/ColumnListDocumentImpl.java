/*
 * An XML document type.
 * Localname: ColumnList
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.ColumnListDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * A document containing one ColumnList(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public class ColumnListDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.ColumnListDocument
{
    private static final long serialVersionUID = 1L;
    
    public ColumnListDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName COLUMNLIST$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "ColumnList");
    
    
    /**
     * Gets the "ColumnList" element
     */
    public de.bund.bfr.pcml10.ColumnListDocument.ColumnList getColumnList()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.ColumnListDocument.ColumnList target = null;
            target = (de.bund.bfr.pcml10.ColumnListDocument.ColumnList)get_store().find_element_user(COLUMNLIST$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "ColumnList" element
     */
    public void setColumnList(de.bund.bfr.pcml10.ColumnListDocument.ColumnList columnList)
    {
        generatedSetterHelperImpl(columnList, COLUMNLIST$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "ColumnList" element
     */
    public de.bund.bfr.pcml10.ColumnListDocument.ColumnList addNewColumnList()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.ColumnListDocument.ColumnList target = null;
            target = (de.bund.bfr.pcml10.ColumnListDocument.ColumnList)get_store().add_element_user(COLUMNLIST$0);
            return target;
        }
    }
    /**
     * An XML ColumnList(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public static class ColumnListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.ColumnListDocument.ColumnList
    {
        private static final long serialVersionUID = 1L;
        
        public ColumnListImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName COLUMN$0 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Column");
        
        
        /**
         * Gets array of all "Column" elements
         */
        public de.bund.bfr.pcml10.ColumnDocument.Column[] getColumnArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(COLUMN$0, targetList);
                de.bund.bfr.pcml10.ColumnDocument.Column[] result = new de.bund.bfr.pcml10.ColumnDocument.Column[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "Column" element
         */
        public de.bund.bfr.pcml10.ColumnDocument.Column getColumnArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.ColumnDocument.Column target = null;
                target = (de.bund.bfr.pcml10.ColumnDocument.Column)get_store().find_element_user(COLUMN$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "Column" element
         */
        public int sizeOfColumnArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(COLUMN$0);
            }
        }
        
        /**
         * Sets array of all "Column" element  WARNING: This method is not atomicaly synchronized.
         */
        public void setColumnArray(de.bund.bfr.pcml10.ColumnDocument.Column[] columnArray)
        {
            check_orphaned();
            arraySetterHelper(columnArray, COLUMN$0);
        }
        
        /**
         * Sets ith "Column" element
         */
        public void setColumnArray(int i, de.bund.bfr.pcml10.ColumnDocument.Column column)
        {
            generatedSetterHelperImpl(column, COLUMN$0, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "Column" element
         */
        public de.bund.bfr.pcml10.ColumnDocument.Column insertNewColumn(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.ColumnDocument.Column target = null;
                target = (de.bund.bfr.pcml10.ColumnDocument.Column)get_store().insert_element_user(COLUMN$0, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "Column" element
         */
        public de.bund.bfr.pcml10.ColumnDocument.Column addNewColumn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.ColumnDocument.Column target = null;
                target = (de.bund.bfr.pcml10.ColumnDocument.Column)get_store().add_element_user(COLUMN$0);
                return target;
            }
        }
        
        /**
         * Removes the ith "Column" element
         */
        public void removeColumn(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(COLUMN$0, i);
            }
        }
    }
}
