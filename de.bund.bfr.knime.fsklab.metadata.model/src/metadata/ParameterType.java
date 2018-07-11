/**
 */
package metadata;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Parameter Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see metadata.MetadataPackage#getParameterType()
 * @model
 * @generated
 */
public enum ParameterType implements Enumerator {
	/**
   * The '<em><b>Null</b></em>' literal object.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #NULL_VALUE
   * @generated
   * @ordered
   */
	NULL(-1, "null", "null"), /**
   * The '<em><b>Integer</b></em>' literal object.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #INTEGER_VALUE
   * @generated
   * @ordered
   */
	INTEGER(0, "Integer", "Integer"),

	/**
   * The '<em><b>Double</b></em>' literal object.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #DOUBLE_VALUE
   * @generated
   * @ordered
   */
	DOUBLE(1, "Double", "Double"),

	/**
   * The '<em><b>Number</b></em>' literal object.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #NUMBER_VALUE
   * @generated
   * @ordered
   */
	NUMBER(2, "Number", "Number"),

	/**
   * The '<em><b>Date</b></em>' literal object.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #DATE_VALUE
   * @generated
   * @ordered
   */
	DATE(3, "Date", "Date"),

	/**
   * The '<em><b>File</b></em>' literal object.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #FILE_VALUE
   * @generated
   * @ordered
   */
	FILE(4, "File", "File"),

	/**
   * The '<em><b>Boolean</b></em>' literal object.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #BOOLEAN_VALUE
   * @generated
   * @ordered
   */
	BOOLEAN(5, "Boolean", "Boolean"),

	/**
   * The '<em><b>Vector Of Numbers</b></em>' literal object.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #VECTOR_OF_NUMBERS_VALUE
   * @generated
   * @ordered
   */
	VECTOR_OF_NUMBERS(6, "VectorOfNumbers", "Vector[number]"),

	/**
   * The '<em><b>Vector Of Strings</b></em>' literal object.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #VECTOR_OF_STRINGS_VALUE
   * @generated
   * @ordered
   */
	VECTOR_OF_STRINGS(7, "VectorOfStrings", "Vector[string]"),

	/**
   * The '<em><b>Matrix Of Numbers</b></em>' literal object.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #MATRIX_OF_NUMBERS_VALUE
   * @generated
   * @ordered
   */
	MATRIX_OF_NUMBERS(8, "MatrixOfNumbers", "Matrix[number,number]"),

	/**
   * The '<em><b>Matrix Of Strings</b></em>' literal object.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #MATRIX_OF_STRINGS_VALUE
   * @generated
   * @ordered
   */
	MATRIX_OF_STRINGS(9, "MatrixOfStrings", "Matrix[string,string]"),

	/**
   * The '<em><b>Object</b></em>' literal object.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #OBJECT_VALUE
   * @generated
   * @ordered
   */
	OBJECT(10, "Object", "Object"),

	/**
   * The '<em><b>Other</b></em>' literal object.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #OTHER_VALUE
   * @generated
   * @ordered
   */
	OTHER(11, "Other", "Other"), /**
   * The '<em><b>String</b></em>' literal object.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #STRING_VALUE
   * @generated
   * @ordered
   */
	STRING(12, "String", "String");

	/**
   * The '<em><b>Null</b></em>' literal value.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Null</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @see #NULL
   * @model name="null"
   * @generated
   * @ordered
   */
	public static final int NULL_VALUE = -1;

	/**
   * The '<em><b>Integer</b></em>' literal value.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Integer</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @see #INTEGER
   * @model name="Integer"
   * @generated
   * @ordered
   */
	public static final int INTEGER_VALUE = 0;

	/**
   * The '<em><b>Double</b></em>' literal value.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Double</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @see #DOUBLE
   * @model name="Double"
   * @generated
   * @ordered
   */
	public static final int DOUBLE_VALUE = 1;

	/**
   * The '<em><b>Number</b></em>' literal value.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Number</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @see #NUMBER
   * @model name="Number"
   * @generated
   * @ordered
   */
	public static final int NUMBER_VALUE = 2;

	/**
   * The '<em><b>Date</b></em>' literal value.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Date</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @see #DATE
   * @model name="Date"
   * @generated
   * @ordered
   */
	public static final int DATE_VALUE = 3;

	/**
   * The '<em><b>File</b></em>' literal value.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>File</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @see #FILE
   * @model name="File"
   * @generated
   * @ordered
   */
	public static final int FILE_VALUE = 4;

	/**
   * The '<em><b>Boolean</b></em>' literal value.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Boolean</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @see #BOOLEAN
   * @model name="Boolean"
   * @generated
   * @ordered
   */
	public static final int BOOLEAN_VALUE = 5;

	/**
   * The '<em><b>Vector Of Numbers</b></em>' literal value.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Vector Of Numbers</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @see #VECTOR_OF_NUMBERS
   * @model name="VectorOfNumbers" literal="Vector[number]"
   * @generated
   * @ordered
   */
	public static final int VECTOR_OF_NUMBERS_VALUE = 6;

	/**
   * The '<em><b>Vector Of Strings</b></em>' literal value.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Vector Of Strings</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @see #VECTOR_OF_STRINGS
   * @model name="VectorOfStrings" literal="Vector[string]"
   * @generated
   * @ordered
   */
	public static final int VECTOR_OF_STRINGS_VALUE = 7;

	/**
   * The '<em><b>Matrix Of Numbers</b></em>' literal value.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Matrix Of Numbers</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @see #MATRIX_OF_NUMBERS
   * @model name="MatrixOfNumbers" literal="Matrix[number,number]"
   * @generated
   * @ordered
   */
	public static final int MATRIX_OF_NUMBERS_VALUE = 8;

	/**
   * The '<em><b>Matrix Of Strings</b></em>' literal value.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Matrix Of Strings</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @see #MATRIX_OF_STRINGS
   * @model name="MatrixOfStrings" literal="Matrix[string,string]"
   * @generated
   * @ordered
   */
	public static final int MATRIX_OF_STRINGS_VALUE = 9;

	/**
   * The '<em><b>Object</b></em>' literal value.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Object</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @see #OBJECT
   * @model name="Object"
   * @generated
   * @ordered
   */
	public static final int OBJECT_VALUE = 10;

	/**
   * The '<em><b>Other</b></em>' literal value.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Other</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @see #OTHER
   * @model name="Other"
   * @generated
   * @ordered
   */
	public static final int OTHER_VALUE = 11;

	/**
   * The '<em><b>String</b></em>' literal value.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>String</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @see #STRING
   * @model name="String"
   * @generated
   * @ordered
   */
	public static final int STRING_VALUE = 12;

	/**
   * An array of all the '<em><b>Parameter Type</b></em>' enumerators.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	private static final ParameterType[] VALUES_ARRAY =
		new ParameterType[] {
      NULL,
      INTEGER,
      DOUBLE,
      NUMBER,
      DATE,
      FILE,
      BOOLEAN,
      VECTOR_OF_NUMBERS,
      VECTOR_OF_STRINGS,
      MATRIX_OF_NUMBERS,
      MATRIX_OF_STRINGS,
      OBJECT,
      OTHER,
      STRING,
    };

	/**
   * A public read-only list of all the '<em><b>Parameter Type</b></em>' enumerators.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public static final List<ParameterType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
   * Returns the '<em><b>Parameter Type</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param literal the literal.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
	public static ParameterType get(String literal) {
    for (int i = 0; i < VALUES_ARRAY.length; ++i) {
      ParameterType result = VALUES_ARRAY[i];
      if (result.toString().equals(literal)) {
        return result;
      }
    }
    return null;
  }

	/**
   * Returns the '<em><b>Parameter Type</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param name the name.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
	public static ParameterType getByName(String name) {
    for (int i = 0; i < VALUES_ARRAY.length; ++i) {
      ParameterType result = VALUES_ARRAY[i];
      if (result.getName().equals(name)) {
        return result;
      }
    }
    return null;
  }

	/**
   * Returns the '<em><b>Parameter Type</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the integer value.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
	public static ParameterType get(int value) {
    switch (value) {
      case NULL_VALUE: return NULL;
      case INTEGER_VALUE: return INTEGER;
      case DOUBLE_VALUE: return DOUBLE;
      case NUMBER_VALUE: return NUMBER;
      case DATE_VALUE: return DATE;
      case FILE_VALUE: return FILE;
      case BOOLEAN_VALUE: return BOOLEAN;
      case VECTOR_OF_NUMBERS_VALUE: return VECTOR_OF_NUMBERS;
      case VECTOR_OF_STRINGS_VALUE: return VECTOR_OF_STRINGS;
      case MATRIX_OF_NUMBERS_VALUE: return MATRIX_OF_NUMBERS;
      case MATRIX_OF_STRINGS_VALUE: return MATRIX_OF_STRINGS;
      case OBJECT_VALUE: return OBJECT;
      case OTHER_VALUE: return OTHER;
      case STRING_VALUE: return STRING;
    }
    return null;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	private final int value;

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	private final String name;

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	private final String literal;

	/**
   * Only this class can construct instances.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	private ParameterType(int value, String name, String literal) {
    this.value = value;
    this.name = name;
    this.literal = literal;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public int getValue() {
    return value;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getName() {
    return name;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getLiteral() {
    return literal;
  }

	/**
   * Returns the literal value of the enumerator, which is its string representation.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public String toString() {
    return literal;
  }
	
} //ParameterType
