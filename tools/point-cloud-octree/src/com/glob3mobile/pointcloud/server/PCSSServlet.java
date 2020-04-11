

package com.glob3mobile.pointcloud.server;


import java.io.*;
import java.nio.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.glob3mobile.pointcloud.octree.*;
import com.glob3mobile.pointcloud.octree.berkeleydb.*;
import com.glob3mobile.utils.*;

import es.igosoftware.euclid.bounding.*;
import es.igosoftware.euclid.vector.*;
import es.igosoftware.util.*;


public class PCSSServlet
                         extends
                            HttpServlet {
   private static final long serialVersionUID = 1L;


   private static class NodeAverageCacheKey {
      private final Planet _planet;
      private final String _cloudName;
      private final String _nodeID;
      private final float  _verticalExaggeration;
      private final double _deltaHeight;


      private NodeAverageCacheKey(final Planet planet,
                                  final String cloudName,
                                  final String nodeID,
                                  final float verticalExaggeration,
                                  final double deltaHeight) {
         _planet               = planet;
         _cloudName            = cloudName;
         _nodeID               = nodeID;
         _verticalExaggeration = verticalExaggeration;
         _deltaHeight          = deltaHeight;
      }


      @Override
      public int hashCode() {
         final int prime  = 31;
         int       result = 1;
         result = (prime * result) + ((_cloudName == null) ? 0 : _cloudName.hashCode());
         long temp;
         temp   = Double.doubleToLongBits(_deltaHeight);
         result = (prime * result) + (int) (temp ^ (temp >>> 32));
         result = (prime * result) + ((_nodeID == null) ? 0 : _nodeID.hashCode());
         result = (prime * result) + ((_planet == null) ? 0 : _planet.hashCode());
         result = (prime * result) + Float.floatToIntBits(_verticalExaggeration);
         return result;
      }


      @Override
      public boolean equals(final Object obj) {
         if (this == obj) {
            return true;
         }
         if (obj == null) {
            return false;
         }
         if (getClass() != obj.getClass()) {
            return false;
         }
         final NodeAverageCacheKey other = (NodeAverageCacheKey) obj;
         if (_cloudName == null) {
            if (other._cloudName != null) {
               return false;
            }
         }
         else if (!_cloudName.equals(other._cloudName)) {
            return false;
         }
         if (Double.doubleToLongBits(_deltaHeight) != Double.doubleToLongBits(other._deltaHeight)) {
            return false;
         }
         if (_nodeID == null) {
            if (other._nodeID != null) {
               return false;
            }
         }
         else if (!_nodeID.equals(other._nodeID)) {
            return false;
         }
         if (_planet == null) {
            if (other._planet != null) {
               return false;
            }
         }
         else if (!_planet.equals(other._planet)) {
            return false;
         }
         if (Float.floatToIntBits(_verticalExaggeration) != Float.floatToIntBits(other._verticalExaggeration)) {
            return false;
         }
         return true;
      }


   }


   private final LRUCache<NodeAverageCacheKey, GVector3F, RuntimeException> _nodeAverageCache;
   {
      final LRUCache.ValueFactory<NodeAverageCacheKey, GVector3F, RuntimeException> factory = key -> {
         final String             cloudName            = key._cloudName;
         final String             nodeID               = key._nodeID;
         final Planet             planet               = key._planet;
         final float              verticalExaggeration = key._verticalExaggeration;
         final double             deltaHeight          = key._deltaHeight;
         final PersistentLOD      db                   = getDB(cloudName);
         final PersistentLOD.Node node                 = db.getNode(nodeID, true);

         double sumX        = 0;
         double sumY        = 0;
         double sumZ        = 0;
         long   pointsCount = 0;

         final PersistentLOD.Transaction transaction = null;

         final List<PersistentLOD.NodeLevel> levels = node.getLevels();
         for (final PersistentLOD.NodeLevel level : levels) {
            final List<Geodetic3D> points = level.getPoints(transaction);
            for (final Geodetic3D point : points) {
               pointsCount++;

               final GVector3D cartesian = planet.toCartesian(point._latitude, point._longitude, point._height + deltaHeight, verticalExaggeration);

               sumX += cartesian._x;
               sumY += cartesian._y;
               sumZ += cartesian._z;
            }
         }

         return new GVector3F( //
               (float) (sumX / pointsCount), //
               (float) (sumY / pointsCount), //
               (float) (sumZ / pointsCount));
      };
      _nodeAverageCache = new LRUCache<>(1024, factory);
   }


   private enum ResponseFormat {
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


   private final Map<String, PersistentLOD> _openedDBs = new HashMap<>();
   private File                             _cloudDirectory;


   @Override
   public void init(final ServletConfig config) throws ServletException {
      super.init(config);

      _cloudDirectory = new File(System.getProperty("user.dir"));
      // _cloudDirectory = new File("/Volumes/My Passport/_minnesota_lidar_/db");

      log("initialization of " + getClass() + " at " + _cloudDirectory);
   }


   private PersistentLOD getDB(final String cloudName) {
      synchronized (_openedDBs) {
         PersistentLOD result = _openedDBs.get(cloudName);
         if (result == null) {
            try {
               result = BerkeleyDBLOD.openReadOnly(_cloudDirectory, cloudName, -1);
               final PersistentLOD.Statistics statistics = result.getStatistics(true);
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
         _id                = id;
         _levelsPointsCount = Arrays.copyOf(levelsPointsCount, levelsPointsCount.length);
         _average           = average;
         _bounds            = bounds;
         _firstPoints       = new ArrayList<>(firstPoints);
      }

   }


   private static List<NodeMetadata> getNodesMetadata(final PersistentLOD db,
                                                      final RenderParameters params) {
      final List<NodeMetadata> result = new ArrayList<>(10000);

      db.acceptDepthFirstVisitor(null, new PersistentLOD.Visitor() {
         @Override
         public void start(final PersistentLOD.Transaction transaction) {
         }


         @Override
         public boolean visit(final PersistentLOD.Transaction transaction,
                              final PersistentLOD.Node node) {

            double sumX        = 0;
            double sumY        = 0;
            double sumZ        = 0;
            long   pointsCount = 0;
            double minX        = Double.POSITIVE_INFINITY;
            double minY        = Double.POSITIVE_INFINITY;
            double minZ        = Double.POSITIVE_INFINITY;
            double maxX        = Double.NEGATIVE_INFINITY;
            double maxY        = Double.NEGATIVE_INFINITY;
            double maxZ        = Double.NEGATIVE_INFINITY;

            final List<PersistentLOD.NodeLevel> levels = node.getLevels();
            for (final PersistentLOD.NodeLevel level : levels) {
               final List<Geodetic3D> points = level.getPoints(transaction);
               for (final Geodetic3D point : points) {
                  final GVector3D cartesian = params._planet.toCartesian(point._latitude, point._longitude, point._height + params._deltaHeight,
                        params._verticalExaggeration);

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

            final List<Geodetic3D> firstPoints = new ArrayList<>();
            for (int i = 0; i < Math.min(levels.size(), 5); i++) {
               firstPoints.addAll(levels.get(i).getPoints(transaction));
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
                                        final MetadataEntry metadata,
                                        final RenderParameters params) throws IOException {


      final PersistentLOD.Statistics statistics = metadata._statistics;
      final List<NodeMetadata>       nodes      = metadata._nodes;

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
      JSONUtils.sendJSON(writer, "averageHeight", statistics.getAverageHeight());

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
         JSONUtils.sendJSON(writer, "p", node._firstPoints, params._planet, node._average, params._verticalExaggeration);
         writer.print(',');
         JSONUtils.sendHeightsJSON(writer, "h", node._firstPoints);
         writer.print('}');
      }
      writer.print(']');

      writer.println('}');
   }


   private static class MetadataEntry {
      private final Planet                   _planet;
      private final PersistentLOD.Statistics _statistics;
      private final List<NodeMetadata>       _nodes;
      private final float                    _verticalExaggeration;
      private final double                   _deltaHeight;

      private byte[] _buffer = null;


      private MetadataEntry(final Planet planet,
                            final PersistentLOD.Statistics statistics,
                            final List<NodeMetadata> nodes,
                            final float verticalExaggeration,
                            final double deltaHeight) {
         _planet               = planet;
         _statistics           = statistics;
         _nodes                = nodes;
         _verticalExaggeration = verticalExaggeration;
         _deltaHeight          = deltaHeight;
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

         os.write(toLittleEndian(_nodes.size()));

         for (final NodeMetadata node : _nodes) {
            os.write(getNodeArray(_planet, node, _verticalExaggeration, _deltaHeight));
         }

         return os.toByteArray();
      }
   }


   private static Map<String, MetadataEntry> _metadataCache = new HashMap<>();


   private static MetadataEntry getMetadataEntry(final PersistentLOD db,
                                                 final RenderParameters params) {
      synchronized (_metadataCache) {
         final String  key   = db.getCloudName() + "/" + params._planet + "/" + params._verticalExaggeration + "/" + params._deltaHeight;
         MetadataEntry entry = _metadataCache.get(key);
         if (entry == null) {
            entry = new MetadataEntry( //
                  params._planet, //
                  db.getStatistics(false), //
                  getNodesMetadata(db, params), //
                  params._verticalExaggeration, //
                  params._deltaHeight);
            _metadataCache.put(key, entry);
         }
         return entry;
      }
   }


   private static void sendBinaryMetadata(final HttpServletResponse response,
                                          final MetadataEntry metadata) throws IOException {


      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("application/octet-stream");

      final ServletOutputStream os = response.getOutputStream();
      os.write(metadata.getBuffer());
   }


   private enum Area {
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
                                      final NodeMetadata node,
                                      final float verticalExaggeration,
                                      final double deltaHeight) {
      final byte[] id = Utils.toBinaryID(node._id);

      final byte idLength = toByte(id.length);

      final int[]         levelsPointsCount = node._levelsPointsCount;
      final List<Byte>    byteLevels        = new ArrayList<>(levelsPointsCount.length);
      final List<Short>   shortLevels       = new ArrayList<>(levelsPointsCount.length);
      final List<Integer> intLevels         = new ArrayList<>(levelsPointsCount.length);


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

      final byte byteLevelsCount  = toByte(byteLevels.size());
      final byte shortLevelsCount = toByte(shortLevels.size());
      final byte intLevelsCount   = toByte(intLevels.size());

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
                  ByteBufferUtils.sizeOf(planet, node._firstPoints, node._average) + //
                  (node._firstPoints.size() * 4);


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

      ByteBufferUtils.put(buffer, planet, node._firstPoints, node._average, verticalExaggeration, deltaHeight);

      for (final Geodetic3D point : node._firstPoints) {
         buffer.putFloat((float) point._height);
      }

      return buffer.array();
   }


   private static byte[] toLittleEndian(final int value) {
      final ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
      buffer.putInt(value);
      return buffer.array();
   }


   private static byte[] getHeaderArray(final PersistentLOD.Statistics statistics) {
      final long   pointsCount   = statistics.getPointsCount();
      final Sector sector        = statistics.getSector();
      final double minHeight     = statistics.getMinHeight();
      final double maxHeight     = statistics.getMaxHeight();
      final double averageHeight = statistics.getAverageHeight();

      final int bufferSize = //
            ByteBufferUtils.sizeOf(pointsCount) + //
                  ByteBufferUtils.sizeOf(sector) + //
                  ByteBufferUtils.sizeOf(minHeight) + //
                  ByteBufferUtils.sizeOf(maxHeight) + //
                  ByteBufferUtils.sizeOf(averageHeight);

      final ByteBuffer buffer = ByteBuffer.allocate(bufferSize).order(ByteOrder.LITTLE_ENDIAN);

      buffer.putLong(pointsCount);
      ByteBufferUtils.put(buffer, sector);
      buffer.putDouble(minHeight);
      buffer.putDouble(maxHeight);
      buffer.putDouble(averageHeight);

      return buffer.array();
   }


   private static void sendMetadata(final PersistentLOD db,
                                    final RenderParameters params,
                                    final HttpServletResponse response) throws IOException {
      switch (params._format) {
      case JSON: {
         response.addHeader("Access-Control-Allow-Origin", "*");
         sendJSONMetadata(response, getMetadataEntry(db, params), params);
         break;
      }
      case BINARY: {
         response.addHeader("Access-Control-Allow-Origin", "*");
         sendBinaryMetadata(response, getMetadataEntry(db, params));
         break;
      }
      default: {
         error(response, "format not supported: " + params._format);
         break;
      }
      }
   }


   private void sendNodeLevelPoints(final PersistentLOD db,
                                    final RenderParameters params,
                                    final String nodeID,
                                    final int level,
                                    final HttpServletResponse response) throws IOException {
      final PersistentLOD.NodeLevel nodeLevel = db.getNodeLevel(nodeID, level, false);
      if (nodeLevel == null) {
         error(response, "node/level not found: " + nodeID + "/" + level);
         return;
      }

      switch (params._format) {
      case JSON: {
         response.addHeader("Access-Control-Allow-Origin", "*");
         sendJSONNodeLevelPoints(response, db, params, nodeID, nodeLevel);
         break;
      }
      case BINARY: {
         response.addHeader("Access-Control-Allow-Origin", "*");
         sendBinaryNodeLevelPoints(response, db, params, nodeID, nodeLevel);
         break;
      }
      default: {
         error(response, "format not supported: " + params._format);
         break;
      }
      }
   }


   private void sendBinaryNodeLevelPoints(final HttpServletResponse response,
                                          final PersistentLOD db,
                                          final RenderParameters params,
                                          final String nodeID,
                                          final PersistentLOD.NodeLevel nodeLevel) throws IOException {
      final GVector3F average = getNodeAverage(db, params, nodeID);

      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("application/octet-stream");

      final List<Geodetic3D> points = nodeLevel.getPoints(null);

      final int bufferSize = ByteBufferUtils.sizeOf(params._planet, points, average) + (points.size() * 4);

      final ByteBuffer buffer = ByteBuffer.allocate(bufferSize).order(ByteOrder.LITTLE_ENDIAN);
      ByteBufferUtils.put(buffer, params._planet, points, average, params._verticalExaggeration, params._deltaHeight);

      for (final Geodetic3D point : points) {
         buffer.putFloat((float) point._height);
      }

      final ServletOutputStream os = response.getOutputStream();
      os.write(buffer.array());
   }


   private void sendJSONNodeLevelPoints(final HttpServletResponse response,
                                        final PersistentLOD db,
                                        final RenderParameters params,
                                        final String nodeID,
                                        final PersistentLOD.NodeLevel nodeLevel) throws IOException {
      final GVector3F average = getNodeAverage(db, params, nodeID);

      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("application/json");

      final PrintWriter writer = response.getWriter();
      writer.print('[');

      final List<Geodetic3D> points = nodeLevel.getPoints(null);
      JSONUtils.sendJSON(writer, points, params._planet, average, params._verticalExaggeration);

      final float[] heights = new float[points.size()];
      for (int i = 0; i < points.size(); i++) {
         final Geodetic3D point = points.get(i);
         heights[i] = (float) point._height;
      }
      writer.print(',');
      JSONUtils.sendJSON(writer, heights);

      writer.print(']');
   }


   private GVector3F getNodeAverage(final PersistentLOD db,
                                    final RenderParameters params,
                                    final String nodeID) {
      final NodeAverageCacheKey key = new NodeAverageCacheKey(params._planet, db.getCloudName(), nodeID, params._verticalExaggeration, params._deltaHeight);
      return _nodeAverageCache.get(key);
   }


   private static class RenderParameters {

      private final Planet         _planet;
      private final ResponseFormat _format;
      private final float          _verticalExaggeration;
      private final double         _deltaHeight;


      public RenderParameters(final Planet planet,
                              final ResponseFormat format,
                              final float verticalExaggeration,
                              final double deltaHeight) {
         _planet               = planet;
         _format               = format;
         _verticalExaggeration = verticalExaggeration;
         _deltaHeight          = deltaHeight;
      }

   }


   private class InvalidFormat
                               extends
                                  Exception {
      private static final long serialVersionUID = 1L;


      protected InvalidFormat(final String message) {
         super(message);
         // TODO Auto-generated constructor stub
      }

   }


   private RenderParameters getRenderParameters(final HttpServletRequest request) throws InvalidFormat {
      final String planetName = request.getParameter("planet");
      final Planet planet     = getPlanet(planetName);
      if (planet == null) {
         throw new InvalidFormat("planet parameter invalid or missing: " + planetName);
      }

      final String         formatName = request.getParameter("format");
      final ResponseFormat format     = ResponseFormat.get(formatName);
      if (format == null) {
         throw new InvalidFormat("format parameter invalid or missing: " + formatName);
      }

      final String verticalExaggerationStr = request.getParameter("verticalExaggeration");
      final float  verticalExaggeration    = getFloat(verticalExaggerationStr, 1);
      if (Float.isNaN(verticalExaggeration)) {
         throw new InvalidFormat("verticalExaggeration parameter invalid or missing: " + verticalExaggerationStr);
      }

      final String deltaHeightStr = request.getParameter("deltaHeight");
      final double deltaHeight    = getDouble(deltaHeightStr, 0);
      if (Double.isNaN(deltaHeight)) {
         throw new InvalidFormat("deltaHeight parameter invalid or missing: " + deltaHeightStr);
      }

      return new RenderParameters(planet, format, verticalExaggeration, deltaHeight);
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
         try {
            final RenderParameters params = getRenderParameters(request);
            sendMetadata(db, params, response);
         }
         catch (final InvalidFormat e) {
            error(response, e.getMessage());
         }
      }
      else if (path.length == 3) {
         final String nodeID   = path[1];
         final String levelStr = path[2];
         try {
            final int level = Integer.parseInt(levelStr);

            try {
               final RenderParameters params = getRenderParameters(request);
               sendNodeLevelPoints(db, params, nodeID, level, response);
            }
            catch (final InvalidFormat e) {
               error(response, e.getMessage());
            }
         }
         catch (final NumberFormatException e) {
            error(response, "invalid level format: " + levelStr);
         }
      }
      else {
         error(response, "Invalid request");
      }
   }


   private static float getFloat(final String str,
                                 final float defaultValue) {
      if (str == null) {
         return defaultValue;
      }

      try {
         return Float.parseFloat(str);
      }
      catch (final NumberFormatException e) {
         return defaultValue;
      }
   }


   private static double getDouble(final String str,
                                   final double defaultValue) {
      if (str == null) {
         return defaultValue;
      }

      try {
         return Double.parseDouble(str);
      }
      catch (final NumberFormatException e) {
         return defaultValue;
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
