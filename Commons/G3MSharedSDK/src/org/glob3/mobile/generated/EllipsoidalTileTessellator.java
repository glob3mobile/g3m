package org.glob3.mobile.generated; 
//
//  EllipsoidalTileTessellator.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  EllipsoidalTileTessellator.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//


//class Sector;

///#include "MutableVector3D.hpp"
///#include "Planet.hpp"

public class EllipsoidalTileTessellator extends TileTessellator
{

  private final int _resolutionX;
  private final int _resolutionY;
  private final boolean _skirted;

  private Vector2I calculateResolution(Sector sector)
  {
    //const short resolution = (short) _resolutionX;
  
  //  /* testing for dynamic latitude-resolution */
  //  double cos = sector.getCenter().latitude().cosinus();
  //  if (cos < 0) {
  //    cos *= -1;
  //  }
  //  short resolution = (short) (_resolution * cos);
  //  if (resolution % 2 == 1) {
  //    resolution += 1;
  //  }
  //  if (resolution < 6) {
  //    resolution = 6;
  //  }
  
    return new Vector2I(_resolutionX, _resolutionY);
  }


  public EllipsoidalTileTessellator(Vector2I resolution, boolean skirted)
  {
     _resolutionX = resolution._x;
     _resolutionY = resolution._y;
     _skirted = skirted;
    //    int __TODO_width_and_height_resolutions;
  }

  public void dispose()
  {
  }


  ///#include "FloatBufferBuilderFromCartesian2D.hpp"
  
  
  public final Vector2I getTileMeshResolution(Planet planet, Tile tile, boolean debug)
  {
    //const short resolution = calculateResolution(tile->getSector());
    //return Vector2I(resolution, resolution);
    return calculateResolution(tile.getSector());
  }


  public final Mesh createTileMesh(Planet planet, Tile tile, ElevationData elevationData, float verticalExaggeration, boolean debug)
  {
  
    final Sector sector = tile.getSector();
  
  //  if (elevationData != NULL) {
  //    ILogger::instance()->logInfo("Elevation data for sector=%s", sector.description().c_str());
  //    ILogger::instance()->logInfo("%s", elevationData->description().c_str());
  //  }
  
    //const short resolution = (short) _resolution;
    final Vector2I resolution = calculateResolution(sector);
  
    //const short resolutionMinus1 = (short) (resolution - 1);
  
  
    double minHeight = 0;
    FloatBufferBuilderFromGeodetic vertices = new FloatBufferBuilderFromGeodetic(CenterStrategy.givenCenter(), planet, sector.getCenter());
    for (int j = 0; j < resolution._y; j++)
    {
      final double v = (double) j / (resolution._y-1);
      for (int i = 0; i < resolution._x; i++)
      {
        final double u = (double) i / (resolution._x-1);
  
        double height = 0;
        if (elevationData != null)
        {
          height = elevationData.getElevationAt(j, i) * verticalExaggeration;
          if (height < minHeight)
          {
            minHeight = height;
          }
        }
  
        vertices.add(sector.getInnerPoint(u, v), height);
      }
    }
  
  
    ShortBufferBuilder indices = new ShortBufferBuilder();
    for (short j = 0; j < (resolution._y-1); j++)
    {
      final short jTimesResolution = (short)(j *resolution._x);
      if (j > 0)
      {
        indices.add(jTimesResolution);
      }
      for (short i = 0; i < resolution._x; i++)
      {
        indices.add((short)(jTimesResolution + i));
        indices.add((short)(jTimesResolution + i + resolution._x));
      }
      indices.add((short)(jTimesResolution + 2 *resolution._x - 1));
    }
  
  
    // create skirts
    if (_skirted)
    {
      // compute skirt height
      final Vector3D sw = planet.toCartesian(sector.getSW());
      final Vector3D nw = planet.toCartesian(sector.getNW());
  //    const double skirtHeight = (nw.sub(sw).length() * 0.05 * -1) + minHeight;
      final double skirtHeight = (nw.sub(sw).length() * 0.1 * -1) + minHeight;
  
      indices.add((short) 0);
      int posS = resolution._x * resolution._y;
  
      // west side
      for (int j = 0; j < resolution._y-1; j++)
      {
        vertices.add(sector.getInnerPoint(0, (double)j/(resolution._y-1)), skirtHeight);
  
        indices.add((short)(j *resolution._x));
        indices.add((short) posS++);
      }
  
      // south side
      for (int i = 0; i < resolution._x-1; i++)
      {
        vertices.add(sector.getInnerPoint((double)i/(resolution._x-1), 1), skirtHeight);
  
        indices.add((short)((resolution._y-1)*resolution._x + i));
        indices.add((short) posS++);
      }
  
      // east side
      for (int j = resolution._y-1; j > 0; j--)
      {
        vertices.add(sector.getInnerPoint(1, (double)j/(resolution._y-1)), skirtHeight);
  
        indices.add((short)(j *resolution._x + (resolution._x-1)));
        indices.add((short) posS++);
      }
  
      // north side
      for (int i = resolution._x-1; i > 0; i--)
      {
        vertices.add(sector.getInnerPoint((double)i/(resolution._x-1), 0), skirtHeight);
  
        indices.add((short) i);
        indices.add((short) posS++);
      }
  
      // last triangle
      indices.add((short) 0);
      indices.add((short)(resolution._x *resolution._y));
    }
  
    Color color = Color.newFromRGBA((float) 1.0, (float) 1.0, (float) 1.0, (float) 1.0);
  
    return new IndexedMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), indices.create(), 1, 1, color); //debug ? GLPrimitive::lineStrip() : GLPrimitive::triangleStrip(),
  }

  public final Mesh createTileDebugMesh(Planet planet, Tile tile)
  {
    final Sector sector = tile.getSector();
  
    final int resolutionXMinus1 = _resolutionX - 1;
    final int resolutionYMinus1 = _resolutionY - 1;
    short posS = 0;
  
    // compute offset for vertices
    final Vector3D sw = planet.toCartesian(sector.getSW());
    final Vector3D nw = planet.toCartesian(sector.getNW());
    final double offset = nw.sub(sw).length() * 1e-3;
  
    FloatBufferBuilderFromGeodetic vertices = new FloatBufferBuilderFromGeodetic(CenterStrategy.givenCenter(), planet, sector.getCenter());
  
    ShortBufferBuilder indices = new ShortBufferBuilder();
  
    // west side
    for (int j = 0; j < resolutionYMinus1; j++)
    {
      vertices.add(sector.getInnerPoint(0, (double)j/resolutionYMinus1), offset);
      indices.add(posS++);
    }
  
    // south side
    for (int i = 0; i < resolutionXMinus1; i++)
    {
      vertices.add(sector.getInnerPoint((double)i/resolutionXMinus1, 1), offset);
      indices.add(posS++);
    }
  
    // east side
    for (int j = resolutionYMinus1; j > 0; j--)
    {
      vertices.add(sector.getInnerPoint(1, (double)j/resolutionYMinus1), offset);
      indices.add(posS++);
    }
  
    // north side
    for (int i = resolutionXMinus1; i > 0; i--)
    {
      vertices.add(sector.getInnerPoint((double)i/resolutionXMinus1, 0), offset);
      indices.add(posS++);
    }
  
    Color color = Color.newFromRGBA((float) 1.0, (float) 0, (float) 0, (float) 1.0);
  
    return new IndexedMesh(GLPrimitive.lineLoop(), true, vertices.getCenter(), vertices.create(), indices.create(), 1, 1, color);
  }

  public final boolean isReady(G3MRenderContext rc)
  {
    return true;
  }

  public final IFloatBuffer createUnitTextCoords(Tile tile)
  {
  
  //  const int resolution       = _resolution;
    Vector2I resolution = calculateResolution(tile.getSector());
    //const int resolutionMinus1 = resolution - 1;
  
    float[] u = new float[resolution._x * resolution._y];
    float[] v = new float[resolution._x * resolution._y];
  
    for (int j = 0; j < resolution._y; j++)
    {
      for (int i = 0; i < resolution._x; i++)
      {
        final int pos = j *resolution._x + i;
        u[pos] = (float) i / (resolution._x-1);
        v[pos] = (float) j / (resolution._y-1);
      }
    }
  
    //FloatBufferBuilderFromCartesian2D textCoords;
    int textCoordsSize = (resolution._x * resolution._y) * 2;
    if (_skirted)
    {
      //textCoordsSize += (resolutionMinus1 * 4) * 2;
      textCoordsSize += ((resolution._x-1)*(resolution._y-1) * 4) * 2;
    }
    IFloatBuffer textCoords = IFactory.instance().createFloatBuffer(textCoordsSize);
    int textCoordsIndex = 0;
  
    for (int j = 0; j < resolution._y; j++)
    {
      for (int i = 0; i < resolution._x; i++)
      {
        final int pos = j *resolution._x + i;
        //textCoords.add( u[pos], v[pos] );
        textCoords.rawPut(textCoordsIndex++, u[pos]);
        textCoords.rawPut(textCoordsIndex++, v[pos]);
      }
    }
  
    // create skirts
    if (_skirted)
    {
      // west side
      for (int j = 0; j < resolution._y-1; j++)
      {
        final int pos = j *resolution._x;
        //textCoords.add( u[pos], v[pos] );
        textCoords.rawPut(textCoordsIndex++, u[pos]);
        textCoords.rawPut(textCoordsIndex++, v[pos]);
      }
  
      // south side
      for (int i = 0; i < resolution._x-1; i++)
      {
        final int pos = (resolution._y-1) * resolution._x + i;
        //textCoords.add( u[pos], v[pos] );
        textCoords.rawPut(textCoordsIndex++, u[pos]);
        textCoords.rawPut(textCoordsIndex++, v[pos]);
      }
  
      // east side
      for (int j = resolution._y-1; j > 0; j--)
      {
        final int pos = j *resolution._x + resolution._x-1;
        //textCoords.add( u[pos], v[pos] );
        textCoords.rawPut(textCoordsIndex++, u[pos]);
        textCoords.rawPut(textCoordsIndex++, v[pos]);
      }
  
      // north side
      for (int i = resolution._x-1; i > 0; i--)
      {
        final int pos = i;
        //textCoords.add( u[pos], v[pos] );
        textCoords.rawPut(textCoordsIndex++, u[pos]);
        textCoords.rawPut(textCoordsIndex++, v[pos]);
      }
    }
  
    // free temp memory
    u = null;
    v = null;
  
    //  return textCoords.create();
    return textCoords;
  }

}