

package com.glob3mobile.geo;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;


public class GEOImage {
   public final GEOSector     _sector;
   public final BufferedImage _bufferedImage;
   public final Point2D       _resolution;


   public GEOImage(final GEOSector sector,
                   final BufferedImage bufferedImage) {
      _sector = sector;
      _bufferedImage = bufferedImage;
      _resolution = calculateResolution();
   }


   private Point2D calculateResolution() {
      final double x = _sector._delta._longitude / _bufferedImage.getWidth();
      final double y = _sector._delta._latitude / _bufferedImage.getHeight();
      return new Point2D.Double(x, y);
   }


   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder();
      builder.append("[GEOImage sector=");
      builder.append(_sector);
      builder.append(", resolution=");
      builder.append(_resolution.getX());
      builder.append("x");
      builder.append(_resolution.getY());
      builder.append("]");
      return builder.toString();
   }


}
