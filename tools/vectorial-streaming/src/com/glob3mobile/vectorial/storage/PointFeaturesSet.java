

package com.glob3mobile.vectorial.storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;


public class PointFeaturesSet {


   public static PointFeaturesSet extractFeatures(final Sector sector,
                                                  final List<PointFeature> features) {
      double minLatRad = Double.POSITIVE_INFINITY;
      double minLonRad = Double.POSITIVE_INFINITY;
      double maxLatRad = Double.NEGATIVE_INFINITY;
      double maxLonRad = Double.NEGATIVE_INFINITY;

      final List<PointFeature> extractedFeatures = new ArrayList<>();

      final Iterator<PointFeature> featuresIterator = features.iterator();
      while (featuresIterator.hasNext()) {
         final PointFeature feature = featuresIterator.next();
         final Geodetic2D point = feature._position;
         if (sector.contains(point)) {
            extractedFeatures.add(feature);

            final double latRad = point._latitude._radians;
            final double lonRad = point._longitude._radians;

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

            featuresIterator.remove();
         }
      }

      if (extractedFeatures.isEmpty()) {
         return null;
      }

      return new PointFeaturesSet( //
               Sector.fromRadians(minLatRad, minLonRad, maxLatRad, maxLonRad), //
               extractedFeatures);
   }


   public final Sector             _minimumSector;
   public final List<PointFeature> _features;


   public PointFeaturesSet(final Sector minimumSector,
                           final List<PointFeature> features) {
      validateFeatures(minimumSector, features);

      _minimumSector = minimumSector;
      _features = features;
   }


   public int getFeaturesCount() {
      return _features.size();
   }


   private static void validateFeatures(final Sector minimumSector,
                                        final List<PointFeature> features) {
      for (final PointFeature feature : features) {
         final Geodetic2D position = feature._position;
         if (!minimumSector.contains(position)) {
            throw new RuntimeException("LOGIC ERROR");
         }
      }
   }

}
