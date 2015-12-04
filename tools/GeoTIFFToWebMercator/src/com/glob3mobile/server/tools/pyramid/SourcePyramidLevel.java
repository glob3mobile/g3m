

package com.glob3mobile.server.tools.pyramid;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


class SourcePyramidLevel {
   public SourcePyramid                    _pyramid;
   private final File                      _directory;
   public final int                        _level;
   private final List<SourcePyramidColumn> _columns;


   SourcePyramidLevel(final SourcePyramid pyramid,
                      final File directory,
                      final boolean isDem) {
      _pyramid = pyramid;
      _directory = directory;
      _level = Integer.parseInt(directory.getName());

      _columns = initializeColumns(isDem);
   }


   private List<SourcePyramidColumn> initializeColumns(final boolean isDem) {
      final File[] levelsDirectories = SourcePyramid.getNumberedDirectories(_directory);
      final List<SourcePyramidColumn> levels = new ArrayList<>(levelsDirectories.length);
      for (final File levelDirectory : levelsDirectories) {
         levels.add(new SourcePyramidColumn(this, levelDirectory, isDem));
      }
      return levels;
   }


   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder();
      builder.append("[SourcePyramidLevel ");
      builder.append(_level);
      builder.append(", columns=");
      builder.append(_columns);
      builder.append("]");
      return builder.toString();
   }


   List<SourcePyramidColumn> getColumns() {
      return Collections.unmodifiableList(_columns);
   }


   SourcePyramidTile getTile(final int column,
                             final int row) {
      for (final SourcePyramidColumn sourceColumn : _columns) {
         if (sourceColumn._column == column) {
            return sourceColumn.getTile(row);
         }
      }
      return null;
   }

}
