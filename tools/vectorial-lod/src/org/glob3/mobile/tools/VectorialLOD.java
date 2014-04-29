

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
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.LayerTilesRenderParameters;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.specific.MathUtils_JavaDesktop;


public class VectorialLOD {

   final static String                       ROOT_DIRECTORY     = "LOD";
   final static String                       PARAMETERS_FILE    = "parameters.xml";

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
   private static float                      QUALITY_FACTOR     = 2.0f;
   private static boolean                    MERCATOR           = true;                         // MERCATOR: EPSG:3857, EPSG:900913 (Google)
   private static int                        FIRST_LEVEL        = 0;
   private static int                        MAX_LEVEL          = 3;
   private static int                        NUM_LEVELS         = (MAX_LEVEL - FIRST_LEVEL) + 1;
   private static int                        MAX_DB_CONNECTIONS = NUM_LEVELS;

   //-- Internal constants definition ------------------------------------------------------------------
   //final static float                        QUALITY_FACTOR     = 2.0f;
   final static double                       OVERLAP_PERCENTAGE = 10;
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


      //final String bboxQuery = buildSectorQuery(sector);
      final String bboxQuery = buildSectorQuery(TileSector.getExtendedSector(sector, OVERLAP_PERCENTAGE));

      if (bboxQuery == null) {
         return null;
      }

      final String propsQuery = buildPropertiesQuery(includeProperties);

      final String baseQuery0 = "SELECT row_to_json(fc) FROM ( SELECT 'FeatureCollection' As type, array_to_json(array_agg(f)) As features FROM (SELECT 'Feature' As type, ST_AsGeoJSON(ST_SimplifyPreserveTopology(ST_Intersection(lg.";
      final String baseQuery1 = "))::json As geometry, row_to_json((SELECT l FROM (SELECT ";
      final String baseQuery2 = ") As l)) As properties FROM (SELECT * FROM ";
      final String baseQuery3 = ")) As lg WHERE ST_Intersects(";
      final String baseQuery4 = " WHERE (";
      final String baseQuery5 = ")) As f ) As fc;";

      String geoJsonResult = null;
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

         //System.out.println("fullQuery: " + fullQuery);

         final ResultSet rs = st.executeQuery(fullQuery);

         if (!rs.next()) {
            st.close();
            return null; //no data on this bbox
         }
         geoJsonResult = rs.getString(1);
         st.close();
         if (geoJsonResult.contains("null")) {
            return null;
         }
      }
      catch (final SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      return geoJsonResult;
   }


   //   public static String batchSelectGeometries(final String dataSourceTable,
   //                                              final TileSector sector,
   //                                              final float qualityFactor,
   //                                              final String geomFilterCriteria,
   //                                              final String... includeProperties) {
   //
   //      return selectGeometries(dataSourceTable, sector.getExtendedSector(OVERLAP_PERCENTAGE), qualityFactor, geomFilterCriteria,
   //               includeProperties);
   //   }


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


   private static String buildSectorQuery(final List<Sector> sectorList) {

      //SELECT row_to_json(fc) FROM ( SELECT 'FeatureCollection' As type, array_to_json(array_agg(f)) As features FROM (SELECT 'Feature' As type, ST_AsGeoJSON(ST_SimplifyPreserveTopology(ST_Intersection(lg.the_geom,ST_Union(ST_SetSRID(ST_MakeBox2D(ST_Point(175.5,-94.5), ST_Point(180.0,-40.5)),4326),ST_SetSRID(ST_MakeBox2D(ST_Point(-180.0,-94.5), ST_Point(-130.5,-40.5)),4326))),0.027))::json As geometry, row_to_json((SELECT l FROM (SELECT "continent", "pop_est") As l)) As properties FROM (SELECT * FROM ne_10m_admin_0_countries WHERE (true)) As lg WHERE ST_Intersects(the_geom,ST_Union(ST_SetSRID(ST_MakeBox2D(ST_Point(175.5,-94.5), ST_Point(180.0,-40.5)),4326),ST_SetSRID(ST_MakeBox2D(ST_Point(-180.0,-94.5), ST_Point(-130.5,-40.5)),4326)))) As f ) As fc;

      //ST_Union(ST_SetSRID(ST_MakeBox2D(ST_Point(175.5,-94.5), ST_Point(180.0,-40.5)),4326),ST_SetSRID(ST_MakeBox2D(ST_Point(-180.0,-94.5), ST_Point(-130.5,-40.5)),4326))

      if ((sectorList.size() < 1) || (sectorList.size() > 2)) {
         return null;
      }

      if (sectorList.size() == 1) {

         return buildSectorQuery(sectorList.get(0));
      }

      final String resultQuery = "ST_Union(" + buildSectorQuery(sectorList.get(0)) + "," + buildSectorQuery(sectorList.get(1))
                                 + ")";

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

            levelZeroTileSectors.add(tileSector);
         }
      }

      return levelZeroTileSectors;
   }


   //   private static ArrayList<TileSector> createFirstLevelExtendedTileSectors(final double tolerance) {
   //
   //      final ArrayList<TileSector> levelZeroTileSectors = createFirstLevelTileSectors();
   //      final ArrayList<TileSector> levelZeroExtendedTileSectors = new ArrayList<TileSector>();
   //
   //      for (final TileSector s : levelZeroTileSectors) {
   //
   //         final Geodetic2D geodeticDelta = new Geodetic2D(s._deltaLatitude.times(tolerance), s._deltaLongitude.times(tolerance));
   //         final Geodetic2D extendLower = s._lower.sub(geodeticDelta);
   //         final Geodetic2D extendUpper = s._upper.add(geodeticDelta);
   //
   //         levelZeroExtendedTileSectors.add(new TileSector(extendLower, extendUpper, s._parent, s._level, s._row, s._column));
   //      }
   //
   //      return levelZeroExtendedTileSectors;
   //   }


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
                  //sector.getExtendedSector(OVERLAP_PERCENTAGE),//
                  QUALITY_FACTOR, // qualityFactor
                  dataSource._geomFilterCriteria, //
                  dataSource._includeProperties);

         //         final String fileName = getTileFileName(sector);
         //         final FileWriter file = new FileWriter(fileName);
         if (geoJson != null) {
            final String fileName = getTileFileName(sector);
            final FileWriter file = new FileWriter(fileName);
            System.out.println("Generating: " + getTileName(sector));
            file.write(geoJson);
            file.flush();
            file.close();
         }
         else {
            System.out.println("Skip empty tile: " + getTileName(sector));
         }
         //         file.close();
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
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         catch (final InvalidPropertiesFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
