

package com.glob3mobile.vectorial.server;


import static com.glob3mobile.server.rest.ServerErrorCodes.INVALID_OPERATION;
import static com.glob3mobile.server.rest.ServerErrorCodes.NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.glob3mobile.server.rest.DefaultRESTProcessor;
import com.glob3mobile.server.rest.RESTException;
import com.glob3mobile.server.rest.RESTJSONErrorException;
import com.glob3mobile.server.rest.RESTJSONResponse;
import com.glob3mobile.server.rest.RESTPath;
import com.glob3mobile.server.rest.RESTResponse;
import com.glob3mobile.vectorial.lod.PointFeatureLODStorage;
import com.glob3mobile.vectorial.lod.mapdb.PointFeatureLODMapDBStorage;
import com.glob3mobile.vectorial.server.utils.JSON;


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
         return new RESTJSONResponse(JSON.toGEOJSON(node, properties));
      }
      catch (final IOException e) {
         throw new RESTJSONErrorException(SC_OK, NOT_FOUND, lodName);
      }
   }


   private static RESTResponse sendLODMetadata(final PointFeatureLODStorage lodStorage) {
      return new RESTJSONResponse(JSON.getMetadataAsJSON(lodStorage));
   }


   private static RESTResponse sendNodeMetadata(final PointFeatureLODStorage lodStorage,
                                                final String nodeID) throws RESTException {
      final PointFeatureLODStorage.Node node = lodStorage.getNode(nodeID);
      if (node == null) {
         throw new RESTJSONErrorException(SC_OK, NOT_FOUND, "node: \'" + nodeID + "\'");
      }
      return new RESTJSONResponse(JSON.toJSON(node));
   }


   private static RESTResponse sendNodesMetadata(final PointFeatureLODStorage lodStorage,
                                                 final String nodesIDs) throws RESTException {

      final String[] ids = nodesIDs.split("\\|");

      final List<Map<String, Object>> nodesJSON = new ArrayList<>(ids.length);
      for (final String nodeID : ids) {
         final PointFeatureLODStorage.Node node = lodStorage.getNode(nodeID);
         if (node == null) {
            throw new RESTJSONErrorException(SC_OK, NOT_FOUND, "node: \'" + nodeID + "\'");
         }
         nodesJSON.add(JSON.toJSON(node));
      }

      return new RESTJSONResponse(nodesJSON);
   }


   private static String getMandatoryParameter(final HttpServletRequest request,
                                               final String name) throws RESTException {
      final String value = request.getParameter(name);
      if (value == null) {
         throw new RESTJSONErrorException(SC_OK, INVALID_OPERATION, "mandatory parameter '" + name + "' is missing");
      }
      return value;
   }


}
