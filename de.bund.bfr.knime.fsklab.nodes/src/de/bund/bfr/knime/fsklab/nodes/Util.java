package de.bund.bfr.knime.fsklab.nodes;

import java.util.List;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.MathContainer;

import de.bund.bfr.knime.fsklab.nodes.FskMetaData.DataType;

public class Util {

	/**
	 * Returns the {@link FskMetaData.DataType} of a value.
	 * 
	 * <ul>
	 * <li>{@code DataType#array} for Matlab like arrays, c(0, 1, 2, ...)
	 * <li>{@code DataType#numeric} for real numbers.
	 * <li>{@code DataType#integer} for integer numbers.
	 * <li>{@code DataType#character} for any other variable. E.g. "zero",
	 * "eins", "dos".
	 * </ul>
	 */
	public static DataType getValueType(final String value) {
		if (value.startsWith("c(") && value.endsWith(")")) {
			return DataType.array;
		} else {
			try {
				Integer.parseInt(value);
				return DataType.integer;
			} catch (NumberFormatException e1) {
				try {
					Double.parseDouble(value);
					return DataType.numeric;
				} catch (NumberFormatException e2) {
					return DataType.character;
				}
			}
		}
	}

	/**
	 * Create the {@link ASTNode} corresponding to a selector node with all the
	 * values of an array.
	 * 
	 * Structure:
	 * {@code
	 * <apply>
	 *   <selector>
	 *   <vector>
	 *     <cn>0</cn>
	 *     <cn>1</cn>
	 *     ...
	 *     <cn>n</cn>
	 *   </vector>
	 * </apply>
	 * }
	 * 
	 * @param numbers
	 * @return
	 */
	public static ASTNode createSelectorNode(List<Double> numbers, MathContainer mc) {
		ASTNode math = new ASTNode(ASTNode.Type.FUNCTION_SELECTOR, mc);
		ASTNode vectorNode = new ASTNode(ASTNode.Type.VECTOR, mc);
		numbers.forEach(d -> vectorNode.addChild(new ASTNode(d)));
		math.addChild(vectorNode);
		
		return math;
	}
}
