/*
 * An XML document type.
 * Localname: DataTable
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.DataTableDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * A document containing one DataTable(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public class DataTableDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.DataTableDocument
{
    private static final long serialVersionUID = 1L;
    
    public DataTableDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DATATABLE$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "DataTable");
    
    
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
     * An XML DataTable(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public static class DataTableImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.DataTableDocument.DataTable
    {
        private static final long serialVersionUID = 1L;
        
        public DataTableImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName EXTENSION$0 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Extension");
        private static final javax.xml.namespace.QName COLUMNLIST$2 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "ColumnList");
        private static final javax.xml.namespace.QName TABLELOCATOR$4 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "TableLocator");
        private static final javax.xml.namespace.QName INLINETABLE$6 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "InlineTable");
        private static final javax.xml.namespace.QName NAME$8 = 
            new javax.xml.namespace.QName("", "name");
        
        
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
         * Gets the "ColumnList" element
         */
        public de.bund.bfr.pcml10.ColumnListDocument.ColumnList getColumnList()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.ColumnListDocument.ColumnList target = null;
                target = (de.bund.bfr.pcml10.ColumnListDocument.ColumnList)get_store().find_element_user(COLUMNLIST$2, 0);
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
            generatedSetterHelperImpl(columnList, COLUMNLIST$2, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
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
                target = (de.bund.bfr.pcml10.ColumnListDocument.ColumnList)get_store().add_element_user(COLUMNLIST$2);
                return target;
            }
        }
        
        /**
         * Gets the "TableLocator" element
         */
        public de.bund.bfr.pcml10.TableLocatorDocument.TableLocator getTableLocator()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.TableLocatorDocument.TableLocator target = null;
                target = (de.bund.bfr.pcml10.TableLocatorDocument.TableLocator)get_store().find_element_user(TABLELOCATOR$4, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "TableLocator" element
         */
        public boolean isSetTableLocator()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(TABLELOCATOR$4) != 0;
            }
        }
        
        /**
         * Sets the "TableLocator" element
         */
        public void setTableLocator(de.bund.bfr.pcml10.TableLocatorDocument.TableLocator tableLocator)
        {
            generatedSetterHelperImpl(tableLocator, TABLELOCATOR$4, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "TableLocator" element
         */
        public de.bund.bfr.pcml10.TableLocatorDocument.TableLocator addNewTableLocator()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.TableLocatorDocument.TableLocator target = null;
                target = (de.bund.bfr.pcml10.TableLocatorDocument.TableLocator)get_store().add_element_user(TABLELOCATOR$4);
                return target;
            }
        }
        
        /**
         * Unsets the "TableLocator" element
         */
        public void unsetTableLocator()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(TABLELOCATOR$4, 0);
            }
        }
        
        /**
         * Gets the "InlineTable" element
         */
        public de.bund.bfr.pcml10.InlineTableDocument.InlineTable getInlineTable()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.InlineTableDocument.InlineTable target = null;
                target = (de.bund.bfr.pcml10.InlineTableDocument.InlineTable)get_store().find_element_user(INLINETABLE$6, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "InlineTable" element
         */
        public boolean isSetInlineTable()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(INLINETABLE$6) != 0;
            }
        }
        
        /**
         * Sets the "InlineTable" element
         */
        public void setInlineTable(de.bund.bfr.pcml10.InlineTableDocument.InlineTable inlineTable)
        {
            generatedSetterHelperImpl(inlineTable, INLINETABLE$6, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
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
                target = (de.bund.bfr.pcml10.InlineTableDocument.InlineTable)get_store().add_element_user(INLINETABLE$6);
                return target;
            }
        }
        
        /**
         * Unsets the "InlineTable" element
         */
        public void unsetInlineTable()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(INLINETABLE$6, 0);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$8);
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
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$8);
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
                return get_store().find_attribute_user(NAME$8) != null;
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NAME$8);
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
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(NAME$8);
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
                get_store().remove_attribute(NAME$8);
            }
        }
    }
}
