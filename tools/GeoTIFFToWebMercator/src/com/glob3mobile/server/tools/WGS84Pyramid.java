

package com.glob3mobile.server.tools;

import java.util.List;


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


}
