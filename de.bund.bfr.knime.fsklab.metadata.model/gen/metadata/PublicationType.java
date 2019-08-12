/**
 */
package metadata;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Publication Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see metadata.MetadataPackage#getPublicationType()
 * @model
 * @generated
 */
public enum PublicationType implements Enumerator {
	/**
	 * The '<em><b>Null</b></em>' literal object.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #NULL_VALUE
	 * @generated
	 * @ordered
	 */
  NULL(-1, "null", "null"), /**
	 * The '<em><b>ABST</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ABST_VALUE
	 * @generated
	 * @ordered
	 */
	ABST(0, "ABST", "Abstract"),

	/**
	 * The '<em><b>ADVS</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ADVS_VALUE
	 * @generated
	 * @ordered
	 */
	ADVS(1, "ADVS", "Audiovisual material"),

	/**
	 * The '<em><b>AGGR</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #AGGR_VALUE
	 * @generated
	 * @ordered
	 */
	AGGR(2, "AGGR", "Aggregated Database"),

	/**
	 * The '<em><b>ANCIENT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ANCIENT_VALUE
	 * @generated
	 * @ordered
	 */
	ANCIENT(3, "ANCIENT", "Ancient Text"),

	/**
	 * The '<em><b>ART</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ART_VALUE
	 * @generated
	 * @ordered
	 */
	ART(4, "ART", "Art Work"),

	/**
	 * The '<em><b>BILL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BILL_VALUE
	 * @generated
	 * @ordered
	 */
	BILL(5, "BILL", "Bill"),

	/**
	 * The '<em><b>BLOG</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BLOG_VALUE
	 * @generated
	 * @ordered
	 */
	BLOG(6, "BLOG", "Blog"),

	/**
	 * The '<em><b>BOOK</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BOOK_VALUE
	 * @generated
	 * @ordered
	 */
	BOOK(7, "BOOK", "Whole book"),

	/**
	 * The '<em><b>CASE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CASE_VALUE
	 * @generated
	 * @ordered
	 */
	CASE(8, "CASE", "Case"),

	/**
	 * The '<em><b>CHAP</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CHAP_VALUE
	 * @generated
	 * @ordered
	 */
	CHAP(9, "CHAP", "Book chapter"),

	/**
	 * The '<em><b>CHART</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CHART_VALUE
	 * @generated
	 * @ordered
	 */
	CHART(10, "CHART", "Chart"),

	/**
	 * The '<em><b>CLSWK</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CLSWK_VALUE
	 * @generated
	 * @ordered
	 */
	CLSWK(11, "CLSWK", "Classical Work"),

	/**
	 * The '<em><b>COMP</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #COMP_VALUE
	 * @generated
	 * @ordered
	 */
	COMP(12, "COMP", "Computer Program"),

	/**
	 * The '<em><b>CONF</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CONF_VALUE
	 * @generated
	 * @ordered
	 */
	CONF(13, "CONF", "Conference proceeding"),

	/**
	 * The '<em><b>CPAPER</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CPAPER_VALUE
	 * @generated
	 * @ordered
	 */
	CPAPER(14, "CPAPER", "Conference paper"),

	/**
	 * The '<em><b>CTLG</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CTLG_VALUE
	 * @generated
	 * @ordered
	 */
	CTLG(15, "CTLG", "Catalog"),

	/**
	 * The '<em><b>DATA</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DATA_VALUE
	 * @generated
	 * @ordered
	 */
	DATA(16, "DATA", "Data file"),

	/**
	 * The '<em><b>DBASE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DBASE_VALUE
	 * @generated
	 * @ordered
	 */
	DBASE(17, "DBASE", "Online Database"),

	/**
	 * The '<em><b>DICT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DICT_VALUE
	 * @generated
	 * @ordered
	 */
	DICT(18, "DICT", "Dictionary"),

	/**
	 * The '<em><b>EBOOK</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #EBOOK_VALUE
	 * @generated
	 * @ordered
	 */
	EBOOK(19, "EBOOK", "Electronic Book"),

	/**
	 * The '<em><b>ECHAP</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ECHAP_VALUE
	 * @generated
	 * @ordered
	 */
	ECHAP(20, "ECHAP", "Electronic Book Section"),

	/**
	 * The '<em><b>EDBOOK</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #EDBOOK_VALUE
	 * @generated
	 * @ordered
	 */
	EDBOOK(21, "EDBOOK", "Edited Book"),

	/**
	 * The '<em><b>EJOUR</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #EJOUR_VALUE
	 * @generated
	 * @ordered
	 */
	EJOUR(22, "EJOUR", "Electronic Article"),

	/**
	 * The '<em><b>ELECT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ELECT_VALUE
	 * @generated
	 * @ordered
	 */
	ELECT(23, "ELECT", "Web Page"),

	/**
	 * The '<em><b>ENCYC</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ENCYC_VALUE
	 * @generated
	 * @ordered
	 */
	ENCYC(24, "ENCYC", "Encyclopedia"),

	/**
	 * The '<em><b>EQUA</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #EQUA_VALUE
	 * @generated
	 * @ordered
	 */
	EQUA(25, "EQUA", "Equation"),

	/**
	 * The '<em><b>FIGURE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #FIGURE_VALUE
	 * @generated
	 * @ordered
	 */
	FIGURE(26, "FIGURE", "Figure"),

	/**
	 * The '<em><b>GEN</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #GEN_VALUE
	 * @generated
	 * @ordered
	 */
	GEN(27, "GEN", "Generic"),

	/**
	 * The '<em><b>GOVDOC</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #GOVDOC_VALUE
	 * @generated
	 * @ordered
	 */
	GOVDOC(28, "GOVDOC", "Government Document"),

	/**
	 * The '<em><b>GRANT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #GRANT_VALUE
	 * @generated
	 * @ordered
	 */
	GRANT(29, "GRANT", "Grant"),

	/**
	 * The '<em><b>HEAR</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #HEAR_VALUE
	 * @generated
	 * @ordered
	 */
	HEAR(30, "HEAR", "Hearing"),

	/**
	 * The '<em><b>ICOMM</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ICOMM_VALUE
	 * @generated
	 * @ordered
	 */
	ICOMM(31, "ICOMM", "Internet Communication"),

	/**
	 * The '<em><b>INPR</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #INPR_VALUE
	 * @generated
	 * @ordered
	 */
	INPR(32, "INPR", "In Press"),

	/**
	 * The '<em><b>JOUR</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #JOUR_VALUE
	 * @generated
	 * @ordered
	 */
	JOUR(33, "JOUR", "Journal"),

	/**
	 * The '<em><b>JFULL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #JFULL_VALUE
	 * @generated
	 * @ordered
	 */
	JFULL(34, "JFULL", "Journal (full)"),

	/**
	 * The '<em><b>LEGAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #LEGAL_VALUE
	 * @generated
	 * @ordered
	 */
	LEGAL(35, "LEGAL", "Legal Rule or Regulation"),

	/**
	 * The '<em><b>MANSCPT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MANSCPT_VALUE
	 * @generated
	 * @ordered
	 */
	MANSCPT(36, "MANSCPT", "Manuscript"),

	/**
	 * The '<em><b>MAP</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MAP_VALUE
	 * @generated
	 * @ordered
	 */
	MAP(37, "MAP", "Map"),

	/**
	 * The '<em><b>MGZN</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MGZN_VALUE
	 * @generated
	 * @ordered
	 */
	MGZN(38, "MGZN", "Magazine article"),

	/**
	 * The '<em><b>MPCT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MPCT_VALUE
	 * @generated
	 * @ordered
	 */
	MPCT(39, "MPCT", "Motion picture"),

	/**
	 * The '<em><b>MULTI</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MULTI_VALUE
	 * @generated
	 * @ordered
	 */
	MULTI(40, "MULTI", "Online Multimedia"),

	/**
	 * The '<em><b>MUSIC</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MUSIC_VALUE
	 * @generated
	 * @ordered
	 */
	MUSIC(41, "MUSIC", "Music score"),

	/**
	 * The '<em><b>NEWS</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NEWS_VALUE
	 * @generated
	 * @ordered
	 */
	NEWS(42, "NEWS", "Newspaper"),

	/**
	 * The '<em><b>PAMP</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PAMP_VALUE
	 * @generated
	 * @ordered
	 */
	PAMP(43, "PAMP", "Pamphlet"),

	/**
	 * The '<em><b>PAT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PAT_VALUE
	 * @generated
	 * @ordered
	 */
	PAT(44, "PAT", "Patent"),

	/**
	 * The '<em><b>PCOMM</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PCOMM_VALUE
	 * @generated
	 * @ordered
	 */
	PCOMM(45, "PCOMM", "Personal communication"),

	/**
	 * The '<em><b>RPRT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #RPRT_VALUE
	 * @generated
	 * @ordered
	 */
	RPRT(46, "RPRT", "Report"),

	/**
	 * The '<em><b>SER</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SER_VALUE
	 * @generated
	 * @ordered
	 */
	SER(47, "SER", "Serial publication"),

	/**
	 * The '<em><b>SLIDE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SLIDE_VALUE
	 * @generated
	 * @ordered
	 */
	SLIDE(48, "SLIDE", "Slide"),

	/**
	 * The '<em><b>SOUND</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SOUND_VALUE
	 * @generated
	 * @ordered
	 */
	SOUND(49, "SOUND", "Sound recording"),

	/**
	 * The '<em><b>STAND</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #STAND_VALUE
	 * @generated
	 * @ordered
	 */
	STAND(50, "STAND", "Standard"),

	/**
	 * The '<em><b>STAT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #STAT_VALUE
	 * @generated
	 * @ordered
	 */
	STAT(51, "STAT", "Statute"),

	/**
	 * The '<em><b>THES</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #THES_VALUE
	 * @generated
	 * @ordered
	 */
	THES(52, "THES", "Thesis/Dissertation"),

	/**
	 * The '<em><b>UNPB</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #UNPB_VALUE
	 * @generated
	 * @ordered
	 */
	UNPB(53, "UNPB", "Unpublished work"),

	/**
	 * The '<em><b>VIDEO</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #VIDEO_VALUE
	 * @generated
	 * @ordered
	 */
	VIDEO(54, "VIDEO", "Video recording");

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
	 * The '<em><b>ABST</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ABST</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ABST
	 * @model literal="Abstract"
	 * @generated
	 * @ordered
	 */
	public static final int ABST_VALUE = 0;

	/**
	 * The '<em><b>ADVS</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ADVS</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ADVS
	 * @model literal="Audiovisual material"
	 * @generated
	 * @ordered
	 */
	public static final int ADVS_VALUE = 1;

	/**
	 * The '<em><b>AGGR</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>AGGR</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #AGGR
	 * @model literal="Aggregated Database"
	 * @generated
	 * @ordered
	 */
	public static final int AGGR_VALUE = 2;

	/**
	 * The '<em><b>ANCIENT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ANCIENT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ANCIENT
	 * @model literal="Ancient Text"
	 * @generated
	 * @ordered
	 */
	public static final int ANCIENT_VALUE = 3;

	/**
	 * The '<em><b>ART</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ART</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ART
	 * @model literal="Art Work"
	 * @generated
	 * @ordered
	 */
	public static final int ART_VALUE = 4;

	/**
	 * The '<em><b>BILL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>BILL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BILL
	 * @model literal="Bill"
	 * @generated
	 * @ordered
	 */
	public static final int BILL_VALUE = 5;

	/**
	 * The '<em><b>BLOG</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>BLOG</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BLOG
	 * @model literal="Blog"
	 * @generated
	 * @ordered
	 */
	public static final int BLOG_VALUE = 6;

	/**
	 * The '<em><b>BOOK</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>BOOK</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BOOK
	 * @model literal="Whole book"
	 * @generated
	 * @ordered
	 */
	public static final int BOOK_VALUE = 7;

	/**
	 * The '<em><b>CASE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>CASE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CASE
	 * @model literal="Case"
	 * @generated
	 * @ordered
	 */
	public static final int CASE_VALUE = 8;

	/**
	 * The '<em><b>CHAP</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>CHAP</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CHAP
	 * @model literal="Book chapter"
	 * @generated
	 * @ordered
	 */
	public static final int CHAP_VALUE = 9;

	/**
	 * The '<em><b>CHART</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>CHART</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CHART
	 * @model literal="Chart"
	 * @generated
	 * @ordered
	 */
	public static final int CHART_VALUE = 10;

	/**
	 * The '<em><b>CLSWK</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>CLSWK</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CLSWK
	 * @model literal="Classical Work"
	 * @generated
	 * @ordered
	 */
	public static final int CLSWK_VALUE = 11;

	/**
	 * The '<em><b>COMP</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>COMP</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #COMP
	 * @model literal="Computer Program"
	 * @generated
	 * @ordered
	 */
	public static final int COMP_VALUE = 12;

	/**
	 * The '<em><b>CONF</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>CONF</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CONF
	 * @model literal="Conference proceeding"
	 * @generated
	 * @ordered
	 */
	public static final int CONF_VALUE = 13;

	/**
	 * The '<em><b>CPAPER</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>CPAPER</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CPAPER
	 * @model literal="Conference paper"
	 * @generated
	 * @ordered
	 */
	public static final int CPAPER_VALUE = 14;

	/**
	 * The '<em><b>CTLG</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>CTLG</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CTLG
	 * @model literal="Catalog"
	 * @generated
	 * @ordered
	 */
	public static final int CTLG_VALUE = 15;

	/**
	 * The '<em><b>DATA</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DATA</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DATA
	 * @model literal="Data file"
	 * @generated
	 * @ordered
	 */
	public static final int DATA_VALUE = 16;

	/**
	 * The '<em><b>DBASE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DBASE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DBASE
	 * @model literal="Online Database"
	 * @generated
	 * @ordered
	 */
	public static final int DBASE_VALUE = 17;

	/**
	 * The '<em><b>DICT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DICT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DICT
	 * @model literal="Dictionary"
	 * @generated
	 * @ordered
	 */
	public static final int DICT_VALUE = 18;

	/**
	 * The '<em><b>EBOOK</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>EBOOK</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #EBOOK
	 * @model literal="Electronic Book"
	 * @generated
	 * @ordered
	 */
	public static final int EBOOK_VALUE = 19;

	/**
	 * The '<em><b>ECHAP</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ECHAP</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ECHAP
	 * @model literal="Electronic Book Section"
	 * @generated
	 * @ordered
	 */
	public static final int ECHAP_VALUE = 20;

	/**
	 * The '<em><b>EDBOOK</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>EDBOOK</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #EDBOOK
	 * @model literal="Edited Book"
	 * @generated
	 * @ordered
	 */
	public static final int EDBOOK_VALUE = 21;

	/**
	 * The '<em><b>EJOUR</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>EJOUR</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #EJOUR
	 * @model literal="Electronic Article"
	 * @generated
	 * @ordered
	 */
	public static final int EJOUR_VALUE = 22;

	/**
	 * The '<em><b>ELECT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ELECT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ELECT
	 * @model literal="Web Page"
	 * @generated
	 * @ordered
	 */
	public static final int ELECT_VALUE = 23;

	/**
	 * The '<em><b>ENCYC</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ENCYC</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ENCYC
	 * @model literal="Encyclopedia"
	 * @generated
	 * @ordered
	 */
	public static final int ENCYC_VALUE = 24;

	/**
	 * The '<em><b>EQUA</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>EQUA</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #EQUA
	 * @model literal="Equation"
	 * @generated
	 * @ordered
	 */
	public static final int EQUA_VALUE = 25;

	/**
	 * The '<em><b>FIGURE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>FIGURE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #FIGURE
	 * @model literal="Figure"
	 * @generated
	 * @ordered
	 */
	public static final int FIGURE_VALUE = 26;

	/**
	 * The '<em><b>GEN</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>GEN</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #GEN
	 * @model literal="Generic"
	 * @generated
	 * @ordered
	 */
	public static final int GEN_VALUE = 27;

	/**
	 * The '<em><b>GOVDOC</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>GOVDOC</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #GOVDOC
	 * @model literal="Government Document"
	 * @generated
	 * @ordered
	 */
	public static final int GOVDOC_VALUE = 28;

	/**
	 * The '<em><b>GRANT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>GRANT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #GRANT
	 * @model literal="Grant"
	 * @generated
	 * @ordered
	 */
	public static final int GRANT_VALUE = 29;

	/**
	 * The '<em><b>HEAR</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>HEAR</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #HEAR
	 * @model literal="Hearing"
	 * @generated
	 * @ordered
	 */
	public static final int HEAR_VALUE = 30;

	/**
	 * The '<em><b>ICOMM</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ICOMM</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ICOMM
	 * @model literal="Internet Communication"
	 * @generated
	 * @ordered
	 */
	public static final int ICOMM_VALUE = 31;

	/**
	 * The '<em><b>INPR</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>INPR</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #INPR
	 * @model literal="In Press"
	 * @generated
	 * @ordered
	 */
	public static final int INPR_VALUE = 32;

	/**
	 * The '<em><b>JOUR</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>JOUR</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #JOUR
	 * @model literal="Journal"
	 * @generated
	 * @ordered
	 */
	public static final int JOUR_VALUE = 33;

	/**
	 * The '<em><b>JFULL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>JFULL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #JFULL
	 * @model literal="Journal (full)"
	 * @generated
	 * @ordered
	 */
	public static final int JFULL_VALUE = 34;

	/**
	 * The '<em><b>LEGAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>LEGAL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #LEGAL
	 * @model literal="Legal Rule or Regulation"
	 * @generated
	 * @ordered
	 */
	public static final int LEGAL_VALUE = 35;

	/**
	 * The '<em><b>MANSCPT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MANSCPT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MANSCPT
	 * @model literal="Manuscript"
	 * @generated
	 * @ordered
	 */
	public static final int MANSCPT_VALUE = 36;

	/**
	 * The '<em><b>MAP</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MAP</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MAP
	 * @model literal="Map"
	 * @generated
	 * @ordered
	 */
	public static final int MAP_VALUE = 37;

	/**
	 * The '<em><b>MGZN</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MGZN</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MGZN
	 * @model literal="Magazine article"
	 * @generated
	 * @ordered
	 */
	public static final int MGZN_VALUE = 38;

	/**
	 * The '<em><b>MPCT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MPCT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MPCT
	 * @model literal="Motion picture"
	 * @generated
	 * @ordered
	 */
	public static final int MPCT_VALUE = 39;

	/**
	 * The '<em><b>MULTI</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MULTI</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MULTI
	 * @model literal="Online Multimedia"
	 * @generated
	 * @ordered
	 */
	public static final int MULTI_VALUE = 40;

	/**
	 * The '<em><b>MUSIC</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MUSIC</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MUSIC
	 * @model literal="Music score"
	 * @generated
	 * @ordered
	 */
	public static final int MUSIC_VALUE = 41;

	/**
	 * The '<em><b>NEWS</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>NEWS</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #NEWS
	 * @model literal="Newspaper"
	 * @generated
	 * @ordered
	 */
	public static final int NEWS_VALUE = 42;

	/**
	 * The '<em><b>PAMP</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>PAMP</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #PAMP
	 * @model literal="Pamphlet"
	 * @generated
	 * @ordered
	 */
	public static final int PAMP_VALUE = 43;

	/**
	 * The '<em><b>PAT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>PAT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #PAT
	 * @model literal="Patent"
	 * @generated
	 * @ordered
	 */
	public static final int PAT_VALUE = 44;

	/**
	 * The '<em><b>PCOMM</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>PCOMM</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #PCOMM
	 * @model literal="Personal communication"
	 * @generated
	 * @ordered
	 */
	public static final int PCOMM_VALUE = 45;

	/**
	 * The '<em><b>RPRT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>RPRT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #RPRT
	 * @model literal="Report"
	 * @generated
	 * @ordered
	 */
	public static final int RPRT_VALUE = 46;

	/**
	 * The '<em><b>SER</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>SER</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SER
	 * @model literal="Serial publication"
	 * @generated
	 * @ordered
	 */
	public static final int SER_VALUE = 47;

	/**
	 * The '<em><b>SLIDE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>SLIDE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SLIDE
	 * @model literal="Slide"
	 * @generated
	 * @ordered
	 */
	public static final int SLIDE_VALUE = 48;

	/**
	 * The '<em><b>SOUND</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>SOUND</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SOUND
	 * @model literal="Sound recording"
	 * @generated
	 * @ordered
	 */
	public static final int SOUND_VALUE = 49;

	/**
	 * The '<em><b>STAND</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>STAND</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #STAND
	 * @model literal="Standard"
	 * @generated
	 * @ordered
	 */
	public static final int STAND_VALUE = 50;

	/**
	 * The '<em><b>STAT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>STAT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #STAT
	 * @model literal="Statute"
	 * @generated
	 * @ordered
	 */
	public static final int STAT_VALUE = 51;

	/**
	 * The '<em><b>THES</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>THES</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #THES
	 * @model literal="Thesis/Dissertation"
	 * @generated
	 * @ordered
	 */
	public static final int THES_VALUE = 52;

	/**
	 * The '<em><b>UNPB</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>UNPB</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #UNPB
	 * @model literal="Unpublished work"
	 * @generated
	 * @ordered
	 */
	public static final int UNPB_VALUE = 53;

	/**
	 * The '<em><b>VIDEO</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>VIDEO</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #VIDEO
	 * @model literal="Video recording"
	 * @generated
	 * @ordered
	 */
	public static final int VIDEO_VALUE = 54;

	/**
	 * An array of all the '<em><b>Publication Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final PublicationType[] VALUES_ARRAY =
		new PublicationType[] {
			NULL,
			ABST,
			ADVS,
			AGGR,
			ANCIENT,
			ART,
			BILL,
			BLOG,
			BOOK,
			CASE,
			CHAP,
			CHART,
			CLSWK,
			COMP,
			CONF,
			CPAPER,
			CTLG,
			DATA,
			DBASE,
			DICT,
			EBOOK,
			ECHAP,
			EDBOOK,
			EJOUR,
			ELECT,
			ENCYC,
			EQUA,
			FIGURE,
			GEN,
			GOVDOC,
			GRANT,
			HEAR,
			ICOMM,
			INPR,
			JOUR,
			JFULL,
			LEGAL,
			MANSCPT,
			MAP,
			MGZN,
			MPCT,
			MULTI,
			MUSIC,
			NEWS,
			PAMP,
			PAT,
			PCOMM,
			RPRT,
			SER,
			SLIDE,
			SOUND,
			STAND,
			STAT,
			THES,
			UNPB,
			VIDEO,
		};

	/**
	 * A public read-only list of all the '<em><b>Publication Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<PublicationType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Publication Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static PublicationType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			PublicationType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Publication Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static PublicationType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			PublicationType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Publication Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static PublicationType get(int value) {
		switch (value) {
			case NULL_VALUE: return NULL;
			case ABST_VALUE: return ABST;
			case ADVS_VALUE: return ADVS;
			case AGGR_VALUE: return AGGR;
			case ANCIENT_VALUE: return ANCIENT;
			case ART_VALUE: return ART;
			case BILL_VALUE: return BILL;
			case BLOG_VALUE: return BLOG;
			case BOOK_VALUE: return BOOK;
			case CASE_VALUE: return CASE;
			case CHAP_VALUE: return CHAP;
			case CHART_VALUE: return CHART;
			case CLSWK_VALUE: return CLSWK;
			case COMP_VALUE: return COMP;
			case CONF_VALUE: return CONF;
			case CPAPER_VALUE: return CPAPER;
			case CTLG_VALUE: return CTLG;
			case DATA_VALUE: return DATA;
			case DBASE_VALUE: return DBASE;
			case DICT_VALUE: return DICT;
			case EBOOK_VALUE: return EBOOK;
			case ECHAP_VALUE: return ECHAP;
			case EDBOOK_VALUE: return EDBOOK;
			case EJOUR_VALUE: return EJOUR;
			case ELECT_VALUE: return ELECT;
			case ENCYC_VALUE: return ENCYC;
			case EQUA_VALUE: return EQUA;
			case FIGURE_VALUE: return FIGURE;
			case GEN_VALUE: return GEN;
			case GOVDOC_VALUE: return GOVDOC;
			case GRANT_VALUE: return GRANT;
			case HEAR_VALUE: return HEAR;
			case ICOMM_VALUE: return ICOMM;
			case INPR_VALUE: return INPR;
			case JOUR_VALUE: return JOUR;
			case JFULL_VALUE: return JFULL;
			case LEGAL_VALUE: return LEGAL;
			case MANSCPT_VALUE: return MANSCPT;
			case MAP_VALUE: return MAP;
			case MGZN_VALUE: return MGZN;
			case MPCT_VALUE: return MPCT;
			case MULTI_VALUE: return MULTI;
			case MUSIC_VALUE: return MUSIC;
			case NEWS_VALUE: return NEWS;
			case PAMP_VALUE: return PAMP;
			case PAT_VALUE: return PAT;
			case PCOMM_VALUE: return PCOMM;
			case RPRT_VALUE: return RPRT;
			case SER_VALUE: return SER;
			case SLIDE_VALUE: return SLIDE;
			case SOUND_VALUE: return SOUND;
			case STAND_VALUE: return STAND;
			case STAT_VALUE: return STAT;
			case THES_VALUE: return THES;
			case UNPB_VALUE: return UNPB;
			case VIDEO_VALUE: return VIDEO;
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
	private PublicationType(int value, String name, String literal) {
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
	
} //PublicationType
