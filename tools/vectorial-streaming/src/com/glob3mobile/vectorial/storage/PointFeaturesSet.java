

package com.glob3mobile.vectorial.storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;


public class PointFeaturesSet {


   public static PointFeaturesSet extractFeatures(final Sector sector,
                                                  final List<PointFeature> features) {
      double sumLatitudeInRadians = 0;
      double sumLongitudeInRadians = 0;

      final List<PointFeature> extracted = new ArrayList<PointFeature>();

      final Iterator<PointFeature> iterator = features.iterator();
      while (iterator.hasNext()) {
         final PointFeature feature = iterator.next();
         final Geodetic2D point = feature._position;
         if (sector.contains(point)) {
            extracted.add(feature);

            sumLatitudeInRadians += point._latitude._radians;
            sumLongitudeInRadians += point._longitude._radians;

            iterator.remove();
         }
      }

      final int extractedSize = extracted.size();
      if (extractedSize == 0) {
         return null;
      }

      final double averageLatitudeInRadians = sumLatitudeInRadians / extractedSize;
      final double averageLongitudeInRadians = sumLongitudeInRadians / extractedSize;

      final Geodetic2D averagePosition = Geodetic2D.fromRadians(averageLatitudeInRadians, averageLongitudeInRadians);

      return new PointFeaturesSet(extracted, averagePosition);
   }


   public final List<PointFeature> _features;
   public final Geodetic2D         _averagePosition;


   public PointFeaturesSet(final List<PointFeature> features,
                           final Geodetic2D averagePosition) {
      _features = features;
      _averagePosition = averagePosition;
   }


   public int size() {
      return _features.size();
   }


}
