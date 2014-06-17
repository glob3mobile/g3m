===============================================
	VECTORIAL LOD GENERATION TOOL
===============================================

RELEASE:
1.0 (2014/04/25)
2.0 (2014/06/17)
  - Support for multiple data sources

-------------------------------------------------------------------------------
DESCRIPTION:

The purpose of this tool is the generation of a complete pyramid for the target vectorial data, in order to be used for the Level Of Detail (LOD) management on Glob3 mobile virtual globe. Full pyramid of vectorial data shall be generated in geojson and/or geobson format.

The selection of the target data, number of levels, projection type, and other configuration parameters will be performed by using a configuration file (default: parameters.xml) in XML format. The name of this configuration file will be a command line parameter for the application.

If the configuration file provided as parameter in the command line exits, the tool try to initialize from it, otherwise try to initialize from default configuration file: parameters.xml (if exits).

So, the tool include three basic components:
  - a runnable jar (vectorialLOD.jar) 
  - a configuration file (parameters.xml), including all the parameters required.
  - a script (run.sh), for launching the application.

Resultant data are structured in foders as follows:
- A root folder shall be created for all the data sources, with a merged name like: tables_names+levels+projection.
- A folder shall be created for any level named with level number.
- At any level, a folder shall be created for any column named with column number.
- The files generated shall be named according the format: row.geojson (row.geobson)

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

The script: run.sh assume the use of one parameter: configFileName.xml, that shall be provided in the command line.

TEMPLATE:
./run.sh parameters.xml

This configuration file will include the following parameters (as tags in XML format) for the correct operation of the tool.

//========================================================================================
** Postgis database parameters:

- host: (String) Url address for postgis database host access.
- port: (String) Port number for postgis database access.
- user: (String) User name for postgis database access.
- password: (String) User password for postgis database access.
- database_name: (String) Name of the accessed postgis database.

//========================================================================================
** LOD parameters:

- mercator: (boolean) Projection selection. true=MERCATOR (epsg:900913 Google) / false=WGS84 (epsg:4326). Used to select to kind of tiles pyramid to be generated.
- first_level: first level of tiles to be generated.
- max_level: last level of tiles to be generated.
- output_format: valid output files format: geojson, geobson, both.
- output_folder: output folder path, using the correct operating system path separator. By default, LOD folder shall be used at the current path.
- max_vertex: maximum number of vertex in a geojson/geobson file. If number of vertex obtained is over this limit, a optimization/filtering process shall be launched.
- replace_filtered: Geometry replacement allowed or not. (MULTI)LINE or (MULTI)POLYGON filtered during processing will be replaced by POINT in the output file. REPLACE_FILTERED == 0, means substitutions not allowed. REPLACE_FILTERED > 0, this number is the maximun number of substitutions per tile.

//========================================================================================
** Parameters for specific vectorial data:

For any data source, the configuration file will include a tag with the followind data:

- datasource: name of the table in the database with the target vectorial data
- filter_criteria: filter criteria using pure database query format that will be included in a WHERE clause (for polygons and lines), or ORDER BY clause (for points). 
    i.e.1: "continent" like 'Euro%' AND "pop_est" > 10000000.
    i.e.2: ORDER BY "LABELRANK" DESC LIMIT 20
    If non filter criteria is used, value "true" will be used by default.
- properties: list of fields/columns associated to the vectorial data that shall be included as feature properties in the resultant geojson/geobson file.


-------------------------------------------------------------------------------
EXAMPLES OF USE:

Execution using parameters.xml default file, simply run the script:
./run.sh

Execution using a specific user configuration file: my_parameters.xml, run the script with the command line parameter:
./run.sh my_parameters.xml



