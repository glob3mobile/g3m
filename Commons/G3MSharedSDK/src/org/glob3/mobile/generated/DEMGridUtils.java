package org.glob3.mobile.generated;
//
//  DEMGridUtils.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/7/16.
//
//

//
//  DEMGridUtils.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/7/16.
//
//


//class Mesh;
//class DEMGrid;
//class Planet;
//class Geodetic3D;
//class Sector;
//class Vector2S;
//class Vector3D;


public class DEMGridUtils
{
  private DEMGridUtils()
  {
  }



  ///#include "ILogger.hpp"
  
  
  public static Vector3D getMinMaxAverageElevations(DEMGrid grid)
  {
    final IMathUtils mu = IMathUtils.instance();
    double minElevation = mu.maxDouble();
    double maxElevation = mu.minDouble();
    double sumElevation = 0.0;
  
    final int width = grid.getExtent()._x;
    final int height = grid.getExtent()._y;
  
    for (int x = 0; x < width; x++)
    {
      for (int y = 0; y < height; y++)
      {
        final double elevation = grid.getElevationAt(x, y);
        if (!(elevation != elevation))
        {
          if (elevation < minElevation)
          {
            minElevation = elevation;
          }
          if (elevation > maxElevation)
          {
            maxElevation = elevation;
          }
          sumElevation += elevation;
        }
      }
    }
  
    return new Vector3D(minElevation, maxElevation, sumElevation / (width * height));
  }

  public static Mesh createDebugMesh(DEMGrid grid, Planet planet, float verticalExaggeration, Geodetic3D offset, float pointSize)
  {
    final Vector3D minMaxAverageElevations = getMinMaxAverageElevations(grid);
    final double minElevation = minMaxAverageElevations._x;
    final double maxElevation = minMaxAverageElevations._y;
  //  const double averageElevation = minMaxAverageElevations._z;
  //  const double deltaElevation   = maxElevation - minElevation;
  //
  //  ILogger::instance()->logInfo("Elevations: average=%f, min=%f max=%f delta=%f",
  //                               averageElevation, minElevation, maxElevation, deltaElevation);
  
    return createDebugMesh(grid, planet, verticalExaggeration, offset, minElevation, maxElevation, pointSize);
  }

  public static Mesh createDebugMesh(DEMGrid grid, Planet planet, float verticalExaggeration, Geodetic3D offset, double minElevation, double maxElevation, float pointSize)
  {
    final double deltaElevation = maxElevation - minElevation;
  
    FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(planet);
    FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();
  
    final Projection projection = grid.getProjection();
    final Vector2I extent = grid.getExtent();
    final Sector sector = grid.getSector();
  
    for (int x = 0; x < extent._x; x++)
    {
      final double u = (double) x / (extent._x - 1);
      final Angle longitude = projection.getInnerPointLongitude(sector, u).add(offset._longitude);
  
      for (int y = 0; y < extent._y; y++)
      {
        final double elevation = grid.getElevationAt(x, y);
        if ((elevation != elevation))
        {
          continue;
        }
  
        final double v = 1.0 - ((double) y / (extent._y - 1));
        final Angle latitude = projection.getInnerPointLatitude(sector, v).add(offset._latitude);
  
        final double height = (elevation + offset._height) * verticalExaggeration;
  
        vertices.add(latitude, longitude, height);
  
        final float gray = (float)((elevation - minElevation) / deltaElevation);
        colors.add(gray, gray, gray, 1);
      }
    }
  
    Mesh result = new DirectMesh(GLPrimitive.points(), true, vertices.getCenter(), vertices.create(), 1, pointSize, null, colors.create(), 0, true); // depthTest -  colorsIntensity -  flatColor -  lineWidth
  
    if (vertices != null)
       vertices.dispose();
  
    return result;
  }

  public static DEMGrid bestGridFor(DEMGrid grid, Sector sector, Vector2S extent)
  {
    if (grid == null)
    {
      return null;
    }
  
    final Sector gridSector = grid.getSector();
    final Vector2I gridExtent = grid.getExtent();
  
    if (gridSector.isEquals(sector) && gridExtent.isEquals(extent))
    {
      grid._retain();
      return grid;
    }
  
    if (!gridSector.touchesWith(sector))
    {
      return null;
    }
  
    DEMGrid subsetGrid = SubsetDEMGrid.create(grid, sector);
    final Vector2I subsetGridExtent = subsetGrid.getExtent();
    if (subsetGridExtent.isEquals(extent))
    {
      return subsetGrid;
    }
    else if ((subsetGridExtent._x > extent._x) || (subsetGridExtent._y > extent._y))
    {
      return DecimatedDEMGrid.create(subsetGrid, extent);
    }
    else
    {
      return InterpolatedDEMGrid.create(subsetGrid, extent);
    }
  }

}
