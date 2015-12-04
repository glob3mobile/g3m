

package com.glob3mobile.server.tools.pyramid;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.glob3mobile.utils.IOUtils;
import com.glob3mobile.utils.Logger;


public class SourcePyramid {
   private final File                     _directory;
   private final List<SourcePyramidLevel> _levels;


   public SourcePyramid(final File directory, final boolean isDem) throws IOException {
      IOUtils.checkDirectory(directory);
      _directory = directory;
      Logger.log("Reading \"" + directory.getAbsolutePath() + "\"");

      _levels = initializeLevels(isDem);
   }


   private List<SourcePyramidLevel> initializeLevels(final boolean isDem) {
      final File[] levelsDirectories = getNumberedDirectories(_directory);
      final List<SourcePyramidLevel> levels = new ArrayList<SourcePyramidLevel>(levelsDirectories.length);
      for (final File levelDirectory : levelsDirectories) {
         levels.add(new SourcePyramidLevel(this, levelDirectory, isDem));
      }
      return levels;
   }


   static File[] getNumberedDirectories(final File directory) {
      final File[] result = directory.listFiles(new FilenameFilter() {
         @Override
         public boolean accept(final File dir,
                               final String name) {
            try {
               Integer.parseInt(name);
               return true;
            }
            catch (final NumberFormatException e) {
               return false;
            }
         }
      });

      Arrays.sort(result, new Comparator<File>() {
         @Override
         public int compare(final File o1,
                            final File o2) {
            final int i1 = Integer.parseInt(o1.getName());
            final int i2 = Integer.parseInt(o2.getName());
            return Integer.compare(i1, i2);
         }
      });

      return result;
   }


   private static String removeExtension(final String name) {
      return name.substring(0, name.length() - ".png".length());
   }


   static File[] getNumberedImages(final File directory, final boolean isDem) {
      final File[] result = directory.listFiles(new FilenameFilter() {
         @Override
         public boolean accept(final File dir,
                               final String name) {
            if (!isDem && !name.toLowerCase().endsWith(".png")) {
               return false;
            }
            if (isDem && !name.toLowerCase().endsWith(".bil")) {
                return false;
            }

            try {
               final String nameSansExtension = removeExtension(name);
               Integer.parseInt(nameSansExtension);
               return true;
            }
            catch (final NumberFormatException e) {
               return false;
            }
         }


      });

      Arrays.sort(result, new Comparator<File>() {
         @Override
         public int compare(final File o1,
                            final File o2) {
            final int i1 = Integer.parseInt(removeExtension(o1.getName()));
            final int i2 = Integer.parseInt(removeExtension(o2.getName()));
            return Integer.compare(i1, i2);
         }
      });

      return result;
   }


   public List<SourcePyramidLevel> getLevels() {
      return Collections.unmodifiableList(_levels);
   }


   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder();
      builder.append("SourcePyramid [_directory=");
      builder.append(_directory);
      builder.append(", _levels=");
      builder.append(_levels);
      builder.append("]");
      return builder.toString();
   }


   SourcePyramidTile getBestAncestor(final int level,
                                     final int column,
                                     final int row) {
      int ancestorLevel = level - 1;
      int ancestorColumn = column / 2;
      int ancestorRow = row / 2;

      while (ancestorLevel >= 0) {
         for (final SourcePyramidLevel sourceLevel : _levels) {
            if (sourceLevel._level == ancestorLevel) {
               return sourceLevel.getTile(ancestorColumn, ancestorRow);
            }
         }
         ancestorLevel = ancestorLevel - 1;
         ancestorColumn = ancestorColumn / 2;
         ancestorRow = ancestorRow / 2;
      }
      return null;
   }


   public int getMaxLevel() {
      int maxLevel = Integer.MIN_VALUE;
      for (final SourcePyramidLevel level : _levels) {
         if (level._level > maxLevel) {
            maxLevel = level._level;
         }
      }
      return maxLevel;
   }


}
