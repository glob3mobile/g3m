package org.glob3.mobile.generated; 
//
//  GEO2DLineStringGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

//
//  GEO2DLineStringGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic2D;

public class GEO2DLineStringGeometry extends GEOLineStringGeometry
{
  private java.util.ArrayList<Geodetic2D> _coordinates;
  private Color _color;
  private float _lineWidth;

  protected final Mesh createMesh(G3MRenderContext rc)
  {
  
	return create2DBoundaryMesh(_coordinates, _color, _lineWidth, rc);
  }


  public GEO2DLineStringGeometry(java.util.ArrayList<Geodetic2D> coordinates, Color color)
  {
	  this(coordinates, color, 2);
  }
  public GEO2DLineStringGeometry(java.util.ArrayList<Geodetic2D> coordinates)
  {
	  this(coordinates, Color.newFromRGBA(1, 1, 0, 1), 2);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: GEO2DLineStringGeometry(java.util.ArrayList<Geodetic2D*>* coordinates, Color* color = Color::newFromRGBA(1, 1, 0, 1), float lineWidth = 2) : _coordinates(coordinates), _color(color), _lineWidth(lineWidth)
  public GEO2DLineStringGeometry(java.util.ArrayList<Geodetic2D> coordinates, Color color, float lineWidth)
  {
	  _coordinates = coordinates;
	  _color = color;
	  _lineWidth = lineWidth;

  }

  public void dispose()
  {
	final int coordinatesCount = _coordinates.size();
	for (int i = 0; i < coordinatesCount; i++)
	{
	  Geodetic2D coordinate = _coordinates.get(i);
	  if (coordinate != null)
		  coordinate.dispose();
	}
	_coordinates = null;
  }

}