package org.glob3.mobile.generated; 
//
//  BuildingSurface.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/3/16.
//
//

//
//  BuildingSurface.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/3/16.
//
//




public class Surface
{

  private double _maxHeight;



  public java.util.ArrayList<Geodetic3D> _geodeticCoordinates = new java.util.ArrayList<Geodetic3D>();

  public Surface(java.util.ArrayList<Geodetic3D> geodeticCoordinates)
  {
     _geodeticCoordinates = geodeticCoordinates;
     _maxHeight = java.lang.Double.NaN;
  }



  public void dispose()
  {
  }

  public final double getBaseHeight()
  {
    double minHeight = 0;
    minHeight = IMathUtils.instance().maxDouble();
    for (int i = 0; i < _geodeticCoordinates.size(); i++)
    {
      final double h = _geodeticCoordinates.get(i)._height;
      if (h < minHeight)
      {
        minHeight = h;
      }
    }
    return minHeight;
  }


  public final Geodetic3D getMin()
  {
    double minLat = IMathUtils.instance().maxDouble();
    double minLon = IMathUtils.instance().maxDouble();
    double minH = IMathUtils.instance().maxDouble();

    for (int i = 0; i < _geodeticCoordinates.size(); i++)
    {
      Geodetic3D g = _geodeticCoordinates.get(i);
      final double lon = g._longitude._degrees;
      if (lon < minLon)
      {
        minLon = lon;
      }
      final double lat = g._latitude._degrees;
      if (lat < minLat)
      {
        minLat = lat;
      }
      final double h = g._height;
      if (h < minH)
      {
        minH = h;
      }
    }
    return Geodetic3D.fromDegrees(minLat, minLon, minH);
  }


  public final Geodetic3D getMax()
  {
    double maxLat = IMathUtils.instance().minDouble();
    double maxLon = IMathUtils.instance().minDouble();
    double maxH = IMathUtils.instance().minDouble();

    for (int i = 0; i < _geodeticCoordinates.size(); i++)
    {
      Geodetic3D g = _geodeticCoordinates.get(i);
      final double lon = g._longitude._degrees;
      if (lon > maxLon)
      {
        maxLon = lon;
      }
      final double lat = g._latitude._degrees;
      if (lat > maxLat)
      {
        maxLat = lat;
      }
      final double h = g._height;
      if (h > maxH)
      {
        maxH = h;
      }
    }
    return Geodetic3D.fromDegrees(maxLat, maxLon, maxH);
  }


  public final Geodetic3D getCenter()
  {
    final Geodetic3D min = getMin();
    final Geodetic3D max = getMax();

    return Geodetic3D.fromDegrees((min._latitude._degrees + max._latitude._degrees) / 2, (min._longitude._degrees + max._longitude._degrees) / 2, (min._height + max._height) / 2);
  }

  public final java.util.ArrayList<Vector3D> createCartesianCoordinates(Planet planet, double baseHeight, ElevationData elevationData)
  {

    java.util.ArrayList<Vector3D> coor3D = new java.util.ArrayList<Vector3D>();

    if (elevationData == null)
    {
      for (int i = 0; i < _geodeticCoordinates.size(); i++)
      {
        Geodetic3D g = _geodeticCoordinates.get(i);
        coor3D.add(new Vector3D(planet.toCartesian(g)));
      }
    }
    else
    {
      for (int i = 0; i < _geodeticCoordinates.size(); i++)
      {
        Geodetic3D g = _geodeticCoordinates.get(i);
        double h = elevationData.getElevationAt(g._latitude, g._longitude);
        coor3D.add(new Vector3D(planet.toCartesian(Geodetic3D.fromDegrees(g._latitude._degrees, g._longitude._degrees, h + g._height))));
      }
    }
    return coor3D;
  }

  public final short addTrianglesByEarClipping(FloatBufferBuilderFromCartesian3D fbb, FloatBufferBuilderFromCartesian3D normals, ShortBufferBuilder indexes, FloatBufferBuilderFromColor colors, double baseHeight, Planet planet, short firstIndex, Color color, ElevationData elevationData)
  {
    final java.util.ArrayList<Vector3D> cartesianC = createCartesianCoordinates(planet, baseHeight, elevationData);
    final Polygon3D polygon = new Polygon3D(cartesianC);
    final short lastVertex = polygon.addTrianglesByEarClipping(fbb, normals, indexes, firstIndex);

    for (short j = firstIndex; j < lastVertex; j++)
    {
      colors.add(color);
    }
    return lastVertex;
  }


  public final void addMarkersToCorners(MarksRenderer mr, double substractHeight)
  {

    for (int i = 0; i < _geodeticCoordinates.size(); i++)
    {
      IStringBuilder isb = IStringBuilder.newStringBuilder();
      isb.addInt(i);
      Mark m = new Mark(isb.getString(), _geodeticCoordinates.get(i), AltitudeMode.ABSOLUTE, 10000.0);
      if (isb != null)
         isb.dispose();
      mr.addMark(m);
    }

  }

  public final boolean includesPoint(Geodetic3D g)
  {
    int nv = (int) _geodeticCoordinates.size();
    for (int i = 0; i < nv; i++)
    {
      if (_geodeticCoordinates.get(i).isEquals(g))
      {
        return true;
      }
    }
    return false;
  }

  public final boolean isEquivalentTo(Surface that)
  {
    if (that._geodeticCoordinates.size() != _geodeticCoordinates.size())
    {
      return false;
    }

    if (that.getMaxHeight() != getMaxHeight())
    {
      return false;
    }

    int nv = (int) _geodeticCoordinates.size();

    for (int i = 0; i < nv; i++)
    {
      Geodetic3D g = _geodeticCoordinates.get(i);
      if (!that.includesPoint(g))
      {
        return false;
      }
    }

    return true;
  }

  public final double getMaxHeight()
  {
    if ((_maxHeight != _maxHeight))
    {
      _maxHeight = -9999999;
      int nv = (int) _geodeticCoordinates.size();
      for (int i = 0; i < nv; i++)
      {
        if (_maxHeight < _geodeticCoordinates.get(i)._height) //SIG = MAX LAT
        {
          _maxHeight = _geodeticCoordinates.get(i)._height;
        }
      }
    }
    return _maxHeight;
  }
}