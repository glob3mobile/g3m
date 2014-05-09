===============================================
	VECTORIAL LOD GENERATION TOOL
===============================================

RELEASE:
1.0 (2014/04/25)

-------------------------------------------------------------------------------
DESCRIPTION:

The purpose of this tool is the generation of a complete pyramid for the target vectorial data, in order to be used for the Level Of Detail (LOD) management on Glob3 mobile virtual globe. Full pyramid of vectorial data shall be generated in geojson format.

The selection of the target data, number of levels, projection type, and other configuration parameters can be performed in two different ways:
  1) Using the file of properties: parameters.xml
  2) Using runtime parameters as described below. 

If the file parameters.xml exits, the tool try to initialize from its values, otherwise try to initialize from
command line arguments.

The tool include a runnable jar (vectorialLOD.jar) and a script (run.sh) including all the parameters required. Other scripts can be used for specific parameters handling (runAgainstDB.sh).

Resultant data are structured in foders as follows:
- A root folder shall be created for any data source including: database_table+Num_level+projection.
- A folder shall be created for any level named with level number.
- At any level, a folder shall be created for any row named with row number.
- The files generated shall be named according the format: numLevel_row-column.json (i.e. 1_1-6.json, file of level 1, row 1, column 6).

- Non file shall be generated for empty tiles.


-------------------------------------------------------------------------------
REQUIREMENTS:

* The tool works as runnable jar, so java jre/jdk must be installed on the system (java version 1.6_x or superior)
* The tool asumme the use of a postgreSQL/postgis data base containing vectorial data. The following versions are necessary:
	
- postgreSQL: 9.1 or superior
- postgis: 1.5 or superior
- pgAdmin: 1.18 or superior


-------------------------------------------------------------------------------
USER PARAMETERS:

The following parameters are necessary for the correct operation of the tool. (The script: run.sh assume the use of all of them). The order of the parameters on the command line shall be as the proposed bellow, white space separated.

TEMPLATE:
./run.sh HOST PORT USER PASSWORD DATABASE_NAME QUALITY_FACTOR MERCATOR FIRST_LEVEL MAX_LEVEL DATABASE_TABLE
         FILTER_CRITERIA PROPERTY0 PROPERTY1 PROPERTY2..

* Postgis database parameters:
- HOST: (String) Url address for postgis database host access.
- PORT: (String) Port number for postgis database access.
- USER: (String) User name for postgis database access.
- PASSWORD: (String) User password for postgis database access.
- DATABASE_NAME: (String) Name of the accessed postgis database.

* LOD parameters:
- QUALITY_FACTOR: value used to adjust simplification tolerance for Douglas-Peucker simplification. Greater values entail less tolerance, and so on less vertex filtered and more vertex generate for the resultant geometry. Usual values
between 1.0 to 10.0
- MERCATOR: (boolean) Projection selection. true=MERCATOR (epsg:900913 Google) / false=WGS84 (epsg:4326).
- FIRST_LEVEL: first level of tiles to be generated.
- MAX_LEVEL: last level of tiles to be generated.

* Parameters for specific vectorial data
- DATABASE_TABLE: name of the table in the database with the target vectorial data
- FILTER_CRITERIA: filter criteria using pure database query format that will be included in a where clause. i.e.
   "continent" like 'Euro%' AND "pop_est" > 10000000.
   Important: If non filter criteria is used, value "true" must be selected for this parameter.
- PROPERTIES: list of fields/columns associated to the vectorial data that shall be included as feature properties in the resultant geoJson file.


-------------------------------------------------------------------------------
EXAMPLES OF USE:

Example 0) Execution using parameters.xml file:
./run.sh

Example 1) LOD of type polygons data: ne_10m_admin_0_countries table, 6 levels (0 - 5), WGS84, properties: continent, mapcolor7, scalerank

* Using run.sh script:
./run.sh igosoftware.dyndns.org 5414 postgres postgres1g0 vectorial_test 2.0 false 0 5 ne_10m_admin_0_countries true continent mapcolor7 scalerank

* Using runAgainstDB.sh script:
./runAgainstDB.sh 2.0 false 0 5 ne_10m_admin_0_countries true continent mapcolor7 scalerank



Example 2) LOD of type linestring data: ne_10m_admin_0_boundary_lines_land, 5 levels (0 - 4), MERCATOR, properties: scalerank, labelrank

./runAgainstDB.sh 2.0 true 0 4 ne_10m_admin_0_boundary_lines_land true scalerank labelrank



Example 3) LOD of type point data: ne_10m_populated_places, 4 levels (0 - 3), WGS84, properties: SCALERANK, LATITUDE, LONGITUDE

./runAgainstDB.sh 2.0 false 0 3 ne_10m_populated_places true SCALERANK LATITUDE LONGITUDE

Example 4) When using the parameters.xml file, simply run the script:

./run.sh



