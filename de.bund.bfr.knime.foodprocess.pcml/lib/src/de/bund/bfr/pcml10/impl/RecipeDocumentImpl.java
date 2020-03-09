/*
 * An XML document type.
 * Localname: Recipe
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.RecipeDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * A document containing one Recipe(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public class RecipeDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.RecipeDocument
{
    private static final long serialVersionUID = 1L;
    
    public RecipeDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RECIPE$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Recipe");
    
    
    /**
     * Gets the "Recipe" element
     */
    public de.bund.bfr.pcml10.RecipeDocument.Recipe getRecipe()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.RecipeDocument.Recipe target = null;
            target = (de.bund.bfr.pcml10.RecipeDocument.Recipe)get_store().find_element_user(RECIPE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Recipe" element
     */
    public void setRecipe(de.bund.bfr.pcml10.RecipeDocument.Recipe recipe)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.RecipeDocument.Recipe target = null;
            target = (de.bund.bfr.pcml10.RecipeDocument.Recipe)get_store().find_element_user(RECIPE$0, 0);
            if (target == null)
            {
                target = (de.bund.bfr.pcml10.RecipeDocument.Recipe)get_store().add_element_user(RECIPE$0);
            }
            target.set(recipe);
        }
    }
    
    /**
     * Appends and returns a new empty "Recipe" element
     */
    public de.bund.bfr.pcml10.RecipeDocument.Recipe addNewRecipe()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.RecipeDocument.Recipe target = null;
            target = (de.bund.bfr.pcml10.RecipeDocument.Recipe)get_store().add_element_user(RECIPE$0);
            return target;
        }
    }
    /**
     * An XML Recipe(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public static class RecipeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.RecipeDocument.Recipe
    {
        private static final long serialVersionUID = 1L;
        
        public RecipeImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName MATRIX$0 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Matrix");
        private static final javax.xml.namespace.QName EXTENSION$2 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Extension");
        
        
        /**
         * Gets array of all "Matrix" elements
         */
        public de.bund.bfr.pcml10.MatrixDocument.Matrix[] getMatrixArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(MATRIX$0, targetList);
                de.bund.bfr.pcml10.MatrixDocument.Matrix[] result = new de.bund.bfr.pcml10.MatrixDocument.Matrix[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "Matrix" element
         */
        public de.bund.bfr.pcml10.MatrixDocument.Matrix getMatrixArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.MatrixDocument.Matrix target = null;
                target = (de.bund.bfr.pcml10.MatrixDocument.Matrix)get_store().find_element_user(MATRIX$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "Matrix" element
         */
        public int sizeOfMatrixArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(MATRIX$0);
            }
        }
        
        /**
         * Sets array of all "Matrix" element
         */
        public void setMatrixArray(de.bund.bfr.pcml10.MatrixDocument.Matrix[] matrixArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(matrixArray, MATRIX$0);
            }
        }
        
        /**
         * Sets ith "Matrix" element
         */
        public void setMatrixArray(int i, de.bund.bfr.pcml10.MatrixDocument.Matrix matrix)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.MatrixDocument.Matrix target = null;
                target = (de.bund.bfr.pcml10.MatrixDocument.Matrix)get_store().find_element_user(MATRIX$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(matrix);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "Matrix" element
         */
        public de.bund.bfr.pcml10.MatrixDocument.Matrix insertNewMatrix(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.MatrixDocument.Matrix target = null;
                target = (de.bund.bfr.pcml10.MatrixDocument.Matrix)get_store().insert_element_user(MATRIX$0, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "Matrix" element
         */
        public de.bund.bfr.pcml10.MatrixDocument.Matrix addNewMatrix()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.MatrixDocument.Matrix target = null;
                target = (de.bund.bfr.pcml10.MatrixDocument.Matrix)get_store().add_element_user(MATRIX$0);
                return target;
            }
        }
        
        /**
         * Removes the ith "Matrix" element
         */
        public void removeMatrix(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(MATRIX$0, i);
            }
        }
        
        /**
         * Gets array of all "Extension" elements
         */
        public de.bund.bfr.pcml10.ExtensionDocument.Extension[] getExtensionArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(EXTENSION$2, targetList);
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
                target = (de.bund.bfr.pcml10.ExtensionDocument.Extension)get_store().find_element_user(EXTENSION$2, i);
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
                return get_store().count_elements(EXTENSION$2);
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
                arraySetterHelper(extensionArray, EXTENSION$2);
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
                target = (de.bund.bfr.pcml10.ExtensionDocument.Extension)get_store().find_element_user(EXTENSION$2, i);
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
                target = (de.bund.bfr.pcml10.ExtensionDocument.Extension)get_store().insert_element_user(EXTENSION$2, i);
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
                target = (de.bund.bfr.pcml10.ExtensionDocument.Extension)get_store().add_element_user(EXTENSION$2);
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
                get_store().remove_element(EXTENSION$2, i);
            }
        }
    }
}
