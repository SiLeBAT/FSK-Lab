/*
 * An XML document type.
 * Localname: ChildParent
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.ChildParentDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10;


/**
 * A document containing one ChildParent(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public interface ChildParentDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ChildParentDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s4077A4D9BB1020F5A2B1C66D5D7AA2C8").resolveHandle("childparentbe86doctype");
    
    /**
     * Gets the "ChildParent" element
     */
    de.bund.bfr.pcml10.ChildParentDocument.ChildParent getChildParent();
    
    /**
     * Sets the "ChildParent" element
     */
    void setChildParent(de.bund.bfr.pcml10.ChildParentDocument.ChildParent childParent);
    
    /**
     * Appends and returns a new empty "ChildParent" element
     */
    de.bund.bfr.pcml10.ChildParentDocument.ChildParent addNewChildParent();
    
    /**
     * An XML ChildParent(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public interface ChildParent extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ChildParent.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s4077A4D9BB1020F5A2B1C66D5D7AA2C8").resolveHandle("childparentec38elemtype");
        
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
         * Gets the "TableLocator" element
         */
        de.bund.bfr.pcml10.TableLocatorDocument.TableLocator getTableLocator();
        
        /**
         * True if has "TableLocator" element
         */
        boolean isSetTableLocator();
        
        /**
         * Sets the "TableLocator" element
         */
        void setTableLocator(de.bund.bfr.pcml10.TableLocatorDocument.TableLocator tableLocator);
        
        /**
         * Appends and returns a new empty "TableLocator" element
         */
        de.bund.bfr.pcml10.TableLocatorDocument.TableLocator addNewTableLocator();
        
        /**
         * Unsets the "TableLocator" element
         */
        void unsetTableLocator();
        
        /**
         * Gets the "InlineTable" element
         */
        de.bund.bfr.pcml10.InlineTableDocument.InlineTable getInlineTable();
        
        /**
         * True if has "InlineTable" element
         */
        boolean isSetInlineTable();
        
        /**
         * Sets the "InlineTable" element
         */
        void setInlineTable(de.bund.bfr.pcml10.InlineTableDocument.InlineTable inlineTable);
        
        /**
         * Appends and returns a new empty "InlineTable" element
         */
        de.bund.bfr.pcml10.InlineTableDocument.InlineTable addNewInlineTable();
        
        /**
         * Unsets the "InlineTable" element
         */
        void unsetInlineTable();
        
        /**
         * Gets the "childField" attribute
         */
        java.lang.String getChildField();
        
        /**
         * Gets (as xml) the "childField" attribute
         */
        org.apache.xmlbeans.XmlString xgetChildField();
        
        /**
         * Sets the "childField" attribute
         */
        void setChildField(java.lang.String childField);
        
        /**
         * Sets (as xml) the "childField" attribute
         */
        void xsetChildField(org.apache.xmlbeans.XmlString childField);
        
        /**
         * Gets the "parentField" attribute
         */
        java.lang.String getParentField();
        
        /**
         * Gets (as xml) the "parentField" attribute
         */
        org.apache.xmlbeans.XmlString xgetParentField();
        
        /**
         * Sets the "parentField" attribute
         */
        void setParentField(java.lang.String parentField);
        
        /**
         * Sets (as xml) the "parentField" attribute
         */
        void xsetParentField(org.apache.xmlbeans.XmlString parentField);
        
        /**
         * Gets the "parentLevelField" attribute
         */
        java.lang.String getParentLevelField();
        
        /**
         * Gets (as xml) the "parentLevelField" attribute
         */
        org.apache.xmlbeans.XmlString xgetParentLevelField();
        
        /**
         * True if has "parentLevelField" attribute
         */
        boolean isSetParentLevelField();
        
        /**
         * Sets the "parentLevelField" attribute
         */
        void setParentLevelField(java.lang.String parentLevelField);
        
        /**
         * Sets (as xml) the "parentLevelField" attribute
         */
        void xsetParentLevelField(org.apache.xmlbeans.XmlString parentLevelField);
        
        /**
         * Unsets the "parentLevelField" attribute
         */
        void unsetParentLevelField();
        
        /**
         * Gets the "isRecursive" attribute
         */
        de.bund.bfr.pcml10.ChildParentDocument.ChildParent.IsRecursive.Enum getIsRecursive();
        
        /**
         * Gets (as xml) the "isRecursive" attribute
         */
        de.bund.bfr.pcml10.ChildParentDocument.ChildParent.IsRecursive xgetIsRecursive();
        
        /**
         * True if has "isRecursive" attribute
         */
        boolean isSetIsRecursive();
        
        /**
         * Sets the "isRecursive" attribute
         */
        void setIsRecursive(de.bund.bfr.pcml10.ChildParentDocument.ChildParent.IsRecursive.Enum isRecursive);
        
        /**
         * Sets (as xml) the "isRecursive" attribute
         */
        void xsetIsRecursive(de.bund.bfr.pcml10.ChildParentDocument.ChildParent.IsRecursive isRecursive);
        
        /**
         * Unsets the "isRecursive" attribute
         */
        void unsetIsRecursive();
        
        /**
         * An XML isRecursive(@).
         *
         * This is an atomic type that is a restriction of de.bund.bfr.pcml10.ChildParentDocument$ChildParent$IsRecursive.
         */
        public interface IsRecursive extends org.apache.xmlbeans.XmlString
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(IsRecursive.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s4077A4D9BB1020F5A2B1C66D5D7AA2C8").resolveHandle("isrecursivee8c8attrtype");
            
            org.apache.xmlbeans.StringEnumAbstractBase enumValue();
            void set(org.apache.xmlbeans.StringEnumAbstractBase e);
            
            static final Enum NO = Enum.forString("no");
            static final Enum YES = Enum.forString("yes");
            
            static final int INT_NO = Enum.INT_NO;
            static final int INT_YES = Enum.INT_YES;
            
            /**
             * Enumeration value class for de.bund.bfr.pcml10.ChildParentDocument$ChildParent$IsRecursive.
             * These enum values can be used as follows:
             * <pre>
             * enum.toString(); // returns the string value of the enum
             * enum.intValue(); // returns an int value, useful for switches
             * // e.g., case Enum.INT_NO
             * Enum.forString(s); // returns the enum value for a string
             * Enum.forInt(i); // returns the enum value for an int
             * </pre>
             * Enumeration objects are immutable singleton objects that
             * can be compared using == object equality. They have no
             * public constructor. See the constants defined within this
             * class for all the valid values.
             */
            static final class Enum extends org.apache.xmlbeans.StringEnumAbstractBase
            {
                /**
                 * Returns the enum value for a string, or null if none.
                 */
                public static Enum forString(java.lang.String s)
                    { return (Enum)table.forString(s); }
                /**
                 * Returns the enum value corresponding to an int, or null if none.
                 */
                public static Enum forInt(int i)
                    { return (Enum)table.forInt(i); }
                
                private Enum(java.lang.String s, int i)
                    { super(s, i); }
                
                static final int INT_NO = 1;
                static final int INT_YES = 2;
                
                public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
                    new org.apache.xmlbeans.StringEnumAbstractBase.Table
                (
                    new Enum[]
                    {
                      new Enum("no", INT_NO),
                      new Enum("yes", INT_YES),
                    }
                );
                private static final long serialVersionUID = 1L;
                private java.lang.Object readResolve() { return forInt(intValue()); } 
            }
            
            /**
             * A factory class with static methods for creating instances
             * of this type.
             */
            
            public static final class Factory
            {
                public static de.bund.bfr.pcml10.ChildParentDocument.ChildParent.IsRecursive newValue(java.lang.Object obj) {
                  return (de.bund.bfr.pcml10.ChildParentDocument.ChildParent.IsRecursive) type.newValue( obj ); }
                
                public static de.bund.bfr.pcml10.ChildParentDocument.ChildParent.IsRecursive newInstance() {
                  return (de.bund.bfr.pcml10.ChildParentDocument.ChildParent.IsRecursive) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static de.bund.bfr.pcml10.ChildParentDocument.ChildParent.IsRecursive newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (de.bund.bfr.pcml10.ChildParentDocument.ChildParent.IsRecursive) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static de.bund.bfr.pcml10.ChildParentDocument.ChildParent newInstance() {
              return (de.bund.bfr.pcml10.ChildParentDocument.ChildParent) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static de.bund.bfr.pcml10.ChildParentDocument.ChildParent newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (de.bund.bfr.pcml10.ChildParentDocument.ChildParent) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static de.bund.bfr.pcml10.ChildParentDocument newInstance() {
          return (de.bund.bfr.pcml10.ChildParentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static de.bund.bfr.pcml10.ChildParentDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (de.bund.bfr.pcml10.ChildParentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static de.bund.bfr.pcml10.ChildParentDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.ChildParentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static de.bund.bfr.pcml10.ChildParentDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.ChildParentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static de.bund.bfr.pcml10.ChildParentDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ChildParentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static de.bund.bfr.pcml10.ChildParentDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ChildParentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static de.bund.bfr.pcml10.ChildParentDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ChildParentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static de.bund.bfr.pcml10.ChildParentDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ChildParentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static de.bund.bfr.pcml10.ChildParentDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ChildParentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static de.bund.bfr.pcml10.ChildParentDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ChildParentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static de.bund.bfr.pcml10.ChildParentDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ChildParentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static de.bund.bfr.pcml10.ChildParentDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.bund.bfr.pcml10.ChildParentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static de.bund.bfr.pcml10.ChildParentDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.ChildParentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static de.bund.bfr.pcml10.ChildParentDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.ChildParentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static de.bund.bfr.pcml10.ChildParentDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.ChildParentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static de.bund.bfr.pcml10.ChildParentDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.bund.bfr.pcml10.ChildParentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.bund.bfr.pcml10.ChildParentDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.bund.bfr.pcml10.ChildParentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.bund.bfr.pcml10.ChildParentDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.bund.bfr.pcml10.ChildParentDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
