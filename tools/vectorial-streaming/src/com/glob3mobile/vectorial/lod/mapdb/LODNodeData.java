

package com.glob3mobile.vectorial.lod.mapdb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.glob3mobile.geo.Angle;
import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;
import com.glob3mobile.vectorial.storage.PointFeature;
import com.glob3mobile.vectorial.storage.PointFeatureCluster;


public class LODNodeData {


   private final List<PointFeatureCluster> _clusters;
   private final List<PointFeature>        _features;


   public LODNodeData(final List<PointFeatureCluster> clusters,
                      final List<PointFeature> features) {
      _clusters = Collections.unmodifiableList(new ArrayList<>(clusters));
      _features = Collections.unmodifiableList(new ArrayList<>(features));
   }


   public List<PointFeatureCluster> getClusters() {
      return _clusters;
   }


   public List<PointFeature> getFeatures() {
      return _features;
   }


   public List<PointFeatureCluster> createClusters() {
      final int Diego_at_work;
      //return (_features.size() < 10) ? createOneCluster() : createQuadrantsClusters();
      return createQuadrantsClusters();
   }


   private List<PointFeatureCluster> createQuadrantsClusters() {
      final List<Sector> quadrants = slit(calculateSector(_clusters, _features));
      final int quadrantsCount = quadrants.size();
      final List<PointFeatureCluster> result = new ArrayList<>(quadrantsCount);

      final List<ClusterAccumulator> accumulators = new ArrayList<>(quadrantsCount);
      for (final Sector quadrant : quadrants) {
         accumulators.add(new ClusterAccumulator(quadrant));
      }

      for (final PointFeatureCluster cluster : _clusters) {
         final Geodetic2D position = cluster._position;
         for (final ClusterAccumulator accumulator : accumulators) {
            if (accumulator._sector.contains(position)) {
               final long clusterSize = cluster._size;
               accumulator._sumLatRad += position._latitude._radians * clusterSize;
               accumulator._sumLonRad += position._longitude._radians * clusterSize;
               accumulator._sumSize += clusterSize;
               break;
            }
         }
      }

      for (final PointFeature feature : _features) {
         final Geodetic2D position = feature._position;
         for (final ClusterAccumulator accumulator : accumulators) {
            if (accumulator._sector.contains(position)) {
               accumulator._sumLatRad += position._latitude._radians;
               accumulator._sumLonRad += position._longitude._radians;
               accumulator._sumSize++;
               break;
            }
         }
      }

      for (final ClusterAccumulator accumulator : accumulators) {
         final PointFeatureCluster cluster = accumulator.createCluster();
         if (cluster != null) {
            result.add(cluster);
         }
      }

      return result;
   }


   private List<PointFeatureCluster> createOneCluster() {
      double sumLatRad = 0;
      double sumLonRad = 0;
      long sumSize = 0;

      for (final PointFeatureCluster cluster : _clusters) {
         final Geodetic2D position = cluster._position;
         final long clusterSize = cluster._size;
         sumLatRad += position._latitude._radians * clusterSize;
         sumLonRad += position._longitude._radians * clusterSize;
         sumSize += clusterSize;
      }

      for (final PointFeature feature : _features) {
         final Geodetic2D position = feature._position;
         sumLatRad += position._latitude._radians;
         sumLonRad += position._longitude._radians;
      }
      sumSize += _features.size();

      final Geodetic2D averagePosition = Geodetic2D.fromRadians(sumLatRad / sumSize, sumLonRad / sumSize);
      return Arrays.asList(new PointFeatureCluster(averagePosition, sumSize));
   }

   private static class ClusterAccumulator {
      private final Sector _sector;
      private double       _sumLatRad = 0;
      private double       _sumLonRad = 0;
      private long         _sumSize   = 0;


      private ClusterAccumulator(final Sector sector) {
         _sector = sector;
      }


      private PointFeatureCluster createCluster() {
         if (_sumSize == 0) {
            return null;
         }
         final Geodetic2D averagePosition = Geodetic2D.fromRadians(_sumLatRad / _sumSize, _sumLonRad / _sumSize);
         return new PointFeatureCluster(averagePosition, _sumSize);
      }

   }


   private static List<Sector> slit(final Sector sector) {
      final Geodetic2D lower = sector._lower;
      final Geodetic2D upper = sector._upper;

      final Angle splitLongitude = Angle.midAngle(lower._longitude, upper._longitude);
      final Angle splitLatitude = Angle.midAngle(lower._latitude, upper._latitude);

      final Sector sector0 = new Sector( //
               splitLatitude, lower._longitude, //
               upper._latitude, splitLongitude);
      final Sector sector1 = new Sector( //
               splitLatitude, splitLongitude, //
               upper._latitude, upper._longitude);
      final Sector sector2 = new Sector( //
               lower._latitude, lower._longitude, //
               splitLatitude, splitLongitude);
      final Sector sector3 = new Sector( //
               lower._latitude, splitLongitude, //
               splitLatitude, upper._longitude);

      return Arrays.asList(sector0, sector1, sector2, sector3);
   }


   private static Sector calculateSector(final List<PointFeatureCluster> clusters,
                                         final List<PointFeature> features) {
      if (clusters.isEmpty() && features.isEmpty()) {
         return null;
      }

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

      return Sector.fromRadians(minLatRad, minLonRad, maxLatRad, maxLonRad);
   }


}
