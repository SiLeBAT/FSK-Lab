/*
 * An XML document type.
 * Localname: AgentIncredient
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.AgentIncredientDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * A document containing one AgentIncredient(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public class AgentIncredientDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.AgentIncredientDocument
{
    private static final long serialVersionUID = 1L;
    
    public AgentIncredientDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName AGENTINCREDIENT$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "AgentIncredient");
    
    
    /**
     * Gets the "AgentIncredient" element
     */
    public de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient getAgentIncredient()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient target = null;
            target = (de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient)get_store().find_element_user(AGENTINCREDIENT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "AgentIncredient" element
     */
    public void setAgentIncredient(de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient agentIncredient)
    {
        generatedSetterHelperImpl(agentIncredient, AGENTINCREDIENT$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "AgentIncredient" element
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
     * An XML AgentIncredient(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public static class AgentIncredientImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient
    {
        private static final long serialVersionUID = 1L;
        
        public AgentIncredientImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName AGENT$0 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Agent");
        private static final javax.xml.namespace.QName FRACTION$2 = 
            new javax.xml.namespace.QName("", "fraction");
        private static final javax.xml.namespace.QName UNIT$4 = 
            new javax.xml.namespace.QName("", "unit");
        private static final javax.xml.namespace.QName OBJECT$6 = 
            new javax.xml.namespace.QName("", "object");
        
        
        /**
         * Gets the "Agent" element
         */
        public de.bund.bfr.pcml10.NameAndDatabaseId getAgent()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.NameAndDatabaseId target = null;
                target = (de.bund.bfr.pcml10.NameAndDatabaseId)get_store().find_element_user(AGENT$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Sets the "Agent" element
         */
        public void setAgent(de.bund.bfr.pcml10.NameAndDatabaseId agent)
        {
            generatedSetterHelperImpl(agent, AGENT$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "Agent" element
         */
        public de.bund.bfr.pcml10.NameAndDatabaseId addNewAgent()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.NameAndDatabaseId target = null;
                target = (de.bund.bfr.pcml10.NameAndDatabaseId)get_store().add_element_user(AGENT$0);
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
         * True if has "fraction" attribute
         */
        public boolean isSetFraction()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(FRACTION$2) != null;
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
        
        /**
         * Unsets the "fraction" attribute
         */
        public void unsetFraction()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(FRACTION$2);
            }
        }
        
        /**
         * Gets the "unit" attribute
         */
        public java.lang.String getUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(UNIT$4);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "unit" attribute
         */
        public org.apache.xmlbeans.XmlString xgetUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(UNIT$4);
                return target;
            }
        }
        
        /**
         * True if has "unit" attribute
         */
        public boolean isSetUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(UNIT$4) != null;
            }
        }
        
        /**
         * Sets the "unit" attribute
         */
        public void setUnit(java.lang.String unit)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(UNIT$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(UNIT$4);
                }
                target.setStringValue(unit);
            }
        }
        
        /**
         * Sets (as xml) the "unit" attribute
         */
        public void xsetUnit(org.apache.xmlbeans.XmlString unit)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(UNIT$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(UNIT$4);
                }
                target.set(unit);
            }
        }
        
        /**
         * Unsets the "unit" attribute
         */
        public void unsetUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(UNIT$4);
            }
        }
        
        /**
         * Gets the "object" attribute
         */
        public java.lang.String getObject()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OBJECT$6);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "object" attribute
         */
        public org.apache.xmlbeans.XmlString xgetObject()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(OBJECT$6);
                return target;
            }
        }
        
        /**
         * True if has "object" attribute
         */
        public boolean isSetObject()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(OBJECT$6) != null;
            }
        }
        
        /**
         * Sets the "object" attribute
         */
        public void setObject(java.lang.String object)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OBJECT$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(OBJECT$6);
                }
                target.setStringValue(object);
            }
        }
        
        /**
         * Sets (as xml) the "object" attribute
         */
        public void xsetObject(org.apache.xmlbeans.XmlString object)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(OBJECT$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(OBJECT$6);
                }
                target.set(object);
            }
        }
        
        /**
         * Unsets the "object" attribute
         */
        public void unsetObject()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(OBJECT$6);
            }
        }
    }
}
