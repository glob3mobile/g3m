

package com.glob3mobile.vectorial.storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;


public class PointFeaturesSet {


   public static PointFeaturesSet extractFeatures(final Sector sector,
                                                  final List<PointFeature> features) {
      double sumLatRad = 0;
      double sumLonRad = 0;
      double minLatRad = Double.POSITIVE_INFINITY;
      double minLonRad = Double.POSITIVE_INFINITY;
      double maxLatRad = Double.NEGATIVE_INFINITY;
      double maxLonRad = Double.NEGATIVE_INFINITY;

      final List<PointFeature> extracted = new ArrayList<PointFeature>();

      final Iterator<PointFeature> iterator = features.iterator();
      while (iterator.hasNext()) {
         final PointFeature feature = iterator.next();
         final Geodetic2D point = feature._position;
         if (sector.contains(point)) {
            extracted.add(feature);

            final double latRad = point._latitude._radians;
            final double lonRad = point._longitude._radians;

            sumLatRad += latRad;
            sumLonRad += lonRad;

            if (latRad < minLatRad) {
               minLatRad = latRad;
            }
            if (latRad > maxLatRad) {
               maxLatRad = latRad;
            }

            if (lonRad < minLonRad) {
               minLonRad = lonRad;
            }
            if (lonRad > maxLonRad) {
               maxLonRad = lonRad;
            }

            iterator.remove();
         }
      }

      final int extractedSize = extracted.size();
      if (extractedSize == 0) {
         return null;
      }

      final double averageLatRad = sumLatRad / extractedSize;
      final double averageLonRad = sumLonRad / extractedSize;

      return new PointFeaturesSet( //
               extracted, //
               Geodetic2D.fromRadians(averageLatRad, averageLonRad), //
               Sector.fromRadians(minLatRad, minLonRad, maxLatRad, maxLonRad));
   }


   public static PointFeaturesSet create(final List<PointFeature> features) {
      if ((features == null) || features.isEmpty()) {
         throw new RuntimeException("Empty features");
      }

      double sumLatRad = 0;
      double sumLonRad = 0;
      double minLatRad = Double.POSITIVE_INFINITY;
      double minLonRad = Double.POSITIVE_INFINITY;
      double maxLatRad = Double.NEGATIVE_INFINITY;
      double maxLonRad = Double.NEGATIVE_INFINITY;

      for (final PointFeature feature : features) {
         final Geodetic2D point = feature._position;

         final double latRad = point._latitude._radians;
         final double lonRad = point._longitude._radians;

         sumLatRad += latRad;
         sumLonRad += lonRad;

         if (latRad < minLatRad) {
            minLatRad = latRad;
         }
         if (latRad > maxLatRad) {
            maxLatRad = latRad;
         }

         if (lonRad < minLonRad) {
            minLonRad = lonRad;
         }
         if (lonRad > maxLonRad) {
            maxLonRad = lonRad;
         }
      }

      final double averageLatRad = sumLatRad / features.size();
      final double averageLonRad = sumLonRad / features.size();

      return new PointFeaturesSet( //
               features, //
               Geodetic2D.fromRadians(averageLatRad, averageLonRad), //
               Sector.fromRadians(minLatRad, minLonRad, maxLatRad, maxLonRad));
   }


   public final List<PointFeature> _features;
   public final Geodetic2D         _averagePosition;
   public final Sector             _minimumSector;


   public PointFeaturesSet(final List<PointFeature> features,
                           final Geodetic2D averagePosition,
                           final Sector minimumSector) {
      _features = features;
      _averagePosition = averagePosition;
      _minimumSector = minimumSector;
   }


   public int size() {
      return _features.size();
   }


}
