/*
 * An XML document type.
 * Localname: Column
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.ColumnDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * A document containing one Column(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public class ColumnDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.ColumnDocument
{
    private static final long serialVersionUID = 1L;
    
    public ColumnDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName COLUMN$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Column");
    
    
    /**
     * Gets the "Column" element
     */
    public de.bund.bfr.pcml10.ColumnDocument.Column getColumn()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.ColumnDocument.Column target = null;
            target = (de.bund.bfr.pcml10.ColumnDocument.Column)get_store().find_element_user(COLUMN$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Column" element
     */
    public void setColumn(de.bund.bfr.pcml10.ColumnDocument.Column column)
    {
        generatedSetterHelperImpl(column, COLUMN$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "Column" element
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
     * An XML Column(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public static class ColumnImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.ColumnDocument.Column
    {
        private static final long serialVersionUID = 1L;
        
        public ColumnImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName MATRIX$0 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Matrix");
        private static final javax.xml.namespace.QName AGENT$2 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Agent");
        private static final javax.xml.namespace.QName COLUMNID$4 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "ColumnId");
        private static final javax.xml.namespace.QName NAME$6 = 
            new javax.xml.namespace.QName("", "name");
        
        
        /**
         * Gets the "Matrix" element
         */
        public de.bund.bfr.pcml10.NameAndDatabaseId getMatrix()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.NameAndDatabaseId target = null;
                target = (de.bund.bfr.pcml10.NameAndDatabaseId)get_store().find_element_user(MATRIX$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "Matrix" element
         */
        public boolean isSetMatrix()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(MATRIX$0) != 0;
            }
        }
        
        /**
         * Sets the "Matrix" element
         */
        public void setMatrix(de.bund.bfr.pcml10.NameAndDatabaseId matrix)
        {
            generatedSetterHelperImpl(matrix, MATRIX$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "Matrix" element
         */
        public de.bund.bfr.pcml10.NameAndDatabaseId addNewMatrix()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.NameAndDatabaseId target = null;
                target = (de.bund.bfr.pcml10.NameAndDatabaseId)get_store().add_element_user(MATRIX$0);
                return target;
            }
        }
        
        /**
         * Unsets the "Matrix" element
         */
        public void unsetMatrix()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(MATRIX$0, 0);
            }
        }
        
        /**
         * Gets the "Agent" element
         */
        public de.bund.bfr.pcml10.NameAndDatabaseId getAgent()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.NameAndDatabaseId target = null;
                target = (de.bund.bfr.pcml10.NameAndDatabaseId)get_store().find_element_user(AGENT$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "Agent" element
         */
        public boolean isSetAgent()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(AGENT$2) != 0;
            }
        }
        
        /**
         * Sets the "Agent" element
         */
        public void setAgent(de.bund.bfr.pcml10.NameAndDatabaseId agent)
        {
            generatedSetterHelperImpl(agent, AGENT$2, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "Agent" element
         */
        public de.bund.bfr.pcml10.NameAndDatabaseId addNewAgent()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.NameAndDatabaseId target = null;
                target = (de.bund.bfr.pcml10.NameAndDatabaseId)get_store().add_element_user(AGENT$2);
                return target;
            }
        }
        
        /**
         * Unsets the "Agent" element
         */
        public void unsetAgent()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(AGENT$2, 0);
            }
        }
        
        /**
         * Gets the "ColumnId" element
         */
        public de.bund.bfr.pcml10.NameAndDatabaseId getColumnId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.NameAndDatabaseId target = null;
                target = (de.bund.bfr.pcml10.NameAndDatabaseId)get_store().find_element_user(COLUMNID$4, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "ColumnId" element
         */
        public boolean isSetColumnId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(COLUMNID$4) != 0;
            }
        }
        
        /**
         * Sets the "ColumnId" element
         */
        public void setColumnId(de.bund.bfr.pcml10.NameAndDatabaseId columnId)
        {
            generatedSetterHelperImpl(columnId, COLUMNID$4, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "ColumnId" element
         */
        public de.bund.bfr.pcml10.NameAndDatabaseId addNewColumnId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.NameAndDatabaseId target = null;
                target = (de.bund.bfr.pcml10.NameAndDatabaseId)get_store().add_element_user(COLUMNID$4);
                return target;
            }
        }
        
        /**
         * Unsets the "ColumnId" element
         */
        public void unsetColumnId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(COLUMNID$4, 0);
            }
        }
        
        /**
         * Gets the "name" attribute
         */
        public java.lang.String getName()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$6);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "name" attribute
         */
        public org.apache.xmlbeans.XmlString xgetName()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$6);
                return target;
            }
        }
        
        /**
         * True if has "name" attribute
         */
        public boolean isSetName()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(NAME$6) != null;
            }
        }
        
        /**
         * Sets the "name" attribute
         */
        public void setName(java.lang.String name)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NAME$6);
                }
                target.setStringValue(name);
            }
        }
        
        /**
         * Sets (as xml) the "name" attribute
         */
        public void xsetName(org.apache.xmlbeans.XmlString name)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(NAME$6);
                }
                target.set(name);
            }
        }
        
        /**
         * Unsets the "name" attribute
         */
        public void unsetName()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(NAME$6);
            }
        }
    }
}
