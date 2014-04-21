

package org.glob3.mobile.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.LayerTilesRenderParameters;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.specific.MathUtils_JavaDesktop;


public class VectorialLOD {

   final static String                       ROOT_DIRECTORY     = "LOD";

   final static boolean                      MERCATOR           = true;
   final static int                          FIRST_LEVEL        = 0;
   final static int                          MAX_LEVEL          = 3;
   final static int                          NUM_LEVELS         = (MAX_LEVEL - FIRST_LEVEL) + 1;

   final static int                          MAX_DB_CONNECTIONS = NUM_LEVELS;
   final static int                          CONNECTION_TIMEOUT = 5;                            //seconds

   final static boolean                      VERBOSE            = false;

   private static DataBaseService            _dataBaseService   = null;
   private static String                     _lodFolder         = null;
   private static GConcurrentService         _concurrentService;
   private static LayerTilesRenderParameters _renderParameters;

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
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         catch (final SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
            // TODO Auto-generated catch block
            e.printStackTrace();
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

      final String bboxQuery = buildSectorQuery(sector);
      final String propsQuery = buildPropertiesQuery(includeProperties);

      final String baseQuery0 = "SELECT row_to_json(fc) FROM ( SELECT 'FeatureCollection' As type, array_to_json(array_agg(f)) As features FROM (SELECT 'Feature' As type, ST_AsGeoJSON(ST_SimplifyPreserveTopology(ST_Intersection(lg.";
      final String baseQuery1 = "))::json As geometry, row_to_json((SELECT l FROM (SELECT ";
      final String baseQuery2 = ") As l)) As properties FROM (SELECT * FROM ";
      final String baseQuery3 = ")) As lg WHERE ST_Intersects(";
      final String baseQuery4 = " WHERE (";
      final String baseQuery5 = ")) As f ) As fc;";

      String geoJsonResult = "";
      Connection conn = null;
      Statement st = null;
      try {
         conn = _dataBaseService.getConnection();
         st = conn.createStatement();

         final String theGeom = getGeometryColumnName(st, dataSourceTable);
         final String simplifyTolerance = getMaxVertexTolerance(sector, qualityFactor);

         if ((theGeom == null) || (simplifyTolerance == null)) {
            st.close();
            return null; // null tolerance means no data on this bbox
         }

         //-- full query where first cut, second simplify
         final String fullQuery = baseQuery0 + theGeom + "," + bboxQuery + ")," + simplifyTolerance + baseQuery1 + propsQuery
                                  + baseQuery2 + dataSourceTable + baseQuery4 + geomFilterCriteria + baseQuery3 + theGeom + ","
                                  + bboxQuery + baseQuery5;

         //         System.out.println("fullQuery: " + fullQuery);

         final ResultSet rs = st.executeQuery(fullQuery);

         if (!rs.next()) {
            st.close();
            return null; //no data on this bbox
         }
         geoJsonResult = rs.getString(1);
         st.close();
      }
      catch (final SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      return geoJsonResult;
   }


   private static String buildPropertiesQuery(final String... includeProperties) {

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
    * version 0.12: 
    * - tolerance obtained from the sector deltaLongitude downscaled by 1000. 
    * - qualityFactor to adjust tolerance (usual values: from 1.0 to 10.0)
    *   greater values entail less tolerance on Douglas-Peuker algorithm and 
    *   so on less simplification and more vertex generated as result.
    */
   private static String getMaxVertexTolerance(final Sector sector,
                                               final float qualityFactor) {

      final String result = Float.toString((float) sector._deltaLongitude._degrees / (qualityFactor * 1000f));

      if (VERBOSE) {
         System.out.println("result: " + result);
      }

      return result;
   }


   private static String buildSectorQuery(final Sector sector) {

      String resultQuery = "ST_SetSRID(ST_MakeBox2D(ST_Point(";
      resultQuery = resultQuery + Double.toString(sector._lower._longitude._degrees) + ","
                    + Double.toString(sector._lower._latitude._degrees) + "), ST_Point("
                    + Double.toString(sector._upper._longitude._degrees) + ","
                    + Double.toString(sector._upper._latitude._degrees) + ")),4326)";

      //      System.out.println("BOX QUERY: " + resultQuery);

      return resultQuery;

      // MERCATOR: EPSG:3857, EPSG:900913 (Google)

      //      SELECT row_to_json(fc) FROM ( SELECT 'FeatureCollection' As type, array_to_json(array_agg(f)) As features 
      //      FROM (SELECT 'Feature' As type, ST_AsGeoJSON(ST_SimplifyPreserveTopology(ST_Intersection(lg.the_geom,
      //      ST_SetSRID(ST_MakeBox2D(ST_Point(123.74999999999999,33.75), ST_Point(135.0,45.0)),4326)),0.005625))::
      //      json As geometry, row_to_json((SELECT l FROM (SELECT "continent", "pop_est") As l)) As properties FROM 
      //      (SELECT * FROM ne_10m_admin_0_countries WHERE (true)) As lg WHERE ST_Intersects(the_geom,ST_SetSRID(
      //      ST_MakeBox2D(ST_Point(123.74999999999999,33.75), ST_Point(135.0,45.0)),4326))) As f ) As fc;

      //      String resultQuery = "ST_Transform(ST_SetSRID(ST_MakeBox2D(ST_Point(";
      //      resultQuery = resultQuery + Double.toString(sector._lower._longitude._degrees) + ","
      //                    + Double.toString(sector._lower._latitude._degrees) + "), ST_Point("
      //                    + Double.toString(sector._upper._longitude._degrees) + ","
      //                    + Double.toString(sector._upper._latitude._degrees) + ")),4326),3857)";

      //      System.out.println("BOX QUERY: " + resultQuery);

      //      return resultQuery;
   }


   private static String getGeometryColumnName(final Statement st,
                                               final String tableName) {

      final String geomQuery = "SELECT f_geometry_column FROM geometry_columns WHERE f_table_name='" + tableName + "'";

      ResultSet rs;
      try {
         rs = st.executeQuery(geomQuery);

         if (!rs.next()) {
            return null;
         }
         return rs.getString(1);
      }
      catch (final SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
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


   private static String getTileFileName(final TileSector sector) {

      final String folderName = _lodFolder + File.separatorChar + sector._level;
      final String subFolderName = folderName + File.separatorChar + sector._row;
      //final String subFolderName = (sector._level == 0) ? folderName : folderName + File.separatorChar + sector._row;

      if (!new File(folderName).exists()) {
         new File(folderName).mkdir();
         System.out.println("LEVEL: " + sector._level);
      }

      if (!new File(subFolderName).exists()) {
         new File(subFolderName).mkdir();
      }

      return subFolderName + File.separatorChar + getTileName(sector);
   }


   private static String getTileName(final TileSector sector) {

      return sector._level + "_" + sector._row + "-" + sector._column + ".json";
   }


   private static void createFolderStructure(final DataSource dataSource) {

      if (!new File(ROOT_DIRECTORY).exists()) {
         new File(ROOT_DIRECTORY).mkdir();
      }

      final String projection = (_renderParameters._mercator) ? "MERCATOR" : "WGS84";
      _lodFolder = ROOT_DIRECTORY + File.separatorChar + dataSource._sourceTable + "_" + NUM_LEVELS + "-LEVELS_" + projection;
      if (!new File(_lodFolder).exists()) {
         new File(_lodFolder).mkdir();
      }
   }


   private static ArrayList<TileSector> createFirstLevelTileSectors() {

      final ArrayList<TileSector> levelZeroSectorTiles = new ArrayList<TileSector>();

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

            levelZeroSectorTiles.add(tileSector);
         }
      }

      return levelZeroSectorTiles;
   }


   private static void launchVectorialLODProcessing(final DataSource dataSource) {

      final long start = System.currentTimeMillis();
      System.out.println("Starting vectorial LOD generation of " + dataSource._sourceTable + " source..");

      createFolderStructure(dataSource);

      //assume full sphere topSector for tiles pyramid generation
      final ArrayList<TileSector> firstLevelTileSectors = createFirstLevelTileSectors();

      for (final TileSector topSector : firstLevelTileSectors) {
         generateVectorialLOD(topSector, dataSource);
         //processSubSectors(topSector, dataSource);
      }

      _concurrentService.awaitTermination();

      _dataBaseService.releaseConnections();

      final long time = System.currentTimeMillis() - start;
      System.out.println("Vectorial LOD generation finished in " + getTimeMessage(time));
   }


   private static void generateVectorialLOD(final TileSector sector,
                                            final DataSource dataSource) {

      if (sector._level >= NUM_LEVELS) {
         return;
      }

      try {

         final String geoJson = selectGeometries(dataSource._sourceTable, //
                  sector.getSector(), //
                  2.0f, // qualityFactor
                  dataSource._geomFilterCriteria, //
                  dataSource._includeProperties);

         final String fileName = getTileFileName(sector);
         final FileWriter file = new FileWriter(fileName);
         if (geoJson != null) {
            System.out.println("Generating: " + getTileName(sector));
            file.write(geoJson);
            file.flush();
         }
         else {
            System.out.println("Generated empty tile: " + getTileName(sector));
         }
         file.close();
      }
      catch (final IOException e) {
         System.out.println("Exception while writting geoJson object ! ");
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
      if (subSectorLevel >= NUM_LEVELS) {
         return;
      }

      final Runnable task = new Runnable() {
         @Override
         public void run() {
            generateVectorialLOD(sector, dataSource);
         }
      };

      _concurrentService.execute(task, subSectorLevel);
   }


   private static void initializeConcurrentService() {

      _concurrentService = GConcurrentService.createDefaultConcurrentService(NUM_LEVELS, "G3m vectorial LOD");
   }


   private static void initilializeRenderParameters(final boolean mercator,
                                                    final int firstLevel,
                                                    final int maxLevel) {

      _renderParameters = mercator ? LayerTilesRenderParameters.createDefaultMercator(firstLevel, maxLevel)
                                  : LayerTilesRenderParameters.createDefaultWGS84(Sector.fullSphere(), firstLevel, maxLevel);


   }


   private static void initializeMathUtils() {

      if (IMathUtils.instance() == null) {
         IMathUtils.setInstance(new MathUtils_JavaDesktop());
      }
   }


   private static void initialize() {

      initializeMathUtils();

      initilializeRenderParameters(MERCATOR, FIRST_LEVEL, MAX_LEVEL);

      initializeConcurrentService();

   }


   public static void main(final String[] args) {

      System.out.print("Connect to POSTGIS DB vectorial_test.. ");

      if (createDataBaseService("192.168.1.14", "5432", "postgres", "postgres1g0", "vectorial_test")) {
         //      if (createDataBaseService("igosoftware.dyndns.org", "5414", "postgres", "postgres1g0", "vectorial_test")) {
         System.out.println("done.");

         initialize();

         final DataSource dataSource = new DataSource("ne_10m_admin_0_countries", "true", "continent", "pop_est");
         //         final DataSource dataSource = new DataSource("ne_10m_admin_0_boundary_lines_land", "true", "adm0_left", "labelrank");
         //         final DataSource dataSource = new DataSource("ne_10m_populated_places", "true", "NAMEASCII", "POP_MAX");

         // batch mode to generate full LOD pyramid for a vectorial data source
         launchVectorialLODProcessing(dataSource);

      }
      else {
         System.out.println("Error connecting to vectorial_test DB.");
      }
   }

}
