

package com.glob3mobile.pointcloud.server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.glob3mobile.pointcloud.EllipsoidalPlanet;
import com.glob3mobile.pointcloud.FlatPlanet;
import com.glob3mobile.pointcloud.Planet;
import com.glob3mobile.pointcloud.SphericalPlanet;
import com.glob3mobile.pointcloud.octree.Geodetic3D;
import com.glob3mobile.pointcloud.octree.PersistentLOD;
import com.glob3mobile.pointcloud.octree.berkeleydb.BerkeleyDBLOD;

import es.igosoftware.euclid.bounding.GAxisAlignedBox;
import es.igosoftware.euclid.vector.GVector3D;
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

      // _cloudDirectory = new File(System.getProperty("user.dir"));
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
                                    final Planet planet,
                                    final HttpServletResponse response) throws IOException {
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("application/json");
      final PrintWriter writer = response.getWriter();

      final PersistentLOD.Statistics statistics = db.getStatistics(false, false);
      //      JSONUtils.sendJSON(writer, statistics);


      writer.print('{');

      JSONUtils.sendJSON(writer, "name", statistics.getPointCloudName());

      writer.print(',');
      JSONUtils.sendJSON(writer, "pointsCount", statistics.getPointsCount());

      writer.print(',');
      JSONUtils.sendJSON(writer, "sector", statistics.getSector());

      writer.print(',');
      JSONUtils.sendJSON(writer, "minHeight", statistics.getMinHeight());

      writer.print(',');
      JSONUtils.sendJSON(writer, "maxHeight", statistics.getMaxHeight());

      writer.print(',');
      JSONUtils.sendJSONKey(writer, "nodes");
      writer.print('[');

      db.acceptDepthFirstVisitor(null, new PersistentLOD.Visitor() {
         private boolean _first;


         @Override
         public void start(final PersistentLOD.Transaction transaction) {
            _first = true;
         }


         @Override
         public boolean visit(final PersistentLOD.Transaction transaction,
                              final PersistentLOD.Node node) {
            if (_first) {
               _first = false;
            }
            else {
               writer.print(',');
            }

            writer.print('{');

            double sumX = 0;
            double sumY = 0;
            double sumZ = 0;
            long pointsCount = 0;
            double minX = Double.POSITIVE_INFINITY;
            double minY = Double.POSITIVE_INFINITY;
            double minZ = Double.POSITIVE_INFINITY;
            double maxX = Double.NEGATIVE_INFINITY;
            double maxY = Double.NEGATIVE_INFINITY;
            double maxZ = Double.NEGATIVE_INFINITY;

            final List<PersistentLOD.NodeLevel> levels = node.getLevels();
            for (final PersistentLOD.NodeLevel level : levels) {
               final List<Geodetic3D> points = level.getPoints(transaction);
               for (final Geodetic3D point : points) {
                  final GVector3D cartesian = planet.toCartesian(point);
                  final double x = cartesian._x;
                  final double y = cartesian._y;
                  final double z = cartesian._z;

                  pointsCount++;

                  sumX += x;
                  sumY += y;
                  sumZ += z;

                  minX = Math.min(minX, x);
                  minY = Math.min(minY, y);
                  minZ = Math.min(minZ, z);

                  maxX = Math.max(maxX, x);
                  maxY = Math.max(maxY, y);
                  maxZ = Math.max(maxZ, z);
               }
            }

            final GVector3D average = new GVector3D(sumX / pointsCount, sumY / pointsCount, sumZ / pointsCount);

            final GAxisAlignedBox bounds = new GAxisAlignedBox(new GVector3D(minX, minY, minZ), new GVector3D(maxX, maxY, maxZ));

            JSONUtils.sendJSON(writer, "i", node.getID());

            writer.print(',');
            JSONUtils.sendJSON(writer, "l", node.getLevelsPointsCount());

            writer.print(',');
            JSONUtils.sendJSON(writer, "a", average);

            writer.print(',');
            JSONUtils.sendJSON(writer, "b", bounds, average);

            writer.print(',');
            JSONUtils.sendJSON(writer, "p", levels.subList(0, 1), planet, average);

            writer.print('}');

            return true;
         }


         @Override
         public void stop(final PersistentLOD.Transaction transaction) {
         }


      });

      writer.print(']');


      writer.println('}');


   }


   //   private static void sendNodeLayout(final PersistentLOD db,
   //                                      final String nodeID,
   //                                      final HttpServletResponse response) throws IOException {
   //      response.setStatus(HttpServletResponse.SC_OK);
   //      response.setContentType("application/json");
   //      final PrintWriter writer = response.getWriter();
   //
   //      final PersistentLOD.NodeLayout layout = db.getNodeLayout(nodeID);
   //      JSONUtils.sendNodeLayoutJSON(writer, layout);
   //   }


   //   private static void sendNodeMetadata(final PersistentLOD db,
   //                                        final String nodeID,
   //                                        final HttpServletResponse response) throws IOException {
   //      final PrintWriter writer = response.getWriter();
   //
   //      final PersistentLOD.Node node = db.getNode(nodeID, false);
   //      if (node == null) {
   //         response.sendError(HttpServletResponse.SC_NOT_FOUND, "node #" + nodeID + " not found");
   //      }
   //      else {
   //         response.setStatus(HttpServletResponse.SC_OK);
   //         response.setContentType("application/json");
   //
   //         //final Planet planet = EllipsoidalPlanet.EARTH;
   //
   //         JSONUtils.sendNodeMetadataJSON(writer, node);
   //      }
   //   }


   //   private static void sendNodeMetadata(final PersistentLOD db,
   //                                        final Sector sector,
   //                                        final HttpServletResponse response) throws IOException {
   //      final PrintWriter writer = response.getWriter();
   //
   //      final PersistentLOD.NodeLayout layout = db.getNodeLayout(sector);
   //      if (layout == null) {
   //         response.sendError(HttpServletResponse.SC_NOT_FOUND, "no layout found for " + sector);
   //      }
   //      else {
   //         response.setStatus(HttpServletResponse.SC_OK);
   //         response.setContentType("application/json");
   //
   //         JSONUtils.sendNodeLayoutJSON(writer, layout);
   //      }
   //   }


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
         final String planetType = request.getParameter("planet");
         final Planet planet = getPlanet(planetType);
         if (planet == null) {
            error(response, "planet parameters invalid or missing: " + planet);
         }
         else {
            sendMetadata(db, planet, response);
         }
      }
      //      else if ((path.length == 3) && path[1].trim().equalsIgnoreCase("layout")) {
      //         final String nodeID = path[2].trim();
      //         sendNodeLayout(db, nodeID, response);
      //      }
      //      else if ((path.length == 3) && path[1].trim().equalsIgnoreCase("metadata")) {
      //         final String nodeID = path[2].trim();
      //         sendNodeMetadata(db, nodeID, response);
      //      }
      //      else if ((path.length == 3) && path[1].trim().equalsIgnoreCase("metadataForSector")) {
      //         final Sector sector = parseSector(path[2]);
      //         if (sector == null) {
      //            error(response, "Invalid sector format");
      //         }
      //         else {
      //            sendNodeMetadata(db, sector, response);
      //         }
      //      }
      else {
         error(response, "Invalid request");
      }
   }


   private static Planet getPlanet(final String type) {
      if ("ellipsoidal".equalsIgnoreCase(type)) {
         return EllipsoidalPlanet.EARTH;
      }
      else if ("flat".equalsIgnoreCase(type)) {
         return FlatPlanet.EARTH;
      }
      else if ("spherical".equalsIgnoreCase(type)) {
         return SphericalPlanet.EARTH;
      }
      else {
         return null;
      }
   }


   //   private static Sector parseSector(final String string) {
   //
   //      // (Sector (lat=39.198205348894802569d, lon=-77.673339843749985789d) - (lat=39.249270846223389242d, lon=-77.607421875d))
   //      // [Sector [lat=39.1982053488948d,      lon=-77.67333984374999d],      [lat=39.24927084622339d,     lon=77.607421875d]]
   //
   //      // "39.198205348894802569|-77.673339843749985789|39.249270846223389242|77.607421875"
   //
   //      final String[] tokens = XStringTokenizer.getAllTokens(string.trim(), "|");
   //      if (tokens.length != 4) {
   //         return null;
   //      }
   //
   //      try {
   //         final double lowerLatitude = Double.parseDouble(tokens[0].trim());
   //         final double lowerLongitude = Double.parseDouble(tokens[1].trim());
   //         final double upperLatitude = Double.parseDouble(tokens[2].trim());
   //         final double upperLongitude = Double.parseDouble(tokens[3].trim());
   //
   //         return Sector.fromDegrees(lowerLatitude, lowerLongitude, upperLatitude, upperLongitude);
   //      }
   //      catch (final NumberFormatException e) {
   //         return null;
   //      }
   //   }


}
