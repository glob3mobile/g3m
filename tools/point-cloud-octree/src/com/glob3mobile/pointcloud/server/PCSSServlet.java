

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

import com.glob3mobile.pointcloud.octree.Angle;
import com.glob3mobile.pointcloud.octree.PersistentLOD;
import com.glob3mobile.pointcloud.octree.Sector;
import com.glob3mobile.pointcloud.octree.berkeleydb.BerkeleyDBLOD;

import es.igosoftware.util.XStringTokenizer;


public class PCSSServlet
         extends
            HttpServlet {

   private static final long serialVersionUID = 1L;
   private File              _cloudDirectory;


   @Override
   public void init(final ServletConfig config) throws ServletException {
      super.init(config);

      _cloudDirectory = new File(System.getProperty("user.dir"));

      System.out.println(_cloudDirectory.getAbsolutePath());


      log("initialization of " + getClass() + " at " + _cloudDirectory);
   }

   private final Map<String, PersistentLOD> _openedDBs = new HashMap<String, PersistentLOD>();


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
   protected void doGet(final HttpServletRequest request,
                        final HttpServletResponse response) throws IOException {

      final String[] path = XStringTokenizer.getAllTokens(request.getPathInfo(), "/");

      if (path.length == 0) {
         error(response, "Invalid request");
      }
      else {
         final String cloudName = path[0];

         if (cloudName.equals("favicon.ico")) {
            error(response, "Invalid request");
         }
         else {
            if (path.length == 1) {
               final PersistentLOD db = getDB(cloudName);
               if (db == null) {
                  error(response, "Can't open DB \"" + cloudName + "\"");
               }
               else {
                  //                  response.setStatus(HttpServletResponse.SC_OK);
                  //                  response.setContentType("text/html;charset=utf-8");
                  //                  final PrintWriter writer = response.getWriter();
                  //                  writer.println(cloudName);
                  response.setStatus(HttpServletResponse.SC_OK);
                  response.setContentType("application/json");
                  final PrintWriter writer = response.getWriter();

                  final PersistentLOD.Statistics s = db.getStatistics(false, false);
                  writer.print('{');
                  sendJSON(writer, "pointsCount", s.getPointsCount());
                  writer.print(",");
                  sendJSON(writer, "sector", s.getSector());
                  writer.print(",");
                  sendJSON(writer, "minHeight", s.getMinHeight());
                  writer.print(",");
                  sendJSON(writer, "maxHeight", s.getMaxHeight());
                  writer.println('}');
               }
            }
            else {
               error(response, "Invalid request");
            }
         }
      }
   }


   private static void sendJSON(final PrintWriter writer,
                                final String key,
                                final Sector value) {
      sendJSONKey(writer, key);
      writer.print('{');
      sendJSON(writer, "lowerLatitude", value._lower._latitude);
      writer.print(",");
      sendJSON(writer, "lowerLongitude", value._lower._longitude);
      writer.print(",");
      sendJSON(writer, "upperLatitude", value._upper._latitude);
      writer.print(",");
      sendJSON(writer, "upperLongitude", value._upper._longitude);
      writer.print('}');
   }


   private static void sendJSON(final PrintWriter writer,
                                final String key,
                                final Angle value) {
      sendJSON(writer, key, value._degrees);
   }


   private static void sendJSON(final PrintWriter writer,
                                final String key,
                                final double value) {
      sendJSONKey(writer, key);
      writer.print(value);
   }


   private static void sendJSON(final PrintWriter writer,
                                final String key,
                                final long value) {
      sendJSONKey(writer, key);
      writer.print(value);
   }


   private static void sendJSONKey(final PrintWriter writer,
                                   final String key) {
      writer.print('"');
      writer.print(key);
      writer.print("\":");
   }


   private void error(final HttpServletResponse response,
                      final String msg) throws IOException {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
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

}
