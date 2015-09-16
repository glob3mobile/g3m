

package com.glob3mobile.vectorial.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;


public class GEOBitmap {


   private final Sector        _sector;
   private final int           _width;
   private final int           _height;

   private final BufferedImage _image;
   private final Graphics2D    _g;


   public GEOBitmap(final Sector sector,
                    final int width,
                    final int height,
                    final Color backgroundColor) {
      _sector = sector;
      _width = width;
      _height = height;

      _image = new BufferedImage(_width, _height, BufferedImage.TYPE_4BYTE_ABGR);
      _g = _image.createGraphics();
      _g.setBackground(backgroundColor);
      _g.clearRect(0, 0, width, height);
   }


   public void drawPoint(final Geodetic2D position,
                         final int width,
                         final int height,
                         final Color color) {
      final int x = Math.round((float) (_sector.getUCoordinate(position._longitude) * _width));
      final int y = Math.round((float) (_sector.getVCoordinate(position._latitude) * _height));

      _g.setColor(color);
      _g.fillOval(x - (width / 2), y - (height / 2), width, height);
   }


   public void drawPoint(final String name,
                         final Geodetic2D position,
                         final int width,
                         final int height,
                         final Color color,
                         final Font font,
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


   public void drawSector(final Sector sector,
                          final Color fillColor,
                          final Color borderColor) {
      final int xFrom = Math.round((float) (_sector.getUCoordinate(sector._lower._longitude) * _width));
      final int yFrom = Math.round((float) (_sector.getVCoordinate(sector._upper._latitude) * _height));
      final int xTo = Math.round((float) (_sector.getUCoordinate(sector._upper._longitude) * _width));
      final int yTo = Math.round((float) (_sector.getVCoordinate(sector._lower._latitude) * _height));

      final int width = xTo - xFrom;
      final int height = yTo - yFrom;

      _g.setColor(fillColor);
      _g.fillRect(xFrom, yFrom, width, height);

      _g.setColor(borderColor);
      _g.drawRect(xFrom, yFrom, width, height);
   }


   public void save(final File file) throws IOException {
      _g.dispose();

      ImageIO.write(_image, "png", file);
   }


}
