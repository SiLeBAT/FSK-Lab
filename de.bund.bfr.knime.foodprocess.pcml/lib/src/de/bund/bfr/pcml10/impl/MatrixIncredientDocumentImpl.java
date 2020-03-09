/*
 * An XML document type.
 * Localname: MatrixIncredient
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.MatrixIncredientDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * A document containing one MatrixIncredient(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public class MatrixIncredientDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.MatrixIncredientDocument
{
    private static final long serialVersionUID = 1L;
    
    public MatrixIncredientDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName MATRIXINCREDIENT$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "MatrixIncredient");
    
    
    /**
     * Gets the "MatrixIncredient" element
     */
    public de.bund.bfr.pcml10.MatrixIncredientDocument.MatrixIncredient getMatrixIncredient()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.MatrixIncredientDocument.MatrixIncredient target = null;
            target = (de.bund.bfr.pcml10.MatrixIncredientDocument.MatrixIncredient)get_store().find_element_user(MATRIXINCREDIENT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "MatrixIncredient" element
     */
    public void setMatrixIncredient(de.bund.bfr.pcml10.MatrixIncredientDocument.MatrixIncredient matrixIncredient)
    {
        generatedSetterHelperImpl(matrixIncredient, MATRIXINCREDIENT$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "MatrixIncredient" element
     */
    public de.bund.bfr.pcml10.MatrixIncredientDocument.MatrixIncredient addNewMatrixIncredient()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.MatrixIncredientDocument.MatrixIncredient target = null;
            target = (de.bund.bfr.pcml10.MatrixIncredientDocument.MatrixIncredient)get_store().add_element_user(MATRIXINCREDIENT$0);
            return target;
        }
    }
    /**
     * An XML MatrixIncredient(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public static class MatrixIncredientImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.MatrixIncredientDocument.MatrixIncredient
    {
        private static final long serialVersionUID = 1L;
        
        public MatrixIncredientImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName MATRIX$0 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Matrix");
        private static final javax.xml.namespace.QName FRACTION$2 = 
            new javax.xml.namespace.QName("", "fraction");
        
        
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
         * Gets the "fraction" attribute
         */
        public double getFraction()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FRACTION$2);
                if (target == null)
                {
                    return 0.0;
                }
                return target.getDoubleValue();
            }
        }
        
        /**
         * Gets (as xml) the "fraction" attribute
         */
        public org.apache.xmlbeans.XmlDouble xgetFraction()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlDouble target = null;
                target = (org.apache.xmlbeans.XmlDouble)get_store().find_attribute_user(FRACTION$2);
                return target;
            }
        }
        
        /**
         * Sets the "fraction" attribute
         */
        public void setFraction(double fraction)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FRACTION$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(FRACTION$2);
                }
                target.setDoubleValue(fraction);
            }
        }
        
        /**
         * Sets (as xml) the "fraction" attribute
         */
        public void xsetFraction(org.apache.xmlbeans.XmlDouble fraction)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlDouble target = null;
                target = (org.apache.xmlbeans.XmlDouble)get_store().find_attribute_user(FRACTION$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlDouble)get_store().add_attribute_user(FRACTION$2);
                }
                target.set(fraction);
            }
        }
    }
}
