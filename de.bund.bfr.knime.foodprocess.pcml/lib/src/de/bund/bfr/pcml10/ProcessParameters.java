/*
 * XML Type:  ProcessParameters
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.ProcessParameters
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10;


/**
 * An XML ProcessParameters(@http://www.bfr.bund.de/PCML-1_0).
 *
 * This is a complex type.
 */
public interface ProcessParameters extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ProcessParameters.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s121DA543B1FCBC764F9DFAF30A6E9DAF").resolveHandle("processparametersf629type");
    
    /**
     * Gets array of all "Extension" elements
     */
    de.bund.bfr.pcml10.ExtensionDocument.Extension[] getExtensionArray();
    
    /**
     * Gets ith "Extension" element
     */
    de.bund.bfr.pcml10.ExtensionDocument.Extension getExtensionArray(int i);
    
    /**
     * Returns number of "Extension" element
     */
    int sizeOfExtensionArray();
    
    /**
     * Sets array of all "Extension" element
     */
    void setExtensionArray(de.bund.bfr.pcml10.ExtensionDocument.Extension[] extensionArray);
    
    /**
     * Sets ith "Extension" element
     */
    void setExtensionArray(int i, de.bund.bfr.pcml10.ExtensionDocument.Extension extension);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Extension" element
     */
    de.bund.bfr.pcml10.ExtensionDocument.Extension insertNewExtension(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Extension" element
     */
    de.bund.bfr.pcml10.ExtensionDocument.Extension addNewExtension();
    
    /**
     * Removes the ith "Extension" element
     */
    void removeExtension(int i);
    
    /**
     * Gets the "capacity" attribute
     */
    double getCapacity();
    
    /**
     * Gets (as xml) the "capacity" attribute
     */
    org.apache.xmlbeans.XmlDouble xgetCapacity();
    
    /**
     * True if has "capacity" attribute
     */
    boolean isSetCapacity();
    
    /**
     * Sets the "capacity" attribute
     */
    void setCapacity(double capacity);
    
    /**
     * Sets (as xml) the "capacity" attribute
     */
    void xsetCapacity(org.apache.xmlbeans.XmlDouble capacity);
    
    /**
     * Unsets the "capacity" attribute
     */
    void unsetCapacity();
    
    /**
     * Gets the "duration" attribute
     */
    double getDuration();
    
    /**
     * Gets (as xml) the "duration" attribute
     */
    org.apache.xmlbeans.XmlDouble xgetDuration();
    
    /**
     * Sets the "duration" attribute
     */
    void setDuration(double duration);
    
    /**
     * Sets (as xml) the "duration" attribute
     */
    void xsetDuration(org.apache.xmlbeans.XmlDouble duration);
    
    /**
     * Gets the "numberComputations" attribute
     */
    int getNumberComputations();
    
    /**
     * Gets (as xml) the "numberComputations" attribute
     */
    org.apache.xmlbeans.XmlInt xgetNumberComputations();
    
    /**
     * True if has "numberComputations" attribute
     */
    boolean isSetNumberComputations();
    
    /**
     * Sets the "numberComputations" attribute
     */
    void setNumberComputations(int numberComputations);
    
    /**
     * Sets (as xml) the "numberComputations" attribute
     */
    void xsetNumberComputations(org.apache.xmlbeans.XmlInt numberComputations);
    
    /**
     * Unsets the "numberComputations" attribute
     */
    void unsetNumberComputations();
    
    /**
     * Gets the "temperature" attribute
     */
    java.lang.String getTemperature();
    
    /**
     * Gets (as xml) the "temperature" attribute
     */
    org.apache.xmlbeans.XmlString xgetTemperature();
    
    /**
     * True if has "temperature" attribute
     */
    boolean isSetTemperature();
    
    /**
     * Sets the "temperature" attribute
     */
    void setTemperature(java.lang.String temperature);
    
    /**
     * Sets (as xml) the "temperature" attribute
     */
    void xsetTemperature(org.apache.xmlbeans.XmlString temperature);
    
    /**
     * Unsets the "temperature" attribute
     */
    void unsetTemperature();
    
    /**
     * Gets the "pH" attribute
     */
    java.lang.String getPH();
    
    /**
     * Gets (as xml) the "pH" attribute
     */
    org.apache.xmlbeans.XmlString xgetPH();
    
    /**
     * True if has "pH" attribute
     */
    boolean isSetPH();
    
    /**
     * Sets the "pH" attribute
     */
    void setPH(java.lang.String ph);
    
    /**
     * Sets (as xml) the "pH" attribute
     */
    void xsetPH(org.apache.xmlbeans.XmlString ph);
    
    /**
     * Unsets the "pH" attribute
     */
    void unsetPH();
    
    /**
     * Gets the "aw" attribute
     */
    java.lang.String getAw();
    
    /**
     * Gets (as xml) the "aw" attribute
     */
    org.apache.xmlbeans.XmlString xgetAw();
    
    /**
     * True if has "aw" attribute
     */
    boolean isSetAw();
    
    /**
     * Sets the "aw" attribute
     */
    void setAw(java.lang.String aw);
    
    /**
     * Sets (as xml) the "aw" attribute
     */
    void xsetAw(org.apache.xmlbeans.XmlString aw);
    
    /**
     * Unsets the "aw" attribute
     */
    void unsetAw();
    
    /**
     * Gets the "pressure" attribute
     */
    java.lang.String getPressure();
    
    /**
     * Gets (as xml) the "pressure" attribute
     */
    org.apache.xmlbeans.XmlString xgetPressure();
    
    /**
     * True if has "pressure" attribute
     */
    boolean isSetPressure();
    
    /**
     * Sets the "pressure" attribute
     */
    void setPressure(java.lang.String pressure);
    
    /**
     * Sets (as xml) the "pressure" attribute
     */
    void xsetPressure(org.apache.xmlbeans.XmlString pressure);
    
    /**
     * Unsets the "pressure" attribute
     */
    void unsetPressure();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static de.bund.bfr.pcml10.ProcessParameters newInstance() {
          return (de.bund.bfr.pcml10.ProcessParameters) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static de.bund.bfr.pcml10.ProcessParameters newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (de.bund.bfr.pcml10.ProcessParameters) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static de.bund.bfr.pcml10.ProcessParameters parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.ProcessParameters) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static de.bund.bfr.pcml10.ProcessParameters parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.ProcessParameters) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static de.bund.bfr.pcml10.ProcessParameters parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ProcessParameters) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static de.bund.bfr.pcml10.ProcessParameters parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ProcessParameters) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static de.bund.bfr.pcml10.ProcessParameters parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ProcessParameters) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static de.bund.bfr.pcml10.ProcessParameters parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ProcessParameters) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static de.bund.bfr.pcml10.ProcessParameters parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ProcessParameters) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static de.bund.bfr.pcml10.ProcessParameters parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ProcessParameters) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static de.bund.bfr.pcml10.ProcessParameters parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ProcessParameters) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static de.bund.bfr.pcml10.ProcessParameters parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ProcessParameters) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static de.bund.bfr.pcml10.ProcessParameters parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.ProcessParameters) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static de.bund.bfr.pcml10.ProcessParameters parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.ProcessParameters) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static de.bund.bfr.pcml10.ProcessParameters parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.ProcessParameters) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static de.bund.bfr.pcml10.ProcessParameters parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.ProcessParameters) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.bund.bfr.pcml10.ProcessParameters parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.bund.bfr.pcml10.ProcessParameters) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.bund.bfr.pcml10.ProcessParameters parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.bund.bfr.pcml10.ProcessParameters) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
