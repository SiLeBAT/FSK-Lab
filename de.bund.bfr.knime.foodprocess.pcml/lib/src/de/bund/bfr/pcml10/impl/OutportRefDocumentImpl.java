/*
 * An XML document type.
 * Localname: OutportRef
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.OutportRefDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * A document containing one OutportRef(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public class OutportRefDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.OutportRefDocument
{
    private static final long serialVersionUID = 1L;
    
    public OutportRefDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName OUTPORTREF$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "OutportRef");
    
    
    /**
     * Gets the "OutportRef" element
     */
    public de.bund.bfr.pcml10.OutportRefDocument.OutportRef getOutportRef()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.OutportRefDocument.OutportRef target = null;
            target = (de.bund.bfr.pcml10.OutportRefDocument.OutportRef)get_store().find_element_user(OUTPORTREF$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "OutportRef" element
     */
    public void setOutportRef(de.bund.bfr.pcml10.OutportRefDocument.OutportRef outportRef)
    {
        generatedSetterHelperImpl(outportRef, OUTPORTREF$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "OutportRef" element
     */
    public de.bund.bfr.pcml10.OutportRefDocument.OutportRef addNewOutportRef()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.OutportRefDocument.OutportRef target = null;
            target = (de.bund.bfr.pcml10.OutportRefDocument.OutportRef)get_store().add_element_user(OUTPORTREF$0);
            return target;
        }
    }
    /**
     * An XML OutportRef(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public static class OutportRefImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.OutportRefDocument.OutportRef
    {
        private static final long serialVersionUID = 1L;
        
        public OutportRefImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName REF$0 = 
            new javax.xml.namespace.QName("", "ref");
        private static final javax.xml.namespace.QName OUTINDEX$2 = 
            new javax.xml.namespace.QName("", "out-index");
        
        
        /**
         * Gets the "ref" attribute
         */
        public java.lang.String getRef()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(REF$0);
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
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(REF$0);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(REF$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(REF$0);
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
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(REF$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(REF$0);
                }
                target.set(ref);
            }
        }
        
        /**
         * Gets the "out-index" attribute
         */
        public int getOutIndex()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OUTINDEX$2);
                if (target == null)
                {
                    return 0;
                }
                return target.getIntValue();
            }
        }
        
        /**
         * Gets (as xml) the "out-index" attribute
         */
        public org.apache.xmlbeans.XmlInt xgetOutIndex()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInt target = null;
                target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(OUTINDEX$2);
                return target;
            }
        }
        
        /**
         * Sets the "out-index" attribute
         */
        public void setOutIndex(int outIndex)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OUTINDEX$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(OUTINDEX$2);
                }
                target.setIntValue(outIndex);
            }
        }
        
        /**
         * Sets (as xml) the "out-index" attribute
         */
        public void xsetOutIndex(org.apache.xmlbeans.XmlInt outIndex)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInt target = null;
                target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(OUTINDEX$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInt)get_store().add_attribute_user(OUTINDEX$2);
                }
                target.set(outIndex);
            }
        }
    }
}
