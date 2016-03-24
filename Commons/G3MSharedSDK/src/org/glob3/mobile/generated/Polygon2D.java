package org.glob3.mobile.generated; 
//
//  Polygon2D.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/3/16.
//
//

//
//  Polygon2D.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/3/16.
//
//




public class Polygon2D
{

  private java.util.ArrayList<Vector2D> _coor2D = new java.util.ArrayList<Vector2D>();
  private final int _nVertices;
  private boolean _verticesCCW;


  private boolean isConvexPolygonCounterClockWise()
  {
    final Vector2D v0 = _coor2D.get(0);
    final Vector2D v1 = _coor2D.get(1);
    final Vector2D v2 = _coor2D.get(2);

    final double z = ((v1._x - v0._x) * (v2._y - v1._y)) - ((v1._y - v0._y) * (v2._x - v1._x));
    return z > 0;
  }


  private double angleInRadiansOfCorner(int i)
  {

    int isub1 = (i - 1) % (_nVertices - 2);
    if (isub1 == -1)
    {
      isub1 = _nVertices - 2;
    }
    final int iadd1 = (i + 1) % (_nVertices - 2); //Last one is repeated

    final Vector2D v1 = _coor2D.get(iadd1).sub(_coor2D.get(i));
    final Vector2D v2 = _coor2D.get(isub1).sub(_coor2D.get(i));

    return IMathUtils.instance().atan2(v2._y - v1._x, v2._x - v1._x);
  }


  private boolean isConcave()
  {
    final double a0 = angleInRadiansOfCorner(0);
    for (int i = 1; i < (_nVertices - 1); i++)
    {
      final double ai = angleInRadiansOfCorner(i);
      if ((ai * a0) < 0)
      {
        return true;
      }
    }
    return false;
  }


  private double concavePolygonArea()
  {
    double sum = 0;
    for (int i = 0; i < (_nVertices - 1); i++)
    {
      final Vector2D vi = _coor2D.get(i);
      final Vector2D vi1 = _coor2D.get(i + 1);
      sum += ((vi._x * vi1._y) - (vi1._x * vi._y));
    }

    return sum / 2;
  }


  private boolean isConcavePolygonCounterClockWise()
  {
    final double area = concavePolygonArea();
    return area > 0;
  }


  private boolean isPolygonCounterClockWise()
  {
    if (isConcave())
    {
      return isConcavePolygonCounterClockWise();
    }
    return isConvexPolygonCounterClockWise();

  }


  private static boolean isEdgeInside(int i, int j, java.util.ArrayList<Vector2D> remainingCorners, boolean counterClockWise)
  {
    //http://stackoverflow.com/questions/693837/how-to-determine-a-diagonal-is-in-or-out-of-a-concave-polygon
  
    final int nVertices = remainingCorners.size() - 1;
  
    int iadd1 = i + 1;
    int isub1 = i - 1;
  
    if (iadd1 == (nVertices + 1))
    {
      iadd1 = 0;
    }
    if (isub1 == -1)
    {
      isub1 = nVertices - 1;
    }
  
    final Vector2D v1 = remainingCorners.get(iadd1).sub(remainingCorners.get(i));
    final Vector2D v2 = remainingCorners.get(isub1).sub(remainingCorners.get(i));
    final Vector2D v3 = remainingCorners.get(j).sub(remainingCorners.get(i));
  
    double av1 = v1.angle()._degrees;
    double av2 = v2.angle()._degrees;
    double av3 = v3.angle()._degrees;
  
  
    while (av1 < 0)
    {
      av1 += 360;
    }
  
    while (av2 < 0)
    {
      av2 += 360;
    }
  
    while (av3 < 0)
    {
      av3 += 360;
    }
  
  
    if (counterClockWise)
    {
  
      while (av1 > av2)
      {
        av2 += 360;
      }
  
      if ((av1 <= av3) && (av3 <= av2))
      {
        return true;
      }
      av3 += 360;
      if ((av1 <= av3) && (av3 <= av2))
      {
        return true;
      }
    }
    else
    {
  
      while (av1 < av2)
      {
        av1 += 360;
      }
  
      if ((av1 >= av3) && (av3 >= av2))
      {
        return true;
      }
      av3 += 360;
      if ((av1 >= av3) && (av3 >= av2))
      {
        return true;
      }
    }
  
    return false;
  }



  private static boolean edgeIntersectsAnyOtherEdge(int i, int j, java.util.ArrayList<Vector2D> remainingCorners)
  {

    final Vector2D a = remainingCorners.get(i);
    final Vector2D b = remainingCorners.get(j);

    for (int k = 0; k < (remainingCorners.size() - 2); k++)
    {

      final int kadd1 = (k + 1) % (remainingCorners.size() - 1);

      if ((i == k) || (i == kadd1) || (j == k) || (j == kadd1))
      {
        continue;
      }

      final Vector2D c = remainingCorners.get(k);
      final Vector2D d = remainingCorners.get(kadd1);

      if (Vector2D.segmentsIntersect(a, b, c, d))
      {
        return true;
      }


    }

    return false;
  }


  private static boolean isAnyVertexInsideTriangle(int i1, int i2, int i3, java.util.ArrayList<Vector2D> remainingCorners)
  {

    final Vector2D cornerA = remainingCorners.get(i1);
    final Vector2D cornerB = remainingCorners.get(i2);
    final Vector2D cornerC = remainingCorners.get(i3);

    for (int j = 0; j < remainingCorners.size(); j++)
    {
      if ((j != i1) && (j != i2) && (j != i3))
      {
        final Vector2D p = remainingCorners.get(j);
        if (Vector2D.isPointInsideTriangle(p, cornerA, cornerB, cornerC))
        {
          return true;
        }
      }
    }

    return false;

  }

  ///////


  public Polygon2D(java.util.ArrayList<Vector2D> coor)
  {
     _nVertices = coor.size();
    //POLYGON MUST BE DEFINED CCW AND LAST VERTEX == FIRST VERTEX
    _coor2D = coor;
    _verticesCCW = isPolygonCounterClockWise();
  }

  public final boolean areVerticesCounterClockWise()
  {
    return _verticesCCW;
  }

  public void dispose()
  {
  }

  public final short addTrianglesIndexesByEarClipping(ShortBufferBuilder indexes, short firstIndex)
  {
  
    //As seen in http://www.geometrictools.com/Documentation/TriangulationByEarClipping.pdf
  
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    //   ILogger::instance()->logInfo("Looking for ears");
  
    java.util.ArrayList<Vector2D> remainingCorners = new java.util.ArrayList<Vector2D>();
    java.util.ArrayList<Short> remainingIndexes = new java.util.ArrayList<Short>();
  
    for (int i = 0; i < _nVertices; i++)
    {
      remainingCorners.add(_coor2D.get(i));
      remainingIndexes.add((short)(i + firstIndex));
    }
  
    while (remainingCorners.size() > 2)
    {
  
      boolean earFound = false;
      for (int i = 0; i < (remainingCorners.size() - 1); i++)
      {
  
        i1 = i;
        i2 = (i + 1) % (remainingCorners.size());
        i3 = (i + 2) % (remainingCorners.size());
  
        final boolean edgeInside = isEdgeInside(i1, i3, remainingCorners, _verticesCCW);
        if (!edgeInside)
        {
          //               ILogger::instance()->logInfo("T: %d, %d, %d -> Edge Not Inside", i1, i2, i3);
          continue;
        }
  
        final boolean edgeIntersects = edgeIntersectsAnyOtherEdge(i1, i3, remainingCorners);
        if (edgeIntersects)
        {
          //               ILogger::instance()->logInfo("T: %d, %d, %d -> Edge Intersects", i1, i2, i3);
          continue;
        }
  
        final boolean triangleContainsVertex = isAnyVertexInsideTriangle(i1, i2, i3, remainingCorners);
        if (triangleContainsVertex)
        {
          //               ILogger::instance()->logInfo("T: %d, %d, %d -> Triangle contains vertex", i1, i2, i3);
          continue;
        }
  
        //            ILogger::instance()->logInfo("T: %d, %d, %d -> IS EAR!", i1, i2, i3);
        earFound = true;
        break;
      }
  
  
      if (earFound) //Valid triangle (ear)
      {
        indexes.add(remainingIndexes.get(i1));
        indexes.add(remainingIndexes.get(i2));
        indexes.add(remainingIndexes.get(i3));
  
        //Removing ear
        remainingCorners.remove(i2);
        remainingIndexes.remove(i2);
      }
      else
      {
        ILogger.instance().logError("NO EAR!!!!");
      }
  
    }
    return (short)(firstIndex + _nVertices);
  }

}