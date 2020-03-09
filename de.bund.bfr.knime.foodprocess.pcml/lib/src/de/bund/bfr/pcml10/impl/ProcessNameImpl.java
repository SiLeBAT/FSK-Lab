/*
 * XML Type:  ProcessName
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.ProcessName
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * An XML ProcessName(@http://www.bfr.bund.de/PCML-1_0).
 *
 * This is a complex type.
 */
public class ProcessNameImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.ProcessName
{
    private static final long serialVersionUID = 1L;
    
    public ProcessNameImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName EXTENSION$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Extension");
    private static final javax.xml.namespace.QName NAME$2 = 
        new javax.xml.namespace.QName("", "name");
    private static final javax.xml.namespace.QName DBID$4 = 
        new javax.xml.namespace.QName("", "db-id");
    
    
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
     * Gets the "name" attribute
     */
    public java.lang.String getName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$2);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$2);
            return target;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NAME$2);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(NAME$2);
            }
            target.set(name);
        }
    }
    
    /**
     * Gets the "db-id" attribute
     */
    public int getDbId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DBID$4);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "db-id" attribute
     */
    public org.apache.xmlbeans.XmlInt xgetDbId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(DBID$4);
            return target;
        }
    }
    
    /**
     * True if has "db-id" attribute
     */
    public boolean isSetDbId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(DBID$4) != null;
        }
    }
    
    /**
     * Sets the "db-id" attribute
     */
    public void setDbId(int dbId)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DBID$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(DBID$4);
            }
            target.setIntValue(dbId);
        }
    }
    
    /**
     * Sets (as xml) the "db-id" attribute
     */
    public void xsetDbId(org.apache.xmlbeans.XmlInt dbId)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(DBID$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_attribute_user(DBID$4);
            }
            target.set(dbId);
        }
    }
    
    /**
     * Unsets the "db-id" attribute
     */
    public void unsetDbId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(DBID$4);
        }
    }
}
