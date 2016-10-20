package org.glob3.mobile.generated; 
//
//  DEMGrid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//

//
//  DEMGrid.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//




//class Mesh;
//class Geodetic3D;


public abstract class DEMGrid extends RCObject
{
  protected final Sector _sector ;
  protected final Vector2I _extent;
  protected final Geodetic2D _resolution ;

  protected DEMGrid(Sector sector, Vector2I extent)
  {
     _sector = new Sector(sector);
     _extent = extent;
     _resolution = new Geodetic2D(sector._deltaLatitude.div(extent._y), sector._deltaLongitude.div(extent._x));
  }

  public void dispose()
  {
    super.dispose();
  }


  public final Sector getSector()
  {
    return _sector;
  }

  public final Vector2I getExtent()
  {
    return _extent;
  }

  public final Geodetic2D getResolution()
  {
    return _resolution;
  }

  public abstract double getElevationAt(int x, int y);

  public abstract Vector3D getMinMaxAverageElevations();

  public final Mesh createDebugMesh(Planet planet, float verticalExaggeration, Geodetic3D offset, float pointSize)
  {
  
    final Vector3D minMaxAverageElevations = getMinMaxAverageElevations();
    final double minElevation = minMaxAverageElevations._x;
    final double maxElevation = minMaxAverageElevations._y;
    final double averageElevation = minMaxAverageElevations._z;
    final double deltaElevation = maxElevation - minElevation;
  
    ILogger.instance().logInfo("Elevations: average=%f, min=%f max=%f delta=%f", averageElevation, minElevation, maxElevation, deltaElevation);
  
    FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(planet);
    FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();
  
    for (int x = 0; x < _extent._x; x++)
    {
      final double u = (double) x / (_extent._x - 1);
      final Angle longitude = _sector.getInnerPointLongitude(u).add(offset._longitude);
  
      for (int y = 0; y < _extent._y; y++)
      {
        final double elevation = getElevationAt(x, y);
        if ((elevation != elevation))
        {
          continue;
        }
  
        final double v = 1.0 - ((double) y / (_extent._y - 1));
        final Angle latitude = _sector.getInnerPointLatitude(v).add(offset._latitude);
  
        final double height = (elevation + offset._height) * verticalExaggeration;
  
        vertices.add(latitude, longitude, height);
  
        final float gray = (float)((elevation - minElevation) / deltaElevation);
        colors.add(gray, gray, gray, 1);
      }
    }
  
    Mesh result = new DirectMesh(GLPrimitive.points(), true, vertices.getCenter(), vertices.create(), 1, pointSize, null, colors.create(), 0, false); // depthTest -  colorsIntensity -  flatColor -  lineWidth
  
    if (vertices != null)
       vertices.dispose();
  
    return result;
  }

}