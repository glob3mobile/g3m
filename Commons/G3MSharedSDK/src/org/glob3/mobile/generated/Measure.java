package org.glob3.mobile.generated;
//
//  Measure.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/25/21.
//

//
//  Measure.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/25/21.
//




//class Geodetic3D;


public class Measure
{
  private final double _vertexSphereRadius;
  private final Color _vertexColor ;
  private final Color _vertexSelectedColor ;
  private final float _segmentLineWidth;
  private final Color _segmentColor ;

  private final java.util.ArrayList<Geodetic3D> _vertices = new java.util.ArrayList<Geodetic3D>();

  private void reset()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO: recreate vertices spheres
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO: recreate edges lines
  }

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO: add vertexSelectionHandler onVertexSelection(measure, geodetic, i);

  public Measure(double vertexSphereRadius, Color vertexColor, Color vertexSelectedColor, float segmentLineWidth, Color segmentColor, Geodetic3D firstVertex)
  {
     _vertexSphereRadius = vertexSphereRadius;
     _vertexColor = vertexColor;
     _vertexSelectedColor = vertexSelectedColor;
     _segmentLineWidth = segmentLineWidth;
     _segmentColor = segmentColor;
    _vertices.add(firstVertex);
  }

  public final int getVexticesCount()
  {
    return _vertices.size();
  }

  public final void addVertex(Geodetic3D vertex)
  {
    _vertices.add(vertex);
  
    reset();
  }

  public final void setVertex(int i, Geodetic3D vertex)
  {
    final Geodetic3D current = _vertices.get(i);
    if (vertex != current)
    {
      if (current != null)
         current.dispose();
      _vertices.set(i, vertex);
    }
  
    reset();
  }

  public final void removeVertex(int i)
  {
    _vertices.remove(i);
  
    reset();
  }

  public void dispose()
  {
    for (int i = 0; i < _vertices.size(); i++)
    {
      final Geodetic3D vertex = _vertices.get(i);
      if (vertex != null)
         vertex.dispose();
    }
  }
}