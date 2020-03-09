/*
 * An XML document type.
 * Localname: ChildParent
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.ChildParentDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * A document containing one ChildParent(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public class ChildParentDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.ChildParentDocument
{
    private static final long serialVersionUID = 1L;
    
    public ChildParentDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CHILDPARENT$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "ChildParent");
    
    
    /**
     * Gets the "ChildParent" element
     */
    public de.bund.bfr.pcml10.ChildParentDocument.ChildParent getChildParent()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.ChildParentDocument.ChildParent target = null;
            target = (de.bund.bfr.pcml10.ChildParentDocument.ChildParent)get_store().find_element_user(CHILDPARENT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "ChildParent" element
     */
    public void setChildParent(de.bund.bfr.pcml10.ChildParentDocument.ChildParent childParent)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.ChildParentDocument.ChildParent target = null;
            target = (de.bund.bfr.pcml10.ChildParentDocument.ChildParent)get_store().find_element_user(CHILDPARENT$0, 0);
            if (target == null)
            {
                target = (de.bund.bfr.pcml10.ChildParentDocument.ChildParent)get_store().add_element_user(CHILDPARENT$0);
            }
            target.set(childParent);
        }
    }
    
    /**
     * Appends and returns a new empty "ChildParent" element
     */
    public de.bund.bfr.pcml10.ChildParentDocument.ChildParent addNewChildParent()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.ChildParentDocument.ChildParent target = null;
            target = (de.bund.bfr.pcml10.ChildParentDocument.ChildParent)get_store().add_element_user(CHILDPARENT$0);
            return target;
        }
    }
    /**
     * An XML ChildParent(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public static class ChildParentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.ChildParentDocument.ChildParent
    {
        private static final long serialVersionUID = 1L;
        
        public ChildParentImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName EXTENSION$0 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Extension");
        private static final javax.xml.namespace.QName TABLELOCATOR$2 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "TableLocator");
        private static final javax.xml.namespace.QName INLINETABLE$4 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "InlineTable");
        private static final javax.xml.namespace.QName CHILDFIELD$6 = 
            new javax.xml.namespace.QName("", "childField");
        private static final javax.xml.namespace.QName PARENTFIELD$8 = 
            new javax.xml.namespace.QName("", "parentField");
        private static final javax.xml.namespace.QName PARENTLEVELFIELD$10 = 
            new javax.xml.namespace.QName("", "parentLevelField");
        private static final javax.xml.namespace.QName ISRECURSIVE$12 = 
            new javax.xml.namespace.QName("", "isRecursive");
        
        
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
         * Sets array of all "Extension" element
         */
        public void setExtensionArray(de.bund.bfr.pcml10.ExtensionDocument.Extension[] extensionArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(extensionArray, EXTENSION$0);
            }
        }
        
        /**
         * Sets ith "Extension" element
         */
        public void setExtensionArray(int i, de.bund.bfr.pcml10.ExtensionDocument.Extension extension)
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
                target.set(extension);
            }
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
         * Gets the "TableLocator" element
         */
        public de.bund.bfr.pcml10.TableLocatorDocument.TableLocator getTableLocator()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.TableLocatorDocument.TableLocator target = null;
                target = (de.bund.bfr.pcml10.TableLocatorDocument.TableLocator)get_store().find_element_user(TABLELOCATOR$2, 0);
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
                return get_store().count_elements(TABLELOCATOR$2) != 0;
            }
        }
        
        /**
         * Sets the "TableLocator" element
         */
        public void setTableLocator(de.bund.bfr.pcml10.TableLocatorDocument.TableLocator tableLocator)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.TableLocatorDocument.TableLocator target = null;
                target = (de.bund.bfr.pcml10.TableLocatorDocument.TableLocator)get_store().find_element_user(TABLELOCATOR$2, 0);
                if (target == null)
                {
                    target = (de.bund.bfr.pcml10.TableLocatorDocument.TableLocator)get_store().add_element_user(TABLELOCATOR$2);
                }
                target.set(tableLocator);
            }
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
                target = (de.bund.bfr.pcml10.TableLocatorDocument.TableLocator)get_store().add_element_user(TABLELOCATOR$2);
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
                get_store().remove_element(TABLELOCATOR$2, 0);
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
                target = (de.bund.bfr.pcml10.InlineTableDocument.InlineTable)get_store().find_element_user(INLINETABLE$4, 0);
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
                return get_store().count_elements(INLINETABLE$4) != 0;
            }
        }
        
        /**
         * Sets the "InlineTable" element
         */
        public void setInlineTable(de.bund.bfr.pcml10.InlineTableDocument.InlineTable inlineTable)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.InlineTableDocument.InlineTable target = null;
                target = (de.bund.bfr.pcml10.InlineTableDocument.InlineTable)get_store().find_element_user(INLINETABLE$4, 0);
                if (target == null)
                {
                    target = (de.bund.bfr.pcml10.InlineTableDocument.InlineTable)get_store().add_element_user(INLINETABLE$4);
                }
                target.set(inlineTable);
            }
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
                target = (de.bund.bfr.pcml10.InlineTableDocument.InlineTable)get_store().add_element_user(INLINETABLE$4);
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
                get_store().remove_element(INLINETABLE$4, 0);
            }
        }
        
        /**
         * Gets the "childField" attribute
         */
        public java.lang.String getChildField()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CHILDFIELD$6);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "childField" attribute
         */
        public org.apache.xmlbeans.XmlString xgetChildField()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(CHILDFIELD$6);
                return target;
            }
        }
        
        /**
         * Sets the "childField" attribute
         */
        public void setChildField(java.lang.String childField)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CHILDFIELD$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(CHILDFIELD$6);
                }
                target.setStringValue(childField);
            }
        }
        
        /**
         * Sets (as xml) the "childField" attribute
         */
        public void xsetChildField(org.apache.xmlbeans.XmlString childField)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(CHILDFIELD$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(CHILDFIELD$6);
                }
                target.set(childField);
            }
        }
        
        /**
         * Gets the "parentField" attribute
         */
        public java.lang.String getParentField()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PARENTFIELD$8);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "parentField" attribute
         */
        public org.apache.xmlbeans.XmlString xgetParentField()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PARENTFIELD$8);
                return target;
            }
        }
        
        /**
         * Sets the "parentField" attribute
         */
        public void setParentField(java.lang.String parentField)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PARENTFIELD$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PARENTFIELD$8);
                }
                target.setStringValue(parentField);
            }
        }
        
        /**
         * Sets (as xml) the "parentField" attribute
         */
        public void xsetParentField(org.apache.xmlbeans.XmlString parentField)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PARENTFIELD$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PARENTFIELD$8);
                }
                target.set(parentField);
            }
        }
        
        /**
         * Gets the "parentLevelField" attribute
         */
        public java.lang.String getParentLevelField()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PARENTLEVELFIELD$10);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "parentLevelField" attribute
         */
        public org.apache.xmlbeans.XmlString xgetParentLevelField()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PARENTLEVELFIELD$10);
                return target;
            }
        }
        
        /**
         * True if has "parentLevelField" attribute
         */
        public boolean isSetParentLevelField()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(PARENTLEVELFIELD$10) != null;
            }
        }
        
        /**
         * Sets the "parentLevelField" attribute
         */
        public void setParentLevelField(java.lang.String parentLevelField)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PARENTLEVELFIELD$10);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PARENTLEVELFIELD$10);
                }
                target.setStringValue(parentLevelField);
            }
        }
        
        /**
         * Sets (as xml) the "parentLevelField" attribute
         */
        public void xsetParentLevelField(org.apache.xmlbeans.XmlString parentLevelField)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PARENTLEVELFIELD$10);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PARENTLEVELFIELD$10);
                }
                target.set(parentLevelField);
            }
        }
        
        /**
         * Unsets the "parentLevelField" attribute
         */
        public void unsetParentLevelField()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(PARENTLEVELFIELD$10);
            }
        }
        
        /**
         * Gets the "isRecursive" attribute
         */
        public de.bund.bfr.pcml10.ChildParentDocument.ChildParent.IsRecursive.Enum getIsRecursive()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ISRECURSIVE$12);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(ISRECURSIVE$12);
                }
                if (target == null)
                {
                    return null;
                }
                return (de.bund.bfr.pcml10.ChildParentDocument.ChildParent.IsRecursive.Enum)target.getEnumValue();
            }
        }
        
        /**
         * Gets (as xml) the "isRecursive" attribute
         */
        public de.bund.bfr.pcml10.ChildParentDocument.ChildParent.IsRecursive xgetIsRecursive()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.ChildParentDocument.ChildParent.IsRecursive target = null;
                target = (de.bund.bfr.pcml10.ChildParentDocument.ChildParent.IsRecursive)get_store().find_attribute_user(ISRECURSIVE$12);
                if (target == null)
                {
                    target = (de.bund.bfr.pcml10.ChildParentDocument.ChildParent.IsRecursive)get_default_attribute_value(ISRECURSIVE$12);
                }
                return target;
            }
        }
        
        /**
         * True if has "isRecursive" attribute
         */
        public boolean isSetIsRecursive()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(ISRECURSIVE$12) != null;
            }
        }
        
        /**
         * Sets the "isRecursive" attribute
         */
        public void setIsRecursive(de.bund.bfr.pcml10.ChildParentDocument.ChildParent.IsRecursive.Enum isRecursive)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ISRECURSIVE$12);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ISRECURSIVE$12);
                }
                target.setEnumValue(isRecursive);
            }
        }
        
        /**
         * Sets (as xml) the "isRecursive" attribute
         */
        public void xsetIsRecursive(de.bund.bfr.pcml10.ChildParentDocument.ChildParent.IsRecursive isRecursive)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.ChildParentDocument.ChildParent.IsRecursive target = null;
                target = (de.bund.bfr.pcml10.ChildParentDocument.ChildParent.IsRecursive)get_store().find_attribute_user(ISRECURSIVE$12);
                if (target == null)
                {
                    target = (de.bund.bfr.pcml10.ChildParentDocument.ChildParent.IsRecursive)get_store().add_attribute_user(ISRECURSIVE$12);
                }
                target.set(isRecursive);
            }
        }
        
        /**
         * Unsets the "isRecursive" attribute
         */
        public void unsetIsRecursive()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(ISRECURSIVE$12);
            }
        }
        /**
         * An XML isRecursive(@).
         *
         * This is an atomic type that is a restriction of de.bund.bfr.pcml10.ChildParentDocument$ChildParent$IsRecursive.
         */
        public static class IsRecursiveImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements de.bund.bfr.pcml10.ChildParentDocument.ChildParent.IsRecursive
        {
            private static final long serialVersionUID = 1L;
            
            public IsRecursiveImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType, false);
            }
            
            protected IsRecursiveImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
            {
                super(sType, b);
            }
        }
    }
}
