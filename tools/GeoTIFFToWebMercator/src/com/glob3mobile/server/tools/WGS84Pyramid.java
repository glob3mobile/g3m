

package com.glob3mobile.server.tools;

import java.awt.geom.Point2D;
import java.util.List;

import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.Ellipsoid;


public class WGS84Pyramid extends Pyramid {

   WGS84Pyramid() {
	   Pyramid.setTopSectorSplits(2, 4);
	   
      _topSector = GEOSector.fullSphere();
      _topTiles = createTopTiles();
   }

   public List<GEOTile> createChildren(final GEOSector sector,
                                       final GEOTile tile) {
      final double splitLatitude = tile._sector._center._latitude;
      final double splitLongitude = tile._sector._center._longitude;
      return tile.createSubTiles(sector, splitLatitude, splitLongitude);
   }


   public static GEOSector sectorFor(final int level,
                                     final int column,
                                     final int row) {
      final int splitsByLatitude = topSectorSplitsByLatitude * (int) Math.pow(2, level);
      final int splitsByLongitude = topSectorSplitsByLongitude * (int) Math.pow(2, level);

      final double deltaLatitude = 180.0 / splitsByLatitude;
      final double deltaLongitude = 360.0 / splitsByLongitude;

      final GEOGeodetic lower = new GEOGeodetic( //
               90 - (deltaLatitude * (row + 1)), //
               -180 + (deltaLongitude * column));
      final GEOGeodetic upper = new GEOGeodetic( //
               lower._latitude + deltaLatitude, //
               lower._longitude + deltaLongitude);

      return new GEOSector(lower, upper);
   }
   
   public static double tileShorterSideDistance(int level, int column, int row){
	   
	   GEOSector sector = sectorFor(level,column,row);
	   double distance = -1;
	   
	   try {
		   CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
		   GeodeticCalculator geoCalculator = new GeodeticCalculator(crs);
		   geoCalculator.setStartingGeographicPoint(sector._upper._latitude, sector._lower._longitude);
		   geoCalculator.setDestinationGeographicPoint(sector._lower._latitude, sector._lower._longitude);
		   distance = geoCalculator.getOrthodromicDistance();
	   }
	   catch (Exception e) {System.out.println("Something really wrong happened while calculating distance"); }
	   return distance;
   }


}
