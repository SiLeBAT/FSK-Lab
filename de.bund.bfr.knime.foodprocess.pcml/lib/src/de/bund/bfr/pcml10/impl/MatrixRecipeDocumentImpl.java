/*
 * An XML document type.
 * Localname: MatrixRecipe
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.MatrixRecipeDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * A document containing one MatrixRecipe(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public class MatrixRecipeDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.MatrixRecipeDocument
{
    private static final long serialVersionUID = 1L;
    
    public MatrixRecipeDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName MATRIXRECIPE$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "MatrixRecipe");
    
    
    /**
     * Gets the "MatrixRecipe" element
     */
    public de.bund.bfr.pcml10.MatrixRecipeDocument.MatrixRecipe getMatrixRecipe()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.MatrixRecipeDocument.MatrixRecipe target = null;
            target = (de.bund.bfr.pcml10.MatrixRecipeDocument.MatrixRecipe)get_store().find_element_user(MATRIXRECIPE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "MatrixRecipe" element
     */
    public void setMatrixRecipe(de.bund.bfr.pcml10.MatrixRecipeDocument.MatrixRecipe matrixRecipe)
    {
        generatedSetterHelperImpl(matrixRecipe, MATRIXRECIPE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "MatrixRecipe" element
     */
    public de.bund.bfr.pcml10.MatrixRecipeDocument.MatrixRecipe addNewMatrixRecipe()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.MatrixRecipeDocument.MatrixRecipe target = null;
            target = (de.bund.bfr.pcml10.MatrixRecipeDocument.MatrixRecipe)get_store().add_element_user(MATRIXRECIPE$0);
            return target;
        }
    }
    /**
     * An XML MatrixRecipe(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public static class MatrixRecipeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.MatrixRecipeDocument.MatrixRecipe
    {
        private static final long serialVersionUID = 1L;
        
        public MatrixRecipeImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName MATRIXINCREDIENT$0 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "MatrixIncredient");
        private static final javax.xml.namespace.QName EXTENSION$2 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Extension");
        
        
        /**
         * Gets array of all "MatrixIncredient" elements
         */
        public de.bund.bfr.pcml10.MatrixIncredientDocument.MatrixIncredient[] getMatrixIncredientArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(MATRIXINCREDIENT$0, targetList);
                de.bund.bfr.pcml10.MatrixIncredientDocument.MatrixIncredient[] result = new de.bund.bfr.pcml10.MatrixIncredientDocument.MatrixIncredient[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "MatrixIncredient" element
         */
        public de.bund.bfr.pcml10.MatrixIncredientDocument.MatrixIncredient getMatrixIncredientArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.MatrixIncredientDocument.MatrixIncredient target = null;
                target = (de.bund.bfr.pcml10.MatrixIncredientDocument.MatrixIncredient)get_store().find_element_user(MATRIXINCREDIENT$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "MatrixIncredient" element
         */
        public int sizeOfMatrixIncredientArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(MATRIXINCREDIENT$0);
            }
        }
        
        /**
         * Sets array of all "MatrixIncredient" element  WARNING: This method is not atomicaly synchronized.
         */
        public void setMatrixIncredientArray(de.bund.bfr.pcml10.MatrixIncredientDocument.MatrixIncredient[] matrixIncredientArray)
        {
            check_orphaned();
            arraySetterHelper(matrixIncredientArray, MATRIXINCREDIENT$0);
        }
        
        /**
         * Sets ith "MatrixIncredient" element
         */
        public void setMatrixIncredientArray(int i, de.bund.bfr.pcml10.MatrixIncredientDocument.MatrixIncredient matrixIncredient)
        {
            generatedSetterHelperImpl(matrixIncredient, MATRIXINCREDIENT$0, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "MatrixIncredient" element
         */
        public de.bund.bfr.pcml10.MatrixIncredientDocument.MatrixIncredient insertNewMatrixIncredient(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.MatrixIncredientDocument.MatrixIncredient target = null;
                target = (de.bund.bfr.pcml10.MatrixIncredientDocument.MatrixIncredient)get_store().insert_element_user(MATRIXINCREDIENT$0, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "MatrixIncredient" element
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
         * Removes the ith "MatrixIncredient" element
         */
        public void removeMatrixIncredient(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(MATRIXINCREDIENT$0, i);
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
         * Sets array of all "Extension" element  WARNING: This method is not atomicaly synchronized.
         */
        public void setExtensionArray(de.bund.bfr.pcml10.ExtensionDocument.Extension[] extensionArray)
        {
            check_orphaned();
            arraySetterHelper(extensionArray, EXTENSION$2);
        }
        
        /**
         * Sets ith "Extension" element
         */
        public void setExtensionArray(int i, de.bund.bfr.pcml10.ExtensionDocument.Extension extension)
        {
            generatedSetterHelperImpl(extension, EXTENSION$2, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
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
