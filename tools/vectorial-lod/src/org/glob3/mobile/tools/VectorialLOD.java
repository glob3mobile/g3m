

package org.glob3.mobile.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.IJSONParser;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.IStringBuilder;
import org.glob3.mobile.generated.LayerTilesRenderParameters;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.specific.Factory_JavaDesktop;
import org.glob3.mobile.specific.JSONParser_JavaDesktop;
import org.glob3.mobile.specific.Logger_JavaDesktop;
import org.glob3.mobile.specific.MathUtils_JavaDesktop;
import org.glob3.mobile.specific.StringBuilder_JavaDesktop;
import org.glob3.mobile.tools.conversion.jbson2bjson.JBson2BJson;
import org.glob3.mobile.tools.conversion.jbson2bjson.JBson2BJsonException;


public class VectorialLOD {

   //-- Internal constants definition ------------------------------------------------------------------
   final static String  ROOT_DIRECTORY          = "LOD";
   final static String  PARAMETERS_FILE         = "parameters.xml";
   final static String  METADATA_FILENAME       = "metadata.json";

   final static double  OVERLAP_PERCENTAGE      = 10.0;
   final static int     CONNECTION_TIMEOUT      = 5;               //seconds
   final static int     PIXELS_PER_TILE         = 256;
   final static int     SQUARED_PIXELS_PER_TILE = (int) Math.pow(
                                                         (PIXELS_PER_TILE + (PIXELS_PER_TILE * ((2 * OVERLAP_PERCENTAGE) / 100))),
                                                         2);
   final static long    VERTEX_THRESHOLD        = 16000;
   final static int     INITIAL_AREA_FACTOR     = 3;

   final static boolean VERBOSE                 = false;

   private enum GeomType {
      POINT,
      LINESTRING,
      MULTILINESTRING,
      POLYGON,
      MULTIPOLYGON
   }

   //-- Data base connection parameters ----------------------------------------------------------------
   private static String                     HOST               = "igosoftware.dyndns.org";
   private static String                     PORT               = "5414";
   //   private static String                     HOST               = "192.168.1.14";
   //   private static String                     PORT               = "5432";
   private static String                     USER               = "postgres";
   private static String                     PASSWORD           = "postgres1g0";
   private static String                     DATABASE_NAME      = "vectorial_test";

   //-- Data source and filter parameters --------------------------------------------------------------
   private static String                     DATABASE_TABLE     = "ne_10m_admin_0_countries";
   private static String                     FILTER_CRITERIA    = "true";
   //private static String                     FILTER_CRITERIA    = "\"continent\" like 'Euro%' AND \"pop_est\" > 10000000";
   private static String[]                   PROPERTIES;

   //-- Vectorial LOD generation algorithm parameters --------------------------------------------------
   private static float                      QUALITY_FACTOR     = 1.0f;
   private static boolean                    MERCATOR           = true;                         // MERCATOR: EPSG:3857, EPSG:900913 (Google)
   private static int                        FIRST_LEVEL        = 0;
   private static int                        MAX_LEVEL          = 3;
   private static int                        NUM_LEVELS         = (MAX_LEVEL - FIRST_LEVEL) + 1;
   private static int                        MAX_DB_CONNECTIONS = NUM_LEVELS;
   private static String                     OUTPUT_FORMAT      = "geojson";                    // valid values: geojson, bson, both

   //-- Variables ----------------------------------------------------------------------

   private static DataBaseService            _dataBaseService   = null;
   private static String                     _lodFolder         = null;
   private static String                     _geojsonFolder     = null;
   private static String                     _bsonFolder        = null;
   private static String                     _metadataFileName  = null;
   private static GConcurrentService         _concurrentService;
   private static LayerTilesRenderParameters _renderParameters;
   private static TileSector                 _boundSector       = TileSector.FULL_SPHERE_SECTOR;
   private static GeomType                   _geomType;
   private static String                     _theGeomColumnName = null;                         //"the_geom";
   private static String                     _projection        = null;
   private static int                        _firstLevelCreated = 0;
   private static int                        _lastLevelCreated  = 0;

   /*
    * For handling postgis database access and connections
    */
   private static class DataBaseService {

      public final String               _host;
      public final String               _port;
      public final String               _user;
      public final String               _password;
      public final String               _dbName;
      private final java.sql.Connection _connectionPool[];
      private int                       _poolIndex;


      public DataBaseService(final String host,
                             final String port,
                             final String user,
                             final String password,
                             final String dbName) {

         _host = host;
         _port = port;
         _user = user;
         _password = password;
         _dbName = dbName;
         _connectionPool = createConnectionPool();
         _poolIndex = MAX_DB_CONNECTIONS - 1;
      }


      private java.sql.Connection[] createConnectionPool() {

         final java.sql.Connection connPool[] = new java.sql.Connection[MAX_DB_CONNECTIONS];

         /* 
          * Load the JDBC driver and establish a connection. 
          */
         try {
            Class.forName("org.postgresql.Driver");
            //         final String url = "jdbc:postgresql://192.168.1.14:5432/vectorial_test";
            final String connectUrl = "jdbc:postgresql://" + _host + ":" + _port + "/" + _dbName;
            //         System.out.println("connectUrl: " + connectUrl);

            /* Create connection 
             */
            //_conn = DriverManager.getConnection(connectUrl, "postgres", "postgres1g0");
            for (int i = 0; i < MAX_DB_CONNECTIONS; i++) {
               connPool[i] = DriverManager.getConnection(connectUrl, _user, _password);
            }
         }
         catch (final ClassNotFoundException e) {
            ILogger.instance().logError("class not found error: " + e.getMessage());
         }
         catch (final SQLException e) {
            ILogger.instance().logError("SQL error creating connection: " + e.getMessage());
         }

         return connPool;
      }


      public java.sql.Connection getConnection() {

         _poolIndex = (_poolIndex + 1) % MAX_DB_CONNECTIONS;

         return _connectionPool[_poolIndex];
      }


      public void releaseConnections() {

         try {
            for (final Connection conn : _connectionPool) {
               conn.close();
            }
         }
         catch (final SQLException e) {
            ILogger.instance().logError("SQL error releasing connection: " + e.getMessage());
         }

      }
   }


   public static boolean createDataBaseService(final String host,
                                               final String port,
                                               final String user,
                                               final String password,
                                               final String dataBaseName) {

      _dataBaseService = new DataBaseService(host, port, user, password, dataBaseName);

      // Check one of the service connections before return 
      try {
         final Connection conn = _dataBaseService.getConnection();
         if ((conn != null) && conn.isValid(CONNECTION_TIMEOUT)) {
            return true;
         }
      }
      catch (final SQLException e) {
         e.printStackTrace();
      }

      return false;
   }


   private static class DataSource {

      public final String   _sourceTable;
      public final String   _geomFilterCriteria;
      public final String[] _includeProperties;


      public DataSource(final String sourceTable,
                        final String geomFilterCriteria,
                        final String... includeProperties) {

         _sourceTable = sourceTable;
         _geomFilterCriteria = geomFilterCriteria;
         _includeProperties = includeProperties;
      }
   }


   /**
    * 
    * @param dataSourceTable
    *           : table from postgis database containing the vectorial data
    * @param sector
    *           : geometry bounding sector for any of the tiles
    * @param qualityFactor
    *           : value used to adjust simplification tolerance during Douglas-Peucker simplification. Greater values entail less
    *           tolerance, and so on less vertex filtered and more vertex generate for the resultant geometry. Usual values
    *           between 1.0 to 10.0.
    * @param geomFilterCriteria
    *           : filter criteria using pure database query format that will be included in a where clause. i.e.
    *           "\"continent\" like 'Euro%' AND \"pop_est\" > 10000000"
    * @param includeProperties
    *           : fields/columns associated to the vectorial data that shall be included as feature properties in the resultant
    *           geoJson data.
    * @return : String with the vectorial data in geoJson format.
    * 
    */
   public static String selectGeometries(final String dataSourceTable,
                                         final Sector sector,
                                         final float qualityFactor,
                                         final String geomFilterCriteria,
                                         final String... includeProperties) {

      String geoJsonResult = null;
      final int areaFactor = INITIAL_AREA_FACTOR;
      try {
         // -- query example --
         // --SELECT row_to_json(fc) FROM ( SELECT 'FeatureCollection' As type, array_to_json(array_agg(f)) As features FROM (SELECT 'Feature' As type, ST_AsGeoJSON(ST_SimplifyPreserveTopology(ST_Intersection(lg.the_geom,ST_SetSRID(ST_MakeBox2D(ST_Point(-49.5,38.426561832270956), ST_Point(4.5,69.06659668046103)),4326)),0.20210655))::json As geometry, row_to_json((SELECT l FROM (SELECT "type") As l)) As properties FROM (SELECT * FROM roads WHERE (ST_Area(Box2D(the_geom))>0.08169412 and true)) As lg WHERE ST_Intersects(the_geom,ST_SetSRID(ST_MakeBox2D(ST_Point(-49.5,38.426561832270956), ST_Point(4.5,69.06659668046103)),4326))) As f ) As fc;
         //-------------------

         //-- full query for geometry select
         String fullQuery = buildSelectQuery(dataSourceTable, sector, qualityFactor, areaFactor, geomFilterCriteria,
                  includeProperties);

         if (fullQuery == null) {
            ILogger.instance().logError("Invalid data for sector: " + sector.toString() + ". ");
            return null;
         }
         //         System.out.println("fullQuery: " + fullQuery);

         // first attempt: usual parameters
         geoJsonResult = executeQuery(fullQuery);

         if (geoJsonResult == null) {
            return null;
         }

         if (getGeomVertexCount(geoJsonResult) < VERTEX_THRESHOLD) {
            return geoJsonResult;
         }

         ILogger.instance().logWarning("Too much vertex for sector, area tunning: " + sector.toString());

         // second attempt: increase area filter factor
         fullQuery = buildSelectQuery(dataSourceTable, sector, qualityFactor, areaFactor + 1, geomFilterCriteria,
                  includeProperties);

         geoJsonResult = executeQuery(fullQuery);

         if (geoJsonResult == null) {
            return null;
         }

         if (getGeomVertexCount(geoJsonResult) < VERTEX_THRESHOLD) {
            return geoJsonResult;
         }

         ILogger.instance().logWarning("Too much vertex for sector, quality factor tunning: " + sector.toString());

         // third attempt: increase area filter factor and reduce quality factor
         fullQuery = buildSelectQuery(dataSourceTable, sector, qualityFactor / 2.0f, areaFactor + 1, geomFilterCriteria,
                  includeProperties);

         geoJsonResult = executeQuery(fullQuery);

         //return result anyway
         return geoJsonResult;
      }
      catch (final SQLException e) {
         ILogger.instance().logError("SQL error getting data for sector: " + sector.toString() + ". " + e.getMessage());
      }

      return geoJsonResult;
   }


   private static String executeQuery(final String query) throws SQLException {

      String result = null;

      final Connection conn = _dataBaseService.getConnection();
      final Statement st = conn.createStatement();

      final ResultSet rs = st.executeQuery(query);

      if (!rs.next()) {
         st.close();
         return null; //no data on this bbox
      }

      result = rs.getString(1);
      st.close();

      if (result.contains("null")) {
         return null;
      }

      return result;
   }


   //TODO: provisional. Change to json parser vertex count 
   private static long getGeomVertexCount(final String geoJson) {

      //return GEOJSONParser.parseJSON(geoJson).getCoordinatesCount();

      //      final GEOObject jsonObject = GEOJSONParser.parseJSON(geoJson);
      //      final long cuenta = jsonObject.getCoordinatesCount();
      //      System.out.println("num Vertex: " + cuenta);
      //      return cuenta;

      long counter = 0;
      for (int index = 0; index < geoJson.length(); index++) {
         if (geoJson.charAt(index) == '.') {
            counter++;
         }
      }

      final long result = counter / 2;
      //      if (result >= 10000) {
      //         System.out.println("num Vertex: " + result);
      //      }

      return result;
   }


   public static String buildSelectQuery(final String dataSourceTable,
                                         final Sector sector,
                                         final float qualityFactor,
                                         final double areaFactor,
                                         final String geomFilterCriteria,
                                         final String... includeProperties) {

      final String baseQuery0 = "SELECT row_to_json(fc) FROM ( SELECT 'FeatureCollection' As type, array_to_json(array_agg(f)) As features FROM (SELECT 'Feature' As type, ST_AsGeoJSON(ST_SimplifyPreserveTopology(ST_Intersection(lg.";
      final String baseQuery1 = "))::json As geometry, row_to_json((SELECT l FROM (SELECT ";
      final String baseQuery2 = ") As l)) As properties FROM (SELECT * FROM ";
      final String baseQuery3 = ")) As lg WHERE ST_Intersects(";
      final String baseQuery4 = " WHERE (";
      final String baseQuery5 = ")) As f ) As fc;";

      final List<Sector> extendedSector = TileSector.getExtendedSector(sector, OVERLAP_PERCENTAGE);
      final String bboxQuery = buildSectorQuery(extendedSector);

      if (bboxQuery == null) {
         return null;
      }

      final String propsQuery = buildPropertiesQuery(includeProperties);
      final String simplifyTolerance = Float.toString(getMaxVertexTolerance(sector, qualityFactor));
      final String filterCriteria = buildFilterCriterium(geomFilterCriteria, areaFactor, bboxQuery, extendedSector);

      //         System.out.println("FILTER CRITERIA: " + filterCriteria);

      //-- full query final where first cut, second simplify
      final String fullQuery = baseQuery0 + _theGeomColumnName + "," + bboxQuery + ")," + simplifyTolerance + baseQuery1
                               + propsQuery + baseQuery2 + dataSourceTable + baseQuery4 + filterCriteria + baseQuery3
                               + _theGeomColumnName + "," + bboxQuery + baseQuery5;

      //         System.out.println("fullQuery: " + fullQuery);

      // -- ejemplo query --
      // --SELECT row_to_json(fc) FROM ( SELECT 'FeatureCollection' As type, array_to_json(array_agg(f)) As features FROM (SELECT 'Feature' As type, ST_AsGeoJSON(ST_SimplifyPreserveTopology(ST_Intersection(lg.the_geom,ST_SetSRID(ST_MakeBox2D(ST_Point(-49.5,38.426561832270956), ST_Point(4.5,69.06659668046103)),4326)),0.20210655))::json As geometry, row_to_json((SELECT l FROM (SELECT "type") As l)) As properties FROM (SELECT * FROM roads WHERE (ST_Area(Box2D(the_geom))>0.08169412 and true)) As lg WHERE ST_Intersects(the_geom,ST_SetSRID(ST_MakeBox2D(ST_Point(-49.5,38.426561832270956), ST_Point(4.5,69.06659668046103)),4326))) As f ) As fc;
      //-------------------

      return fullQuery;
   }


   //   /**
   //    * 
   //    * @param dataSourceTable
   //    *           : table from postgis database containing the vectorial data
   //    * @param sector
   //    *           : geometry bounding sector for any of the tiles
   //    * @param qualityFactor
   //    *           : value used to adjust simplification tolerance during Douglas-Peucker simplification. Greater values entail less
   //    *           tolerance, and so on less vertex filtered and more vertex generate for the resultant geometry. Usual values
   //    *           between 1.0 to 10.0.
   //    * @param geomFilterCriteria
   //    *           : filter criteria using pure database query format that will be included in a where clause. i.e.
   //    *           "\"continent\" like 'Euro%' AND \"pop_est\" > 10000000"
   //    * @param includeProperties
   //    *           : fields/columns associated to the vectorial data that shall be included as feature properties in the resultant
   //    *           geoJson data.
   //    * @return : String with the vectorial data in geoJson format.
   //    * 
   //    */
   //   public static String selectGeometries(final String dataSourceTable,
   //                                         final Sector sector,
   //                                         final float qualityFactor,
   //                                         final String geomFilterCriteria,
   //                                         final String... includeProperties) {
   //
   //
   //      //final String bboxQuery = buildSectorQuery(sector);
   //      final String bboxQuery = buildSectorQuery(TileSector.getExtendedSector(sector, OVERLAP_PERCENTAGE));
   //
   //      if (bboxQuery == null) {
   //         return null;
   //      }
   //
   //      final String propsQuery = buildPropertiesQuery(includeProperties);
   //
   //      final String baseQuery0 = "SELECT row_to_json(fc) FROM ( SELECT 'FeatureCollection' As type, array_to_json(array_agg(f)) As features FROM (SELECT 'Feature' As type, ST_AsGeoJSON(ST_SimplifyPreserveTopology(ST_Intersection(lg.";
   //      final String baseQuery1 = "))::json As geometry, row_to_json((SELECT l FROM (SELECT ";
   //      final String baseQuery2 = ") As l)) As properties FROM (SELECT * FROM ";
   //      final String baseQuery3 = ")) As lg WHERE ST_Intersects(";
   //      final String baseQuery4 = " WHERE (";
   //      final String baseQuery5 = ")) As f ) As fc;";
   //
   //      String geoJsonResult = null;
   //      Connection conn = null;
   //      Statement st = null;
   //      try {
   //         conn = _dataBaseService.getConnection();
   //         st = conn.createStatement();
   //
   //         final String theGeom = getGeometryColumnName(st, dataSourceTable);
   //         final float tolerance = getMaxVertexTolerance(sector, qualityFactor);
   //         final String simplifyTolerance = Float.toString(tolerance);
   //         final String filterCriteria = buildFilterCriterium(geomFilterCriteria, sector);
   //
   //         //         System.out.println("FILTER CRITERIA: " + filterCriteria);
   //
   //         if ((theGeom == null) || (simplifyTolerance == null)) {
   //            st.close();
   //            return null; // null tolerance means no data on this bbox
   //         }
   //
   //         //-- full query final where first cut, second simplify
   //         final String fullQuery = baseQuery0 + theGeom + "," + bboxQuery + ")," + simplifyTolerance + baseQuery1 + propsQuery
   //                                  + baseQuery2 + dataSourceTable + baseQuery4 + filterCriteria + baseQuery3 + theGeom + ","
   //                                  + bboxQuery + baseQuery5;
   //
   //         //         System.out.println("fullQuery: " + fullQuery);
   //
   //         // -- ejemplos --
   //         // --SELECT row_to_json(fc) FROM ( SELECT 'FeatureCollection' As type, array_to_json(array_agg(f)) As features FROM (SELECT 'Feature' As type, ST_AsGeoJSON(ST_SimplifyPreserveTopology(ST_Intersection(lg.the_geom,ST_SetSRID(ST_MakeBox2D(ST_Point(-49.5,38.426561832270956), ST_Point(4.5,69.06659668046103)),4326)),0.20210655))::json As geometry, row_to_json((SELECT l FROM (SELECT "type") As l)) As properties FROM (SELECT * FROM roads WHERE (ST_Area(Box2D(the_geom))>0.08169412 and true)) As lg WHERE ST_Intersects(the_geom,ST_SetSRID(ST_MakeBox2D(ST_Point(-49.5,38.426561832270956), ST_Point(4.5,69.06659668046103)),4326))) As f ) As fc;
   //         //----------
   //         //SELECT row_to_json(fc) FROM ( 
   //         //  SELECT 'FeatureCollection' As type, array_to_json(array_agg(f)) As features FROM (
   //         //    SELECT 'Feature' As type, ST_AsGeoJSON(ST_SimplifyPreserveTopology(ST_Intersection(lg.the_geom,ST_SetSRID(ST_MakeBox2D(ST_Point(-4.5,38.426561832270956), ST_Point(49.5,69.06659668046103)),4326)),0.20210655))::json As geometry, row_to_json((SELECT l FROM (SELECT "type") As l)) As properties FROM (SELECT * FROM roads WHERE (ST_Area(Box2D(the_geom))>0.08169412 and true)) As lg WHERE ST_Intersects(the_geom,ST_SetSRID(ST_MakeBox2D(ST_Point(-4.5,38.426561832270956), ST_Point(49.5,69.06659668046103)),4326))) As f ) As fc;
   //         //-----------------
   //
   //
   //         //-- PRUEBA => QUITAR ------------------------------------------------------
   //         //         final String fullQuery = "select type, count(*) from roads group by type";
   //         //         final ResultSet rs = st.executeQuery(fullQuery);
   //         //         while (rs.next()) {
   //         //            System.out.println(rs.getString(1) + ", " + rs.getInt(2));
   //         //         }
   //         //         System.exit(1);
   //         //--------------------------------------------------------------------------
   //         final ResultSet rs = st.executeQuery(fullQuery);
   //
   //         if (!rs.next()) {
   //            st.close();
   //            return null; //no data on this bbox
   //         }
   //         geoJsonResult = rs.getString(1);
   //         st.close();
   //         if (geoJsonResult.contains("null")) {
   //            return null;
   //         }
   //      }
   //      catch (final SQLException e) {
   //         // TODO Auto-generated catch block
   //         e.printStackTrace();
   //      }
   //
   //      return geoJsonResult;
   //   }


   //   public static String batchSelectGeometries(final String dataSourceTable,
   //                                              final TileSector sector,
   //                                              final float qualityFactor,
   //                                              final String geomFilterCriteria,
   //                                              final String... includeProperties) {
   //
   //      return selectGeometries(dataSourceTable, sector.getExtendedSector(OVERLAP_PERCENTAGE), qualityFactor, geomFilterCriteria,
   //               includeProperties);
   //   }


   private static TileSector getGeometriesBound(final String dataSourceTable) {

      TileSector boundSector = TileSector.FULL_SPHERE_SECTOR;
      Connection conn = null;
      Statement st = null;
      try {
         conn = _dataBaseService.getConnection();
         st = conn.createStatement();

         //         final String theGeom = getGeometryColumnName(st, dataSourceTable);
         //
         //         if (theGeom == null) {
         //            st.close();
         //            return TileSector.FULL_SPHERE;
         //         }

         final String bboxQuery = "SELECT Box2D(ST_Extent(" + _theGeomColumnName + ")) from " + dataSourceTable;
         //System.out.println("bboxQuery: " + bboxQuery);

         final ResultSet rs = st.executeQuery(bboxQuery);

         if (!rs.next()) {
            st.close();
            return TileSector.FULL_SPHERE_SECTOR;
         }

         final String bboxStr = rs.getString(1);
         st.close();

         boundSector = parseBoundSectorFromBbox(bboxStr);
         //System.out.println("boundSector: " + boundSector.toString());
      }
      catch (final SQLException e) {
         ILogger.instance().logError("SQL error getting geometry bound sector: " + e.getMessage());
      }

      return boundSector;
   }


   private static TileSector parseBoundSectorFromBbox(final String bbox) {

      if ((bbox == null) || bbox.equals("")) {
         return TileSector.FULL_SPHERE_SECTOR;
      }

      System.out.println("Source data bound: " + bbox);

      final int begin = bbox.indexOf("(") + 1;
      final int end = bbox.indexOf(")") - 1;
      final String subBbox = bbox.substring(begin, end);
      final String[] points = subBbox.split(",");
      final String[] lowerStr = points[0].split(" ");
      final String[] upperStr = points[1].split(" ");

      final Geodetic2D lower = new Geodetic2D(Angle.fromDegrees(Double.parseDouble(lowerStr[1])),
               Angle.fromDegrees(Double.parseDouble(lowerStr[0])));
      final Geodetic2D upper = new Geodetic2D(Angle.fromDegrees(Double.parseDouble(upperStr[1])),
               Angle.fromDegrees(Double.parseDouble(upperStr[0])));

      return new TileSector(lower, upper, null, 0, 0, 0);
   }


   private static GeomType getGeometriesType(final String dataSourceTable) {

      //http://postgis.net/docs/GeometryType.html
      //select GeometryType(way) from planet_osm_polygon LIMIT 1;

      final String geomQuery = "SELECT type FROM geometry_columns WHERE f_table_name='" + dataSourceTable + "'";
      final String auxGeomQuery = "SELECT GeometryType(" + _theGeomColumnName + ") FROM " + dataSourceTable + " LIMIT 1";

      try {
         final Connection conn = _dataBaseService.getConnection();
         final Statement st = conn.createStatement();

         ResultSet rs = st.executeQuery(geomQuery);

         if (!rs.next()) {
            st.close();
            return null;
         }

         String geomTypeStr = rs.getString(1);

         final GeomType geomType = parseGeometryType(geomTypeStr);

         if (geomType != null) {
            st.close();
            return geomType;
         }

         ILogger.instance().logWarning("Unknown geometry type. Attempt alternative strategy.");

         //-- alternative strategy for unknown geometry types. Query to any of the rows
         rs = st.executeQuery(auxGeomQuery);

         if (!rs.next()) {
            st.close();
            return null;
         }

         geomTypeStr = rs.getString(1);
         st.close();

         return parseGeometryType(geomTypeStr);

      }
      catch (final SQLException e) {
         ILogger.instance().logError("SQL error getting geometry type: " + e.getMessage());
      }
      return null;
   }


   private static GeomType parseGeometryType(final String type) {

      GeomType geomType = null;

      if (type.trim().equalsIgnoreCase("POLYGON")) {
         geomType = GeomType.POLYGON;
      }
      else if (type.trim().equalsIgnoreCase("MULTIPOLYGON")) {
         geomType = GeomType.MULTIPOLYGON;
      }
      else if (type.trim().equalsIgnoreCase("LINESTRING")) {
         geomType = GeomType.LINESTRING;
      }
      else if (type.trim().equalsIgnoreCase("MULTILINESTRING")) {
         geomType = GeomType.MULTILINESTRING;
      }
      else if (type.trim().equalsIgnoreCase("POINT")) {
         geomType = GeomType.POINT;
      }

      return geomType;
   }


   private static String buildPropertiesQuery(final String... includeProperties) {

      if (includeProperties == null) {
         return "false";
      }

      String result = "";
      boolean first = true;

      for (final String prop : includeProperties) {
         if (first) {
            result = getQuotedString(prop);
            first = false;
         }
         else {
            result = result + ", " + getQuotedString(prop);
         }
      }

      return result;
   }


   private static String getQuotedString(final String data) {

      return "\"" + data.trim() + "\"";
   }


   /*
    * version 0.13: 
    * - tolerance obtained from the sector deltaLongitude downscaled by 500. 
    * - qualityFactor to adjust tolerance (usual values: from 1.0 to 10.0)
    *   greater values entail less tolerance on Douglas-Peuker algorithm and 
    *   so on less simplification and more vertex generated as result.
    */
   private static float getMaxVertexTolerance(final Sector sector,
                                              final float qualityFactor) {

      //final float tolerance = (float) (sector._deltaLongitude._degrees / (qualityFactor * 1000f));
      final double hypotenuse = Math.sqrt(Math.pow(sector._deltaLatitude._degrees, 2)
                                          + Math.pow(sector._deltaLongitude._degrees, 2));
      //final float tolerance = (float) (hypotenuse / (qualityFactor * 500f));
      final float tolerance = (float) (hypotenuse / (qualityFactor * 512f));
      //      System.out.println("tolerance: " + tolerance);
      //      final float tolerance2 = (float) (sector.getAngularAreaInSquaredDegrees() / (qualityFactor * 256f * 256f));
      //      System.out.println("tolerance2: " + tolerance2);

      if (VERBOSE) {
         System.out.println("tolerance: " + tolerance);
      }

      return tolerance;
   }


   private static String buildFilterCriterium(final String filterCriteria,
                                              final double areaFactor,
                                              final String bboxQuery,
                                              final List<Sector> extendedSector) {

      //http://postgis.refractions.net/documentation/manual-1.4/ST_NPoints.html
      //http://postgis.refractions.net/docs/ST_Extent.html
      //http://postgis.refractions.net/docs/ST_Area.html

      if (_geomType == null) {
         return filterCriteria;
      }

      if (_geomType == GeomType.POINT) {
         return filterCriteria;
      }

      final double sectorArea = TileSector.getAngularAreaInSquaredDegrees(extendedSector);
      final double factor = areaFactor * areaFactor;

      return "ST_Area(Box2D(" + _theGeomColumnName + "))>" + Double.toString(factor * (sectorArea / SQUARED_PIXELS_PER_TILE))
             + " and " + filterCriteria;

      //      return "ST_Area(Box2D(ST_Intersection(" + _theGeomColumnName + "," + bboxQuery + ")))>"
      //             + Double.toString(factor * (sectorArea / SQUARED_PIXELS_PER_TILE)) + " and " + filterCriteria;

      //      return "ST_Area(ST_Intersection(ST_SetSRID(Box2D(" + _theGeomColumnName + "),4326)," + bboxQuery + "))>"
      //             + Double.toString(factor * (sectorArea / SQUARED_PIXELS_PER_TILE)) + " and " + filterCriteria;

      // ST_Area(Box2D(ST_Intersection(the_geom,ST_SetSRID(ST_MakeBox2D(ST_Point(-49.5,38.426561832270956), ST_Point(4.5,69.06659668046103)),4326))))>0.08169412
   }


   //   private static String buildFilterCriterium(final String filterCriteria,
   //                                              final double areaFactor,
   //                                              final List<Sector> extendedSector) {
   //
   //      //http://postgis.refractions.net/documentation/manual-1.4/ST_NPoints.html
   //      //http://postgis.refractions.net/docs/ST_Extent.html
   //      //http://postgis.refractions.net/docs/ST_Area.html
   //
   //      if (_geomType == null) {
   //         return filterCriteria;
   //      }
   //
   //      if (_geomType == GeomType.POINT) {
   //         return filterCriteria;
   //      }
   //
   //      final double sectorArea = TileSector.getAngularAreaInSquaredDegrees(extendedSector);
   //      final double factor = areaFactor * areaFactor;
   //
   //      return "ST_Area(Box2D(the_geom))>" + Double.toString(factor * (sectorArea / SQUARED_PIXELS_PER_TILE)) + " and "
   //             + filterCriteria;
   //
   //      // -- debería ser así:
   //      //      return "ST_Area(Box2D(ST_Intersection(the_geom,"+bboxQuery+")))>" + Double.toString(9 * (sectorArea / SQUARED_PIXELS_PER_TILE))
   //      //             + " and " + filterCriteria;
   //
   //      // -- o así:
   //      //      return "ST_Area(ST_Intersection(Box2D(the_geom)," + bboxQuery + "))>"
   //      //             + Double.toString(9 * (sectorArea / SQUARED_PIXELS_PER_TILE)) + " and " + filterCriteria;
   //
   //      // ST_Area(Box2D(ST_Intersection(the_geom,ST_SetSRID(ST_MakeBox2D(ST_Point(-49.5,38.426561832270956), ST_Point(4.5,69.06659668046103)),4326))))>0.08169412
   //
   //      //      return "(ST_Area(Box2D(the_geom))>" + Float.toString(2 * (tolerance * tolerance)) + " and " + filterCriteria + ")";
   //   }


   private static String buildSectorQuery(final List<Sector> extendedSector) {

      //SELECT row_to_json(fc) FROM ( SELECT 'FeatureCollection' As type, array_to_json(array_agg(f)) As features FROM (SELECT 'Feature' As type, ST_AsGeoJSON(ST_SimplifyPreserveTopology(ST_Intersection(lg.the_geom,ST_Union(ST_SetSRID(ST_MakeBox2D(ST_Point(175.5,-94.5), ST_Point(180.0,-40.5)),4326),ST_SetSRID(ST_MakeBox2D(ST_Point(-180.0,-94.5), ST_Point(-130.5,-40.5)),4326))),0.027))::json As geometry, row_to_json((SELECT l FROM (SELECT "continent", "pop_est") As l)) As properties FROM (SELECT * FROM ne_10m_admin_0_countries WHERE (true)) As lg WHERE ST_Intersects(the_geom,ST_Union(ST_SetSRID(ST_MakeBox2D(ST_Point(175.5,-94.5), ST_Point(180.0,-40.5)),4326),ST_SetSRID(ST_MakeBox2D(ST_Point(-180.0,-94.5), ST_Point(-130.5,-40.5)),4326)))) As f ) As fc;
      //ST_Union(ST_SetSRID(ST_MakeBox2D(ST_Point(175.5,-94.5), ST_Point(180.0,-40.5)),4326),ST_SetSRID(ST_MakeBox2D(ST_Point(-180.0,-94.5), ST_Point(-130.5,-40.5)),4326))

      if ((extendedSector.size() < 1) || (extendedSector.size() > 2)) {
         return null;
      }

      if (extendedSector.size() == 1) {

         return buildSectorQuery(extendedSector.get(0));
      }

      final String resultQuery = "ST_Union(" + buildSectorQuery(extendedSector.get(0)) + ","
                                 + buildSectorQuery(extendedSector.get(1)) + ")";

      return resultQuery;

   }


   private static String buildSectorQuery(final Sector sector) {

      String resultQuery = "ST_SetSRID(ST_MakeBox2D(ST_Point(";
      resultQuery = resultQuery + Double.toString(sector._lower._longitude._degrees) + ","
                    + Double.toString(sector._lower._latitude._degrees) + "), ST_Point("
                    + Double.toString(sector._upper._longitude._degrees) + ","
                    + Double.toString(sector._upper._latitude._degrees) + ")),4326)";

      //      System.out.println("BOX QUERY: " + resultQuery);

      return resultQuery;
   }


   //   private static String getGeometryColumnName(final Statement st,
   //                                               final String tableName) {
   //
   //      final String geomQuery = "SELECT f_geometry_column FROM geometry_columns WHERE f_table_name='" + tableName + "'";
   //
   //      ResultSet rs;
   //      try {
   //         rs = st.executeQuery(geomQuery);
   //
   //         if (!rs.next()) {
   //            return null;
   //         }
   //         return rs.getString(1);
   //      }
   //      catch (final SQLException e) {
   //         // TODO Auto-generated catch block
   //         e.printStackTrace();
   //      }
   //      return null;
   //   }

   private static String getGeometryColumnName(final String dataSourceTable) {

      final String geomQuery = "SELECT f_geometry_column FROM geometry_columns WHERE f_table_name='" + dataSourceTable + "'";
      //System.out.println("getGeometryColumnNameQuery: " + geomQuery);

      Connection conn = null;
      Statement st = null;
      ResultSet rs = null;
      try {
         conn = _dataBaseService.getConnection();
         st = conn.createStatement();
         rs = st.executeQuery(geomQuery);

         if (!rs.next()) {
            st.close();
            return null;
         }

         final String geomColumnName = rs.getString(1);
         st.close();

         return geomColumnName;
      }
      catch (final SQLException e) {
         ILogger.instance().logError("SQL error getting geometry column: " + e.getMessage());
      }
      return null;
   }


   private static String getTimeMessage(final long ms) {
      return getTimeMessage(ms, true);
   }


   private static String getTimeMessage(final long ms,
                                        final boolean rounded) {
      if (ms < 1000) {
         return ms + "ms";
      }

      if (ms < 60000) {
         final double seconds = ms / 1000d;
         return (rounded ? Math.round(seconds) : seconds) + "s";
      }

      final long minutes = ms / 60000;
      final double seconds = (ms - (minutes * 60000d)) / 1000d;
      if (seconds <= 0) {
         return minutes + "m";
      }
      return minutes + "m " + (rounded ? Math.round(seconds) : seconds) + "s";
   }


   private static String getGeojsonFileName(final TileSector sector) {

      return getFileName(sector, false);
   }


   private static String getBsonFileName(final TileSector sector) {

      return getFileName(sector, true);
   }


   private static String getFileName(final TileSector sector,
                                     final boolean isBson) {

      final String baseFolder = (isBson) ? _bsonFolder : _geojsonFolder;

      final String folderName = baseFolder + File.separatorChar + sector._level;
      if (!new File(folderName).exists()) {
         new File(folderName).mkdir();
         //TODO: -- provisional: dejarlo comentado mientras generemos tiles vacios. Descomentar luego --
         //         if (sector._level <= _firstLevelCreated) {
         //            _firstLevelCreated = sector._level;
         //         }
         //         if (sector._level >= _lastLevelCreated) {
         //            _lastLevelCreated = sector._level;
         //         }
         // -------------------------------------------------------------------------
      }

      final String subFolderName = folderName + File.separatorChar + sector._column;
      if (!new File(subFolderName).exists()) {
         new File(subFolderName).mkdir();
      }

      final String fileName = (isBson) ? subFolderName + File.separatorChar + getTileBsonName(sector)
                                      : subFolderName + File.separatorChar + getTileGeojsonName(sector);

      return fileName;
   }


   private static String getTileGeojsonName(final TileSector sector) {

      //return sector._column + ".geojson";
      return sector.getRow(_renderParameters) + ".geojson";
   }


   private static String getTileBsonName(final TileSector sector) {

      //return sector._column + ".bson";
      return sector.getRow(_renderParameters) + ".bson";
   }


   @SuppressWarnings("unused")
   private static String getTileLabel(final TileSector sector) {

      return sector._level + "/" + sector._column + "/" + sector.getRow(_renderParameters);
   }


   private static void createFolderStructure(final DataSource dataSource) {

      if (!new File(ROOT_DIRECTORY).exists()) {
         new File(ROOT_DIRECTORY).mkdir();
      }

      //final String projection = (_renderParameters._mercator) ? "MERCATOR" : "WGS84";
      _lodFolder = ROOT_DIRECTORY + File.separatorChar + dataSource._sourceTable + "_" + NUM_LEVELS + "-LEVELS_" + _projection;

      if (!new File(_lodFolder).exists()) {
         new File(_lodFolder).mkdir();
      }

      if (generateGeojson()) {
         _geojsonFolder = _lodFolder + File.separatorChar + "GEOJSON";
         if (!new File(_geojsonFolder).exists()) {
            new File(_geojsonFolder).mkdir();
         }
      }

      if (generateBson()) {
         _bsonFolder = _lodFolder + File.separatorChar + "BSON";
         if (!new File(_bsonFolder).exists()) {
            new File(_bsonFolder).mkdir();
         }
      }
   }


   private static ArrayList<TileSector> createFirstLevelTileSectors() {

      final ArrayList<TileSector> levelZeroTileSectors = new ArrayList<TileSector>();

      final Angle fromLatitude = _renderParameters._topSector._lower._latitude;
      final Angle fromLongitude = _renderParameters._topSector._lower._longitude;

      final Angle deltaLan = _renderParameters._topSector._deltaLatitude;
      final Angle deltaLon = _renderParameters._topSector._deltaLongitude;

      final int topSectorSplitsByLatitude = _renderParameters._topSectorSplitsByLatitude;
      final int topSectorSplitsByLongitude = _renderParameters._topSectorSplitsByLongitude;

      final Angle tileHeight = deltaLan.div(topSectorSplitsByLatitude);
      final Angle tileWidth = deltaLon.div(topSectorSplitsByLongitude);

      for (int row = 0; row < topSectorSplitsByLatitude; row++) {
         final Angle tileLatFrom = tileHeight.times(row).add(fromLatitude);
         final Angle tileLatTo = tileLatFrom.add(tileHeight);

         for (int col = 0; col < topSectorSplitsByLongitude; col++) {
            final Angle tileLonFrom = tileWidth.times(col).add(fromLongitude);
            final Angle tileLonTo = tileLonFrom.add(tileWidth);

            final Geodetic2D tileLower = new Geodetic2D(tileLatFrom, tileLonFrom);
            final Geodetic2D tileUpper = new Geodetic2D(tileLatTo, tileLonTo);
            final Sector sector = new Sector(tileLower, tileUpper);

            final TileSector tileSector = new TileSector(sector, null, 0, row, col);

            if (tileSector.intersects(_boundSector)) {
               levelZeroTileSectors.add(tileSector);
            }
         }
      }

      return levelZeroTileSectors;
   }


   private static void launchVectorialLODProcessing(final DataSource dataSource) {

      final long start = System.currentTimeMillis();
      System.out.println("Starting vectorial LOD generation of " + dataSource._sourceTable + " source..");

      createFolderStructure(dataSource);

      _theGeomColumnName = getGeometryColumnName(dataSource._sourceTable);
      if (_theGeomColumnName != null) {
         System.out.println("Geometry column name: " + _theGeomColumnName);
      }

      _boundSector = getGeometriesBound(dataSource._sourceTable);
      //System.out.println(_boundSector.toString());

      _geomType = getGeometriesType(dataSource._sourceTable);
      if (_geomType != null) {
         System.out.println("Source data type: " + _geomType.toString());
      }

      //assume full sphere topSector for tiles pyramid generation
      final ArrayList<TileSector> firstLevelTileSectors = createFirstLevelTileSectors();

      for (final TileSector sector : firstLevelTileSectors) {
         generateVectorialLOD(sector, dataSource);
         //processSubSectors(sector, dataSource);
      }
      System.out.println("Generating.. await termination...");

      _concurrentService.awaitTermination();

      _dataBaseService.releaseConnections();

      writeMetadataFile();

      final long time = System.currentTimeMillis() - start;
      System.out.println("Vectorial LOD generation finished in " + getTimeMessage(time));
   }


   private static void generateVectorialLOD(final TileSector sector,
                                            final DataSource dataSource) {

      if (sector._level > MAX_LEVEL) {
         return;
      }

      if (!_boundSector.intersects(sector)) {
         return;
      }

      if (sector._level >= FIRST_LEVEL) {

         final String geoJson = selectGeometries(dataSource._sourceTable, //
                  sector.getSector(), //
                  QUALITY_FACTOR, // 
                  dataSource._geomFilterCriteria, //
                  dataSource._includeProperties);

         if (geoJson != null) {
            //System.out.println("Generating: ../" + getTileLabel(sector));
            writeOutputFile(geoJson, sector);
         }
         else {
            //System.out.println("Skip empty tile: ../" + getTileLabel(sector));
            writeEmptyFile(sector);
         }

      }

      //final List<TileSector> subSectors = sector.getSubTileSectors();
      final List<TileSector> subSectors = sector.getSubTileSectors(_renderParameters._mercator);
      for (final TileSector s : subSectors) {
         processSubSectors(s, dataSource);
      }
   }


   /*
    * release 0.2
    * A new task will be created to process any subsector of the parent sector
    * 
    */
   private static void processSubSectors(final TileSector sector,
                                         final DataSource dataSource) {

      final int subSectorLevel = sector._level;
      if (subSectorLevel > MAX_LEVEL) {
         return;
      }

      final Runnable task = new Runnable() {
         @Override
         public void run() {
            generateVectorialLOD(sector, dataSource);
         }
      };

      //_concurrentService.execute(task, subSectorLevel);
      _concurrentService.execute(task);
   }


   private static void writeMetadataFile() {

      if (_firstLevelCreated > _lastLevelCreated) {
         _firstLevelCreated = _lastLevelCreated;
      }

      final String metadata = "{ sector: [" + _boundSector._lower._latitude._degrees + ", "
                              + _boundSector._lower._longitude._degrees + ", " + _boundSector._upper._latitude._degrees + ", "
                              + _boundSector._upper._longitude._degrees + " ], minLevel: " + _firstLevelCreated + ", maxLevel: "
                              + _lastLevelCreated + ", projection: " + _projection + " }";

      _metadataFileName = _lodFolder + File.separatorChar + METADATA_FILENAME;
      final File metadataFile = new File(_metadataFileName);
      try {
         if (metadataFile.exists()) {
            metadataFile.delete();
         }
         new File(_metadataFileName).createNewFile();

         final FileWriter file = new FileWriter(_metadataFileName);
         file.write(metadata);
         file.flush();
         file.close();
         System.out.println("File " + METADATA_FILENAME + " created.");
      }
      catch (final IOException e) {
         ILogger.instance().logError("IO error creating metadata file: " + e.getMessage());
      }
   }


   private static void writeEmptyFile(final TileSector sector) {

      try {
         if (generateGeojson()) {
            final File geojsonFile = new File(getGeojsonFileName(sector));
            geojsonFile.createNewFile();
         }
         if (generateBson()) {
            final File bsonFile = new File(getBsonFileName(sector));
            bsonFile.createNewFile();
         }
      }
      catch (final IOException e) {
         ILogger.instance().logError("IO Error writting output file: " + e.getMessage());
      }

   }


   private static void writeOutputFile(final String geoJson,
                                       final TileSector sector) {

      try {

         //TODO: -- provisional: dejarlo aqui mientras generemos tiles vacios. Quitar luego --
         if (sector._level < _firstLevelCreated) {
            _firstLevelCreated = sector._level;
         }
         if (sector._level > _lastLevelCreated) {
            _lastLevelCreated = sector._level;
         }
         // -------------------------------------------------------------------------

         if (generateGeojson()) {
            //System.out.println("Generating: ../" + getTileLabel(sector) + ".geojson");
            final FileWriter file = new FileWriter(getGeojsonFileName(sector));
            file.write(geoJson);
            file.flush();
            file.close();
         }

         if (generateBson()) {
            final File bsonFile = new File(getBsonFileName(sector));
            bsonFile.createNewFile();

            try {
               JBson2BJson.instance().json2bson(geoJson, bsonFile, true);
               if (!generateGeojson()) {
                  //System.out.println("Generating: ../" + getTileLabel(sector) + ".bson");
               }
            }
            catch (final JBson2BJsonException e) {
               ILogger.instance().logError("Error generating bson file: " + e.getMessage());
            }
         }
      }
      catch (final IOException e) {
         ILogger.instance().logError("Error generating output file: " + e.getMessage());
      }
   }


   private static boolean generateGeojson() {

      return (OUTPUT_FORMAT.equalsIgnoreCase("geojson") || OUTPUT_FORMAT.equalsIgnoreCase("both"));
   }


   private static boolean generateBson() {

      return (OUTPUT_FORMAT.equalsIgnoreCase("bson") || OUTPUT_FORMAT.equalsIgnoreCase("both"));
   }


   private static void initializeConcurrentService() {

      //_concurrentService = GConcurrentService.createDefaultConcurrentService(MAX_LEVEL + 1, "G3m vectorial LOD");
      _concurrentService = new GConcurrentService();
   }


   private static void initilializeRenderParameters(final boolean mercator,
                                                    final int firstLevel,
                                                    final int maxLevel) {

      _renderParameters = mercator ? LayerTilesRenderParameters.createDefaultMercator(firstLevel, maxLevel)
                                  : LayerTilesRenderParameters.createDefaultWGS84(Sector.fullSphere(), firstLevel, maxLevel);


   }


   private static void initializeUtils() {

      if (ILogger.instance() == null) {
         ILogger.setInstance(new Logger_JavaDesktop(LogLevel.ErrorLevel));
      }

      if (IFactory.instance() == null) {
         IFactory.setInstance(new Factory_JavaDesktop());
      }

      if (IMathUtils.instance() == null) {
         IMathUtils.setInstance(new MathUtils_JavaDesktop());
      }

      if (IJSONParser.instance() == null) {
         IJSONParser.setInstance(new JSONParser_JavaDesktop());
      }

      IStringBuilder.setInstance(new StringBuilder_JavaDesktop());
   }


   private static void initialize() {

      initializeUtils();

      initilializeRenderParameters(MERCATOR, FIRST_LEVEL, MAX_LEVEL);

      initializeConcurrentService();

      _firstLevelCreated = MAX_LEVEL;
      _lastLevelCreated = FIRST_LEVEL;
      _projection = (_renderParameters._mercator) ? "MERCATOR" : "WGS84";

   }


   private static void initializeFromArguments(final String[] args) {

      //COMMAND LINE example:
      // igosoftware.dyndns.org 5414 postgres postgres1g0 vectorial_test 2.0 false 0 3 ne_10m_admin_0_countries true continent pop_est
      // igosoftware.dyndns.org 5414 postgres postgres1g0 vectorial_test 2.0 false 0 6 ne_10m_admin_0_countries true continent mapcolor7 scalerank
      System.out.println("Initializing from parameters.. ");
      //System.out.println("NUM parameters: " + args.length);

      if (args.length < 11) {
         System.err.println("FAIL: Invalid number of parameters [" + args.length + "].");
         System.exit(1);
      }

      if ((args[0] != null) && (!args[0].equals(""))) {
         HOST = args[0];
         System.out.println("HOST: " + HOST);
      }
      else {
         System.err.println("Invalid HOST argument.");
         System.exit(1);
      }

      if ((args[1] != null) && (!args[1].equals(""))) {
         PORT = args[1];
         System.out.println("PORT: " + PORT);
      }
      else {
         System.err.println("Invalid PORT argument.");
         System.exit(1);
      }

      if ((args[2] != null) && (!args[2].equals(""))) {
         USER = args[2];
         System.out.println("USER: " + USER);
      }
      else {
         System.err.println("Invalid USER argument.");
         System.exit(1);
      }

      if ((args[3] != null) && (!args[3].equals(""))) {
         PASSWORD = args[3];
         System.out.println("PASSWORD: " + PASSWORD);
      }
      else {
         System.err.println("Invalid PASSWORD argument.");
         System.exit(1);
      }

      if ((args[4] != null) && (!args[4].equals(""))) {
         DATABASE_NAME = args[4];
         System.out.println("DATABASE_NAME: " + DATABASE_NAME);
      }
      else {
         System.err.println("Invalid DATABASE_NAME argument.");
         System.exit(1);
      }

      if ((args[5] != null) && (!args[5].equals(""))) {
         QUALITY_FACTOR = Float.parseFloat(args[5]);
         System.out.println("QUALITY_FACTOR: " + QUALITY_FACTOR);
      }
      else {
         System.err.println("Invalid QUALITY_FACTOR argument. Using default QUALITY_FACTOR.");
      }

      if ((args[6] != null) && (!args[6].equals(""))) {
         MERCATOR = Boolean.parseBoolean(args[6]);
         if (MERCATOR) {
            System.out.println("MERCATOR projection");
         }
         else {
            System.out.println("WGS84 projection");
         }
      }
      else {
         System.err.println("Invalid PROJECTION specification.");
         System.exit(1);
      }

      if ((args[7] != null) && (!args[7].equals(""))) {
         FIRST_LEVEL = Integer.parseInt(args[7]);
         System.out.println("FIRST_LEVEL: " + FIRST_LEVEL);
      }
      else {
         System.err.println("Invalid FIRST_LEVEL argument.");
         System.exit(1);
      }

      if ((args[8] != null) && (!args[8].equals(""))) {
         MAX_LEVEL = Integer.parseInt(args[8]);
         System.out.println("MAX_LEVEL: " + MAX_LEVEL);
         NUM_LEVELS = (MAX_LEVEL - FIRST_LEVEL) + 1;
         MAX_DB_CONNECTIONS = NUM_LEVELS;
      }
      else {
         System.err.println("Invalid MAX_LEVEL argument.");
         System.exit(1);
      }

      if ((args[9] != null) && (!args[9].equals(""))) {
         DATABASE_TABLE = args[9];
         System.out.println("DATABASE_TABLE: " + DATABASE_TABLE);
      }
      else {
         System.err.println("Invalid DATABASE_TABLE argument.");
         System.exit(1);
      }

      if ((args[10] != null) && (!args[10].equals(""))) {
         FILTER_CRITERIA = args[10];
         System.out.println("FILTER_CRITERIA: " + FILTER_CRITERIA);
      }
      else {
         System.err.println("Invalid FILTER_CRITERIA argument. Using default FILTER_CRITERIA=true.");
      }

      final int numProperties = args.length - 11;
      if (numProperties > 0) {
         PROPERTIES = new String[numProperties];
         System.out.print("PROPERTIES: ");
         for (int i = 0; i < numProperties; i++) {
            PROPERTIES[i] = args[11 + i];
            System.out.print(PROPERTIES[i]);
            if (i == (numProperties - 1)) {
               System.out.println(".");
            }
            else {
               System.out.print(", ");
            }
         }
         System.out.println();
      }
      else {
         System.err.println("Non PROPERTIES argument. No property included from datasource.");
      }

   }


   private static String[] parsePropertiesFromFile(final String propList) {

      if ((propList == null) || propList.trim().equals("")) {
         return null;
      }

      final String[] properties = propList.split(",");

      for (int i = 0; i < properties.length; i++) {
         properties[i] = properties[i].trim();
      }

      return properties;
   }


   private static boolean initializeFromFile(final String fileName) {

      if (new File(fileName).exists()) {

         System.out.println("Initializing from file.. ");

         final Properties properties = new Properties();
         properties.clear();
         try {
            final BufferedInputStream stream = new BufferedInputStream(new FileInputStream(fileName));
            properties.loadFromXML(stream);

            String tmp;
            HOST = properties.getProperty("HOST");

            if ((HOST != null) && (!HOST.equals(""))) {
               System.out.println("HOST: " + HOST);
            }
            else {
               System.err.println("Invalid HOST argument.");
               System.exit(1);
            }

            PORT = properties.getProperty("PORT");
            if ((PORT != null) && (!PORT.equals(""))) {
               System.out.println("PORT: " + PORT);
            }
            else {
               System.err.println("Invalid PORT argument.");
               System.exit(1);
            }

            USER = properties.getProperty("USER");
            if ((USER != null) && (!USER.equals(""))) {
               System.out.println("USER: " + USER);
            }
            else {
               System.err.println("Invalid USER argument.");
               System.exit(1);
            }

            PASSWORD = properties.getProperty("PASSWORD");
            if ((PASSWORD != null) && (!PASSWORD.equals(""))) {
               System.out.println("PASSWORD: " + PASSWORD);
            }
            else {
               System.err.println("Invalid PASSWORD argument.");
               System.exit(1);
            }

            DATABASE_NAME = properties.getProperty("DATABASE_NAME");
            if ((DATABASE_NAME != null) && (!DATABASE_NAME.equals(""))) {
               System.out.println("DATABASE_NAME: " + DATABASE_NAME);
            }
            else {
               System.err.println("Invalid DATABASE_NAME argument.");
               System.exit(1);
            }

            tmp = properties.getProperty("QUALITY_FACTOR");
            if ((tmp != null) && (!tmp.equals(""))) {
               QUALITY_FACTOR = Float.parseFloat(tmp);
               System.out.println("QUALITY_FACTOR: " + QUALITY_FACTOR);
            }
            else {
               System.err.println("Invalid QUALITY_FACTOR argument. Using default QUALITY_FACTOR.");
            }

            tmp = properties.getProperty("MERCATOR");
            if ((tmp != null) && (!tmp.equals(""))) {
               MERCATOR = Boolean.parseBoolean(tmp);
               if (MERCATOR) {
                  System.out.println("MERCATOR projection");
               }
               else {
                  System.out.println("WGS84 projection");
               }
            }
            else {
               System.err.println("Invalid PROJECTION specification.");
               System.exit(1);
            }

            tmp = properties.getProperty("FIRST_LEVEL");
            if ((tmp != null) && (!tmp.equals(""))) {
               FIRST_LEVEL = Integer.parseInt(tmp);
               System.out.println("FIRST_LEVEL: " + FIRST_LEVEL);
            }
            else {
               System.err.println("Invalid FIRST_LEVEL argument.");
               System.exit(1);
            }

            tmp = properties.getProperty("MAX_LEVEL");
            if ((tmp != null) && (!tmp.equals(""))) {
               MAX_LEVEL = Integer.parseInt(tmp);
               System.out.println("MAX_LEVEL: " + MAX_LEVEL);
               NUM_LEVELS = (MAX_LEVEL - FIRST_LEVEL) + 1;
               MAX_DB_CONNECTIONS = NUM_LEVELS;
            }
            else {
               System.err.println("Invalid MAX_LEVEL argument.");
               System.exit(1);
            }

            OUTPUT_FORMAT = properties.getProperty("OUTPUT_FORMAT");
            if ((OUTPUT_FORMAT != null) && (!OUTPUT_FORMAT.equals(""))) {
               System.out.println("OUTPUT_FORMAT: " + OUTPUT_FORMAT);
            }
            else {
               System.err.println("Invalid OUTPUT_FORMAT argument.");
               System.exit(1);
            }

            DATABASE_TABLE = properties.getProperty("DATABASE_TABLE");
            if ((DATABASE_TABLE != null) && (!DATABASE_TABLE.equals(""))) {
               System.out.println("DATABASE_TABLE: " + DATABASE_TABLE);
            }
            else {
               System.err.println("Invalid DATABASE_TABLE argument.");
               System.exit(1);
            }

            FILTER_CRITERIA = properties.getProperty("FILTER_CRITERIA");
            if ((FILTER_CRITERIA != null) && (!FILTER_CRITERIA.equals(""))) {
               System.out.println("FILTER_CRITERIA: " + FILTER_CRITERIA);
            }
            else {
               System.err.println("Invalid FILTER_CRITERIA argument. Using default FILTER_CRITERIA=true.");
            }

            PROPERTIES = parsePropertiesFromFile(properties.getProperty("PROPERTIES"));
            if ((PROPERTIES != null) && (PROPERTIES.length > 0)) {
               System.out.print("PROPERTIES: ");
               for (int i = 0; i < PROPERTIES.length; i++) {
                  System.out.print(PROPERTIES[i]);
                  if (i == (PROPERTIES.length - 1)) {
                     System.out.println(".");
                  }
                  else {
                     System.out.print(", ");
                  }
               }
               System.out.println();
            }
            else {
               System.err.println("Non PROPERTIES argument. No property included from datasource.");
            }

            return true;
         }
         catch (final FileNotFoundException e) {
            ILogger.instance().logError("Initialization file not found: " + e.getMessage());
         }
         catch (final InvalidPropertiesFormatException e) {
            ILogger.instance().logError("Initialization file invalid format: " + e.getMessage());
         }
         catch (final IOException e) {
            ILogger.instance().logError("Initialization file IO error: " + e.getMessage());
         }

      }
      return false;
   }


   public static void main(final String[] args) {

      if (!initializeFromFile(PARAMETERS_FILE)) {
         initializeFromArguments(args);
      }

      System.out.print("Connecting to " + DATABASE_NAME + " postGIS database.. ");

      if (createDataBaseService(HOST, PORT, USER, PASSWORD, DATABASE_NAME)) {
         //      if (createDataBaseService("igosoftware.dyndns.org", "5414", "postgres", "postgres1g0", "vectorial_test")) {
         System.out.println("done.");

         initialize();

         final DataSource dataSource = new DataSource(DATABASE_TABLE, FILTER_CRITERIA, PROPERTIES);
         //         final DataSource dataSource = new DataSource(DATABASE_TABLE, FILTER_CRITERIA, "continent", "pop_est");
         //         final DataSource dataSource = new DataSource("ne_10m_admin_0_boundary_lines_land", "true", "adm0_left", "labelrank");
         //         final DataSource dataSource = new DataSource("ne_10m_populated_places", "true", "NAMEASCII", "POP_MAX");

         // batch mode to generate full LOD pyramid for a vectorial data source
         launchVectorialLODProcessing(dataSource);

      }
      else {
         System.out.println("Failed. Error connecting to database.");
      }
   }

}
