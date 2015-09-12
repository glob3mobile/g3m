

package com.glob3mobile.vectorial.storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;


public class PointFeaturesSet {


   public static PointFeaturesSet extractFeatures(final Sector sector,
                                                  final List<PointFeatureCluster> clusters,
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

      final List<PointFeatureCluster> extractedClusters = new ArrayList<>();
      final Iterator<PointFeatureCluster> clustersIterator = clusters.iterator();
      while (clustersIterator.hasNext()) {
         final PointFeatureCluster cluster = clustersIterator.next();
         final Geodetic2D point = cluster._position;
         if (sector.contains(point)) {
            extractedClusters.add(cluster);

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

            clustersIterator.remove();
         }
      }

      if (extractedFeatures.isEmpty() && extractedClusters.isEmpty()) {
         return null;
      }


      return new PointFeaturesSet( //
               Sector.fromRadians(minLatRad, minLonRad, maxLatRad, maxLonRad), //
               extractedClusters, //
               extractedFeatures);
   }


   public static PointFeaturesSet create(final List<PointFeatureCluster> clusters,
                                         final List<PointFeature> features) {
      //      if ((features == null) || features.isEmpty()) {
      //         throw new RuntimeException("Empty features");
      //      }

      double minLatRad = Double.POSITIVE_INFINITY;
      double minLonRad = Double.POSITIVE_INFINITY;
      double maxLatRad = Double.NEGATIVE_INFINITY;
      double maxLonRad = Double.NEGATIVE_INFINITY;

      for (final PointFeature feature : features) {
         final Geodetic2D point = feature._position;

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
      }

      for (final PointFeatureCluster cluster : clusters) {
         final Geodetic2D point = cluster._position;

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
      }

      return new PointFeaturesSet( //
               Sector.fromRadians(minLatRad, minLonRad, maxLatRad, maxLonRad), //
               clusters, //
               features);
   }


   public final Sector                    _minimumSector;
   public final List<PointFeatureCluster> _clusters;
   public final List<PointFeature>        _features;


   public PointFeaturesSet(final Sector minimumSector,
                           final List<PointFeatureCluster> clusters,
                           final List<PointFeature> features) {
      _minimumSector = minimumSector;
      _clusters = clusters;
      _features = features;
   }


   public int getFClustersCount() {
      return _clusters.size();
   }


   public int getFeaturesCount() {
      return _features.size();
   }


}
