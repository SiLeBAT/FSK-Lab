/*
 * An XML document type.
 * Localname: AgentRecipe
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.AgentRecipeDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * A document containing one AgentRecipe(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public class AgentRecipeDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.AgentRecipeDocument
{
    private static final long serialVersionUID = 1L;
    
    public AgentRecipeDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName AGENTRECIPE$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "AgentRecipe");
    
    
    /**
     * Gets the "AgentRecipe" element
     */
    public de.bund.bfr.pcml10.AgentRecipeDocument.AgentRecipe getAgentRecipe()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.AgentRecipeDocument.AgentRecipe target = null;
            target = (de.bund.bfr.pcml10.AgentRecipeDocument.AgentRecipe)get_store().find_element_user(AGENTRECIPE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "AgentRecipe" element
     */
    public void setAgentRecipe(de.bund.bfr.pcml10.AgentRecipeDocument.AgentRecipe agentRecipe)
    {
        generatedSetterHelperImpl(agentRecipe, AGENTRECIPE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "AgentRecipe" element
     */
    public de.bund.bfr.pcml10.AgentRecipeDocument.AgentRecipe addNewAgentRecipe()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.AgentRecipeDocument.AgentRecipe target = null;
            target = (de.bund.bfr.pcml10.AgentRecipeDocument.AgentRecipe)get_store().add_element_user(AGENTRECIPE$0);
            return target;
        }
    }
    /**
     * An XML AgentRecipe(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public static class AgentRecipeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.AgentRecipeDocument.AgentRecipe
    {
        private static final long serialVersionUID = 1L;
        
        public AgentRecipeImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName AGENTINCREDIENT$0 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "AgentIncredient");
        private static final javax.xml.namespace.QName EXTENSION$2 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Extension");
        
        
        /**
         * Gets array of all "AgentIncredient" elements
         */
        public de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient[] getAgentIncredientArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(AGENTINCREDIENT$0, targetList);
                de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient[] result = new de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "AgentIncredient" element
         */
        public de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient getAgentIncredientArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient target = null;
                target = (de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient)get_store().find_element_user(AGENTINCREDIENT$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "AgentIncredient" element
         */
        public int sizeOfAgentIncredientArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(AGENTINCREDIENT$0);
            }
        }
        
        /**
         * Sets array of all "AgentIncredient" element  WARNING: This method is not atomicaly synchronized.
         */
        public void setAgentIncredientArray(de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient[] agentIncredientArray)
        {
            check_orphaned();
            arraySetterHelper(agentIncredientArray, AGENTINCREDIENT$0);
        }
        
        /**
         * Sets ith "AgentIncredient" element
         */
        public void setAgentIncredientArray(int i, de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient agentIncredient)
        {
            generatedSetterHelperImpl(agentIncredient, AGENTINCREDIENT$0, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "AgentIncredient" element
         */
        public de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient insertNewAgentIncredient(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient target = null;
                target = (de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient)get_store().insert_element_user(AGENTINCREDIENT$0, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "AgentIncredient" element
         */
        public de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient addNewAgentIncredient()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient target = null;
                target = (de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient)get_store().add_element_user(AGENTINCREDIENT$0);
                return target;
            }
        }
        
        /**
         * Removes the ith "AgentIncredient" element
         */
        public void removeAgentIncredient(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(AGENTINCREDIENT$0, i);
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
