/*
 * An XML document type.
 * Localname: ProcessNode
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.ProcessNodeDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10;


/**
 * A document containing one ProcessNode(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public interface ProcessNodeDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ProcessNodeDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s121DA543B1FCBC764F9DFAF30A6E9DAF").resolveHandle("processnode1eb1doctype");
    
    /**
     * Gets the "ProcessNode" element
     */
    de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode getProcessNode();
    
    /**
     * Sets the "ProcessNode" element
     */
    void setProcessNode(de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode processNode);
    
    /**
     * Appends and returns a new empty "ProcessNode" element
     */
    de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode addNewProcessNode();
    
    /**
     * An XML ProcessNode(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public interface ProcessNode extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ProcessNode.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s121DA543B1FCBC764F9DFAF30A6E9DAF").resolveHandle("processnode854eelemtype");
        
        /**
         * Gets the "Process" element
         */
        de.bund.bfr.pcml10.NameAndDatabaseId getProcess();
        
        /**
         * Sets the "Process" element
         */
        void setProcess(de.bund.bfr.pcml10.NameAndDatabaseId process);
        
        /**
         * Appends and returns a new empty "Process" element
         */
        de.bund.bfr.pcml10.NameAndDatabaseId addNewProcess();
        
        /**
         * Gets the "Parameters" element
         */
        de.bund.bfr.pcml10.ProcessParameters getParameters();
        
        /**
         * Sets the "Parameters" element
         */
        void setParameters(de.bund.bfr.pcml10.ProcessParameters parameters);
        
        /**
         * Appends and returns a new empty "Parameters" element
         */
        de.bund.bfr.pcml10.ProcessParameters addNewParameters();
        
        /**
         * Gets array of all "Inport" elements
         */
        de.bund.bfr.pcml10.InportDocument.Inport[] getInportArray();
        
        /**
         * Gets ith "Inport" element
         */
        de.bund.bfr.pcml10.InportDocument.Inport getInportArray(int i);
        
        /**
         * Returns number of "Inport" element
         */
        int sizeOfInportArray();
        
        /**
         * Sets array of all "Inport" element
         */
        void setInportArray(de.bund.bfr.pcml10.InportDocument.Inport[] inportArray);
        
        /**
         * Sets ith "Inport" element
         */
        void setInportArray(int i, de.bund.bfr.pcml10.InportDocument.Inport inport);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "Inport" element
         */
        de.bund.bfr.pcml10.InportDocument.Inport insertNewInport(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "Inport" element
         */
        de.bund.bfr.pcml10.InportDocument.Inport addNewInport();
        
        /**
         * Removes the ith "Inport" element
         */
        void removeInport(int i);
        
        /**
         * Gets array of all "Outport" elements
         */
        de.bund.bfr.pcml10.OutportDocument.Outport[] getOutportArray();
        
        /**
         * Gets ith "Outport" element
         */
        de.bund.bfr.pcml10.OutportDocument.Outport getOutportArray(int i);
        
        /**
         * Returns number of "Outport" element
         */
        int sizeOfOutportArray();
        
        /**
         * Sets array of all "Outport" element
         */
        void setOutportArray(de.bund.bfr.pcml10.OutportDocument.Outport[] outportArray);
        
        /**
         * Sets ith "Outport" element
         */
        void setOutportArray(int i, de.bund.bfr.pcml10.OutportDocument.Outport outport);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "Outport" element
         */
        de.bund.bfr.pcml10.OutportDocument.Outport insertNewOutport(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "Outport" element
         */
        de.bund.bfr.pcml10.OutportDocument.Outport addNewOutport();
        
        /**
         * Removes the ith "Outport" element
         */
        void removeOutport(int i);
        
        /**
         * Gets the "id" attribute
         */
        java.lang.String getId();
        
        /**
         * Gets (as xml) the "id" attribute
         */
        org.apache.xmlbeans.XmlString xgetId();
        
        /**
         * Sets the "id" attribute
         */
        void setId(java.lang.String id);
        
        /**
         * Sets (as xml) the "id" attribute
         */
        void xsetId(org.apache.xmlbeans.XmlString id);
        
        /**
         * Gets the "type" attribute
         */
        de.bund.bfr.pcml10.ProcessNodeType.Enum getType();
        
        /**
         * Gets (as xml) the "type" attribute
         */
        de.bund.bfr.pcml10.ProcessNodeType xgetType();
        
        /**
         * True if has "type" attribute
         */
        boolean isSetType();
        
        /**
         * Sets the "type" attribute
         */
        void setType(de.bund.bfr.pcml10.ProcessNodeType.Enum type);
        
        /**
         * Sets (as xml) the "type" attribute
         */
        void xsetType(de.bund.bfr.pcml10.ProcessNodeType type);
        
        /**
         * Unsets the "type" attribute
         */
        void unsetType();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode newInstance() {
              return (de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static de.bund.bfr.pcml10.ProcessNodeDocument newInstance() {
          return (de.bund.bfr.pcml10.ProcessNodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static de.bund.bfr.pcml10.ProcessNodeDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (de.bund.bfr.pcml10.ProcessNodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static de.bund.bfr.pcml10.ProcessNodeDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.ProcessNodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static de.bund.bfr.pcml10.ProcessNodeDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.ProcessNodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static de.bund.bfr.pcml10.ProcessNodeDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ProcessNodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static de.bund.bfr.pcml10.ProcessNodeDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ProcessNodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static de.bund.bfr.pcml10.ProcessNodeDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ProcessNodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static de.bund.bfr.pcml10.ProcessNodeDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ProcessNodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static de.bund.bfr.pcml10.ProcessNodeDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ProcessNodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static de.bund.bfr.pcml10.ProcessNodeDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ProcessNodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static de.bund.bfr.pcml10.ProcessNodeDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ProcessNodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static de.bund.bfr.pcml10.ProcessNodeDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ProcessNodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static de.bund.bfr.pcml10.ProcessNodeDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.ProcessNodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static de.bund.bfr.pcml10.ProcessNodeDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.ProcessNodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static de.bund.bfr.pcml10.ProcessNodeDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.ProcessNodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static de.bund.bfr.pcml10.ProcessNodeDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.ProcessNodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.bund.bfr.pcml10.ProcessNodeDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.bund.bfr.pcml10.ProcessNodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.bund.bfr.pcml10.ProcessNodeDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.bund.bfr.pcml10.ProcessNodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
