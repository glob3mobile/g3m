

package com.glob3mobile.vectorial.server;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.server.rest.DefaultRESTProcessor;
import com.glob3mobile.server.rest.ErrorCodes;
import com.glob3mobile.server.rest.RESTException;
import com.glob3mobile.server.rest.RESTJSONErrorException;
import com.glob3mobile.server.rest.RESTJSONResponse;
import com.glob3mobile.server.rest.RESTPath;
import com.glob3mobile.server.rest.RESTResponse;
import com.glob3mobile.vectorial.lod.PointFeatureLODStorage;
import com.glob3mobile.vectorial.lod.mapdb.PointFeatureLODMapDBStorage;
import com.glob3mobile.vectorial.server.utils.GEOJSONUtils;
import com.glob3mobile.vectorial.storage.PointFeature;


public class VectorialStreamingRESTProcessor
   extends
      DefaultRESTProcessor {


   private File _lodStorageDirectory;


   public VectorialStreamingRESTProcessor() {
      super(HttpServletResponse.SC_OK);
   }


   @Override
   public void initialize(final ServletConfig servletConfig) throws ServletException {
      super.initialize(servletConfig);

      final String lodStorageDirectoryName = servletConfig.getInitParameter("lod_storage_directory");
      if ((lodStorageDirectoryName == null) || lodStorageDirectoryName.isEmpty()) {
         throw new ServletException("Init parameter 'lod_storage_directory' is required.");
      }

      _lodStorageDirectory = new File(lodStorageDirectoryName);
      validateDirectory(_lodStorageDirectory);
   }


   private static void validateDirectory(final File directory) throws ServletException {
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


   @Override
   public void destroy() {
      _lodStorageDirectory = null;

      super.destroy();
   }


   @Override
   public RESTResponse doGet(final RESTPath path,
                             final HttpServletRequest request) throws RESTException {

      if (path.size() == 1) {
         final String lodName = path.getFirst();

         final String nodeID = request.getParameter("node");
         final String nodesIDs = request.getParameter("nodes");

         try (final PointFeatureLODStorage lodStorage = PointFeatureLODMapDBStorage.openReadOnly(_lodStorageDirectory, lodName)) {

            if ((nodeID == null) && (nodesIDs == null)) {
               return sendMetadata(lodStorage);
            }
            if (nodeID != null) {
               return sendNode(lodStorage, nodeID);
            }
            return sendNodes(lodStorage, nodesIDs);
         }
         catch (final IOException e) {
            throw new RESTJSONErrorException(HttpServletResponse.SC_OK, ErrorCodes.NOT_FOUND, lodName);
         }
      }
      else if ((path.size() == 2) && path.get(1).equals("features")) {
         return sendNodeFeaturesGEOJSON(path, request);
      }

      throw new RESTJSONErrorException(HttpServletResponse.SC_OK, ErrorCodes.INVALID_OPERATION);
   }


   private RESTResponse sendNodeFeaturesGEOJSON(final RESTPath path,
                                                final HttpServletRequest request) throws RESTJSONErrorException {
      final String lodName = path.getFirst();

      final String nodeID = request.getParameter("node");
      if (nodeID == null) {
         throw new RESTJSONErrorException(HttpServletResponse.SC_OK, ErrorCodes.INVALID_OPERATION,
                  "mandatory parameter 'node' is missing");
      }

      final String propertiesS = request.getParameter("properties");
      if (propertiesS == null) {
         throw new RESTJSONErrorException(HttpServletResponse.SC_OK, ErrorCodes.INVALID_OPERATION,
                  "mandatory parameter 'properties' is missing");
      }
      final String[] properties = propertiesS.split("\\|");

      try (final PointFeatureLODStorage lodStorage = PointFeatureLODMapDBStorage.openReadOnly(_lodStorageDirectory, lodName)) {
         final PointFeatureLODStorage.Node node = lodStorage.getNode(nodeID);
         if (node == null) {
            throw new RESTJSONErrorException(HttpServletResponse.SC_OK, ErrorCodes.NOT_FOUND, "node: \'" + nodeID + "\'");
         }

         final List<PointFeature> features = node.getFeatures();

         final Map<String, Object> result = new LinkedHashMap<>();

         result.put("type", "FeatureCollection");
         result.put("features", toGEOJSONFeatures(features, properties));

         return new RESTJSONResponse(result);
      }
      catch (final IOException e) {
         throw new RESTJSONErrorException(HttpServletResponse.SC_OK, ErrorCodes.NOT_FOUND, lodName);
      }
   }


   private List<Map<String, Object>> toGEOJSONFeatures(final List<PointFeature> features,
                                                       final String[] properties) {
      final List<Map<String, Object>> result = new ArrayList<>(features.size());
      for (final PointFeature feature : features) {
         result.add(toGEOJSONFeature(feature, properties));
      }
      return result;
   }


   private Map<String, Object> toGEOJSONFeature(final PointFeature feature,
                                                final String[] properties) {
      final Map<String, Object> result = new LinkedHashMap<>();
      result.put("type", "Feature");
      result.put("properties", filterProperties(feature._properties, properties));
      result.put("geometry", toGEOJSONPointGeometry(feature._position));

      return result;
   }


   private Map<String, Object> filterProperties(final Map<String, Object> properties,
                                                final String[] names) {
      final Map<String, Object> result = new LinkedHashMap<>();
      for (final String name : names) {
         if (properties.containsKey(name)) {
            result.put(name, properties.get(name));
         }
      }
      return result;
   }


   private Map<String, Object> toGEOJSONPointGeometry(final Geodetic2D position) {
      final Map<String, Object> result = new LinkedHashMap<>();
      result.put("type", "Point");
      result.put("coordinates", toGEOJSONCoordinates(position));
      return result;
   }


   private List<Double> toGEOJSONCoordinates(final Geodetic2D position) {
      return Arrays.asList(position._longitude._degrees, position._latitude._degrees);
   }


   private RESTResponse sendNode(final PointFeatureLODStorage lodStorage,
                                 final String nodeID) throws RESTException {
      final PointFeatureLODStorage.Node node = lodStorage.getNode(nodeID);
      if (node == null) {
         throw new RESTJSONErrorException(HttpServletResponse.SC_OK, ErrorCodes.NOT_FOUND, "node: \'" + nodeID + "\'");
      }

      return new RESTJSONResponse(toJSON(node));
   }


   private RESTResponse sendNodes(final PointFeatureLODStorage lodStorage,
                                  final String nodesIDs) throws RESTException {

      final String[] ids = nodesIDs.split("\\|");

      final List<Map<String, Object>> nodes = new ArrayList<>(ids.length);
      for (final String nodeID : ids) {
         final PointFeatureLODStorage.Node node = lodStorage.getNode(nodeID);
         if (node == null) {
            throw new RESTJSONErrorException(HttpServletResponse.SC_OK, ErrorCodes.NOT_FOUND, "node: \'" + nodeID + "\'");
         }
         nodes.add(toJSON(node));
      }

      return new RESTJSONResponse(nodes);
   }


   private RESTResponse sendMetadata(final PointFeatureLODStorage lodStorage) {
      final PointFeatureLODStorage.Statistics statistics = lodStorage.getStatistics(false);
      final Map<String, Object> result = new LinkedHashMap<>();

      result.put("name", lodStorage.getName());
      result.put("sector", GEOJSONUtils.toJSON(lodStorage.getSector()));

      result.put("featuresCount", statistics.getFeaturesCount());
      result.put("averagePosition", GEOJSONUtils.toJSON(statistics.getAveragePosition()));
      result.put("nodesCount", statistics.getNodesCount());

      final int minNodeDepth = statistics.getMinNodeDepth();
      result.put("minNodeDepth", minNodeDepth);
      result.put("maxNodeDepth", statistics.getMaxNodeDepth());

      result.put("rootNodes", toNodesJSON(lodStorage.getAllNodesOfDepth(minNodeDepth)));

      return new RESTJSONResponse(result);
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
      result.put("sector", GEOJSONUtils.toJSON(node.getSector()));
      result.put("featuresCount", node.getFeaturesCount());
      result.put("averagePosition", GEOJSONUtils.toJSON(node.getAveragePosition()));
      result.put("children", node.getChildrenIDs());
      return result;
   }


}
