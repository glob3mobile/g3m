

package com.glob3mobile.tools.tiling;

import java.awt.geom.Point2D;
import java.util.List;

import com.glob3mobile.geo.GEOSector;


public abstract class TilingPyramid {

   protected TilingPyramid() {
   }


   public abstract List<Tile> createChildren(GEOSector sector,
                                             Tile tile);


   public abstract Point2D resolutionForLevel(int level);


   public abstract int getTileImageWidth();


   public abstract int getTileImageHeight();


   public abstract int bestLevelForResolution(double x,
                                              double y);


   public abstract List<Tile> getTopTiles();


   public abstract int getNumberOfRows(int level);

}
