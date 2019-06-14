package de.bund.bfr.knime.fsklab.nodes;

import java.util.Comparator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class EMFComparator extends EcoreUtil.EqualityHelper {

  private static final long serialVersionUID = 8176942128228933259L;


  class EObjectComparator implements Comparator<EObject> {
    public int compare(EObject object1, EObject object2) {
      String targetString1 = extractComparisonString(object1);
      String targetString2 = extractComparisonString(object2);

      return targetString1.compareTo(targetString2);
    }

    private String extractComparisonString(EObject object) {
      return object.toString().replaceAll(object.getClass().getName(), "")
          .replaceAll(Integer.toHexString(object.hashCode()), "");
    }
  }


  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public boolean equals(EObject object1, EObject object2) {

    Comparator comparator = new EObjectComparator();

    return comparator.compare(object1, object2) == 0 ? true : false;
  }
}
