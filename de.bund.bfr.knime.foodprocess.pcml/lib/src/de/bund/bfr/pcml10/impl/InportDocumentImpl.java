/*
 * An XML document type.
 * Localname: Inport
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.InportDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * A document containing one Inport(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public class InportDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.InportDocument
{
    private static final long serialVersionUID = 1L;
    
    public InportDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName INPORT$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Inport");
    
    
    /**
     * Gets the "Inport" element
     */
    public de.bund.bfr.pcml10.InportDocument.Inport getInport()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.InportDocument.Inport target = null;
            target = (de.bund.bfr.pcml10.InportDocument.Inport)get_store().find_element_user(INPORT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Inport" element
     */
    public void setInport(de.bund.bfr.pcml10.InportDocument.Inport inport)
    {
        generatedSetterHelperImpl(inport, INPORT$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "Inport" element
     */
    public de.bund.bfr.pcml10.InportDocument.Inport addNewInport()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.InportDocument.Inport target = null;
            target = (de.bund.bfr.pcml10.InportDocument.Inport)get_store().add_element_user(INPORT$0);
            return target;
        }
    }
    /**
     * An XML Inport(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public static class InportImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.InportDocument.Inport
    {
        private static final long serialVersionUID = 1L;
        
        public InportImpl(org.apache.xmlbeans.SchemaType sType)
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
    }
}
