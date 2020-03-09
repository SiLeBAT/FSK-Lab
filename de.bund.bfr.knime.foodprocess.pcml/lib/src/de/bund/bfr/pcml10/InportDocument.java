/*
 * An XML document type.
 * Localname: Inport
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.InportDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10;


/**
 * A document containing one Inport(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public interface InportDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(InportDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s121DA543B1FCBC764F9DFAF30A6E9DAF").resolveHandle("inport08d8doctype");
    
    /**
     * Gets the "Inport" element
     */
    de.bund.bfr.pcml10.InportDocument.Inport getInport();
    
    /**
     * Sets the "Inport" element
     */
    void setInport(de.bund.bfr.pcml10.InportDocument.Inport inport);
    
    /**
     * Appends and returns a new empty "Inport" element
     */
    de.bund.bfr.pcml10.InportDocument.Inport addNewInport();
    
    /**
     * An XML Inport(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public interface Inport extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Inport.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s121DA543B1FCBC764F9DFAF30A6E9DAF").resolveHandle("inporte136elemtype");
        
        /**
         * Gets the "OutportRef" element
         */
        de.bund.bfr.pcml10.OutportRefDocument.OutportRef getOutportRef();
        
        /**
         * Sets the "OutportRef" element
         */
        void setOutportRef(de.bund.bfr.pcml10.OutportRefDocument.OutportRef outportRef);
        
        /**
         * Appends and returns a new empty "OutportRef" element
         */
        de.bund.bfr.pcml10.OutportRefDocument.OutportRef addNewOutportRef();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static de.bund.bfr.pcml10.InportDocument.Inport newInstance() {
              return (de.bund.bfr.pcml10.InportDocument.Inport) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static de.bund.bfr.pcml10.InportDocument.Inport newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (de.bund.bfr.pcml10.InportDocument.Inport) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static de.bund.bfr.pcml10.InportDocument newInstance() {
          return (de.bund.bfr.pcml10.InportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static de.bund.bfr.pcml10.InportDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (de.bund.bfr.pcml10.InportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static de.bund.bfr.pcml10.InportDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.InportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static de.bund.bfr.pcml10.InportDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.InportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static de.bund.bfr.pcml10.InportDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.InportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static de.bund.bfr.pcml10.InportDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.InportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static de.bund.bfr.pcml10.InportDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.InportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static de.bund.bfr.pcml10.InportDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.InportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static de.bund.bfr.pcml10.InportDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.InportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static de.bund.bfr.pcml10.InportDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.InportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static de.bund.bfr.pcml10.InportDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.InportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static de.bund.bfr.pcml10.InportDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.InportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static de.bund.bfr.pcml10.InportDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.InportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static de.bund.bfr.pcml10.InportDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.InportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static de.bund.bfr.pcml10.InportDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.InportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static de.bund.bfr.pcml10.InportDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.InportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.bund.bfr.pcml10.InportDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.bund.bfr.pcml10.InportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.bund.bfr.pcml10.InportDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.bund.bfr.pcml10.InportDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
