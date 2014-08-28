

package com.glob3mobile.pointcloud.server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.glob3mobile.pointcloud.kdtree.EllipsoidalPlanet;
import com.glob3mobile.pointcloud.kdtree.Planet;
import com.glob3mobile.pointcloud.octree.PersistentLOD;
import com.glob3mobile.pointcloud.octree.berkeleydb.BerkeleyDBLOD;

import es.igosoftware.util.XStringTokenizer;


public class PCSSServlet
         extends
            HttpServlet {
   private static final long                serialVersionUID = 1L;

   private final Map<String, PersistentLOD> _openedDBs       = new HashMap<String, PersistentLOD>();
   private File                             _cloudDirectory;


   @Override
   public void init(final ServletConfig config) throws ServletException {
      super.init(config);

      //_cloudDirectory = new File(System.getProperty("user.dir"));
      _cloudDirectory = new File("/Volumes/My Passport/_LIDAR_COPY");

      log("initialization of " + getClass() + " at " + _cloudDirectory);
   }


   private PersistentLOD getDB(final String cloudName) {
      synchronized (_openedDBs) {
         PersistentLOD result = _openedDBs.get(cloudName);
         if (result == null) {
            try {
               result = BerkeleyDBLOD.openReadOnly(_cloudDirectory, cloudName, -1);
               final PersistentLOD.Statistics statistics = result.getStatistics(true, true);
               statistics.show();
            }
            catch (final Exception e) {
               log("Error opening \"" + cloudName + "\"", e);
               return null;
            }
            _openedDBs.put(cloudName, result);
         }
         return result;
      }
   }


   @Override
   public void destroy() {
      super.destroy();
      log("destroying " + getClass());

      synchronized (_openedDBs) {
         for (final Map.Entry<String, PersistentLOD> entry : _openedDBs.entrySet()) {
            try {
               entry.getValue().close();
            }
            catch (final Exception e) {
               log("can't close DB \"" + entry.getKey() + "\"", e);
            }
         }
         _openedDBs.clear();
      }
   }


   private static void error(final HttpServletResponse response,
                             final String msg) throws IOException {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
   }


   private static void sendMetadata(final PersistentLOD db,
                                    final HttpServletResponse response) throws IOException {
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("application/json");
      final PrintWriter writer = response.getWriter();

      final PersistentLOD.Statistics statistics = db.getStatistics(false, false);
      // final String projection = "EPSG:4326";
      JSONUtils.sendJSON(writer, statistics);
   }


   private static void sendNodeLayout(final PersistentLOD db,
                                      final String nodeID,
                                      final HttpServletResponse response) throws IOException {
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("application/json");
      final PrintWriter writer = response.getWriter();

      final PersistentLOD.NodeLayout layout = db.getNodeLayout(nodeID);
      JSONUtils.sendNodeLayoutJSON(writer, layout);
   }


   private static void sendNodeMetadata(final PersistentLOD db,
                                        final String nodeID,
                                        final HttpServletResponse response) throws IOException {
      final PrintWriter writer = response.getWriter();

      final PersistentLOD.Node node = db.getNode(nodeID, false);
      if (node == null) {
         response.sendError(HttpServletResponse.SC_NOT_FOUND, "node #" + nodeID + " not found");
      }
      else {
         response.setStatus(HttpServletResponse.SC_OK);
         response.setContentType("application/json");

         final Planet planet = EllipsoidalPlanet.EARTH;

         JSONUtils.sendNodeMetadataJSON(writer, planet, node);
      }
   }


   @Override
   protected void doGet(final HttpServletRequest request,
                        final HttpServletResponse response) throws IOException {

      final String[] path = XStringTokenizer.getAllTokens(request.getPathInfo(), "/");

      if (path.length == 0) {
         error(response, "Invalid request");
         return;
      }

      final String cloudName = path[0];

      if (cloudName.equals("favicon.ico")) {
         error(response, "Invalid request");
         return;
      }

      final PersistentLOD db = getDB(cloudName);
      if (db == null) {
         error(response, "Can't open DB \"" + cloudName + "\"");
         return;
      }

      if (path.length == 1) {
         sendMetadata(db, response);
      }
      else if ((path.length == 3) && path[1].trim().equalsIgnoreCase("layout")) {
         final String nodeID = path[2].trim();
         sendNodeLayout(db, nodeID, response);
      }
      else if ((path.length == 3) && path[1].trim().equalsIgnoreCase("metadata")) {
         final String nodeID = path[2].trim();
         sendNodeMetadata(db, nodeID, response);
      }
      else {
         error(response, "Invalid request");
      }
   }


}
