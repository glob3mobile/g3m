package org.glob3.mobile.generated; 
//
//  GEOLabelRasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/10/14.
//
//

//
//  GEOLabelRasterSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/10/14.
//
//






public class GEOLabelRasterSymbol extends GEORasterSymbol
{
  private final String _label;
  private final Geodetic2D _position ;
  private final GFont _font;
  private final Color _color ;

  private Sector _sector;
  private static Sector calculateSectorFromPosition(Geodetic2D position)
  {
    final double delta = 2;
    return new Sector(Geodetic2D.fromDegrees(position._latitude._degrees - delta, position._longitude._degrees - delta), Geodetic2D.fromDegrees(position._latitude._degrees + delta, position._longitude._degrees + delta));
  }

  protected final void rawRasterize(ICanvas canvas, GEORasterProjection projection)
  {
    canvas.setFont(_font);
  
    final Vector2F textExtent = canvas.textExtent(_label);
  
    final Vector2F pixelPosition = projection.project(_position);
  
    final float left = pixelPosition._x - textExtent._x/2;
    final float top = pixelPosition._y - textExtent._y/2;
  
    canvas.setFillColor(_color);
    canvas.fillText(_label, left, top);
  }


  public GEOLabelRasterSymbol(String label, Geodetic2D position, GFont font, Color color, int minTileLevel)
  {
     this(label, position, font, color, minTileLevel, -1);
  }
  public GEOLabelRasterSymbol(String label, Geodetic2D position, GFont font, Color color)
  {
     this(label, position, font, color, -1, -1);
  }
  public GEOLabelRasterSymbol(String label, Geodetic2D position, GFont font, Color color, int minTileLevel, int maxTileLevel)
  {
     super(minTileLevel, maxTileLevel);
     _position = new Geodetic2D(position);
     _label = label;
     _font = font;
     _color = new Color(color);
     _sector = null;
  }

  public void dispose()
  {
    if (_sector != null)
       _sector.dispose();
    super.dispose();
  }

  public final Sector getSector()
  {
    if (_sector == null)
    {
      _sector = calculateSectorFromPosition(_position);
    }
    return _sector;
  }

}