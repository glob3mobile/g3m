

package com.glob3mobile.server.tools.pyramid;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


class SourcePyramidColumn {
   public final SourcePyramidLevel       _level;
   private final File                    _directory;
   public final int                      _column;
   private final List<SourcePyramidTile> _tiles;


   SourcePyramidColumn(final SourcePyramidLevel level,
                       final File directory,
                       final boolean isDem) {
      _level = level;
      _directory = directory;
      _column = Integer.parseInt(directory.getName());

      _tiles = initializeTiles(isDem);
   }


   private List<SourcePyramidTile> initializeTiles(final boolean isDem) {
      final File[] imagesFiles = SourcePyramid.getNumberedImages(_directory, isDem);
      final List<SourcePyramidTile> tiles = new ArrayList<>(imagesFiles.length);
      for (final File imageFile : imagesFiles) {
         tiles.add(new SourcePyramidTile(this, imageFile));
      }
      return tiles;
   }


   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder();
      builder.append("[SourcePyramidColumn ");
      builder.append(_column);
      builder.append(", tiles=");
      builder.append(_tiles);
      builder.append("]");
      return builder.toString();
   }


   public List<SourcePyramidTile> getTiles() {
      return Collections.unmodifiableList(_tiles);
   }


   SourcePyramidTile getTile(final int row) {
      for (final SourcePyramidTile sourceTile : _tiles) {
         if (sourceTile._row == row) {
            return sourceTile;
         }
      }
      return null;
   }

}
