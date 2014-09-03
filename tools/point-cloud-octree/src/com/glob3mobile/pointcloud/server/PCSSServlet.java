

package com.glob3mobile.pointcloud.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.glob3mobile.pointcloud.EllipsoidalPlanet;
import com.glob3mobile.pointcloud.FlatPlanet;
import com.glob3mobile.pointcloud.Planet;
import com.glob3mobile.pointcloud.SphericalPlanet;
import com.glob3mobile.pointcloud.octree.Geodetic3D;
import com.glob3mobile.pointcloud.octree.PersistentLOD;
import com.glob3mobile.pointcloud.octree.PersistentLOD.Statistics;
import com.glob3mobile.pointcloud.octree.Sector;
import com.glob3mobile.pointcloud.octree.Utils;
import com.glob3mobile.pointcloud.octree.berkeleydb.BerkeleyDBLOD;
import com.glob3mobile.pointcloud.octree.berkeleydb.ByteBufferUtils;

import es.igosoftware.euclid.bounding.GAxisAlignedBox;
import es.igosoftware.euclid.vector.GVector3D;
import es.igosoftware.euclid.vector.GVector3F;
import es.igosoftware.util.XStringTokenizer;


public class PCSSServlet
extends
HttpServlet {
   private static final long serialVersionUID = 1L;


   private static enum ResponseFormat {
      JSON,
      BINARY;

      private static ResponseFormat get(final String name) {
         for (final ResponseFormat candidate : ResponseFormat.values()) {
            if (candidate.name().equalsIgnoreCase(name)) {
               return candidate;
            }
         }
         return null;
      }
   }


   private final Map<String, PersistentLOD> _openedDBs = new HashMap<String, PersistentLOD>();
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


   private static class NodeMetadata {
      private final String           _id;
      private final int[]            _levelsPointsCount;
      private final GVector3F        _average;
      private final GAxisAlignedBox  _bounds;
      private final List<Geodetic3D> _firstPoints;


      public NodeMetadata(final String id,
                          final int[] levelsPointsCount,
                          final GVector3F average,
                          final GAxisAlignedBox bounds,
                          final List<Geodetic3D> firstPoints) {
         _id = id;
         _levelsPointsCount = Arrays.copyOf(levelsPointsCount, levelsPointsCount.length);
         _average = average;
         _bounds = bounds;
         _firstPoints = new ArrayList<Geodetic3D>(firstPoints);
      }

   }


   private static List<NodeMetadata> getNodesMetadata(final PersistentLOD db,
            final Planet planet) {
      final List<NodeMetadata> result = new ArrayList<PCSSServlet.NodeMetadata>(10000);

      db.acceptDepthFirstVisitor(null, new PersistentLOD.Visitor() {
         @Override
         public void start(final PersistentLOD.Transaction transaction) {
         }


         @Override
         public boolean visit(final PersistentLOD.Transaction transaction,
                              final PersistentLOD.Node node) {

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

            final GVector3F average = new GVector3F( //
                     (float) (sumX / pointsCount), //
                     (float) (sumY / pointsCount), //
                     (float) (sumZ / pointsCount));

            final GAxisAlignedBox bounds = new GAxisAlignedBox(new GVector3D(minX, minY, minZ), new GVector3D(maxX, maxY, maxZ));

            final List<Geodetic3D> firstPoints = new ArrayList<Geodetic3D>();
            firstPoints.addAll(levels.get(0).getPoints(transaction));
            if (levels.size() > 1) {
               firstPoints.addAll(levels.get(1).getPoints(transaction));
            }


            final NodeMetadata nodeMetadata = new NodeMetadata( //
                     node.getID(), //
                     node.getLevelsPointsCount(), //
                     average, //
                     bounds, //
                     firstPoints);

            result.add(nodeMetadata);

            return true;
         }


         @Override
         public void stop(final PersistentLOD.Transaction transaction) {
         }


      });

      return result;
   }


   private static void sendJSONMetadata(final HttpServletResponse response,
                                        final PersistentLOD db,
                                        final Planet planet) throws IOException {

      final MetadataEntry metadata = getMetadataEntry(db, planet);

      final Statistics statistics = metadata._statistics;
      final List<NodeMetadata> nodes = metadata._nodes;

      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("application/json");

      final PrintWriter writer = response.getWriter();

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
      boolean first = true;
      for (final NodeMetadata node : nodes) {
         if (first) {
            first = false;
         }
         else {
            writer.print(',');
         }

         writer.print('{');
         JSONUtils.sendJSON(writer, "i", node._id);
         writer.print(',');
         JSONUtils.sendJSON(writer, "l", node._levelsPointsCount);
         writer.print(',');
         JSONUtils.sendJSON(writer, "a", node._average);
         writer.print(',');
         JSONUtils.sendJSON(writer, "b", node._bounds, node._average);
         writer.print(',');
         JSONUtils.sendJSON(writer, "p", node._firstPoints, planet, node._average);
         writer.print('}');
      }
      writer.print(']');

      writer.println('}');
   }


   private static class MetadataEntry {
      private final Planet                   _planet;
      private final PersistentLOD.Statistics _statistics;
      private final List<NodeMetadata>       _nodes;

      private byte[]                         _buffer = null;


      private MetadataEntry(final Planet planet,
                            final PersistentLOD.Statistics statistics,
                            final List<NodeMetadata> nodes) {
         _planet = planet;
         _statistics = statistics;
         _nodes = nodes;
      }


      private synchronized byte[] getBuffer() throws IOException {
         if (_buffer == null) {
            _buffer = createBuffer();
         }
         return _buffer;
      }


      private byte[] createBuffer() throws IOException {
         final ByteArrayOutputStream os = new ByteArrayOutputStream();

         os.write(getHeaderArray(_statistics));

         os.write(toLittleEndiang(_nodes.size()));

         for (final NodeMetadata node : _nodes) {
            os.write(getNodeArray(_planet, node));
         }

         return os.toByteArray();
      }
   }

   private static Map<String, MetadataEntry> _metadataCache = new HashMap<String, MetadataEntry>();


   private static MetadataEntry getMetadataEntry(final PersistentLOD db,
                                                 final Planet planet) {
      synchronized (_metadataCache) {
         final String key = db.getCloudName() + "/" + planet;
         MetadataEntry entry = _metadataCache.get(key);
         if (entry == null) {
            entry = new MetadataEntry(planet, db.getStatistics(false, false), getNodesMetadata(db, planet));
            _metadataCache.put(key, entry);
         }
         return entry;
      }
   }


   private static void sendBinaryMetadata(final HttpServletResponse response,
                                          final PersistentLOD db,
                                          final Planet planet) throws IOException {

      final MetadataEntry metadata = getMetadataEntry(db, planet);

      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("application/octet-stream");

      final ServletOutputStream os = response.getOutputStream();
      os.write(metadata.getBuffer());

      //      os.write(getHeaderArray(statistics));
      //
      //      os.write(toLittleEndiang(nodes.size()));
      //
      //      for (final NodeMetadata node : nodes) {
      //         os.write(getNodeArray(planet, node));
      //      }
   }


   private static enum Area {
      BYTE,
      SHORT,
      INT;
   }


   private static byte toByte(final int value) {
      final byte result = (byte) value;
      if (result != value) {
         throw new RuntimeException("Logic error");
      }
      return result;
   }


   private static byte[] getNodeArray(final Planet planet,
                                      final NodeMetadata node) {
      final byte[] id = Utils.toBinaryID(node._id);

      final byte idLength = toByte(id.length);

      final int[] levelsPointsCount = node._levelsPointsCount;
      final List<Byte> byteLevels = new ArrayList<Byte>(levelsPointsCount.length);
      final List<Short> shortLevels = new ArrayList<Short>(levelsPointsCount.length);
      final List<Integer> intLevels = new ArrayList<Integer>(levelsPointsCount.length);


      Area currentArea = Area.BYTE;
      for (final int levelPointCount : levelsPointsCount) {
         if ((currentArea == Area.SHORT) && (levelPointCount > Short.MAX_VALUE)) {
            currentArea = Area.INT;
         }

         if ((currentArea == Area.BYTE) && (levelPointCount > Byte.MAX_VALUE)) {
            currentArea = Area.SHORT;
         }

         switch (currentArea) {
            case BYTE:
               byteLevels.add((byte) levelPointCount);
               break;
            case SHORT:
               shortLevels.add((short) levelPointCount);
               break;
            case INT:
               intLevels.add(levelPointCount);
               break;
            default:
               throw new RuntimeException("area not supported: " + currentArea);
         }
      }

      final byte byteLevelsCount = toByte(byteLevels.size());
      final byte shortLevelsCount = toByte(shortLevels.size());
      final byte intLevelsCount = toByte(intLevels.size());

      final int bufferSize = //
               ByteBufferUtils.sizeOf(idLength) + //
               idLength + //
               ByteBufferUtils.sizeOf(byteLevelsCount) + //
               ByteBufferUtils.sizeOf(shortLevelsCount) + //
               ByteBufferUtils.sizeOf(intLevelsCount) + //
               byteLevelsCount + //
               (shortLevelsCount * 2) + //
               (intLevelsCount * 4) + //
               ByteBufferUtils.sizeOf(node._average) + //
               ByteBufferUtils.sizeOf(node._bounds, node._average) + //
               ByteBufferUtils.sizeOf(planet, node._firstPoints, node._average);


      final ByteBuffer buffer = ByteBuffer.allocate(bufferSize).order(ByteOrder.LITTLE_ENDIAN);
      buffer.put(idLength);
      buffer.put(id);

      buffer.put(byteLevelsCount);
      buffer.put(shortLevelsCount);
      buffer.put(intLevelsCount);
      for (final byte byteLevel : byteLevels) {
         buffer.put(byteLevel);
      }
      for (final short shortLevel : shortLevels) {
         buffer.putShort(shortLevel);
      }
      for (final int intLevel : intLevels) {
         buffer.putInt(intLevel);
      }

      ByteBufferUtils.put(buffer, node._average);

      ByteBufferUtils.put(buffer, node._bounds, node._average);

      ByteBufferUtils.put(buffer, planet, node._firstPoints, node._average);

      return buffer.array();
   }


   private static byte[] toLittleEndiang(final int value) {
      final ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
      buffer.putInt(value);
      return buffer.array();
   }


   private static byte[] getHeaderArray(final PersistentLOD.Statistics statistics) {
      final long pointsCount = statistics.getPointsCount();
      final Sector sector = statistics.getSector();
      final double minHeight = statistics.getMinHeight();
      final double maxHeight = statistics.getMaxHeight();

      final int bufferSize = //
      ByteBufferUtils.sizeOf(pointsCount) + //
               ByteBufferUtils.sizeOf(sector) + //
               ByteBufferUtils.sizeOf(minHeight) + //
               ByteBufferUtils.sizeOf(maxHeight);

      final ByteBuffer buffer = ByteBuffer.allocate(bufferSize).order(ByteOrder.LITTLE_ENDIAN);

      buffer.putLong(pointsCount);
      ByteBufferUtils.put(buffer, sector);
      buffer.putDouble(minHeight);
      buffer.putDouble(maxHeight);

      return buffer.array();
   }


   private static void sendMetadata(final PersistentLOD db,
                                    final Planet planet,
                                    final ResponseFormat format,
                                    final HttpServletResponse response) throws IOException {
      switch (format) {
         case JSON: {
            sendJSONMetadata(response, db, planet);
            break;
         }
         case BINARY: {
            sendBinaryMetadata(response, db, planet);
            break;
         }
         default: {
            error(response, "format not supported: " + format);
            break;
         }
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
         final String planetName = request.getParameter("planet");
         final String formatName = request.getParameter("format");

         final Planet planet = getPlanet(planetName);
         final ResponseFormat format = ResponseFormat.get(formatName);
         if (planet == null) {
            error(response, "planet parameter invalid or missing: " + planetName);
         }
         else if (format == null) {
            error(response, "format parameter invalid or missing: " + formatName);
         }
         else {
            sendMetadata(db, planet, format, response);
         }
      }
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


}
