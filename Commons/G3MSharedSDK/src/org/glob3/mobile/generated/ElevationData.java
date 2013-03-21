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

public abstract class ElevationData
{
  protected final Sector _sector ;
  protected final int _width;
  protected final int _height;
  protected final double _noDataValue;

  public ElevationData(Sector sector, Vector2I resolution, double noDataValue)
  {
     _sector = new Sector(sector);
     _width = resolution._x;
     _height = resolution._y;
     _noDataValue = noDataValue;
  }

  public void dispose()
  {
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

  public abstract double getElevationAt(int x, int y, int type);

  public abstract double getElevationAt(Angle latitude, Angle longitude, int type);

  public double getElevationAt(Geodetic2D position, int type)
  {
    return getElevationAt(position.latitude(), position.longitude(), type);
  }

  public abstract String description(boolean detailed);

  public abstract Vector2D getMinMaxHeights();

  public Mesh createMesh(Ellipsoid ellipsoid, float verticalExaggeration, Geodetic3D positionOffset)
  {
  
    final Vector2D minMaxHeights = getMinMaxHeights();
    final double minHeight = minMaxHeights._x;
    final double maxHeight = minMaxHeights._y;
    final double deltaHeight = maxHeight - minHeight;
  
    ILogger.instance().logInfo("minHeight=%f maxHeight=%f delta=%f", minHeight, maxHeight, deltaHeight);
  
  ////  const double latStepInDegrees = (_sector.getDeltaLatitude()._degrees  / _height) / 3 * 2;
  ////  const double lonStepInDegrees = (_sector.getDeltaLongitude()._degrees / _width ) / 3 * 2;
  //  const double latStepInDegrees = (_sector.getDeltaLatitude()._degrees  / _height);
  //  const double lonStepInDegrees = (_sector.getDeltaLongitude()._degrees / _width);
  //
  //  const Geodetic2D targetLower(_sector.lower());
  //  const Geodetic2D targetUpper(_sector.upper());
  
  
  
    FloatBufferBuilderFromGeodetic vertices = new FloatBufferBuilderFromGeodetic(CenterStrategy.firstVertex(), ellipsoid, Vector3D.zero());
    FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();
  
    int type = -1;
  
    for (int x = 0; x < _width; x++)
    {
      for (int y = 0; y < _height; y++)
      {
        final double height = getElevationAt(x, y, type);
  
        final float alpha = (float)((height - minHeight) / deltaHeight);
  
        float r = alpha;
        float g = alpha;
        float b = alpha;
        /*
        if (type == 1) {
          g = 1;
        }
        else if (type == 2) {
          r = 1;
          g = 1;
        }
        else if (type == 3) {
          r = 1;
          b = 1;
        }
        else if (type == 4) {
          r = 1;
        }
        */
  
        final double u = (double) x / (_width - 1);
        final double v = 1.0 - ((double) y / (_height - 1));
  
        final Geodetic2D position = _sector.getInnerPoint(u, v).add(positionOffset.asGeodetic2D());
  
        vertices.add(position, positionOffset.height() + (height * verticalExaggeration));
  
        colors.add(r, g, b, 1);
      }
    }
  
    final float lineWidth = 1F;
    final float pointSize = 1F;
    Color flatColor = null;
  
    return new DirectMesh(GLPrimitive.points(), true, vertices.getCenter(), vertices.create(), lineWidth, pointSize, flatColor, colors.create());
                          //GLPrimitive::lineStrip(),
  }

  public Sector getSector()
  {
    return _sector;
  }

}