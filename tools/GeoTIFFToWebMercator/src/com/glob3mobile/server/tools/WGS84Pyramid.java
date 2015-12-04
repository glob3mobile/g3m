

package com.glob3mobile.server.tools;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class WGS84Pyramid {
   static final int           topSectorSplitsByLatitude  = 2;
   static final int           topSectorSplitsByLongitude = 4;

   static int TILE_IMAGE_WIDTH = 256;
   static int TILE_IMAGE_HEIGHT = 256;

   public final GEOSector     _topSector;
   public final List<GEOTile> _topTiles;


   WGS84Pyramid() {
      _topSector = GEOSector.fullSphere();
      _topTiles = createTopTiles();
   }
   
   public static void setTileImageDimensions (final int width,final int height){
	   TILE_IMAGE_WIDTH = width;
	   TILE_IMAGE_HEIGHT = height;
   }


   private List<GEOTile> createTopTiles() {
      final List<GEOTile> result = new ArrayList<GEOTile>(topSectorSplitsByLatitude * topSectorSplitsByLongitude);

      final double fromLatitude = _topSector._lower._latitude;
      final double fromLongitude = _topSector._lower._longitude;

      final double deltaLan = _topSector._delta._latitude;
      final double deltaLon = _topSector._delta._longitude;

      final double tileHeight = deltaLan / topSectorSplitsByLatitude;
      final double tileWidth = deltaLon / topSectorSplitsByLongitude;

      for (int row = 0; row < topSectorSplitsByLatitude; row++) {
         final double tileLatFrom = (tileHeight * row) + fromLatitude;
         final double tileLatTo = tileLatFrom + tileHeight;

         for (int col = 0; col < topSectorSplitsByLongitude; col++) {
            final double tileLonFrom = (tileWidth * col) + fromLongitude;
            final double tileLonTo = tileLonFrom + tileWidth;

            final GEOGeodetic tileLower = new GEOGeodetic(tileLatFrom, tileLonFrom);
            final GEOGeodetic tileUpper = new GEOGeodetic(tileLatTo, tileLonTo);
            final GEOSector sector = new GEOSector(tileLower, tileUpper);

            final int level = 0;
            result.add(new GEOTile(null, sector, level, row, col));
         }
      }

      sortTiles(result);

      return Collections.unmodifiableList(result);
   }


   private static void sortTiles(final List<GEOTile> tiles) {
      Collections.sort( //
               tiles, //
               new Comparator<GEOTile>() {
                  @Override
                  public int compare(final GEOTile i,
                                     final GEOTile j) {
                     final int rowI = i._row;
                     final int rowJ = j._row;
                     if (rowI < rowJ) {
                        return -1;
                     }
                     if (rowI > rowJ) {
                        return 1;
                     }

                     final int columnI = i._column;
                     final int columnJ = j._column;
                     if (columnI < columnJ) {
                        return -1;
                     }
                     if (columnI > columnJ) {
                        return 1;
                     }
                     return 0;
                  }
               });
   }


   public static Point2D resolutionForLevel(final int level) {
      final int splitsByLatitude = topSectorSplitsByLatitude * (int) Math.pow(2, level);
      final int splitsByLongitude = topSectorSplitsByLongitude * (int) Math.pow(2, level);

      final double deltaLatitude = 180.0 / splitsByLatitude;
      final double deltaLongitude = 360.0 / splitsByLongitude;

      final double x = deltaLongitude / TILE_IMAGE_WIDTH;
      final double y = deltaLatitude / TILE_IMAGE_HEIGHT;
      return new Point2D.Double(x, y);
   }


   public static int bestLevelForResolution(final double resX,
                                            final double resY) {
      int level = 0;
      while (true) {
         final Point2D resolution = resolutionForLevel(level);
         if ((resolution.getX() < resX) || (resolution.getY() < resY)) {
            return (level > 0) ? level - 1 : level;
            //return level;
         }
         level++;
      }
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
