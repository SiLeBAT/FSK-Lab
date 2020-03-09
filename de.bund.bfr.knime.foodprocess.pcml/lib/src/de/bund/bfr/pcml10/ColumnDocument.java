/*
 * An XML document type.
 * Localname: Column
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.ColumnDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10;


/**
 * A document containing one Column(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public interface ColumnDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ColumnDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s121DA543B1FCBC764F9DFAF30A6E9DAF").resolveHandle("column9108doctype");
    
    /**
     * Gets the "Column" element
     */
    de.bund.bfr.pcml10.ColumnDocument.Column getColumn();
    
    /**
     * Sets the "Column" element
     */
    void setColumn(de.bund.bfr.pcml10.ColumnDocument.Column column);
    
    /**
     * Appends and returns a new empty "Column" element
     */
    de.bund.bfr.pcml10.ColumnDocument.Column addNewColumn();
    
    /**
     * An XML Column(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public interface Column extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Column.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s121DA543B1FCBC764F9DFAF30A6E9DAF").resolveHandle("column1736elemtype");
        
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
         * Gets the "Agent" element
         */
        de.bund.bfr.pcml10.NameAndDatabaseId getAgent();
        
        /**
         * True if has "Agent" element
         */
        boolean isSetAgent();
        
        /**
         * Sets the "Agent" element
         */
        void setAgent(de.bund.bfr.pcml10.NameAndDatabaseId agent);
        
        /**
         * Appends and returns a new empty "Agent" element
         */
        de.bund.bfr.pcml10.NameAndDatabaseId addNewAgent();
        
        /**
         * Unsets the "Agent" element
         */
        void unsetAgent();
        
        /**
         * Gets the "ColumnId" element
         */
        de.bund.bfr.pcml10.NameAndDatabaseId getColumnId();
        
        /**
         * True if has "ColumnId" element
         */
        boolean isSetColumnId();
        
        /**
         * Sets the "ColumnId" element
         */
        void setColumnId(de.bund.bfr.pcml10.NameAndDatabaseId columnId);
        
        /**
         * Appends and returns a new empty "ColumnId" element
         */
        de.bund.bfr.pcml10.NameAndDatabaseId addNewColumnId();
        
        /**
         * Unsets the "ColumnId" element
         */
        void unsetColumnId();
        
        /**
         * Gets the "name" attribute
         */
        java.lang.String getName();
        
        /**
         * Gets (as xml) the "name" attribute
         */
        org.apache.xmlbeans.XmlString xgetName();
        
        /**
         * True if has "name" attribute
         */
        boolean isSetName();
        
        /**
         * Sets the "name" attribute
         */
        void setName(java.lang.String name);
        
        /**
         * Sets (as xml) the "name" attribute
         */
        void xsetName(org.apache.xmlbeans.XmlString name);
        
        /**
         * Unsets the "name" attribute
         */
        void unsetName();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static de.bund.bfr.pcml10.ColumnDocument.Column newInstance() {
              return (de.bund.bfr.pcml10.ColumnDocument.Column) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static de.bund.bfr.pcml10.ColumnDocument.Column newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (de.bund.bfr.pcml10.ColumnDocument.Column) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static de.bund.bfr.pcml10.ColumnDocument newInstance() {
          return (de.bund.bfr.pcml10.ColumnDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static de.bund.bfr.pcml10.ColumnDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (de.bund.bfr.pcml10.ColumnDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static de.bund.bfr.pcml10.ColumnDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.ColumnDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static de.bund.bfr.pcml10.ColumnDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.ColumnDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static de.bund.bfr.pcml10.ColumnDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ColumnDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static de.bund.bfr.pcml10.ColumnDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ColumnDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static de.bund.bfr.pcml10.ColumnDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ColumnDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static de.bund.bfr.pcml10.ColumnDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ColumnDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static de.bund.bfr.pcml10.ColumnDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ColumnDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static de.bund.bfr.pcml10.ColumnDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ColumnDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static de.bund.bfr.pcml10.ColumnDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ColumnDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static de.bund.bfr.pcml10.ColumnDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ColumnDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static de.bund.bfr.pcml10.ColumnDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.ColumnDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static de.bund.bfr.pcml10.ColumnDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.ColumnDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static de.bund.bfr.pcml10.ColumnDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.ColumnDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static de.bund.bfr.pcml10.ColumnDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.ColumnDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.bund.bfr.pcml10.ColumnDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.bund.bfr.pcml10.ColumnDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.bund.bfr.pcml10.ColumnDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.bund.bfr.pcml10.ColumnDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
