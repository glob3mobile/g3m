

package com.glob3mobile.vectorial.server;


import static com.glob3mobile.server.rest.ServerErrorCodes.INVALID_OPERATION;
import static com.glob3mobile.server.rest.ServerErrorCodes.NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.server.rest.DefaultRESTProcessor;
import com.glob3mobile.server.rest.RESTException;
import com.glob3mobile.server.rest.RESTJSONErrorException;
import com.glob3mobile.server.rest.RESTJSONResponse;
import com.glob3mobile.server.rest.RESTPath;
import com.glob3mobile.server.rest.RESTResponse;
import com.glob3mobile.vectorial.lod.PointFeatureLODStorage;
import com.glob3mobile.vectorial.lod.mapdb.PointFeatureLODMapDBStorage;
import com.glob3mobile.vectorial.server.utils.GEOJSONUtils;
import com.glob3mobile.vectorial.storage.PointFeature;
import com.glob3mobile.vectorial.storage.PointFeatureCluster;


public abstract class AbstractVectorialStreamingRESTProcessor
   extends
      DefaultRESTProcessor {


   protected AbstractVectorialStreamingRESTProcessor(final int notSupportedResponseStatus) {
      super(notSupportedResponseStatus);
   }


   protected static void validateDirectory(final File directory) throws ServletException {
      if (!directory.exists()) {
         throw new ServletException("\"" + directory.getAbsolutePath() + "\" doesn't exist.");
      }
      if (!directory.canRead()) {
         throw new ServletException("can't read \"" + directory.getAbsolutePath() + "\"");
      }
      if (!directory.isDirectory()) {
         throw new ServletException("\"" + directory.getAbsolutePath() + "\" is not a directory.");
      }
   }


   protected abstract File getDirectoryFor(String lodName) throws FileNotFoundException;


   private File directoryFor(final String lodName) throws ServletException, FileNotFoundException {
      final File dir = getDirectoryFor(lodName);
      validateDirectory(dir);
      return dir;
   }


   @Override
   public RESTResponse doGet(final RESTPath path,
                             final HttpServletRequest request) throws RESTException {

      try {
         if (path.size() == 1) {
            return sendMetadata(path, request);
         }
         else if ((path.size() == 2) && path.get(1).equals("features")) {
            return sendNodeFeaturesGEOJSON(path, request);
         }
      }
      catch (final ServletException e) {
         e.printStackTrace();
         throw new RESTJSONErrorException(SC_OK, INVALID_OPERATION);
      }

      throw new RESTJSONErrorException(SC_OK, INVALID_OPERATION);
   }


   private RESTResponse sendMetadata(final RESTPath path,
                                     final HttpServletRequest request) throws RESTException, ServletException {
      final String lodName = path.getFirst();

      final String nodeID = request.getParameter("node");
      final String nodesIDs = request.getParameter("nodes");

      try (final PointFeatureLODStorage lodStorage = PointFeatureLODMapDBStorage.openReadOnly(directoryFor(lodName), lodName)) {
         if ((nodeID == null) && (nodesIDs == null)) {
            return sendLODMetadata(lodStorage);
         }
         if (nodeID == null) {
            return sendNodesMetadata(lodStorage, nodesIDs);
         }
         return sendNodeMetadata(lodStorage, nodeID);
      }
      catch (final IOException e) {
         throw new RESTJSONErrorException(SC_OK, NOT_FOUND, lodName);
      }
   }


   private RESTResponse sendNodeFeaturesGEOJSON(final RESTPath path,
                                                final HttpServletRequest request) throws RESTException, ServletException {
      final String lodName = path.getFirst();
      final String nodeID = getMandatoryParameter(request, "node");
      final String[] properties = getMandatoryParameter(request, "properties").split("\\|");

      try (final PointFeatureLODStorage lodStorage = PointFeatureLODMapDBStorage.openReadOnly(directoryFor(lodName), lodName)) {
         final PointFeatureLODStorage.Node node = lodStorage.getNode(nodeID);
         if (node == null) {
            throw new RESTJSONErrorException(SC_OK, NOT_FOUND, "node: \'" + nodeID + "\'");
         }
         return new RESTJSONResponse(toGEOJSON(node, properties));
      }
      catch (final IOException e) {
         throw new RESTJSONErrorException(SC_OK, NOT_FOUND, lodName);
      }
   }


   private static RESTResponse sendLODMetadata(final PointFeatureLODStorage lodStorage) {
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
      result.put("rootNodes", toNodesJSON(lodStorage.getAllNodesOfDepth(minNodeDepth)));
      return new RESTJSONResponse(result);
   }


   private static RESTResponse sendNodeMetadata(final PointFeatureLODStorage lodStorage,
                                                final String nodeID) throws RESTException {
      final PointFeatureLODStorage.Node node = lodStorage.getNode(nodeID);
      if (node == null) {
         throw new RESTJSONErrorException(SC_OK, NOT_FOUND, "node: \'" + nodeID + "\'");
      }
      return new RESTJSONResponse(toJSON(node));
   }


   private static RESTResponse sendNodesMetadata(final PointFeatureLODStorage lodStorage,
                                                 final String nodesIDs) throws RESTException {

      final String[] ids = nodesIDs.split("\\|");

      final List<Map<String, Object>> nodes = new ArrayList<>(ids.length);
      for (final String nodeID : ids) {
         final PointFeatureLODStorage.Node node = lodStorage.getNode(nodeID);
         if (node == null) {
            throw new RESTJSONErrorException(SC_OK, NOT_FOUND, "node: \'" + nodeID + "\'");
         }
         nodes.add(toJSON(node));
      }

      return new RESTJSONResponse(nodes);
   }


   private static String getMandatoryParameter(final HttpServletRequest request,
                                               final String name) throws RESTException {
      final String value = request.getParameter(name);
      if (value == null) {
         throw new RESTJSONErrorException(SC_OK, INVALID_OPERATION, "mandatory parameter '" + name + "' is missing");
      }
      return value;
   }


   private static Map<String, Object> toGEOJSON(final PointFeatureLODStorage.Node node,
                                                final String[] properties) {
      final Map<String, Object> result = new LinkedHashMap<>();

      result.put("clusters", toJSONClusters(node.getClusters()));

      final Map<String, Object> features = new LinkedHashMap<>();
      features.put("type", "FeatureCollection");
      features.put("features", toGEOJSONFeatures(node.getFeatures(), properties));
      result.put("features", features);

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


   private static List<Map<String, Object>> toNodesJSON(final List<PointFeatureLODStorage.Node> nodes) {
      final List<Map<String, Object>> result = new ArrayList<>(nodes.size());
      for (final PointFeatureLODStorage.Node node : nodes) {
         result.add(toJSON(node));
      }
      return result;
   }


   private static Map<String, Object> toJSON(final PointFeatureLODStorage.Node node) {
      final Map<String, Object> result = new LinkedHashMap<>();
      result.put("id", node.getID());
      result.put("nodeSector", GEOJSONUtils.toJSON(node.getNodeSector()));
      result.put("minimumSector", GEOJSONUtils.toJSON(node.getMinimumSector()));
      result.put("clustersCount", node.getClustersCount());
      result.put("featuresCount", node.getFeaturesCount());
      result.put("children", node.getChildrenIDs());
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
