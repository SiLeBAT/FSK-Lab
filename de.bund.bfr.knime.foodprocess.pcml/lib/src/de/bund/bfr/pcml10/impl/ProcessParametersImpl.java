/*
 * XML Type:  ProcessParameters
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.ProcessParameters
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * An XML ProcessParameters(@http://www.bfr.bund.de/PCML-1_0).
 *
 * This is a complex type.
 */
public class ProcessParametersImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.ProcessParameters
{
    private static final long serialVersionUID = 1L;
    
    public ProcessParametersImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName EXTENSION$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Extension");
    private static final javax.xml.namespace.QName CAPACITY$2 = 
        new javax.xml.namespace.QName("", "capacity");
    private static final javax.xml.namespace.QName DURATION$4 = 
        new javax.xml.namespace.QName("", "duration");
    private static final javax.xml.namespace.QName NUMBERCOMPUTATIONS$6 = 
        new javax.xml.namespace.QName("", "numberComputations");
    private static final javax.xml.namespace.QName TEMPERATURE$8 = 
        new javax.xml.namespace.QName("", "temperature");
    private static final javax.xml.namespace.QName PH$10 = 
        new javax.xml.namespace.QName("", "pH");
    private static final javax.xml.namespace.QName AW$12 = 
        new javax.xml.namespace.QName("", "aw");
    private static final javax.xml.namespace.QName PRESSURE$14 = 
        new javax.xml.namespace.QName("", "pressure");
    
    
    /**
     * Gets array of all "Extension" elements
     */
    public de.bund.bfr.pcml10.ExtensionDocument.Extension[] getExtensionArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(EXTENSION$0, targetList);
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
            target = (de.bund.bfr.pcml10.ExtensionDocument.Extension)get_store().find_element_user(EXTENSION$0, i);
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
            return get_store().count_elements(EXTENSION$0);
        }
    }
    
    /**
     * Sets array of all "Extension" element  WARNING: This method is not atomicaly synchronized.
     */
    public void setExtensionArray(de.bund.bfr.pcml10.ExtensionDocument.Extension[] extensionArray)
    {
        check_orphaned();
        arraySetterHelper(extensionArray, EXTENSION$0);
    }
    
    /**
     * Sets ith "Extension" element
     */
    public void setExtensionArray(int i, de.bund.bfr.pcml10.ExtensionDocument.Extension extension)
    {
        generatedSetterHelperImpl(extension, EXTENSION$0, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
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
            target = (de.bund.bfr.pcml10.ExtensionDocument.Extension)get_store().insert_element_user(EXTENSION$0, i);
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
            target = (de.bund.bfr.pcml10.ExtensionDocument.Extension)get_store().add_element_user(EXTENSION$0);
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
            get_store().remove_element(EXTENSION$0, i);
        }
    }
    
    /**
     * Gets the "capacity" attribute
     */
    public double getCapacity()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CAPACITY$2);
            if (target == null)
            {
                return 0.0;
            }
            return target.getDoubleValue();
        }
    }
    
    /**
     * Gets (as xml) the "capacity" attribute
     */
    public org.apache.xmlbeans.XmlDouble xgetCapacity()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_attribute_user(CAPACITY$2);
            return target;
        }
    }
    
    /**
     * True if has "capacity" attribute
     */
    public boolean isSetCapacity()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(CAPACITY$2) != null;
        }
    }
    
    /**
     * Sets the "capacity" attribute
     */
    public void setCapacity(double capacity)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CAPACITY$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(CAPACITY$2);
            }
            target.setDoubleValue(capacity);
        }
    }
    
    /**
     * Sets (as xml) the "capacity" attribute
     */
    public void xsetCapacity(org.apache.xmlbeans.XmlDouble capacity)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_attribute_user(CAPACITY$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDouble)get_store().add_attribute_user(CAPACITY$2);
            }
            target.set(capacity);
        }
    }
    
    /**
     * Unsets the "capacity" attribute
     */
    public void unsetCapacity()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(CAPACITY$2);
        }
    }
    
    /**
     * Gets the "duration" attribute
     */
    public double getDuration()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DURATION$4);
            if (target == null)
            {
                return 0.0;
            }
            return target.getDoubleValue();
        }
    }
    
    /**
     * Gets (as xml) the "duration" attribute
     */
    public org.apache.xmlbeans.XmlDouble xgetDuration()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_attribute_user(DURATION$4);
            return target;
        }
    }
    
    /**
     * Sets the "duration" attribute
     */
    public void setDuration(double duration)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DURATION$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(DURATION$4);
            }
            target.setDoubleValue(duration);
        }
    }
    
    /**
     * Sets (as xml) the "duration" attribute
     */
    public void xsetDuration(org.apache.xmlbeans.XmlDouble duration)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_attribute_user(DURATION$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDouble)get_store().add_attribute_user(DURATION$4);
            }
            target.set(duration);
        }
    }
    
    /**
     * Gets the "numberComputations" attribute
     */
    public int getNumberComputations()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NUMBERCOMPUTATIONS$6);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "numberComputations" attribute
     */
    public org.apache.xmlbeans.XmlInt xgetNumberComputations()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(NUMBERCOMPUTATIONS$6);
            return target;
        }
    }
    
    /**
     * True if has "numberComputations" attribute
     */
    public boolean isSetNumberComputations()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(NUMBERCOMPUTATIONS$6) != null;
        }
    }
    
    /**
     * Sets the "numberComputations" attribute
     */
    public void setNumberComputations(int numberComputations)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NUMBERCOMPUTATIONS$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NUMBERCOMPUTATIONS$6);
            }
            target.setIntValue(numberComputations);
        }
    }
    
    /**
     * Sets (as xml) the "numberComputations" attribute
     */
    public void xsetNumberComputations(org.apache.xmlbeans.XmlInt numberComputations)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(NUMBERCOMPUTATIONS$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_attribute_user(NUMBERCOMPUTATIONS$6);
            }
            target.set(numberComputations);
        }
    }
    
    /**
     * Unsets the "numberComputations" attribute
     */
    public void unsetNumberComputations()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(NUMBERCOMPUTATIONS$6);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TEMPERATURE$8);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(TEMPERATURE$8);
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
            return get_store().find_attribute_user(TEMPERATURE$8) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TEMPERATURE$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(TEMPERATURE$8);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(TEMPERATURE$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(TEMPERATURE$8);
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
            get_store().remove_attribute(TEMPERATURE$8);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PH$10);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PH$10);
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
            return get_store().find_attribute_user(PH$10) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PH$10);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PH$10);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PH$10);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PH$10);
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
            get_store().remove_attribute(PH$10);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(AW$12);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(AW$12);
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
            return get_store().find_attribute_user(AW$12) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(AW$12);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(AW$12);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(AW$12);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(AW$12);
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
            get_store().remove_attribute(AW$12);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PRESSURE$14);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PRESSURE$14);
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
            return get_store().find_attribute_user(PRESSURE$14) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PRESSURE$14);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PRESSURE$14);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PRESSURE$14);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PRESSURE$14);
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
            get_store().remove_attribute(PRESSURE$14);
        }
    }
}
