
package org.glob3.mobile.tools.utils;

import java.awt.*;
import java.awt.Color;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import org.glob3.mobile.generated.*;

public class GEOBitmap {
   private final Sector _sector;
   private final int    _width;
   private final int    _height;

   private final BufferedImage _image;
   private final Graphics2D    _g;

   public GEOBitmap(final Sector sector, final int width, final int height, final Color backgroundColor, final boolean hd) {
      _sector = sector;
      _width  = width;
      _height = height;

      _image = new BufferedImage(_width, _height, BufferedImage.TYPE_4BYTE_ABGR);

      _g = _image.createGraphics();
      if (hd) {
         _g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
         _g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
         _g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      }
      _g.setBackground(backgroundColor);
      _g.clearRect(0, 0, width, height);
   }

   public void drawPoint(final Geodetic2D position, final int width, final int height, final Color color) {
      final int x = Math.round((float) (_sector.getUCoordinate(position._longitude) * _width));
      final int y = Math.round((float) (_sector.getVCoordinate(position._latitude) * _height));

      _g.setColor(color);
      _g.fillOval(x - (width / 2), y - (height / 2), width, height);
   }

   public void drawPoint(final String name, final Geodetic2D position, final int width, final int height, final Color color, final Font font,
                         final Color fontColor) {
      final int x = Math.round((float) (_sector.getUCoordinate(position._longitude) * _width));
      final int y = Math.round((float) (_sector.getVCoordinate(position._latitude) * _height));

      _g.setColor(color);
      _g.fillOval(x - (width / 2), y - (height / 2), width, height);

      _g.setColor(fontColor);
      _g.setFont(font);
      final double w = _g.getFontMetrics().getStringBounds(name, _g).getWidth();
      _g.drawString(name, Math.round((float) (x - (w / 2))), y);
   }

   public void drawSector(final Sector sector, final Color fillColor, final Color borderColor) {
      final int xFrom = Math.round((float) (_sector.getUCoordinate(sector._lower._longitude) * _width));
      final int yFrom = Math.round((float) (_sector.getVCoordinate(sector._upper._latitude) * _height));
      final int xTo   = Math.round((float) (_sector.getUCoordinate(sector._upper._longitude) * _width));
      final int yTo   = Math.round((float) (_sector.getVCoordinate(sector._lower._latitude) * _height));

      final int width  = xTo - xFrom;
      final int height = yTo - yFrom;

      _g.setColor(fillColor);
      _g.fillRect(xFrom, yFrom, width, height);

      _g.setColor(borderColor);
      _g.drawRect(xFrom, yFrom, width, height);
   }

   public void savePNG(final File file) throws IOException {
      _g.dispose();

      ImageIO.write(_image, "png", file);
   }

   public synchronized void drawImage(final Sector sector, final BufferedImage image) {
      final int xFrom = Math.round((float) (_sector.getUCoordinate(sector._lower._longitude) * _width));
      final int yFrom = Math.round((float) (_sector.getVCoordinate(sector._upper._latitude) * _height));
      final int xTo   = Math.round((float) (_sector.getUCoordinate(sector._upper._longitude) * _width));
      final int yTo   = Math.round((float) (_sector.getVCoordinate(sector._lower._latitude) * _height));

      final int width  = xTo - xFrom;
      final int height = yTo - yFrom;

      _g.drawImage(image, xFrom, yFrom, width, height, null);
   }

   //   public void drawPolygon(final List<Geodetic2D> outerRing,
   //                           final List<? extends List<Geodetic2D>> holesRings,
   //                           final Color fillColor,
   //                           final Color borderColor,
   //                           final boolean drawVertices,
   //                           final Color verticesColor) {
   //      final Shape shape = createShape(outerRing, holesRings);
   //
   //      _g.setColor(fillColor);
   //      _g.fill(shape);
   //
   //      _g.setColor(borderColor);
   //      _g.draw(shape);
   //
   //      if (drawVertices) {
   //         for (final Geodetic2D position : outerRing) {
   //            drawPoint(position, 2, 2, verticesColor);
   //         }
   //         if (holesRings != null) {
   //            for (final List<Geodetic2D> holeRing : holesRings) {
   //               for (final Geodetic2D position : holeRing) {
   //                  drawPoint(position, 2, 2, verticesColor);
   //               }
   //            }
   //         }
   //      }
   //   }
   //
   //
   //   private Shape createShape(final List<Geodetic2D> outerRing,
   //                             final List<? extends List<Geodetic2D>> holesRings) {
   //      final Polygon outer = createPolygon(outerRing);
   //      if ((holesRings == null) || holesRings.isEmpty()) {
   //         return outer;
   //      }
   //
   //      final Area a = new Area(outer);
   //      for (final List<Geodetic2D> holeRing : holesRings) {
   //         final Area hole = new Area(createPolygon(holeRing));
   //         a.subtract(hole);
   //      }
   //
   //      return a;
   //   }
   //
   //
   //   private Polygon createPolygon(final List<Geodetic2D> positions) {
   //      final Polygon p = new Polygon();
   //      for (final Geodetic2D position : positions) {
   //         final int x = Math.round((float) (_sector.getUCoordinate(position._longitude) * _width));
   //         final int y = Math.round((float) (_sector.getVCoordinate(position._latitude) * _height));
   //         p.addPoint(x, y);
   //      }
   //      return p;
   //   }

}
