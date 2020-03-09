/*
 * An XML document type.
 * Localname: Outport
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.OutportDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * A document containing one Outport(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public class OutportDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.OutportDocument
{
    private static final long serialVersionUID = 1L;
    
    public OutportDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName OUTPORT$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Outport");
    
    
    /**
     * Gets the "Outport" element
     */
    public de.bund.bfr.pcml10.OutportDocument.Outport getOutport()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.OutportDocument.Outport target = null;
            target = (de.bund.bfr.pcml10.OutportDocument.Outport)get_store().find_element_user(OUTPORT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Outport" element
     */
    public void setOutport(de.bund.bfr.pcml10.OutportDocument.Outport outport)
    {
        generatedSetterHelperImpl(outport, OUTPORT$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "Outport" element
     */
    public de.bund.bfr.pcml10.OutportDocument.Outport addNewOutport()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.OutportDocument.Outport target = null;
            target = (de.bund.bfr.pcml10.OutportDocument.Outport)get_store().add_element_user(OUTPORT$0);
            return target;
        }
    }
    /**
     * An XML Outport(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public static class OutportImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.OutportDocument.Outport
    {
        private static final long serialVersionUID = 1L;
        
        public OutportImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName MATRIX$0 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Matrix");
        private static final javax.xml.namespace.QName MATRIXRECIPE$2 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "MatrixRecipe");
        private static final javax.xml.namespace.QName AGENTRECIPE$4 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "AgentRecipe");
        private static final javax.xml.namespace.QName EXTENSION$6 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Extension");
        private static final javax.xml.namespace.QName VOLUME$8 = 
            new javax.xml.namespace.QName("", "volume");
        private static final javax.xml.namespace.QName TEMPERATURE$10 = 
            new javax.xml.namespace.QName("", "temperature");
        private static final javax.xml.namespace.QName PH$12 = 
            new javax.xml.namespace.QName("", "pH");
        private static final javax.xml.namespace.QName AW$14 = 
            new javax.xml.namespace.QName("", "aw");
        private static final javax.xml.namespace.QName PRESSURE$16 = 
            new javax.xml.namespace.QName("", "pressure");
        private static final javax.xml.namespace.QName FLOWSPEED$18 = 
            new javax.xml.namespace.QName("", "flowSpeed");
        
        
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
         * True if has "Matrix" element
         */
        public boolean isSetMatrix()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(MATRIX$0) != 0;
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
         * Unsets the "Matrix" element
         */
        public void unsetMatrix()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(MATRIX$0, 0);
            }
        }
        
        /**
         * Gets the "MatrixRecipe" element
         */
        public de.bund.bfr.pcml10.MatrixRecipeDocument.MatrixRecipe getMatrixRecipe()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.MatrixRecipeDocument.MatrixRecipe target = null;
                target = (de.bund.bfr.pcml10.MatrixRecipeDocument.MatrixRecipe)get_store().find_element_user(MATRIXRECIPE$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "MatrixRecipe" element
         */
        public boolean isSetMatrixRecipe()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(MATRIXRECIPE$2) != 0;
            }
        }
        
        /**
         * Sets the "MatrixRecipe" element
         */
        public void setMatrixRecipe(de.bund.bfr.pcml10.MatrixRecipeDocument.MatrixRecipe matrixRecipe)
        {
            generatedSetterHelperImpl(matrixRecipe, MATRIXRECIPE$2, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
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
                target = (de.bund.bfr.pcml10.MatrixRecipeDocument.MatrixRecipe)get_store().add_element_user(MATRIXRECIPE$2);
                return target;
            }
        }
        
        /**
         * Unsets the "MatrixRecipe" element
         */
        public void unsetMatrixRecipe()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(MATRIXRECIPE$2, 0);
            }
        }
        
        /**
         * Gets the "AgentRecipe" element
         */
        public de.bund.bfr.pcml10.AgentRecipeDocument.AgentRecipe getAgentRecipe()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.AgentRecipeDocument.AgentRecipe target = null;
                target = (de.bund.bfr.pcml10.AgentRecipeDocument.AgentRecipe)get_store().find_element_user(AGENTRECIPE$4, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "AgentRecipe" element
         */
        public boolean isSetAgentRecipe()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(AGENTRECIPE$4) != 0;
            }
        }
        
        /**
         * Sets the "AgentRecipe" element
         */
        public void setAgentRecipe(de.bund.bfr.pcml10.AgentRecipeDocument.AgentRecipe agentRecipe)
        {
            generatedSetterHelperImpl(agentRecipe, AGENTRECIPE$4, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
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
                target = (de.bund.bfr.pcml10.AgentRecipeDocument.AgentRecipe)get_store().add_element_user(AGENTRECIPE$4);
                return target;
            }
        }
        
        /**
         * Unsets the "AgentRecipe" element
         */
        public void unsetAgentRecipe()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(AGENTRECIPE$4, 0);
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
                get_store().find_all_element_users(EXTENSION$6, targetList);
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
                target = (de.bund.bfr.pcml10.ExtensionDocument.Extension)get_store().find_element_user(EXTENSION$6, i);
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
                return get_store().count_elements(EXTENSION$6);
            }
        }
        
        /**
         * Sets array of all "Extension" element  WARNING: This method is not atomicaly synchronized.
         */
        public void setExtensionArray(de.bund.bfr.pcml10.ExtensionDocument.Extension[] extensionArray)
        {
            check_orphaned();
            arraySetterHelper(extensionArray, EXTENSION$6);
        }
        
        /**
         * Sets ith "Extension" element
         */
        public void setExtensionArray(int i, de.bund.bfr.pcml10.ExtensionDocument.Extension extension)
        {
            generatedSetterHelperImpl(extension, EXTENSION$6, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
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
                target = (de.bund.bfr.pcml10.ExtensionDocument.Extension)get_store().insert_element_user(EXTENSION$6, i);
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
                target = (de.bund.bfr.pcml10.ExtensionDocument.Extension)get_store().add_element_user(EXTENSION$6);
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
                get_store().remove_element(EXTENSION$6, i);
            }
        }
        
        /**
         * Gets the "volume" attribute
         */
        public java.lang.String getVolume()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VOLUME$8);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "volume" attribute
         */
        public org.apache.xmlbeans.XmlString xgetVolume()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(VOLUME$8);
                return target;
            }
        }
        
        /**
         * Sets the "volume" attribute
         */
        public void setVolume(java.lang.String volume)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VOLUME$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(VOLUME$8);
                }
                target.setStringValue(volume);
            }
        }
        
        /**
         * Sets (as xml) the "volume" attribute
         */
        public void xsetVolume(org.apache.xmlbeans.XmlString volume)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(VOLUME$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(VOLUME$8);
                }
                target.set(volume);
            }
        }
        
        /**
         * Gets the "temperature" attribute
         */
        public java.lang.String getTemperature()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TEMPERATURE$10);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "temperature" attribute
         */
        public org.apache.xmlbeans.XmlString xgetTemperature()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(TEMPERATURE$10);
                return target;
            }
        }
        
        /**
         * True if has "temperature" attribute
         */
        public boolean isSetTemperature()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(TEMPERATURE$10) != null;
            }
        }
        
        /**
         * Sets the "temperature" attribute
         */
        public void setTemperature(java.lang.String temperature)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TEMPERATURE$10);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(TEMPERATURE$10);
                }
                target.setStringValue(temperature);
            }
        }
        
        /**
         * Sets (as xml) the "temperature" attribute
         */
        public void xsetTemperature(org.apache.xmlbeans.XmlString temperature)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(TEMPERATURE$10);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(TEMPERATURE$10);
                }
                target.set(temperature);
            }
        }
        
        /**
         * Unsets the "temperature" attribute
         */
        public void unsetTemperature()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(TEMPERATURE$10);
            }
        }
        
        /**
         * Gets the "pH" attribute
         */
        public java.lang.String getPH()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PH$12);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "pH" attribute
         */
        public org.apache.xmlbeans.XmlString xgetPH()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PH$12);
                return target;
            }
        }
        
        /**
         * True if has "pH" attribute
         */
        public boolean isSetPH()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(PH$12) != null;
            }
        }
        
        /**
         * Sets the "pH" attribute
         */
        public void setPH(java.lang.String ph)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PH$12);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PH$12);
                }
                target.setStringValue(ph);
            }
        }
        
        /**
         * Sets (as xml) the "pH" attribute
         */
        public void xsetPH(org.apache.xmlbeans.XmlString ph)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PH$12);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PH$12);
                }
                target.set(ph);
            }
        }
        
        /**
         * Unsets the "pH" attribute
         */
        public void unsetPH()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(PH$12);
            }
        }
        
        /**
         * Gets the "aw" attribute
         */
        public java.lang.String getAw()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(AW$14);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "aw" attribute
         */
        public org.apache.xmlbeans.XmlString xgetAw()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(AW$14);
                return target;
            }
        }
        
        /**
         * True if has "aw" attribute
         */
        public boolean isSetAw()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(AW$14) != null;
            }
        }
        
        /**
         * Sets the "aw" attribute
         */
        public void setAw(java.lang.String aw)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(AW$14);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(AW$14);
                }
                target.setStringValue(aw);
            }
        }
        
        /**
         * Sets (as xml) the "aw" attribute
         */
        public void xsetAw(org.apache.xmlbeans.XmlString aw)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(AW$14);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(AW$14);
                }
                target.set(aw);
            }
        }
        
        /**
         * Unsets the "aw" attribute
         */
        public void unsetAw()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(AW$14);
            }
        }
        
        /**
         * Gets the "pressure" attribute
         */
        public java.lang.String getPressure()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PRESSURE$16);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "pressure" attribute
         */
        public org.apache.xmlbeans.XmlString xgetPressure()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PRESSURE$16);
                return target;
            }
        }
        
        /**
         * True if has "pressure" attribute
         */
        public boolean isSetPressure()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(PRESSURE$16) != null;
            }
        }
        
        /**
         * Sets the "pressure" attribute
         */
        public void setPressure(java.lang.String pressure)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PRESSURE$16);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PRESSURE$16);
                }
                target.setStringValue(pressure);
            }
        }
        
        /**
         * Sets (as xml) the "pressure" attribute
         */
        public void xsetPressure(org.apache.xmlbeans.XmlString pressure)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PRESSURE$16);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PRESSURE$16);
                }
                target.set(pressure);
            }
        }
        
        /**
         * Unsets the "pressure" attribute
         */
        public void unsetPressure()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(PRESSURE$16);
            }
        }
        
        /**
         * Gets the "flowSpeed" attribute
         */
        public java.lang.String getFlowSpeed()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FLOWSPEED$18);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "flowSpeed" attribute
         */
        public org.apache.xmlbeans.XmlString xgetFlowSpeed()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(FLOWSPEED$18);
                return target;
            }
        }
        
        /**
         * True if has "flowSpeed" attribute
         */
        public boolean isSetFlowSpeed()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(FLOWSPEED$18) != null;
            }
        }
        
        /**
         * Sets the "flowSpeed" attribute
         */
        public void setFlowSpeed(java.lang.String flowSpeed)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FLOWSPEED$18);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(FLOWSPEED$18);
                }
                target.setStringValue(flowSpeed);
            }
        }
        
        /**
         * Sets (as xml) the "flowSpeed" attribute
         */
        public void xsetFlowSpeed(org.apache.xmlbeans.XmlString flowSpeed)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(FLOWSPEED$18);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(FLOWSPEED$18);
                }
                target.set(flowSpeed);
            }
        }
        
        /**
         * Unsets the "flowSpeed" attribute
         */
        public void unsetFlowSpeed()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(FLOWSPEED$18);
            }
        }
    }
}
