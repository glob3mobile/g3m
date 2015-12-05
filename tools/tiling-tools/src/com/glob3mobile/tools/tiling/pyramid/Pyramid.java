

package com.glob3mobile.tools.tiling.pyramid;

import java.awt.geom.Point2D;
import java.util.List;

import org.geotools.coverage.grid.GridCoverage2D;

import com.glob3mobile.geo.GEOSector;


public abstract class Pyramid {

   protected Pyramid() {
   }


   public abstract List<Tile> createChildren(GEOSector sector,
                                             Tile tile);


   public abstract Point2D resolutionForLevel(int level);


   public abstract int getTileImageWidth();


   public abstract int getTileImageHeight();


   //   public abstract int bestLevelForResolution(double x,
   //                                              double y);

   public int bestLevelForResolution(final double resX,
                                     final double resY) {
      int currentLevel = 0;
      while (true) {
         final Point2D resolution = resolutionForLevel(currentLevel);
         if ((resolution.getX() < resX) || (resolution.getY() < resY)) {
            return (currentLevel > 0) ? currentLevel - 1 : currentLevel;
         }
         currentLevel++;
      }
   }


   public abstract List<Tile> getTopTiles();


   public abstract int getNumberOfRows(int level);


   public abstract void checkCRS(GridCoverage2D coverage);

}
