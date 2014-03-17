package org.glob3.mobile.generated; 
//
//  ElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

//
//  ElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//



//class Vector2I;
//class Mesh;
//class Ellipsoid;
//class Vector3D;
//class Interpolator;

public abstract class ElevationData
{
  private Interpolator _interpolator;
  private Interpolator getInterpolator()
  {
    if (_interpolator == null)
    {
      _interpolator = new BilinearInterpolator();
    }
    return _interpolator;
  }

  protected final Sector _sector ;
  protected final int _width;
  protected final int _height;

  protected final Geodetic2D _resolution ;

  public ElevationData(Sector sector, Vector2I extent)
  {
     _sector = new Sector(sector);
     _width = extent._x;
     _height = extent._y;
     _resolution = new Geodetic2D(sector._deltaLatitude.div(extent._y), sector._deltaLongitude.div(extent._x));
     _interpolator = null;
  }

  public void dispose()
  {
    if (_interpolator != null)
       _interpolator.dispose();
  }

  public Vector2I getExtent()
  {
    return new Vector2I(_width, _height);
  }

  public int getExtentWidth()
  {
    return _width;
  }

  public int getExtentHeight()
  {
    return _height;
  }

  public final Geodetic2D getResolution()
  {
    return _resolution;
  }

  public abstract double getElevationAt(int x, int y);

  public final double getElevationAt(Vector2I position)
  {
    return getElevationAt(position._x, position._y);
  }

  public abstract String description(boolean detailed);

  public abstract Vector3D getMinMaxAverageElevations();

  public Mesh createMesh(Planet planet, float verticalExaggeration, Geodetic3D positionOffset, float pointSize)
  {
  
    final Vector3D minMaxAverageElevations = getMinMaxAverageElevations();
    final double minElevation = minMaxAverageElevations._x;
    final double maxElevation = minMaxAverageElevations._y;
    final double deltaElevation = maxElevation - minElevation;
    final double averageElevation = minMaxAverageElevations._z;
  
    ILogger.instance().logInfo("Elevations: average=%f, min=%f max=%f delta=%f", averageElevation, minElevation, maxElevation, deltaElevation);
  
    FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(planet);
    FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();
  
    final Geodetic2D positionOffset2D = positionOffset.asGeodetic2D();
  
    for (int x = 0; x < _width; x++)
    {
      final double u = (double) x / (_width - 1);
  
      for (int y = 0; y < _height; y++)
      {
        final double elevation = getElevationAt(x, y);
        if ((elevation != elevation))
        {
          continue;
        }
  
        final float alpha = (float)((elevation - minElevation) / deltaElevation);
        final float r = alpha;
        final float g = alpha;
        final float b = alpha;
        colors.add(r, g, b, 1);
  
        final double v = 1.0 - ((double) y / (_height - 1));
  
        final Geodetic2D position = _sector.getInnerPoint(u, v).add(positionOffset2D);
  
        vertices.add(position, positionOffset._height + (elevation * verticalExaggeration));
  
      }
    }
  
    final float lineWidth = 1F;
    Color flatColor = null;
  
    Mesh result = new DirectMesh(GLPrimitive.points(), true, vertices.getCenter(), vertices.create(), lineWidth, pointSize, flatColor, colors.create(), 0, false);
                                  //GLPrimitive::lineStrip(),
  
    if (vertices != null)
       vertices.dispose();
  
    return result;
  }

  public Mesh createMesh(Planet planet, float verticalExaggeration, Geodetic3D positionOffset, float pointSize, Sector sector, Vector2I resolution)
  {
    final Vector3D minMaxAverageElevations = getMinMaxAverageElevations();
    final double minElevation = minMaxAverageElevations._x;
    final double maxElevation = minMaxAverageElevations._y;
    final double deltaElevation = maxElevation - minElevation;
    final double averageElevation = minMaxAverageElevations._z;
  
    ILogger.instance().logInfo("Elevations: average=%f, min=%f max=%f delta=%f", averageElevation, minElevation, maxElevation, deltaElevation);
  
    FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithGivenCenter(planet, sector._center);
  
    FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();
  
    final Geodetic2D positionOffset2D = positionOffset.asGeodetic2D();
  
    final int width = resolution._x;
    final int height = resolution._y;
    for (int x = 0; x < width; x++)
    {
      final double u = (double) x / (width - 1);
  
      for (int y = 0; y < height; y++)
      {
        final double v = 1.0 - ((double) y / (height - 1));
  
        final Geodetic2D position = sector.getInnerPoint(u, v);
  
        final double elevation = getElevationAt(position);
        if ((elevation != elevation))
        {
          continue;
        }
  
        final float alpha = (float)((elevation - minElevation) / deltaElevation);
        final float r = alpha;
        final float g = alpha;
        final float b = alpha;
        colors.add(r, g, b, 1);
  
        vertices.add(position.add(positionOffset2D), positionOffset._height + (elevation * verticalExaggeration));
      }
    }
  
    final float lineWidth = 1F;
    Color flatColor = null;
  
    Mesh result = new DirectMesh(GLPrimitive.points(), true, vertices.getCenter(), vertices.create(), lineWidth, pointSize, flatColor, colors.create(), 0, false);
  
    if (vertices != null)
       vertices.dispose();
  
    return result;
  }

  public Sector getSector()
  {
    return _sector;
  }

  public abstract boolean hasNoData();


  public final double getElevationAt(Angle latitude, Angle longitude)
  {
  
    final Vector2D uv = _sector.getUVCoordinates(latitude, longitude);
    final double u = uv._x;
    final double v = uv._y;
  
    if (u < 0 || u > 1 || v < 0 || v > 1)
    {
      return java.lang.Double.NaN;
    }
  
    final double dX = u * (_width - 1);
    final double dY = (1.0 - v) * (_height - 1);
  
    final int x = (int) dX;
    final int y = (int) dY;
    final int nextX = x + 1;
    final int nextY = y + 1;
    final double alphaY = dY - y;
    final double alphaX = dX - x;
  
    double result;
    if (x == dX)
    {
      if (y == dY)
      {
        // exact on grid point
        result = getElevationAt(x, y);
      }
      else
      {
        // linear on Y
        final double heightY = getElevationAt(x, y);
        if ((heightY != heightY))
        {
          return java.lang.Double.NaN;
        }
  
        final double heightNextY = getElevationAt(x, nextY);
        if ((heightNextY != heightNextY))
        {
          return java.lang.Double.NaN;
        }
  
        //result = IMathUtils::instance()->linearInterpolation(heightNextY, heightY, alphaY);
        result = IMathUtils.instance().linearInterpolation(heightY, heightNextY, alphaY);
      }
    }
    else
    {
      if (y == dY)
      {
        // linear on X
        final double heightX = getElevationAt(x, y);
        if ((heightX != heightX))
        {
          return java.lang.Double.NaN;
        }
        final double heightNextX = getElevationAt(nextX, y);
        if ((heightNextX != heightNextX))
        {
          return java.lang.Double.NaN;
        }
  
        result = IMathUtils.instance().linearInterpolation(heightX, heightNextX, alphaX);
      }
      else
      {
        // bilinear
        final double valueNW = getElevationAt(x, y);
        if ((valueNW != valueNW))
        {
          return java.lang.Double.NaN;
        }
        final double valueNE = getElevationAt(nextX, y);
        if ((valueNE != valueNE))
        {
          return java.lang.Double.NaN;
        }
        final double valueSE = getElevationAt(nextX, nextY);
        if ((valueSE != valueSE))
        {
          return java.lang.Double.NaN;
        }
        final double valueSW = getElevationAt(x, nextY);
        if ((valueSW != valueSW))
        {
          return java.lang.Double.NaN;
        }
  
        result = getInterpolator().interpolation(valueSW, valueSE, valueNE, valueNW, alphaX, alphaY);
      }
    }
  
    return result;
  }

  public final double getElevationAt(Geodetic2D position)
  {
    return getElevationAt(position._latitude, position._longitude);
  }

  //  bool isEquivalentTo(const ElevationData* ed) {
  //    bool equivalent = true;
  //    const int width  = 3;
  //    const int height = 3;
  //    for (int x = 0; x < width; x++) {
  //      const double u = (double) x / (width  - 1);
  //
  //      for (int y = 0; y < height; y++) {
  //        const double v = 1.0 - ( (double) y / (height - 1) );
  //
  //        const Geodetic2D position = _sector.getInnerPoint(u, v);
  //
  //        const double elevation = getElevationAt(position);
  //        const double elevation2 = ed->getElevationAt(position);
  //
  //        if (elevation != elevation2) {
  //          printf("%s -> %f != %f\n", position.description().c_str(), elevation, elevation2);
  //          equivalent = false;
  //        }
  //      }
  //    }
  //    return equivalent;
  //  }

}