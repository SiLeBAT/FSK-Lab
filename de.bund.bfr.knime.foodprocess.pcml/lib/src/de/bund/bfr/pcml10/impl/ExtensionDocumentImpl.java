/*
 * An XML document type.
 * Localname: Extension
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.ExtensionDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * A document containing one Extension(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public class ExtensionDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.ExtensionDocument
{
    private static final long serialVersionUID = 1L;
    
    public ExtensionDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName EXTENSION$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Extension");
    
    
    /**
     * Gets the "Extension" element
     */
    public de.bund.bfr.pcml10.ExtensionDocument.Extension getExtension()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.ExtensionDocument.Extension target = null;
            target = (de.bund.bfr.pcml10.ExtensionDocument.Extension)get_store().find_element_user(EXTENSION$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Extension" element
     */
    public void setExtension(de.bund.bfr.pcml10.ExtensionDocument.Extension extension)
    {
        generatedSetterHelperImpl(extension, EXTENSION$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "Extension" element
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
     * An XML Extension(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public static class ExtensionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.ExtensionDocument.Extension
    {
        private static final long serialVersionUID = 1L;
        
        public ExtensionImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName EXTENDER$0 = 
            new javax.xml.namespace.QName("", "extender");
        private static final javax.xml.namespace.QName NAME$2 = 
            new javax.xml.namespace.QName("", "name");
        private static final javax.xml.namespace.QName VALUE$4 = 
            new javax.xml.namespace.QName("", "value");
        
        
        /**
         * Gets the "extender" attribute
         */
        public java.lang.String getExtender()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(EXTENDER$0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "extender" attribute
         */
        public org.apache.xmlbeans.XmlString xgetExtender()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(EXTENDER$0);
                return target;
            }
        }
        
        /**
         * True if has "extender" attribute
         */
        public boolean isSetExtender()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(EXTENDER$0) != null;
            }
        }
        
        /**
         * Sets the "extender" attribute
         */
        public void setExtender(java.lang.String extender)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(EXTENDER$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(EXTENDER$0);
                }
                target.setStringValue(extender);
            }
        }
        
        /**
         * Sets (as xml) the "extender" attribute
         */
        public void xsetExtender(org.apache.xmlbeans.XmlString extender)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(EXTENDER$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(EXTENDER$0);
                }
                target.set(extender);
            }
        }
        
        /**
         * Unsets the "extender" attribute
         */
        public void unsetExtender()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(EXTENDER$0);
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
         * True if has "name" attribute
         */
        public boolean isSetName()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(NAME$2) != null;
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
         * Unsets the "name" attribute
         */
        public void unsetName()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(NAME$2);
            }
        }
        
        /**
         * Gets the "value" attribute
         */
        public java.lang.String getValue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VALUE$4);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "value" attribute
         */
        public org.apache.xmlbeans.XmlString xgetValue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(VALUE$4);
                return target;
            }
        }
        
        /**
         * True if has "value" attribute
         */
        public boolean isSetValue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(VALUE$4) != null;
            }
        }
        
        /**
         * Sets the "value" attribute
         */
        public void setValue(java.lang.String value)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VALUE$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(VALUE$4);
                }
                target.setStringValue(value);
            }
        }
        
        /**
         * Sets (as xml) the "value" attribute
         */
        public void xsetValue(org.apache.xmlbeans.XmlString value)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(VALUE$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(VALUE$4);
                }
                target.set(value);
            }
        }
        
        /**
         * Unsets the "value" attribute
         */
        public void unsetValue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(VALUE$4);
            }
        }
    }
}
