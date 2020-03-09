/*
 * An XML document type.
 * Localname: row
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.RowDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * A document containing one row(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public class RowDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.RowDocument
{
    private static final long serialVersionUID = 1L;
    
    public RowDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ROW$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "row");
    
    
    /**
     * Gets the "row" element
     */
    public de.bund.bfr.pcml10.RowDocument.Row getRow()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.RowDocument.Row target = null;
            target = (de.bund.bfr.pcml10.RowDocument.Row)get_store().find_element_user(ROW$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "row" element
     */
    public void setRow(de.bund.bfr.pcml10.RowDocument.Row row)
    {
        generatedSetterHelperImpl(row, ROW$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "row" element
     */
    public de.bund.bfr.pcml10.RowDocument.Row addNewRow()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.RowDocument.Row target = null;
            target = (de.bund.bfr.pcml10.RowDocument.Row)get_store().add_element_user(ROW$0);
            return target;
        }
    }
    /**
     * An XML row(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public static class RowImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.RowDocument.Row
    {
        private static final long serialVersionUID = 1L;
        
        public RowImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        
    }
}
