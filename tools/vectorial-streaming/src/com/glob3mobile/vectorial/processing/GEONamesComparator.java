

package com.glob3mobile.vectorial.processing;

import java.util.Comparator;
import java.util.Map;

import com.glob3mobile.vectorial.storage.PointFeature;


public class GEONamesComparator
   implements
      Comparator<PointFeature> {


   @Override
   public int compare(final PointFeature o1,
                      final PointFeature o2) {
      final Map<String, Object> properties1 = o1._properties;
      final Map<String, Object> properties2 = o2._properties;

      final long population1 = (Long) properties1.get("population");
      final long population2 = (Long) properties2.get("population");
      if (population1 > population2) {
         return -1;
      }
      if (population1 < population2) {
         return 1;
      }

      final String featureCode1 = (String) properties1.get("featureCode");
      final String featureCode2 = (String) properties2.get("featureCode");
      return featureCode1.compareTo(featureCode2);
   }


}
