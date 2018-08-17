package org.glob3.mobile.generated;import java.util.*;

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
  private final Geodetic2D _position = new Geodetic2D();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final GFont _font = new GFont();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final GFont _font = new internal();
//#endif
  private final Color _color = new Color();

  private Sector _sector;
  private static Sector calculateSectorFromPosition(Geodetic2D position)
  {
	final double delta = 2;
	return new Sector(Geodetic2D.fromDegrees(position._latitude._degrees - delta, position._longitude._degrees - delta), Geodetic2D.fromDegrees(position._latitude._degrees + delta, position._longitude._degrees + delta));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rawRasterize(ICanvas* canvas, const GEORasterProjection* projection) const
  protected final void rawRasterize(ICanvas canvas, GEORasterProjection projection)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: canvas->setFont(_font);
	canvas.setFont(new GFont(_font));
  
	final Vector2F textExtent = canvas.textExtent(_label);
  
	final Vector2F pixelPosition = projection.project(_position);
  
	final float left = pixelPosition._x - textExtent._x/2;
	final float top = pixelPosition._y - textExtent._y/2;
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: canvas->setFillColor(_color);
	canvas.setFillColor(new Color(_color));
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
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: GEOLabelRasterSymbol(const String& label, const Geodetic2D& position, const GFont& font, const Color& color, const int minTileLevel = -1, const int maxTileLevel = -1) : GEORasterSymbol(minTileLevel, maxTileLevel), _position(position), _label(label), _font(font), _color(color), _sector(null)
  public GEOLabelRasterSymbol(String label, Geodetic2D position, GFont font, Color color, int minTileLevel, int maxTileLevel)
  {
	  super(minTileLevel, maxTileLevel);
	  _position = new Geodetic2D(position);
	  _label = label;
	  _font = new GFont(font);
	  _color = new Color(color);
	  _sector = null;
  }

  public void dispose()
  {
	if (_sector != null)
		_sector.dispose();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Sector* getSector() const
  public final Sector getSector()
  {
	if (_sector == null)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _sector = calculateSectorFromPosition(_position);
	  _sector = calculateSectorFromPosition(new Geodetic2D(_position));
	}
	return _sector;
  }

}
