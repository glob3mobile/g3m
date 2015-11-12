package com.glob3mobile.vectorial.processing;

import java.util.Comparator;
import java.util.Map;

import com.glob3mobile.vectorial.storage.PointFeature;

public class PopulatedPlacesComparator
   implements
      Comparator<PointFeature> {


   @Override
   public int compare(final PointFeature o1,
                      final PointFeature o2) {
      final Map<String, Object> properties1 = o1._properties;
      final Map<String, Object> properties2 = o2._properties;

      final long scaleRank1 = (Long) properties1.get("SCALERANK");
      final long scaleRank2 = (Long) properties2.get("SCALERANK");
      if (scaleRank1 < scaleRank2) {
         return -1;
      }
      if (scaleRank1 > scaleRank2) {
         return 1;
      }

      final long population1 = (Long) properties1.get("POP_MAX");
      final long population2 = (Long) properties2.get("POP_MAX");
      return Long.compare(population2, population1);
   }


}