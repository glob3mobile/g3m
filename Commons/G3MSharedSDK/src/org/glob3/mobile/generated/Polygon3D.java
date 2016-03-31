package org.glob3.mobile.generated; 
//
//  Polygon3D.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/3/16.
//
//

//
//  Polygon3D.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/3/16.
//
//



public class Polygon3D
{

  private java.util.ArrayList<Vector3D> _coor3D = new java.util.ArrayList<Vector3D>();
  private Vector3D _normal ;
  private Polygon2D _polygon2D;

  private Vector3D getNormalOfFirstVertex(java.util.ArrayList<Vector3D> coor3D)
  {
    final Vector3D e1 = coor3D.get(0).sub(coor3D.get(1));
    final Vector3D e2 = coor3D.get(2).sub(coor3D.get(1));

    return e1.cross(e2);
  }


  public Polygon3D(java.util.ArrayList<Vector3D> coor3D)
  {
     _coor3D = coor3D;
     _normal = new Vector3D(getNormalOfFirstVertex(coor3D));
    java.util.ArrayList<Vector2D> _coor2D = createCoordinates2D();
    _polygon2D = new Polygon2D(_coor2D);
  }

  public void dispose()
  {
    if (_polygon2D != null)
       _polygon2D.dispose();
  }

  public final Vector3D getCCWNormal()
  {
    if (_polygon2D.areVerticesCounterClockWise())
    {
      return _normal.times(-1);
    }
    return _normal;
  }


  public final java.util.ArrayList<Vector2D> createCoordinates2D()
  {
  
    final Vector3D z = Vector3D.upZ();
    final Vector3D rotationAxis = z.cross(_normal);
    java.util.ArrayList<Vector2D> coor2D = new java.util.ArrayList<Vector2D>();
  
    if (rotationAxis.isZero())
    {
  
      if (_normal._z > 0)
      {
        for (int i = 0; i < _coor3D.size(); i++)
        {
          final Vector3D v3D = _coor3D.get(i);
          coor2D.add(new Vector2D(v3D._x, v3D._y));
        }
      }
      else
      {
        for (int i = 0; i < _coor3D.size(); i++)
        {
          final Vector3D v3D = _coor3D.get(i);
          coor2D.add(new Vector2D(v3D._x, -v3D._y));
        }
      }
    }
    else
    {
  
      final Angle a = _normal.signedAngleBetween(rotationAxis, z);
  
      for (int i = 0; i < _coor3D.size(); i++)
      {
        final Vector3D v3D = _coor3D.get(i).rotateAroundAxis(rotationAxis, a);
        coor2D.add(new Vector2D(v3D._x, v3D._y));
      }
    }
  
    return coor2D;
  }

  public final short addTrianglesByEarClipping(FloatBufferBuilderFromCartesian3D fbb, FloatBufferBuilderFromCartesian3D normals, ShortBufferBuilder indexes, short firstIndex)
  {

    //As seen in http://www.geometrictools.com/Documentation/TriangulationByEarClipping.pdf

    java.util.ArrayList<Short> indexes2D = _polygon2D.calculateTrianglesIndexesByEarClipping();
    if (indexes2D.size() > 3)
    {

      Vector3D normal = getCCWNormal();
      for (int i = 0; i < _coor3D.size(); i++)
      {
        fbb.add(_coor3D.get(i));
        normals.add(normal);
      }

      for (int i = 0; i < indexes2D.size(); i++)
      {
        indexes.add(indexes2D.get(i) + firstIndex);
      }

      return (short) _coor3D.size() + firstIndex;
    }

    return firstIndex;


//    return _polygon2D->addTrianglesIndexesByEarClipping(indexes, firstIndex);
  }


}