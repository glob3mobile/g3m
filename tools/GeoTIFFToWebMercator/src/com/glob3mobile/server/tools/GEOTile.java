

package com.glob3mobile.server.tools;

import java.util.ArrayList;
import java.util.List;


public class GEOTile {
   public final GEOTile   _parent;
   public final GEOSector _sector;
   public final int       _level;
   public final int       _row;
   public final int       _column;
   
   public short _maxValue;
   public short _minValue;


   GEOTile(final GEOTile parent,
           final GEOSector sector,
           final int level,
           final int row,
           final int column) {
      _parent = parent;
      _sector = sector;
      _level = level;
      _row = row;
      _column = column;
      
      _minValue = Short.MAX_VALUE;
      _maxValue = Short.MIN_VALUE;
   }
   
   public void updateMaxMinValues(short max, short min){
	   _minValue = (short) Math.min(_minValue, min);
	   _maxValue = (short) Math.max(_maxValue, max);
   }


   private GEOTile createSubTile(final double lowerLat,
                                 final double lowerLon,
                                 final double upperLat,
                                 final double upperLon,
                                 final int level,
                                 final int row,
                                 final int column) {
      final GEOSector sector = new GEOSector(new GEOGeodetic(lowerLat, lowerLon), new GEOGeodetic(upperLat, upperLon));
      return new GEOTile(this, sector, level, row, column);
   }


   public final List<GEOTile> createSubTiles(final GEOSector topSector,
                                             final double splitLatitude,
                                             final double splitLongitude) {
      final GEOGeodetic lower = _sector._lower;
      final GEOGeodetic upper = _sector._upper;

      final int nextLevel = _level + 1;

      final int row2 = 2 * _row;
      final int column2 = 2 * _column;

      final List<GEOTile> subTiles = new ArrayList<GEOTile>();


      final GEOSector s1 = new GEOSector( //
               new GEOGeodetic(lower._latitude, lower._longitude), //
               new GEOGeodetic(splitLatitude, splitLongitude));
      if (topSector.touchesWith(s1)) {
         subTiles.add(createSubTile(lower._latitude, lower._longitude, splitLatitude, splitLongitude, nextLevel, row2, column2));
      }

      final GEOSector s2 = new GEOSector( //
               new GEOGeodetic(lower._latitude, splitLongitude), //
               new GEOGeodetic(splitLatitude, upper._longitude));
      if (topSector.touchesWith(s2)) {
         subTiles.add(createSubTile(lower._latitude, splitLongitude, splitLatitude, upper._longitude, nextLevel, row2,
                  column2 + 1));
      }

      final GEOSector s3 = new GEOSector( //
               new GEOGeodetic(splitLatitude, lower._longitude), //
               new GEOGeodetic(upper._latitude, splitLongitude));
      if (topSector.touchesWith(s3)) {
         subTiles.add(createSubTile(splitLatitude, lower._longitude, upper._latitude, splitLongitude, nextLevel, row2 + 1,
                  column2));
      }

      final GEOSector s4 = new GEOSector( //
               new GEOGeodetic(splitLatitude, splitLongitude), //
               new GEOGeodetic(upper._latitude, upper._longitude));
      if (topSector.touchesWith(s4)) {
         subTiles.add(createSubTile(splitLatitude, splitLongitude, upper._latitude, upper._longitude, nextLevel, row2 + 1,
                  column2 + 1));
      }

      return subTiles;
   }


   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder();
      builder.append("[level=");
      builder.append(_level);
      builder.append(", row=");
      builder.append(_row);
      builder.append(", column=");
      builder.append(_column);
      builder.append(", sector=");
      builder.append(_sector);
      builder.append("]");
      return builder.toString();
   }

}
