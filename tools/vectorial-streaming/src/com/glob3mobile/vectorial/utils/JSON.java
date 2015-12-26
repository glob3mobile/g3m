

package com.glob3mobile.vectorial.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.vectorial.lod.PointFeatureLODStorage;
import com.glob3mobile.vectorial.storage.PointFeature;
import com.glob3mobile.vectorial.storage.PointFeatureCluster;


public class JSON {
   private JSON() {
   }


   public static Map<String, Object> getMetadataAsJSON(final PointFeatureLODStorage lodStorage,
                                                       final boolean includeChildren) {
      final PointFeatureLODStorage.Statistics statistics = lodStorage.getStatistics(false);
      final Map<String, Object> result = new LinkedHashMap<>();
      result.put("name", lodStorage.getName());
      result.put("sector", GEOJSONUtils.toJSON(lodStorage.getSector()));
      result.put("clustersCount", statistics.getClustersCount());
      result.put("featuresCount", statistics.getFeaturesCount());
      result.put("nodesCount", statistics.getNodesCount());
      final int minNodeDepth = statistics.getMinNodeDepth();
      result.put("minNodeDepth", minNodeDepth);
      result.put("maxNodeDepth", statistics.getMaxNodeDepth());
      result.put("rootNodes", toNodesJSON(lodStorage.getAllNodesOfDepth(minNodeDepth), includeChildren, lodStorage));
      return result;
   }


   private static List<Map<String, Object>> toNodesJSON(final List<PointFeatureLODStorage.Node> nodes,
                                                        final boolean includeChildren,
                                                        final PointFeatureLODStorage lodStorage) {
      final List<Map<String, Object>> result = new ArrayList<>(nodes.size());
      for (final PointFeatureLODStorage.Node node : nodes) {
         result.add(toJSON(node, includeChildren, lodStorage));
      }
      return result;
   }


   public static Map<String, Object> toJSON(final PointFeatureLODStorage.Node node,
                                            final boolean includeChildren,
                                            final PointFeatureLODStorage lodStorage) {
      final Map<String, Object> result = new LinkedHashMap<>();
      result.put("id", node.getID());
      result.put("nodeSector", GEOJSONUtils.toJSON(node.getNodeSector()));
      result.put("minimumSector", GEOJSONUtils.toJSON(node.getMinimumSector()));
      result.put("clustersCount", node.getClustersCount());
      result.put("featuresCount", node.getFeaturesCount());
      if (includeChildren) {
         final List<String> childrenIDs = node.getChildrenIDs();

         final List<Map<String, Object>> childrenMetadataJSON = new ArrayList<>(childrenIDs.size());

         for (final String childID : childrenIDs) {
            final PointFeatureLODStorage.Node child = lodStorage.getNode(childID);
            childrenMetadataJSON.add(JSON.toJSON(child, false, null));
         }
         result.put("children", childrenMetadataJSON);
      }
      else {
         result.put("children", node.getChildrenIDs());
      }
      return result;
   }


   public static Map<String, Object> toGEOJSON(final PointFeatureLODStorage.Node node,
                                               final String[] properties,
                                               final boolean includeChildren,
                                               final PointFeatureLODStorage lodStorage) {
      final Map<String, Object> result = new LinkedHashMap<>();

      result.put("clusters", toJSONClusters(node.getClusters()));

      final Map<String, Object> features = new LinkedHashMap<>();
      features.put("type", "FeatureCollection");
      features.put("features", toGEOJSONFeatures(node.getFeatures(), properties));
      result.put("features", features);

      if (includeChildren) {
         final List<String> childrenIDs = node.getChildrenIDs();

         final List<Map<String, Object>> childrenMetadataJSON = new ArrayList<>(childrenIDs.size());

         for (final String childID : childrenIDs) {
            final PointFeatureLODStorage.Node child = lodStorage.getNode(childID);
            childrenMetadataJSON.add(JSON.toJSON(child, false, null));
         }
         result.put("children", childrenMetadataJSON);
      }

      return result;
   }


   private static List<Map<String, Object>> toJSONClusters(final List<PointFeatureCluster> clusters) {
      final List<Map<String, Object>> result = new ArrayList<>(clusters.size());
      for (final PointFeatureCluster cluster : clusters) {
         result.add(toJSONCluster(cluster));
      }
      return result;
   }


   private static Map<String, Object> toJSONCluster(final PointFeatureCluster cluster) {
      final Map<String, Object> result = new LinkedHashMap<>();
      result.put("position", toGEOJSONCoordinates(cluster._position));
      result.put("size", cluster._size);
      return result;
   }


   private static List<Map<String, Object>> toGEOJSONFeatures(final List<PointFeature> features,
                                                              final String[] properties) {
      final List<Map<String, Object>> result = new ArrayList<>(features.size());
      for (final PointFeature feature : features) {
         result.add(toGEOJSONFeature(feature, properties));
      }
      return result;
   }


   private static Map<String, Object> toGEOJSONFeature(final PointFeature feature,
                                                       final String[] properties) {
      final Map<String, Object> result = new LinkedHashMap<>();
      result.put("type", "Feature");
      result.put("properties", filterProperties(feature._properties, properties));
      result.put("geometry", toGEOJSONPointGeometry(feature._position));
      return result;
   }


   private static Map<String, Object> filterProperties(final Map<String, Object> properties,
                                                       final String[] names) {
      final Map<String, Object> result = new LinkedHashMap<>();
      for (final String name : names) {
         if (properties.containsKey(name)) {
            result.put(name, properties.get(name));
         }
      }
      return result;
   }


   private static Map<String, Object> toGEOJSONPointGeometry(final Geodetic2D position) {
      final Map<String, Object> result = new LinkedHashMap<>();
      result.put("type", "Point");
      result.put("coordinates", toGEOJSONCoordinates(position));
      return result;
   }


   private static List<Double> toGEOJSONCoordinates(final Geodetic2D position) {
      return Arrays.asList(position._longitude._degrees, position._latitude._degrees);
   }


}
