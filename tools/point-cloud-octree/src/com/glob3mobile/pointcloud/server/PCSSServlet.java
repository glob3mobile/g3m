

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

import com.glob3mobile.pointcloud.octree.PersistentLOD;
import com.glob3mobile.pointcloud.octree.berkeleydb.BerkeleyDBLOD;

import es.igosoftware.util.XStringTokenizer;


public class PCSSServlet
         extends
            HttpServlet {
   private final Map<String, PersistentLOD> _openedDBs       = new HashMap<String, PersistentLOD>();

   private static final long                serialVersionUID = 1L;
   private File                             _cloudDirectory;


   @Override
   public void init(final ServletConfig config) throws ServletException {
      super.init(config);

      _cloudDirectory = new File(System.getProperty("user.dir"));

      System.out.println(_cloudDirectory.getAbsolutePath());


      log("initialization of " + getClass() + " at " + _cloudDirectory);
   }


   private PersistentLOD getDB(final String cloudName) {
      synchronized (_openedDBs) {
         PersistentLOD result = _openedDBs.get(cloudName);
         if (result == null) {
            try {
               result = BerkeleyDBLOD.openReadOnly(_cloudDirectory, cloudName);
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
      else {
         error(response, "Invalid request");
      }
   }


   private static void sendMetadata(final PersistentLOD db,
                                    final HttpServletResponse response) throws IOException {
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("application/json");
      final PrintWriter writer = response.getWriter();

      final PersistentLOD.Statistics statistics = db.getStatistics(false, false);
      writer.print('{');

      JSONUtils.sendJSON(writer, "projection", "EPSG:4326");

      writer.print(',');
      JSONUtils.sendJSON(writer, "pointsCount", statistics.getPointsCount());

      writer.print(',');
      JSONUtils.sendJSON(writer, "sector", statistics.getSector());

      writer.print(',');
      JSONUtils.sendJSON(writer, "minHeight", statistics.getMinHeight());

      writer.print(',');
      JSONUtils.sendJSON(writer, "maxHeight", statistics.getMaxHeight());

      writer.println('}');
   }


}
