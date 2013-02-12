package org.glob3.mobile.generated; 
//
//  GEO2DMultiLineStringGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

//
//  GEO2DMultiLineStringGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic2D;

public class GEO2DMultiLineStringGeometry extends GEOMultiLineStringGeometry
{
  private java.util.ArrayList<java.util.ArrayList<Geodetic2D>> _coordinatesArray;
  private Color _color;
  private final float _lineWidth;

  protected final Mesh createMesh(G3MRenderContext rc)
  {
  //  CompositeMesh* composite = new CompositeMesh();
  //  const int coordinatesArrayCount = _coordinatesArray->size();
  //  for (int i = 0; i < coordinatesArrayCount; i++) {
  //    std::vector<Geodetic2D*>* coordinates = _coordinatesArray->at(i);
  //
  //    Color* color = Color::newFromRGBA(1, 1, 0, 1);
  //    const float lineWidth = 2;
  //
  //    composite->addMesh( create2DBoundaryMesh(coordinates, color, lineWidth, rc) );
  //  }
  //  return composite;
  
    FloatBufferBuilderFromGeodetic vertices = new FloatBufferBuilderFromGeodetic(CenterStrategy.firstVertex(), rc.getPlanet(), Geodetic2D.zero());
  
    final int coordinatesArrayCount = _coordinatesArray.size();
    for (int i = 0; i < coordinatesArrayCount; i++)
    {
      java.util.ArrayList<Geodetic2D> coordinates = _coordinatesArray.get(i);
      final int coordinatesCount = coordinates.size();
      for (int j = 0; j < coordinatesCount; j++)
      {
        Geodetic2D coordinate = coordinates.get(j);
        vertices.add(coordinate);
        if ((j > 0) && (j < (coordinatesCount-1)))
        {
          vertices.add(coordinate);
        }
      }
    }
  
	return new DirectMesh(GLPrimitive.lines(), true, vertices.getCenter(), vertices.create(), _lineWidth, 1, _color);
  }


	public GEO2DMultiLineStringGeometry(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray, Color color, float lineWidth)
  {
		_coordinatesArray = coordinatesArray;
		_color = color;
		_lineWidth = lineWidth;

  }



  ///#include "CompositeMesh.hpp"
  
  
  
  public void dispose()
  {
    final int coordinatesArrayCount = _coordinatesArray.size();
    for (int i = 0; i < coordinatesArrayCount; i++)
    {
      java.util.ArrayList<Geodetic2D> coordinates = _coordinatesArray.get(i);
      final int coordinatesCount = coordinates.size();
      for (int j = 0; j < coordinatesCount; j++)
      {
        Geodetic2D coordinate = coordinates.get(j);
        if (coordinate != null)
           coordinate.dispose();
      }
      coordinates = null;
    }
  
    _coordinatesArray = null;
  }

}