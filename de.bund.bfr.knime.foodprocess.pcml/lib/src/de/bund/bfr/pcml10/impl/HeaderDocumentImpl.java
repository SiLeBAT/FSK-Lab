/*
 * An XML document type.
 * Localname: Header
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.HeaderDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * A document containing one Header(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public class HeaderDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.HeaderDocument
{
    private static final long serialVersionUID = 1L;
    
    public HeaderDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName HEADER$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Header");
    
    
    /**
     * Gets the "Header" element
     */
    public de.bund.bfr.pcml10.HeaderDocument.Header getHeader()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.HeaderDocument.Header target = null;
            target = (de.bund.bfr.pcml10.HeaderDocument.Header)get_store().find_element_user(HEADER$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Header" element
     */
    public void setHeader(de.bund.bfr.pcml10.HeaderDocument.Header header)
    {
        generatedSetterHelperImpl(header, HEADER$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "Header" element
     */
    public de.bund.bfr.pcml10.HeaderDocument.Header addNewHeader()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.HeaderDocument.Header target = null;
            target = (de.bund.bfr.pcml10.HeaderDocument.Header)get_store().add_element_user(HEADER$0);
            return target;
        }
    }
    /**
     * An XML Header(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public static class HeaderImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.HeaderDocument.Header
    {
        private static final long serialVersionUID = 1L;
        
        public HeaderImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName EXTENSION$0 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Extension");
        private static final javax.xml.namespace.QName APPLICATION$2 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Application");
        private static final javax.xml.namespace.QName ANNOTATION$4 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Annotation");
        private static final javax.xml.namespace.QName TIMESTAMP$6 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Timestamp");
        private static final javax.xml.namespace.QName COPYRIGHT$8 = 
            new javax.xml.namespace.QName("", "copyright");
        private static final javax.xml.namespace.QName DESCRIPTION$10 = 
            new javax.xml.namespace.QName("", "description");
        
        
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
         * Gets the "Application" element
         */
        public de.bund.bfr.pcml10.ApplicationDocument.Application getApplication()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.ApplicationDocument.Application target = null;
                target = (de.bund.bfr.pcml10.ApplicationDocument.Application)get_store().find_element_user(APPLICATION$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "Application" element
         */
        public boolean isSetApplication()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(APPLICATION$2) != 0;
            }
        }
        
        /**
         * Sets the "Application" element
         */
        public void setApplication(de.bund.bfr.pcml10.ApplicationDocument.Application application)
        {
            generatedSetterHelperImpl(application, APPLICATION$2, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "Application" element
         */
        public de.bund.bfr.pcml10.ApplicationDocument.Application addNewApplication()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.ApplicationDocument.Application target = null;
                target = (de.bund.bfr.pcml10.ApplicationDocument.Application)get_store().add_element_user(APPLICATION$2);
                return target;
            }
        }
        
        /**
         * Unsets the "Application" element
         */
        public void unsetApplication()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(APPLICATION$2, 0);
            }
        }
        
        /**
         * Gets array of all "Annotation" elements
         */
        public de.bund.bfr.pcml10.AnnotationDocument.Annotation[] getAnnotationArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(ANNOTATION$4, targetList);
                de.bund.bfr.pcml10.AnnotationDocument.Annotation[] result = new de.bund.bfr.pcml10.AnnotationDocument.Annotation[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "Annotation" element
         */
        public de.bund.bfr.pcml10.AnnotationDocument.Annotation getAnnotationArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.AnnotationDocument.Annotation target = null;
                target = (de.bund.bfr.pcml10.AnnotationDocument.Annotation)get_store().find_element_user(ANNOTATION$4, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "Annotation" element
         */
        public int sizeOfAnnotationArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(ANNOTATION$4);
            }
        }
        
        /**
         * Sets array of all "Annotation" element  WARNING: This method is not atomicaly synchronized.
         */
        public void setAnnotationArray(de.bund.bfr.pcml10.AnnotationDocument.Annotation[] annotationArray)
        {
            check_orphaned();
            arraySetterHelper(annotationArray, ANNOTATION$4);
        }
        
        /**
         * Sets ith "Annotation" element
         */
        public void setAnnotationArray(int i, de.bund.bfr.pcml10.AnnotationDocument.Annotation annotation)
        {
            generatedSetterHelperImpl(annotation, ANNOTATION$4, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "Annotation" element
         */
        public de.bund.bfr.pcml10.AnnotationDocument.Annotation insertNewAnnotation(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.AnnotationDocument.Annotation target = null;
                target = (de.bund.bfr.pcml10.AnnotationDocument.Annotation)get_store().insert_element_user(ANNOTATION$4, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "Annotation" element
         */
        public de.bund.bfr.pcml10.AnnotationDocument.Annotation addNewAnnotation()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.AnnotationDocument.Annotation target = null;
                target = (de.bund.bfr.pcml10.AnnotationDocument.Annotation)get_store().add_element_user(ANNOTATION$4);
                return target;
            }
        }
        
        /**
         * Removes the ith "Annotation" element
         */
        public void removeAnnotation(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(ANNOTATION$4, i);
            }
        }
        
        /**
         * Gets the "Timestamp" element
         */
        public de.bund.bfr.pcml10.TimestampDocument.Timestamp getTimestamp()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.TimestampDocument.Timestamp target = null;
                target = (de.bund.bfr.pcml10.TimestampDocument.Timestamp)get_store().find_element_user(TIMESTAMP$6, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "Timestamp" element
         */
        public boolean isSetTimestamp()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(TIMESTAMP$6) != 0;
            }
        }
        
        /**
         * Sets the "Timestamp" element
         */
        public void setTimestamp(de.bund.bfr.pcml10.TimestampDocument.Timestamp timestamp)
        {
            generatedSetterHelperImpl(timestamp, TIMESTAMP$6, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "Timestamp" element
         */
        public de.bund.bfr.pcml10.TimestampDocument.Timestamp addNewTimestamp()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.TimestampDocument.Timestamp target = null;
                target = (de.bund.bfr.pcml10.TimestampDocument.Timestamp)get_store().add_element_user(TIMESTAMP$6);
                return target;
            }
        }
        
        /**
         * Unsets the "Timestamp" element
         */
        public void unsetTimestamp()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(TIMESTAMP$6, 0);
            }
        }
        
        /**
         * Gets the "copyright" attribute
         */
        public java.lang.String getCopyright()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(COPYRIGHT$8);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "copyright" attribute
         */
        public org.apache.xmlbeans.XmlString xgetCopyright()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(COPYRIGHT$8);
                return target;
            }
        }
        
        /**
         * Sets the "copyright" attribute
         */
        public void setCopyright(java.lang.String copyright)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(COPYRIGHT$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(COPYRIGHT$8);
                }
                target.setStringValue(copyright);
            }
        }
        
        /**
         * Sets (as xml) the "copyright" attribute
         */
        public void xsetCopyright(org.apache.xmlbeans.XmlString copyright)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(COPYRIGHT$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(COPYRIGHT$8);
                }
                target.set(copyright);
            }
        }
        
        /**
         * Gets the "description" attribute
         */
        public java.lang.String getDescription()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DESCRIPTION$10);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "description" attribute
         */
        public org.apache.xmlbeans.XmlString xgetDescription()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(DESCRIPTION$10);
                return target;
            }
        }
        
        /**
         * True if has "description" attribute
         */
        public boolean isSetDescription()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(DESCRIPTION$10) != null;
            }
        }
        
        /**
         * Sets the "description" attribute
         */
        public void setDescription(java.lang.String description)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DESCRIPTION$10);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(DESCRIPTION$10);
                }
                target.setStringValue(description);
            }
        }
        
        /**
         * Sets (as xml) the "description" attribute
         */
        public void xsetDescription(org.apache.xmlbeans.XmlString description)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(DESCRIPTION$10);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(DESCRIPTION$10);
                }
                target.set(description);
            }
        }
        
        /**
         * Unsets the "description" attribute
         */
        public void unsetDescription()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(DESCRIPTION$10);
            }
        }
    }
}
