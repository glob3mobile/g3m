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

  public abstract Vector3D getMinMaxAverageHeights();

  public Mesh createMesh(Planet planet, float verticalExaggeration, Geodetic3D positionOffset, float pointSize)
  {
  
    final Vector3D minMaxAverageHeights = getMinMaxAverageHeights();
    final double minHeight = minMaxAverageHeights._x;
    final double maxHeight = minMaxAverageHeights._y;
    final double deltaHeight = maxHeight - minHeight;
    final double averageHeight = minMaxAverageHeights._z;
  
    ILogger.instance().logInfo("averageHeight=%f, minHeight=%f maxHeight=%f delta=%f", averageHeight, minHeight, maxHeight, deltaHeight);
  
    FloatBufferBuilderFromGeodetic vertices = new FloatBufferBuilderFromGeodetic(CenterStrategy.firstVertex(), planet, Vector3D.zero());
    FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();
  
    int type = -1;
  
    for (int x = 0; x < _width; x++)
    {
      final double u = (double) x / (_width - 1);
  
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
  
        final double v = 1.0 - ((double) y / (_height - 1));
  
        final Geodetic2D position = _sector.getInnerPoint(u, v).add(positionOffset.asGeodetic2D());
  
        vertices.add(position, positionOffset.height() + (height * verticalExaggeration));
  
        colors.add(r, g, b, 1);
      }
    }
  
    final float lineWidth = 1F;
  //  const float pointSize = 1;
    Color flatColor = null;
  
    return new DirectMesh(GLPrimitive.points(), true, vertices.getCenter(), vertices.create(), lineWidth, pointSize, flatColor, colors.create());
                          //GLPrimitive::lineStrip(),
  }

  public Sector getSector()
  {
    return _sector;
  }

}