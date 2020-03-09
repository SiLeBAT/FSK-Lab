/*
 * An XML document type.
 * Localname: AgentIncredient
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.AgentIncredientDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10;


/**
 * A document containing one AgentIncredient(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public interface AgentIncredientDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(AgentIncredientDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s121DA543B1FCBC764F9DFAF30A6E9DAF").resolveHandle("agentincredient295adoctype");
    
    /**
     * Gets the "AgentIncredient" element
     */
    de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient getAgentIncredient();
    
    /**
     * Sets the "AgentIncredient" element
     */
    void setAgentIncredient(de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient agentIncredient);
    
    /**
     * Appends and returns a new empty "AgentIncredient" element
     */
    de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient addNewAgentIncredient();
    
    /**
     * An XML AgentIncredient(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public interface AgentIncredient extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(AgentIncredient.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s121DA543B1FCBC764F9DFAF30A6E9DAF").resolveHandle("agentincredient0360elemtype");
        
        /**
         * Gets the "Agent" element
         */
        de.bund.bfr.pcml10.NameAndDatabaseId getAgent();
        
        /**
         * Sets the "Agent" element
         */
        void setAgent(de.bund.bfr.pcml10.NameAndDatabaseId agent);
        
        /**
         * Appends and returns a new empty "Agent" element
         */
        de.bund.bfr.pcml10.NameAndDatabaseId addNewAgent();
        
        /**
         * Gets the "fraction" attribute
         */
        double getFraction();
        
        /**
         * Gets (as xml) the "fraction" attribute
         */
        org.apache.xmlbeans.XmlDouble xgetFraction();
        
        /**
         * True if has "fraction" attribute
         */
        boolean isSetFraction();
        
        /**
         * Sets the "fraction" attribute
         */
        void setFraction(double fraction);
        
        /**
         * Sets (as xml) the "fraction" attribute
         */
        void xsetFraction(org.apache.xmlbeans.XmlDouble fraction);
        
        /**
         * Unsets the "fraction" attribute
         */
        void unsetFraction();
        
        /**
         * Gets the "unit" attribute
         */
        java.lang.String getUnit();
        
        /**
         * Gets (as xml) the "unit" attribute
         */
        org.apache.xmlbeans.XmlString xgetUnit();
        
        /**
         * True if has "unit" attribute
         */
        boolean isSetUnit();
        
        /**
         * Sets the "unit" attribute
         */
        void setUnit(java.lang.String unit);
        
        /**
         * Sets (as xml) the "unit" attribute
         */
        void xsetUnit(org.apache.xmlbeans.XmlString unit);
        
        /**
         * Unsets the "unit" attribute
         */
        void unsetUnit();
        
        /**
         * Gets the "object" attribute
         */
        java.lang.String getObject();
        
        /**
         * Gets (as xml) the "object" attribute
         */
        org.apache.xmlbeans.XmlString xgetObject();
        
        /**
         * True if has "object" attribute
         */
        boolean isSetObject();
        
        /**
         * Sets the "object" attribute
         */
        void setObject(java.lang.String object);
        
        /**
         * Sets (as xml) the "object" attribute
         */
        void xsetObject(org.apache.xmlbeans.XmlString object);
        
        /**
         * Unsets the "object" attribute
         */
        void unsetObject();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient newInstance() {
              return (de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static de.bund.bfr.pcml10.AgentIncredientDocument newInstance() {
          return (de.bund.bfr.pcml10.AgentIncredientDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static de.bund.bfr.pcml10.AgentIncredientDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (de.bund.bfr.pcml10.AgentIncredientDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static de.bund.bfr.pcml10.AgentIncredientDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.AgentIncredientDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static de.bund.bfr.pcml10.AgentIncredientDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.AgentIncredientDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static de.bund.bfr.pcml10.AgentIncredientDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.AgentIncredientDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static de.bund.bfr.pcml10.AgentIncredientDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.AgentIncredientDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static de.bund.bfr.pcml10.AgentIncredientDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.AgentIncredientDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static de.bund.bfr.pcml10.AgentIncredientDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.AgentIncredientDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static de.bund.bfr.pcml10.AgentIncredientDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.AgentIncredientDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static de.bund.bfr.pcml10.AgentIncredientDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.AgentIncredientDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static de.bund.bfr.pcml10.AgentIncredientDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.AgentIncredientDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static de.bund.bfr.pcml10.AgentIncredientDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.AgentIncredientDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static de.bund.bfr.pcml10.AgentIncredientDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.AgentIncredientDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static de.bund.bfr.pcml10.AgentIncredientDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.AgentIncredientDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static de.bund.bfr.pcml10.AgentIncredientDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.AgentIncredientDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static de.bund.bfr.pcml10.AgentIncredientDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.AgentIncredientDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.bund.bfr.pcml10.AgentIncredientDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.bund.bfr.pcml10.AgentIncredientDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.bund.bfr.pcml10.AgentIncredientDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.bund.bfr.pcml10.AgentIncredientDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
