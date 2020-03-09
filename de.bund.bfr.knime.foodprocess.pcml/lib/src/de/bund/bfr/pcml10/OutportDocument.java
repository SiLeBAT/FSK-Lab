/*
 * An XML document type.
 * Localname: Outport
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.OutportDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10;


/**
 * A document containing one Outport(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public interface OutportDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(OutportDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s121DA543B1FCBC764F9DFAF30A6E9DAF").resolveHandle("outport21efdoctype");
    
    /**
     * Gets the "Outport" element
     */
    de.bund.bfr.pcml10.OutportDocument.Outport getOutport();
    
    /**
     * Sets the "Outport" element
     */
    void setOutport(de.bund.bfr.pcml10.OutportDocument.Outport outport);
    
    /**
     * Appends and returns a new empty "Outport" element
     */
    de.bund.bfr.pcml10.OutportDocument.Outport addNewOutport();
    
    /**
     * An XML Outport(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public interface Outport extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Outport.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s121DA543B1FCBC764F9DFAF30A6E9DAF").resolveHandle("outportb54aelemtype");
        
        /**
         * Gets the "Matrix" element
         */
        de.bund.bfr.pcml10.NameAndDatabaseId getMatrix();
        
        /**
         * True if has "Matrix" element
         */
        boolean isSetMatrix();
        
        /**
         * Sets the "Matrix" element
         */
        void setMatrix(de.bund.bfr.pcml10.NameAndDatabaseId matrix);
        
        /**
         * Appends and returns a new empty "Matrix" element
         */
        de.bund.bfr.pcml10.NameAndDatabaseId addNewMatrix();
        
        /**
         * Unsets the "Matrix" element
         */
        void unsetMatrix();
        
        /**
         * Gets the "MatrixRecipe" element
         */
        de.bund.bfr.pcml10.MatrixRecipeDocument.MatrixRecipe getMatrixRecipe();
        
        /**
         * True if has "MatrixRecipe" element
         */
        boolean isSetMatrixRecipe();
        
        /**
         * Sets the "MatrixRecipe" element
         */
        void setMatrixRecipe(de.bund.bfr.pcml10.MatrixRecipeDocument.MatrixRecipe matrixRecipe);
        
        /**
         * Appends and returns a new empty "MatrixRecipe" element
         */
        de.bund.bfr.pcml10.MatrixRecipeDocument.MatrixRecipe addNewMatrixRecipe();
        
        /**
         * Unsets the "MatrixRecipe" element
         */
        void unsetMatrixRecipe();
        
        /**
         * Gets the "AgentRecipe" element
         */
        de.bund.bfr.pcml10.AgentRecipeDocument.AgentRecipe getAgentRecipe();
        
        /**
         * True if has "AgentRecipe" element
         */
        boolean isSetAgentRecipe();
        
        /**
         * Sets the "AgentRecipe" element
         */
        void setAgentRecipe(de.bund.bfr.pcml10.AgentRecipeDocument.AgentRecipe agentRecipe);
        
        /**
         * Appends and returns a new empty "AgentRecipe" element
         */
        de.bund.bfr.pcml10.AgentRecipeDocument.AgentRecipe addNewAgentRecipe();
        
        /**
         * Unsets the "AgentRecipe" element
         */
        void unsetAgentRecipe();
        
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
         * Gets the "volume" attribute
         */
        java.lang.String getVolume();
        
        /**
         * Gets (as xml) the "volume" attribute
         */
        org.apache.xmlbeans.XmlString xgetVolume();
        
        /**
         * Sets the "volume" attribute
         */
        void setVolume(java.lang.String volume);
        
        /**
         * Sets (as xml) the "volume" attribute
         */
        void xsetVolume(org.apache.xmlbeans.XmlString volume);
        
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
         * Gets the "flowSpeed" attribute
         */
        java.lang.String getFlowSpeed();
        
        /**
         * Gets (as xml) the "flowSpeed" attribute
         */
        org.apache.xmlbeans.XmlString xgetFlowSpeed();
        
        /**
         * True if has "flowSpeed" attribute
         */
        boolean isSetFlowSpeed();
        
        /**
         * Sets the "flowSpeed" attribute
         */
        void setFlowSpeed(java.lang.String flowSpeed);
        
        /**
         * Sets (as xml) the "flowSpeed" attribute
         */
        void xsetFlowSpeed(org.apache.xmlbeans.XmlString flowSpeed);
        
        /**
         * Unsets the "flowSpeed" attribute
         */
        void unsetFlowSpeed();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static de.bund.bfr.pcml10.OutportDocument.Outport newInstance() {
              return (de.bund.bfr.pcml10.OutportDocument.Outport) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static de.bund.bfr.pcml10.OutportDocument.Outport newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (de.bund.bfr.pcml10.OutportDocument.Outport) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static de.bund.bfr.pcml10.OutportDocument newInstance() {
          return (de.bund.bfr.pcml10.OutportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static de.bund.bfr.pcml10.OutportDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (de.bund.bfr.pcml10.OutportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static de.bund.bfr.pcml10.OutportDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.OutportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static de.bund.bfr.pcml10.OutportDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.OutportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static de.bund.bfr.pcml10.OutportDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.OutportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static de.bund.bfr.pcml10.OutportDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.OutportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static de.bund.bfr.pcml10.OutportDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.OutportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static de.bund.bfr.pcml10.OutportDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.OutportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static de.bund.bfr.pcml10.OutportDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.OutportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static de.bund.bfr.pcml10.OutportDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.OutportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static de.bund.bfr.pcml10.OutportDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.OutportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static de.bund.bfr.pcml10.OutportDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.OutportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static de.bund.bfr.pcml10.OutportDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.OutportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static de.bund.bfr.pcml10.OutportDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.OutportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static de.bund.bfr.pcml10.OutportDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.OutportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static de.bund.bfr.pcml10.OutportDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.OutportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.bund.bfr.pcml10.OutportDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.bund.bfr.pcml10.OutportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.bund.bfr.pcml10.OutportDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.bund.bfr.pcml10.OutportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
