

package poly2Tri;

import java.util.Comparator;


public class PointbaseComparatorCoordinatesReverse
         implements
            Comparator<Pointbase> {

   @Override
   public int compare(final Pointbase pb1,
                      final Pointbase pb2) {
      return (-pb1.compareTo(pb2));
   }


}
