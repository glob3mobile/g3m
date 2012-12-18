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

  protected final Mesh createMesh(G3MRenderContext rc)
  {
	Color color = Color.newFromRGBA(1, 1, 0, 1);
	final float lineWidth = 2F;
  
	return create2DBoundaryMesh(_coordinates, color, lineWidth, rc);
  }


  public GEO2DLineStringGeometry(java.util.ArrayList<Geodetic2D> coordinates)
  {
	  _coordinates = coordinates;

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