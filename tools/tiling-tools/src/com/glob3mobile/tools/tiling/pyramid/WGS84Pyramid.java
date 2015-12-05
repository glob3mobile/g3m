

package com.glob3mobile.tools.tiling.pyramid;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.geotools.coverage.grid.GridCoverage2D;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.glob3mobile.geo.GEOGeodetic;
import com.glob3mobile.geo.GEOSector;


public class WGS84Pyramid
   extends
      Pyramid {


   public static Pyramid createDefault() {
      final GEOSector topSector = GEOSector.fullSphere();
      final int topSectorSplitsByLatitude = 2;
      final int topSectorSplitsByLongitude = 4;
      final int tileImageWidth = 256;
      final int tileImageHeight = 256;

      return new WGS84Pyramid( //
               topSector, //
               topSectorSplitsByLatitude, //
               topSectorSplitsByLongitude, //
               tileImageWidth, //
               tileImageHeight);
   }


   private final GEOSector  _topSector;
   private final int        _topSectorSplitsByLatitude;
   private final int        _topSectorSplitsByLongitude;
   private final int        _tileImageWidth;
   private final int        _tileImageHeight;

   private final List<Tile> _topTiles;


   public WGS84Pyramid(final GEOSector topSector,
                       final int topSectorSplitsByLatitude,
                       final int topSectorSplitsByLongitude,
                       final int tileImageWidth,
                       final int tileImageHeight) {
      _topSector = topSector;
      _topSectorSplitsByLatitude = topSectorSplitsByLatitude;
      _topSectorSplitsByLongitude = topSectorSplitsByLongitude;
      _tileImageWidth = tileImageWidth;
      _tileImageHeight = tileImageHeight;

      _topTiles = createTopTiles();
   }


   private List<Tile> createTopTiles() {
      final List<Tile> result = new ArrayList<Tile>(_topSectorSplitsByLatitude * _topSectorSplitsByLongitude);

      final double fromLatitude = _topSector._lower._latitude;
      final double fromLongitude = _topSector._lower._longitude;

      final double deltaLan = _topSector._delta._latitude;
      final double deltaLon = _topSector._delta._longitude;

      final double tileHeight = deltaLan / _topSectorSplitsByLatitude;
      final double tileWidth = deltaLon / _topSectorSplitsByLongitude;

      for (int row = 0; row < _topSectorSplitsByLatitude; row++) {
         final double tileLatFrom = (tileHeight * row) + fromLatitude;
         final double tileLatTo = tileLatFrom + tileHeight;

         for (int col = 0; col < _topSectorSplitsByLongitude; col++) {
            final double tileLonFrom = (tileWidth * col) + fromLongitude;
            final double tileLonTo = tileLonFrom + tileWidth;

            final GEOGeodetic tileLower = new GEOGeodetic(tileLatFrom, tileLonFrom);
            final GEOGeodetic tileUpper = new GEOGeodetic(tileLatTo, tileLonTo);
            final GEOSector sector = new GEOSector(tileLower, tileUpper);

            final int level = 0;
            result.add(new Tile(null, sector, level, row, col));
         }
      }

      sortTiles(result);

      return Collections.unmodifiableList(result);
   }


   private static void sortTiles(final List<Tile> tiles) {
      Collections.sort( //
               tiles, //
               new Comparator<Tile>() {
                  @Override
                  public int compare(final Tile i,
                                     final Tile j) {
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


   @Override
   public int getNumberOfRows(final int level) {
      return _topSectorSplitsByLatitude * (int) Math.pow(2, level);
   }


   @Override
   public int getTileImageWidth() {
      return _tileImageWidth;
   }


   @Override
   public int getTileImageHeight() {
      return _tileImageHeight;
   }


   @Override
   public List<Tile> getTopTiles() {
      return _topTiles;
   }


   @Override
   public Point2D resolutionForLevel(final int level) {
      final int splitsByLatitude = _topSectorSplitsByLatitude * (int) Math.pow(2, level);
      final int splitsByLongitude = _topSectorSplitsByLongitude * (int) Math.pow(2, level);

      final double deltaLatitude = 180.0 / splitsByLatitude;
      final double deltaLongitude = 360.0 / splitsByLongitude;

      final double x = deltaLongitude / _tileImageWidth;
      final double y = deltaLatitude / _tileImageHeight;
      return new Point2D.Double(x, y);
   }


   @Override
   public List<Tile> createChildren(final GEOSector sector,
                                    final Tile tile) {
      final double splitLatitude = tile._sector._center._latitude;
      final double splitLongitude = tile._sector._center._longitude;
      return tile.createSubTiles(sector, splitLatitude, splitLongitude);
   }


   @Override
   public void checkCRS(final GridCoverage2D coverage) {
      final CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem();
      if (!crs.getName().getCode().equalsIgnoreCase("WGS 84")) {
         throw new RuntimeException("Invalid CRS\n" + crs);
      }
   }


}
